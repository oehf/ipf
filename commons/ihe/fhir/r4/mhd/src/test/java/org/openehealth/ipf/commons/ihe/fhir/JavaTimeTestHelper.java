package org.openehealth.ipf.commons.ihe.fhir;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

public final class JavaTimeTestHelper {
    public static Date convert(ZonedDateTime zonedDateTime) {
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }
}
