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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Name;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XcnName;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XpnName;

/**
 * Tests for {@link SourcePatientNamePIDTransformer}.
 * @author Jens Riemschneider
 */
public class SourcePatientNamePIDTransformerTest {
    private SourcePatientNamePIDTransformer transformer;
    
    @Before
    public void setUp() {
        transformer = new SourcePatientNamePIDTransformer();        
    }
    
    @Test
    public void testToHL7() {
        PatientInfo patientInfo = new PatientInfo();
        Name name = new XpnName();
        name.setFamilyName("Jo|man");
        name.setGivenName("Jo|chen");
        name.setSecondAndFurtherGivenNames("Jo|achim");
        name.setSuffix("von Jo|del");
        name.setPrefix("Jo|dler");
        patientInfo.setName(name);
        assertEquals("Jo\\F\\man^Jo\\F\\chen^Jo\\F\\achim^von Jo\\F\\del^Jo\\F\\dler", 
                transformer.toHL7(patientInfo).get(0));
    }
    
    @Test
    public void testToHL7EmptyName() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setName(new XcnName());
        assertNull(transformer.toHL7(patientInfo));        
    }
    
    @Test
    public void testToHL7NoName() {
        PatientInfo patientInfo = new PatientInfo();
        assertNull(transformer.toHL7(patientInfo));        
    }

    
    @Test
    public void testFromHL7() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7("Jo\\F\\man^Jo\\F\\chen^Jo\\F\\achim^von Jo\\F\\del^Jo\\F\\dler", patientInfo);
        Name name = patientInfo.getName();
        assertNotNull(name);
        assertEquals("Jo|man", name.getFamilyName());
        assertEquals("Jo|chen", name.getGivenName());
        assertEquals("Jo|achim", name.getSecondAndFurtherGivenNames());
        assertEquals("von Jo|del", name.getSuffix());
        assertEquals("Jo|dler", name.getPrefix());
    }

    @Test
    public void testFromHL7Empty() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7("^^", patientInfo);
        assertNull(patientInfo.getName());
    }

    @Test
    public void testFromHL7Null() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7(null, patientInfo);
        assertNull(patientInfo.getName());
    }
}
