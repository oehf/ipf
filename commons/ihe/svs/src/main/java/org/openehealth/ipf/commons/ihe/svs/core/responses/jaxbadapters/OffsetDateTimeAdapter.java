/*
 * Copyright 2021 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.core.responses.jaxbadapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A JAXB converter between {@link String} and {@link OffsetDateTime}.
 *
 * @author Quentin Ligier
 */
public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

    // The ISO date-time formatter that formats or parses a date-time with an offset (e.g. '2011-12-03T10:15:30+01:00')
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public OffsetDateTime unmarshal(final String value) {
        return (value != null) ? OffsetDateTime.parse(value) : null;
    }

    @Override
    public String marshal(final OffsetDateTime value) {
        return (value != null) ? FORMATTER.format(value) : null;
    }
}
