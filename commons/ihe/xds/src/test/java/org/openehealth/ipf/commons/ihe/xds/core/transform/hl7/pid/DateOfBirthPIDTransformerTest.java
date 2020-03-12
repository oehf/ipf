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

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ListIterator;

import static org.junit.Assert.*;

/**
 * Tests for date of birth transformation in SourcePatientInfo.
 * @author Jens Riemschneider
 */
public class DateOfBirthPIDTransformerTest {
    @Test
    public void testToHL7() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setDateOfBirth("19800102030405+0100");
        ListIterator<String> iterator = patientInfo.getHl7FieldIterator("PID-7");
        assertEquals("19800102030405+0100", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testToHL7WithNoDate() {
        PatientInfo patientInfo = new PatientInfo();
        ListIterator<String> iterator = patientInfo.getHl7FieldIterator("PID-7");
        assertFalse(iterator.hasNext());
        assertNull(patientInfo.getDateOfBirth());
    }

    @Test
    public void testFromHL7() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-7").add("19800102030405-0100^sdf");
        ZonedDateTime expected = ZonedDateTime.of(1980, 1, 2, 3, 4, 5, 0, ZoneId.of("-01:00"));
        assertEquals(
                new Timestamp(expected, Timestamp.Precision.SECOND),
                patientInfo.getDateOfBirth());
    }

    @Test
    public void testFromHL7Null() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-7").add(null);
        assertNull(patientInfo.getDateOfBirth());
    }

    @Test
    public void testFromHL7Empty() {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getHl7FieldIterator("PID-7").add("");
        assertNull(patientInfo.getDateOfBirth());
    }
}
