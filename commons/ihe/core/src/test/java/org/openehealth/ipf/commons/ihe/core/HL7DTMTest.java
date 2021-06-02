package org.openehealth.ipf.commons.ihe.core;

import ca.uhn.hl7v2.model.DataTypeException;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class HL7DTMTest {

    @Test
    public void testToZonedDateTime() throws DataTypeException {
        assertEquals(ZonedDateTime.parse("2007-08-10T14:09:00+00:00").toInstant(),
                HL7DTM.toZonedDateTime("20070810140900").toInstant());
    }

    @Test
    public void testToSimpleString() {
        assertEquals("20070810140900", HL7DTM.toSimpleString(ZonedDateTime.parse("2007-08-10T14:09:00+00:00")));
    }
}