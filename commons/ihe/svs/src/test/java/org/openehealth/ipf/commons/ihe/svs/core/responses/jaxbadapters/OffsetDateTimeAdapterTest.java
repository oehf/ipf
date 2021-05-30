package org.openehealth.ipf.commons.ihe.svs.core.responses.jaxbadapters;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class OffsetDateTimeAdapterTest {

    private final OffsetDateTimeAdapter adapter = new OffsetDateTimeAdapter();

    /**
     * Simple pattern for an XSD:dateTime
     */
    private final Pattern dateTimePattern = Pattern.compile("-?\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?([+-]\\d{2}:\\d{2}|Z)?");

    @Test
    public void testUnmarshal() {
        assertNotNull(adapter.unmarshal("2007-12-03T10:15:30+01:00"));
        assertNotNull(adapter.unmarshal("2002-10-10T17:00:00Z"));
        assertNotNull(adapter.unmarshal("2001-10-26T21:32:52.12679"));
        assertNotNull(adapter.unmarshal("2001-10-26T21:32:52.1267946-00:00"));
        assertNotNull(adapter.unmarshal("2001-10-26T21:32:52"));
        assertNotNull(adapter.unmarshal("-2001-10-26T21:32:52"));
    }

    @Test
    public void testMarshal() {
        assertTrue(dateTimePattern.matcher(adapter.marshal(OffsetDateTime.now())).matches());
        assertNotNull(adapter.unmarshal(adapter.marshal(OffsetDateTime.now())));
    }
}