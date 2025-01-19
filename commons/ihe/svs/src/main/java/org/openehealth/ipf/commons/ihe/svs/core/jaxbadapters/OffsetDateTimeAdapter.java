/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.core.jaxbadapters;

import jakarta.xml.bind.DatatypeConverter;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

/**
 * A JAXB converter between {@link String} and {@link OffsetDateTime}.
 *
 * @author Quentin Ligier
 */
public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime unmarshal(final String value) {
        if (value == null) {
            return null;
        }
        var calendar = DatatypeConverter.parseDateTime(value);
        if (calendar instanceof GregorianCalendar) {
            return ((GregorianCalendar) calendar).toZonedDateTime().toOffsetDateTime();
        }
        else {
            return OffsetDateTime.ofInstant(Instant.ofEpochMilli(calendar.getTimeInMillis()),
                                            calendar.getTimeZone().toZoneId());
        }
    }

    @Override
    public String marshal(final OffsetDateTime value) {
        return (value != null) ? DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value) : null;
    }
}