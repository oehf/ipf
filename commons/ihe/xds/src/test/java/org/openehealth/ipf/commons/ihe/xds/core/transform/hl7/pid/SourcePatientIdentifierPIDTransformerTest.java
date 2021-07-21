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

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for patient ID list transformation in SourcePatientInfo.
 * @author Jens Riemschneider
 */
public class SourcePatientIdentifierPIDTransformerTest {

    @Test
    public void testToHL7() {
        var patientInfo = new PatientInfo();
        var id1 = new Identifiable();
        id1.setId("id1");
        var assigningAuthority1 = new AssigningAuthority();
        assigningAuthority1.setUniversalId("1.1.1.1");
        assigningAuthority1.setUniversalIdType("ISO");
        id1.setAssigningAuthority(assigningAuthority1);

        var id2 = new Identifiable();
        id2.setId("id2");
        var assigningAuthority2 = new AssigningAuthority();
        assigningAuthority2.setUniversalId("2.2.2.2");
        assigningAuthority2.setUniversalIdType("ISO");
        id2.setAssigningAuthority(assigningAuthority2);
        
        patientInfo.getIds().add(id1);
        patientInfo.getIds().add(id2);

        var iterator = patientInfo.getHl7FieldIterator("PID-3");
        assertEquals("id2^^^&2.2.2.2&ISO", iterator.next());
        assertEquals("id1^^^&1.1.1.1&ISO", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToHL7EmptyIds() {
        var patientInfo = new PatientInfo();
        patientInfo.getIds().add(new Identifiable());
        patientInfo.getIds().add(new Identifiable());
        var iterator = patientInfo.getHl7FieldIterator("PID-3");
        assertEquals("", iterator.next());
        assertEquals("", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToHL7NoIds() {
        var patientInfo = new PatientInfo();
        var iterator = patientInfo.getHl7FieldIterator("PID-3");
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testFromHL7() {
        var patientInfo = new PatientInfo();
        var iterator = patientInfo.getHl7FieldIterator("PID-3");
        iterator.add("id1^^^&1.1.1.1&ISO");
        iterator.add("id2^^^&2.2.2.2&ISO");

        var ids = patientInfo.getIds();
        var id0 = ids.next();
        var id1 = ids.next();
        assertFalse(ids.hasNext());
        
        assertEquals("id1", id0.getId());
        assertEquals("1.1.1.1", id0.getAssigningAuthority().getUniversalId());
        assertEquals("ISO", id0.getAssigningAuthority().getUniversalIdType());

        assertEquals("id2", id1.getId());
        assertEquals("2.2.2.2", id1.getAssigningAuthority().getUniversalId());
        assertEquals("ISO", id1.getAssigningAuthority().getUniversalIdType());
    }
    
    @Test
    public void testFromHL7OneEmptyId() {
        var patientInfo = new PatientInfo();
        var iterator = patientInfo.getHl7FieldIterator("PID-3");
        iterator.add("^^^");
        iterator.add("id2^^^&2.2.2.2&ISO");

        var ids = patientInfo.getIds();
        assertNull(ids.next());
        var id0 = ids.next();
        assertFalse(ids.hasNext());
        
        assertEquals("id2", id0.getId());
        assertEquals("2.2.2.2", id0.getAssigningAuthority().getUniversalId());
        assertEquals("ISO", id0.getAssigningAuthority().getUniversalIdType());
    }
    
    @Test
    public void testFromHL7Empty() {
        var patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-3").add("");
        var ids = patientInfo.getIds();
        assertNull(ids.next());
        assertFalse(ids.hasNext());
    }

    @Test
    public void testFromHL7Null() {
        var patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-3").add(null);
        var ids = patientInfo.getIds();
        assertNull(ids.next());
        assertFalse(ids.hasNext());
    }
}
