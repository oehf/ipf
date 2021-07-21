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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Address;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for patient address transformation in SourcePatientInfo.
 * @author Jens Riemschneider
 */
public class PatientAddressPIDTransformerTest {

    @Test
    public void testToHL7() {
        var patientInfo = new PatientInfo();
        var address = new Address();
        address.setStreetAddress("I live here 42");
        address.setCountry("ECU");
        patientInfo.getAddresses().add(address);
        var iterator = patientInfo.getHl7FieldIterator("PID-11");
        assertEquals("I live here 42^^^^^ECU", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToHL7EmptyAddress() {
        var patientInfo = new PatientInfo();
        var address = new Address();
        patientInfo.getAddresses().add(address);
        var iterator = patientInfo.getHl7FieldIterator("PID-11");
        assertEquals("", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToHL7Null() {
        var patientInfo = new PatientInfo();
        patientInfo.getAddresses().add(null);
        var iterator = patientInfo.getHl7FieldIterator("PID-11");
        assertEquals("", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToHL7NoAddress() {
        var patientInfo = new PatientInfo();
        assertFalse(patientInfo.getHl7FieldIterator("PID-11").hasNext());
        assertFalse(patientInfo.getAddresses().hasNext());
    }
    
    @Test
    public void testFromHL7() {
        var patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-11").add("I live here 42^^^^^ECU");
        var addresses = patientInfo.getAddresses();
        var address = addresses.next();
        assertEquals("I live here 42", address.getStreetAddress());
        assertEquals("ECU", address.getCountry());
        assertFalse(addresses.hasNext());
    }

    @Test
    public void testFromHL7Empty() {
        var patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-11").add("");
        var addresses = patientInfo.getAddresses();
        assertNull(addresses.next());
        assertFalse(addresses.hasNext());
    }

    @Test
    public void testFromHL7Null() {
        var patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-11").add(null);
        var addresses = patientInfo.getAddresses();
        assertNull(addresses.next());
        assertFalse(addresses.hasNext());
    }
}
