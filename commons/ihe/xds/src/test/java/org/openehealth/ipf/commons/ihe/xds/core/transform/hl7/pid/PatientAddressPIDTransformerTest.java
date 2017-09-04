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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Address;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

import java.util.ListIterator;

/**
 * Tests for patient address transformation in SourcePatientInfo.
 * @author Jens Riemschneider
 */
public class PatientAddressPIDTransformerTest {

    @Test
    public void testToHL7() {
        PatientInfo patientInfo = new PatientInfo();
        Address address = new Address();
        address.setStreetAddress("I live here 42");
        address.setCountry("ECU");
        patientInfo.getAddresses().add(address);
        ListIterator<String> iterator = patientInfo.getHl7FieldIterator("PID-11");
        assertEquals("I live here 42^^^^^ECU", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToHL7EmptyAddress() {
        PatientInfo patientInfo = new PatientInfo();
        Address address = new Address();
        patientInfo.getAddresses().add(address);
        ListIterator<String> iterator = patientInfo.getHl7FieldIterator("PID-11");
        assertEquals("", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToHL7Null() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getAddresses().add(null);
        ListIterator<String> iterator = patientInfo.getHl7FieldIterator("PID-11");
        assertEquals("", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToHL7NoAddress() {
        PatientInfo patientInfo = new PatientInfo();
        assertFalse(patientInfo.getHl7FieldIterator("PID-11").hasNext());
        assertFalse(patientInfo.getAddresses().hasNext());
    }
    
    @Test
    public void testFromHL7() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-11").add("I live here 42^^^^^ECU");
        ListIterator<Address> addresses = patientInfo.getAddresses();
        Address address = addresses.next();
        assertEquals("I live here 42", address.getStreetAddress());
        assertEquals("ECU", address.getCountry());
        assertFalse(addresses.hasNext());
    }

    @Test
    public void testFromHL7Empty() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-11").add("");
        ListIterator<Address> addresses = patientInfo.getAddresses();
        assertNull(addresses.next());
        assertFalse(addresses.hasNext());
    }

    @Test
    public void testFromHL7Null() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-11").add(null);
        ListIterator<Address> addresses = patientInfo.getAddresses();
        assertNull(addresses.next());
        assertFalse(addresses.hasNext());
    }
}
