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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid.DateOfBirthPIDTransformer;

/**
 * Tests for {@link DateOfBirthPIDTransformer}.
 * @author Jens Riemschneider
 */
public class DateOfBirthPIDTransformerTest {
    private DateOfBirthPIDTransformer transformer;
    
    @Before
    public void setUp() {
        transformer = new DateOfBirthPIDTransformer();
    }
    
    @Test
    public void testToHL7() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setDateOfBirth("123456+0100");
        assertEquals("123456+0100", transformer.toHL7(patientInfo).get(0));
    }
    
    @Test
    public void testToHL7WithNoDate() {
        assertNull(transformer.toHL7(new PatientInfo()));
    }
    
    
    @Test
    public void testFromHL7() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7("6543-0100^sdf", patientInfo);
        assertEquals("6543-0100", patientInfo.getDateOfBirth());
    }

    @Test
    public void testFromHL7Null() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7(null, patientInfo);
        assertNull(patientInfo.getDateOfBirth());
    }

    @Test
    public void testFromHL7Empty() {
        PatientInfo patientInfo = new PatientInfo();
        transformer.fromHL7("", patientInfo);
        assertNull(patientInfo.getDateOfBirth());
    }
}
