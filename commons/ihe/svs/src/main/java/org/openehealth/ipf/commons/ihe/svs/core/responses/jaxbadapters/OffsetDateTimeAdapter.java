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
