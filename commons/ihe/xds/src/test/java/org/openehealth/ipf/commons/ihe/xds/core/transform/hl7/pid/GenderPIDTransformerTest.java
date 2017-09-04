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

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

import java.util.ListIterator;

/**
 * Tests for administrative gender code transformation in SourcePatientInfo.
 * @author Jens Riemschneider
 */
public class GenderPIDTransformerTest {

    @Test
    public void testToHL7() {
        PatientInfo patientInfo = new PatientInfo();        
        patientInfo.setGender("M");
        ListIterator<String> iterator = patientInfo.getHl7FieldIterator("PID-8");
        assertEquals("M", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testToHL7Null() {
        PatientInfo patientInfo = new PatientInfo();
        ListIterator<String> iterator = patientInfo.getHl7FieldIterator("PID-8");
        assertFalse(iterator.hasNext());
        assertNull(patientInfo.getGender());
    }
    
    
    @Test
    public void testFromHL7() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-8").add("F");
        assertEquals("F", patientInfo.getGender());
    }
    
    @Test
    public void testFromHL7Null() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-8").add(null);
        assertNull(patientInfo.getGender());
    }
    
    @Test
    public void testFromHL7Empty() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-8").add("");
        assertNull(patientInfo.getGender());
    }    
}
