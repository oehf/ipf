package org.openehealth.ipf.commons.ihe.svs.core.responses.jaxbadapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.OffsetDateTime;

/**
 * A JAXB converter between {@link String} and {@link OffsetDateTime}.
 *
 * @author Quentin Ligier
 */
public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime unmarshal(final String value) {
        return (value != null) ? OffsetDateTime.parse(value) : null;
    }

    @Override
    public String marshal(final OffsetDateTime value) {
        return (value != null) ? value.toString() : null;
    }
}
