/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

/**
 * Tests for {@link SourcePatientIdentifierPIDTransformer}.
 * @author Jens Riemschneider
 */
public class SourcePatientIdentifierPIDTransformerTest {
    private SourcePatientIdentifierPIDTransformer transformer;
    
    @Before
    public void setUp() {
        transformer = new SourcePatientIdentifierPIDTransformer();
    }
    
    @Test
    public void testToHL7() {
        PatientInfo patientInfo = new PatientInfo();
        Identifiable id1 = new Identifiable();
        id1.setId("id1");
        AssigningAuthority assigningAuthority1 = new AssigningAuthority();
        assigningAuthority1.setUniversalId("1.1.1.1");
        assigningAuthority1.setUniversalIdType("ISO");
        id1.setAssigningAuthority(assigningAuthority1);
        
        Identifiable id2 = new Identifiable();
        id2.setId("id2");
        AssigningAuthority assigningAuthority2 = new AssigningAuthority();
        assigningAuthority2.setUniversalId("2.2.2.2");
        assigningAuthority2.setUniversalIdType("ISO");
        id2.setAssigningAuthority(assigningAuthority2);
        
        patientInfo.getIds().add(id1);
        patientInfo.getIds().add(id2);

        List<String> result = transformer.toHL7(patientInfo);
        assertEquals(2, result.size());
        assertEquals("id1^^^&1.1.1.1&ISO", result.get(0));
        assertEquals("id2^^^&2.2.2.2&ISO", result.get(1));
    }

    @Test
    public void testToHL7EmptyIds() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getIds().add(new Identifiable());
        patientInfo.getIds().add(new Identifiable());
        assertNull(transformer.toHL7(patientInfo));
    }

    @Test
    public void testToHL7NoIds() {
        assertNull(transformer.toHL7(new PatientInfo()));
    }
    
    
    @Test
    public void testFromHL7() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7("id1^^^&1.1.1.1&ISO~id2^^^&2.2.2.2&ISO", patientInfo);
        
        List<Identifiable> ids = patientInfo.getIds();        
        assertEquals(2, ids.size());
        
        assertEquals("id1", ids.get(0).getId());
        assertEquals("1.1.1.1", ids.get(0).getAssigningAuthority().getUniversalId());
        assertEquals("ISO", ids.get(0).getAssigningAuthority().getUniversalIdType());

        assertEquals("id2", ids.get(1).getId());
        assertEquals("2.2.2.2", ids.get(1).getAssigningAuthority().getUniversalId());
        assertEquals("ISO", ids.get(1).getAssigningAuthority().getUniversalIdType());
    }
    
    @Test
    public void testFromHL7OneEmptyId() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7("^^^~id2^^^&2.2.2.2&ISO", patientInfo);
        
        List<Identifiable> ids = patientInfo.getIds();        
        assertEquals(1, ids.size());
        
        assertEquals("id2", ids.get(0).getId());
        assertEquals("2.2.2.2", ids.get(0).getAssigningAuthority().getUniversalId());
        assertEquals("ISO", ids.get(0).getAssigningAuthority().getUniversalIdType());
    }
    
    @Test
    public void testFromHL7Empty() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7("", patientInfo);
        assertEquals(0, patientInfo.getIds().size());
    }

    @Test
    public void testFromHL7Null() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7(null, patientInfo);
        assertEquals(0, patientInfo.getIds().size());
    }
}
