package org.openehealth.ipf.modules.hl7.parser;

import ca.uhn.hl7v2.parser.EncodingCharacters;

/**
 * Fixes https://github.com/hapifhir/hapi-hl7v2/issues/7, which misses to properly encode segment
 * terminator characters.
 *
 * @author Christian Ohr
 */
public class DefaultEscaping extends ca.uhn.hl7v2.parser.DefaultEscaping {

    public static final DefaultEscaping INSTANCE = new DefaultEscaping();

    @Override
    public String escape(String text, EncodingCharacters encChars) {
        String fixed = text.replace("\r", "\\X000d\\");
        return super.escape(fixed, encChars);
    }

}
