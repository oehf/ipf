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
package org.openehealth.ipf.commons.ihe.xds.core.transform.hl7;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

/**
 * Tests for {@link PatientInfoTransformer}.
 * @author Jens Riemschneider
 */
public class PatientInfoTransformerTest {
    private PatientInfoTransformer transformer;
    
    @Before
    public void setUp() {
        transformer = new PatientInfoTransformer();
    }
    
    @Test
    public void testToHL7() {
        PatientInfo patientInfo = new PatientInfo();
        
        Identifiable id = new Identifiable();
        id.setId("abcdef");
        patientInfo.getIds().add(id);
        
        Name name = new XcnName();
        name.setFamilyName("Joman");
        patientInfo.setName(name);

        Address address = new Address();
        address.setStreetAddress("Jo Str. 3");
        patientInfo.setAddress(address);

        patientInfo.setDateOfBirth("19800102");
        patientInfo.setGender("A");
        
        List<String> hl7Data = transformer.toHL7(patientInfo);
        assertEquals(5, hl7Data.size());
        
        assertEquals("PID-3|abcdef", hl7Data.get(0));
        assertEquals("PID-5|Joman", hl7Data.get(1));
        assertEquals("PID-7|19800102", hl7Data.get(2));
        assertEquals("PID-8|A", hl7Data.get(3));
        assertEquals("PID-11|Jo Str. 3", hl7Data.get(4));
    }
    
    @Test
    public void testToHL7MultiId() {
        PatientInfo patientInfo = new PatientInfo();

        Identifiable id = new Identifiable();
        id.setId("abcdef");
        patientInfo.getIds().add(id);
        Identifiable id2 = new Identifiable();
        id2.setId("ghijkl");
        patientInfo.getIds().add(id2);

        Name name = new XpnName();
        name.setFamilyName("Joman");
        patientInfo.setName(name);

        Address address = new Address();
        address.setStreetAddress("Jo Str. 3");
        patientInfo.setAddress(address);

        patientInfo.setDateOfBirth("19800102");
        patientInfo.setGender("A");

        List<String> hl7Data = transformer.toHL7(patientInfo);
        assertEquals(5, hl7Data.size());

        assertEquals("PID-3|abcdef~ghijkl", hl7Data.get(0));
        assertEquals("PID-5|Joman", hl7Data.get(1));
        assertEquals("PID-7|19800102", hl7Data.get(2));
        assertEquals("PID-8|A", hl7Data.get(3));
        assertEquals("PID-11|Jo Str. 3", hl7Data.get(4));
    }

    @Test
    public void testToHL7Empty() {
        PatientInfo patientInfo = new PatientInfo();        
        List<String> hl7Data = transformer.toHL7(patientInfo);
        assertEquals(0, hl7Data.size());
    }
    
    @Test
    public void testToHL7Null() {
        List<String> hl7Data = transformer.toHL7(null);
        assertEquals(0, hl7Data.size());
    }
    
    
    @Test
    public void testFromHL7() {
        List<String> hl7PID = Arrays.asList(
            "PID-3|abcdef~fedcba",
            "PID-3|uvwxyz~zyxwvu",
            "PID-5|Joman",
            "PID-7|19800102",
            "PID-8|A",
            "PID-9|Not used",
            "PID-11|Jo Str. 3");
        
        PatientInfo patientInfo = transformer.fromHL7(hl7PID);
        assertNotNull(patientInfo);
        assertEquals(4, patientInfo.getIds().size());
        assertEquals("abcdef", patientInfo.getIds().get(0).getId());
        assertEquals("fedcba", patientInfo.getIds().get(1).getId());
        assertEquals("uvwxyz", patientInfo.getIds().get(2).getId());
        assertEquals("zyxwvu", patientInfo.getIds().get(3).getId());
        assertEquals("Joman", patientInfo.getName().getFamilyName());
        assertEquals(
                new Timestamp(new DateTime(1980, 1, 2, 0, 0, DateTimeZone.UTC), Timestamp.Precision.DAY),
                patientInfo.getDateOfBirth());
        assertEquals("A", patientInfo.getGender());
        assertEquals("Jo Str. 3", patientInfo.getAddress().getStreetAddress());
    }
    
    @Test
    public void testFromHL7EmptyList() {
        assertNull(transformer.fromHL7(Collections.<String>emptyList()));
    }
    
    @Test
    public void testFromHL7Null() {
        assertNull(transformer.fromHL7(null));
    }

    @Test
    public void testLongPid3List() {
        PatientInfo originalPatientInfo = new PatientInfo();
        for (int i = 0; i < 9; ++i) {
            // length of each such ID is ~100 characters, therefore only two IDs per value are possible
            originalPatientInfo.getIds().add(new Identifiable("abcdefghijklmnop_" + i, new AssigningAuthority(
                    "1.2.3.4.5.6.7.8.9.10.11.12.15.16.17.19.19.20.21.22.23.24.25.26.27.28.29.30.31", "ISO")));
        }

        List<String> hl7 = transformer.toHL7(originalPatientInfo);
        assertEquals(1, hl7.size());
        String pid3 = hl7.get(0);
        assertTrue(pid3.startsWith("PID-3|"));
        assertTrue(pid3.length() <= 256);

        PatientInfo recoveredPatientInfo = transformer.fromHL7(hl7);
        assertEquals(2, recoveredPatientInfo.getIds().size());
    }
}
