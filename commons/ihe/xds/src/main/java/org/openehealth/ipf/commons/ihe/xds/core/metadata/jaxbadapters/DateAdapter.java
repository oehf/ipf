/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * A JAXB {@link XmlAdapter} that translates between the IHE XDS date format
 * and a more native XML date format.
 * <p/>
 * Note that the variable precision nature of the IHE XDS date format makes it
 * difficult to create a conversion that can survive a round trip from IHE XDS to Java
 * date and back and come out EXACTLY the same. The current code tries to infer the
 * necessary output precision of an IHE XDS date. The date that pops out should be a
 * proper representation of the Java date but may not look EXACTLY the same as the
 * input to the round trip because there are multiple ways to represent the same thing.
 * For example, 2011030108 means the same time as 201103010800.
 */
public class DateAdapter extends XmlAdapter<Calendar, String> {
    /**
     * Converts an IHE XDS date in String format into a Java date in Calendar format.
     *
     * @param v an IHE XDS date in String format
     * @return a Java date in Calendar format
     * @throws Exception
     */
    @Override
    public Calendar marshal(String v) throws Exception {
        if (v == null)
            return null;

        String[] parts = v.split("[-+]");

        // Isolate the core date portion.
        String dateString = parts[0];
        if (dateString.length() > 19)
            throw new IllegalArgumentException();
        if (dateString.length() > 14 && dateString.charAt(14) != '.')
            throw new IllegalArgumentException();

        // Isolate the offset portion (if it exists).
        String offsetString = parts.length > 1 ? parts[1] : null;
        if (offsetString != null && offsetString.length() != 4)
            throw new IllegalArgumentException();

        // Calculate the time zone. It defaults to GMT.
        int offsetInMilliseconds = getOffset(parts.length > 1 ? parts[1] : null, v.contains("-"));
        TimeZone timeZone = new SimpleTimeZone(offsetInMilliseconds, offsetString == null ? "GMT" : offsetString);

        // Transfer fields from the IHE date to the Calendar.
        GregorianCalendar calendar = new GregorianCalendar(timeZone);
        calendar.set(Calendar.YEAR, getFieldValue(dateString, 0, 4));
        int month = getFieldValue(dateString, 4, 2);
        calendar.set(Calendar.MONTH, month == 0 ? 0 : month - 1); // Calendar value is zero-based
        int day = getFieldValue(dateString, 6, 2);
        calendar.set(Calendar.DAY_OF_MONTH, day == 0 ? 1 : day);
        calendar.set(Calendar.HOUR_OF_DAY, getFieldValue(dateString, 8, 2));
        calendar.set(Calendar.MINUTE, getFieldValue(dateString, 10, 2));
        calendar.set(Calendar.SECOND, getFieldValue(dateString, 12, 2));
        calendar.set(Calendar.MILLISECOND, getLastFieldValue(dateString, 15, 4) / 10);
        return calendar;
    }

    /**
     * Converts a Java date in Calendar format into an IHE XDS date in String format.
     *
     * @param v a Java date in Calendar format
     * @return an IHE XDS date in String format
     * @throws Exception
     */
    @Override
    public String unmarshal(Calendar v) throws Exception {
        String mainPart =
                String.format(
                        getFormatForSignificantPart(v),
                        v.get(Calendar.YEAR),
                        v.get(Calendar.MONTH) + 1,
                        v.get(Calendar.DAY_OF_MONTH),
                        v.get(Calendar.HOUR_OF_DAY),
                        v.get(Calendar.MINUTE),
                        v.get(Calendar.SECOND),
                        v.get(Calendar.MILLISECOND));

        if (v.getTimeZone().getRawOffset() != 0) {
            String offsetPart = String.format("%+05d", v.getTimeZone().getRawOffset());
            return mainPart + offsetPart;
        } else
            return mainPart;
    }

    private int getOffset(String offsetString, boolean negative) {
        if (offsetString != null) {

            int hours = getFieldValue(offsetString, 0, 2);
            int minutes = getFieldValue(offsetString, 2, 2);
            return (((hours * 60) + minutes) * 60 * 1000) * (negative ? -1 : 1);
        } else
            return 0;
    }

    private int getFieldValue(String input, int fieldOffset, int fieldLength) {
        if (input.length() < (fieldOffset + fieldLength))
            return 0;

        return Integer.valueOf(input.substring(fieldOffset, fieldOffset + fieldLength));
    }

    private int getLastFieldValue(String input, int fieldOffset, int maximumLength) {
        int remainingLength = input.length() - fieldOffset;
        if (remainingLength <= 0)
            return 0;

        int value = Integer.valueOf(input.substring(fieldOffset, fieldOffset + remainingLength));
        for (int i = remainingLength; i < maximumLength; i++)
            value *= 10;

        return value;
    }

    private String getFormatForSignificantPart(Calendar value) {
        if (value.get(Calendar.MILLISECOND) > 0)
            return "%04d%02d%02d%02d%02d%02d.%03d";
        if (value.get(Calendar.SECOND) > 0)
            return "%04d%02d%02d%02d%02d%02d";
        if (value.get(Calendar.MINUTE) > 0)
            return "%04d%02d%02d%02d%02d";
        if (value.get(Calendar.HOUR_OF_DAY) > 0)
            return "%04d%02d%02d%02d";
        if (value.get(Calendar.DAY_OF_MONTH) > 1)
            return "%04d%02d%02d";
        if (value.get(Calendar.MONTH) > 0)
            return "%04d%02d";

        return "%04d";
    }
}
