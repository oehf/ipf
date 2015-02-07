/*
 * Copyright 2015 the original author or authors.
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

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.primitive.CommonTS;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Transfers between HL7 DTM timestamps used in IHE XDS profiles and {@link DateTime}.
 */
public class DateTransformer {
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyyMMddHHmmss");

    /**
     * Converts from the HL7 DTM format to {@link DateTime}.
     *
     * @param timestamp
     *      String of the pattern <code>YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ]</code>.
     * @return
     *      {@link DateTime} object, or <code>null</code> is the parameter was <code>null</code>.
     */
    public static DateTime fromHL7(String timestamp) {
        if (timestamp == null) {
            return null;
        }

        // default time zone is UTC
        if ((timestamp.indexOf('-') < 0) && (timestamp.indexOf('+') < 0)) {
            timestamp += "+0000";
        }

        try {
            CommonTS ts = new CommonTS(timestamp);
            DateTime dateTime = new DateTime(
                    ts.getYear(),
                    (ts.getMonth() == 0) ? 1 : ts.getMonth(),
                    (ts.getDay() == 0) ? 1 : ts.getDay(),
                    ts.getHour(),
                    ts.getMinute(),
                    ts.getSecond(),
                    DateTimeZone.forOffsetHoursMinutes(
                            ts.getGMTOffset() / 100,
                            ts.getGMTOffset() % 100));
            return dateTime.toDateTime(DateTimeZone.UTC);
        } catch (DataTypeException e) {
            throw new XDSMetaDataException(ValidationMessage.INVALID_TIME, timestamp);
        }
    }

    /**
     * Converts from {@link DateTime} to the HL7 DTM format.
     * According to IHE requirements, time zone is UTC, and milliseconds are not used.
     *
     * @param dateTime
     *      {@link DateTime} object.
     * @return
     *      String of the pattern <code>YYYYMMDDhhmmss</code>.
     */
    public static String toHL7(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return FORMATTER.print(dateTime.toDateTime(DateTimeZone.UTC));
    }
}
