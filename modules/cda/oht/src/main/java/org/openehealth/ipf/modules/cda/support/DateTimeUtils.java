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
package org.openehealth.ipf.modules.cda.support;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Collection of Date/Time Utilities to create/check proper date/time strings
 * 
 * @author Christian Ohr
 */
public class DateTimeUtils {

    private static final DateTimeFormatter YEAR_FORMAT = new DateTimeFormatterBuilder()
            .appendYear(4, 4).toFormatter();
    private static final DateTimeFormatter YEAR_MONTH_FORMAT = new DateTimeFormatterBuilder()
            .appendYear(4, 4).appendMonthOfYear(2).toFormatter();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat
            .forPattern("yyyyMMdd");
    private static final DateTimeFormatter DATETIME_MINUTE_FORMAT = DateTimeFormat
            .forPattern("yyyyMMddHHmm");
    private static final DateTimeFormatter DATETIME_MINUTE_TZ_FORMAT = DateTimeFormat
            .forPattern("yyyyMMddHHmmZ");
    private static final DateTimeFormatter DATETIME_SECOND_FORMAT = DateTimeFormat
            .forPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter DATETIME_SECOND_TZ_FORMAT = DateTimeFormat
            .forPattern("yyyyMMddHHmmssZ");
    private static final DateTimeFormatter DATETIME_MILLISECOND_FORMAT = DateTimeFormat
            .forPattern("yyyyMMddHHmmss.SSS");
    private static final DateTimeFormatter DATETIME_MILLISECOND_TZ_FORMAT = DateTimeFormat
            .forPattern("yyyyMMddHHmmss.SSSZ");

    private static final DateTimeFormatter[][] formats = new DateTimeFormatter[][] {
            new DateTimeFormatter[] { YEAR_FORMAT },
            new DateTimeFormatter[] { YEAR_MONTH_FORMAT },
            new DateTimeFormatter[] { DATE_FORMAT },
            new DateTimeFormatter[] { DATETIME_MINUTE_FORMAT,
                    DATETIME_MINUTE_TZ_FORMAT },
            new DateTimeFormatter[] { DATETIME_SECOND_FORMAT,
                    DATETIME_SECOND_TZ_FORMAT },
            new DateTimeFormatter[] { DATETIME_MILLISECOND_FORMAT,
                    DATETIME_MILLISECOND_TZ_FORMAT } };

    public static final int YEAR_PRECISION = 0;
    public static final int MONTH_PRECISION = 1;
    public static final int DATE_PRECISION = 2;
    public static final int DATETIME_MINUTE_PRECISION = 3;
    public static final int DATETIME_SECOND_PRECISION = 4;
    public static final int DATETIME_MILLISECOND_PRECISION = 5;

    public static boolean isValidDateTime(String s) {
        return isValidDateTime(s, 0, formats.length - 1);
    }

    public static boolean isValidDateTime(String s, int minimumPrecision) {
        return isValidDateTime(s, minimumPrecision, formats.length - 1);
    }

    /**
     * Checks whether the datetime string is a valid point in time in the proper
     * precision. For birth dates, e.g., usually only the date is given.
     * 
     * @param s
     * @param minimumPrecision
     * @param maximumPrecision
     * @return
     */
    public static boolean isValidDateTime(String s, int minimumPrecision,
            int maximumPrecision) {
        DateTime d = null;
        for (int i = minimumPrecision; i <= maximumPrecision; i++) {
            for (int j = 0; j < formats[i].length; j++) {
                try {
                    d = formats[i][j].parseDateTime(s);
                    if (d != null) {
                        break;
                    }
                } catch (Exception e) {
                    // try next....
                }
            }
        }
        return (d != null);
    }
}
