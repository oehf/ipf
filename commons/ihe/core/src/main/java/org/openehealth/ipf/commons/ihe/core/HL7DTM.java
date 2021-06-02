package org.openehealth.ipf.commons.ihe.core;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.primitive.CommonTS;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * HL7 timestamps (data type DTM) with particular precision, normalized to UTC. It's the usual date format used by IHE.
 *
 * @author Dmytro Rud
 */
public class HL7DTM {

    private static final DateTimeFormatter SIMPLE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Creates a {@link ZonedDateTime} object from the given string.
     * @param s
     *      String of the pattern <code>YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]</code>.
     *      Milliseconds will be ignored.
     * @return
     *      a {@link ZonedDateTime} object, or <code>null</code> if the parameter is <code>null</code> or empty.
     * @throws DataTypeException if the DTM string is invalid.
     */
    public static ZonedDateTime toZonedDateTime(String s) throws DataTypeException {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        var pos = Math.max(s.indexOf('-'), s.indexOf('+'));

        // default time zone is UTC
        if (pos < 0) {
            s += "+0000";
        }

        // parse timestamp
        var ts = new CommonTS(s);
        return ZonedDateTime.of(
                ts.getYear(),
                (ts.getMonth() == 0) ? 1 : ts.getMonth(),
                (ts.getDay() == 0) ? 1 : ts.getDay(),
                ts.getHour(),
                ts.getMinute(),
                ts.getSecond(),
                0,
                ZoneId.ofOffset("GMT", ZoneOffset.ofHoursMinutes(
                        ts.getGMTOffset() / 100,
                        ts.getGMTOffset() % 100
                ))
        );
    }

    /**
     * Creates a simple DTM representation of a {@link TemporalAccessor}.
     * @param temporalAccessor The {@link TemporalAccessor} to convert (e.g. {@link Instant}, {@link ZonedDateTime}).
     * @return
     *      a string, or <code>null</code> if the parameter is <code>null</code>.
     */
    public static String toSimpleString(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor == null) {
            return null;
        }
        return SIMPLE_FORMAT.format(temporalAccessor);
    }
}
