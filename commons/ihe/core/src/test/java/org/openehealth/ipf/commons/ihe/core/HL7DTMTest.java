/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core;

import ca.uhn.hl7v2.model.DataTypeException;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HL7DTMTest {

    @Test
    public void testToZonedDateTimeWithoutTimezone() throws DataTypeException {
        assertEquals(ZonedDateTime.parse("2007-08-10T14:09:00+00:00").toInstant(),
                HL7DTM.toZonedDateTime("20070810140900").toInstant());
    }

    @Test
    public void testToZonedDateTimeWithTimezone() throws DataTypeException {
        assertEquals(ZonedDateTime.parse("2007-08-10T14:09:00+01:00").toInstant(),
            HL7DTM.toZonedDateTime("20070810140900+0100").toInstant());
    }

    @Test
    public void testToZonedDateTimeWithoutSeconds() throws DataTypeException {
        assertEquals(ZonedDateTime.parse("2007-08-10T14:09:00+00:00").toInstant(),
            HL7DTM.toZonedDateTime("200708101409").toInstant());
    }

    @Test
    public void testToZonedDateTimeWithoutMinutes() throws DataTypeException {
        assertEquals(ZonedDateTime.parse("2007-08-10T14:00:00+00:00").toInstant(),
            HL7DTM.toZonedDateTime("2007081014").toInstant());
    }

    @Test
    public void testToZonedDateTimeWithoutHours() throws DataTypeException {
        assertEquals(ZonedDateTime.parse("2007-08-10T00:00:00+00:00").toInstant(),
            HL7DTM.toZonedDateTime("20070810+0000").toInstant());
    }

    @Test
    public void testToSimpleString() {
        assertEquals("20070810140900", HL7DTM.toSimpleString(ZonedDateTime.parse("2007-08-10T14:09:00+00:00")));
    }
}