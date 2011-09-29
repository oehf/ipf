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
package org.openehealth.ipf.commons.ihe.xds.core.hl7;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for rendering and parsing HL7 strings.
 * 
 * @author Jens Riemschneider
 */
public abstract class HL7 {
    private HL7() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Renders the given HL7 parts into an HL7 string.
     * <p>
     * The given HL7 separator is used to delimit the parts in the
     * result string. It is expected that all parts were escaped
     * using {@link #escape(String)} prior to calling this method.
     * @param separator
     *          the character that separates the parts in the result.
     * @param parts
     *          an array of strings that are joined together to form
     *          the result. This array can contain <code>null</code>
     *          elements which are replaced by empty strings.
     * @return the rendered string. <code>null</code> if the resulting 
     *          string would be empty.
     */
    public static String render(HL7Delimiter separator, String... parts) {
        notNull(separator, "separator cannot be null");
        notNull(parts, "parts cannot be null");
        String joined = StringUtils.join(parts, separator.getValue());
        String result = removeTrailingDelimiters(joined);
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result;
    }
    
    /**
     * Escapes a text that is used as part of an HL7 message.
     * <p>
     * This method is intended for usage with the ST data type. It is
     * NOT to be used for TX, FT or CF data types. These require additional 
     * escaping for text formatting. Other data types should not require
     * escaping because they do not allow problematic character to occur
     * in their values. 
     * @param text
     *          the text to escape.
     * @return the escaped text.
     */
    public static String escape(String text) {
        if (text == null) {
            return null;
        }
        
        // Always escape the escape character first!
        text = escape(text, HL7Delimiter.ESCAPE);
        text = escape(text, HL7Delimiter.FIELD);
        text = escape(text, HL7Delimiter.COMPONENT);
        text = escape(text, HL7Delimiter.SUBCOMPONENT);
        text = escape(text, HL7Delimiter.REPETITION);
        return text;
    }

    private static String escape(String text, HL7Delimiter delimiter) {
        return text.replace(delimiter.getValue(), delimiter.getSubstitute());
    }

    /**
     * Unescapes a text that was used as part of an HL7 message.
     * @param text
     *          the text to unescape. Can be {@code null}.
     * @return the unescaped text or {@code null} if the input was {@code null}.
     */
    public static String unescape(String text) {
        if (text == null) {
            return null;
        }
        
        // Always unescape the escape character last!
        text = unescape(text, HL7Delimiter.FIELD);
        text = unescape(text, HL7Delimiter.COMPONENT);
        text = unescape(text, HL7Delimiter.SUBCOMPONENT);
        text = unescape(text, HL7Delimiter.REPETITION);
        text = unescape(text, HL7Delimiter.ESCAPE);
        return text;
    }
    
    private static String unescape(String text, HL7Delimiter separator) {
        return text.replace(separator.getSubstitute(), separator.getValue());
    }
    
    /**
     * Removes all trailing delimiters from an HL7 string.
     * @param hl7Data
     *          the HL7 text.
     * @return the trimmed string.
     */
    public static String removeTrailingDelimiters(String hl7Data) {
        if (hl7Data == null) {
            return null;
        }
        
        if (hl7Data.isEmpty()) {
            return hl7Data;
        }
        
        int idx = hl7Data.length() - 1;
        while (idx >= 0 && isDelimiter(hl7Data.charAt(idx))) {
            --idx;
        }
        
        return hl7Data.substring(0, idx + 1);
    }

    private static boolean isDelimiter(char character) {
        return character == HL7Delimiter.COMPONENT.getValue().charAt(0)
                || character == HL7Delimiter.SUBCOMPONENT.getValue().charAt(0)
                || character == HL7Delimiter.FIELD.getValue().charAt(0)
                || character == HL7Delimiter.REPETITION.getValue().charAt(0);
    }

    /**
     * Parses an HL7 message and returns the parts delimited by the given separator.
     * <p>
     * This method does not unescape any parts. Use {@link #unescape(String)} after
     * parsing the value.
     * @param separator
     *          the separator that delimits the parts in the value.
     * @param hl7Value
     *          the value to parse. Will be treated as empty if it is <code>null</code>.
     * @return the list of parts.
     */
    public static List<String> parse(HL7Delimiter separator, String hl7Value) {
        notNull(separator, "separator cannot be null");
        
        if (hl7Value == null || hl7Value.isEmpty()) {
            return Collections.emptyList();
        }
        String[] parts = hl7Value.split("\\" + separator.getValue());
        List<String> results = Arrays.asList(parts);
        for (int idx = 0; idx < results.size(); ++idx) {
            if (results.get(idx).isEmpty()) {
                results.set(idx, null);
            }
        }
        return results;
    }

    /**
     * Safely retrieves a part from an String list.
     * <p>
     * This method ensures that indices that are not part of the list simply return
     * null and do not throw any exception. 
     * @param parts
     *          the parts of a message.
     * @param hl7Idx
     *          the index to look up. Note that HL7 indices start at position 1.
     * @param unescape
     *          <code>true</code> to call {@link #unescape(String)} for the result.
     * @return the value of the part. Can be <code>null</code> if the part is not
     *          present in the given list or empty.
     */
    public static String get(List<String> parts, int hl7Idx, boolean unescape) {
        notNull(parts, "parts cannot be null");        
        Validate.isTrue(hl7Idx >= 1, "HL7 indices must be >= 1");
        
        if (hl7Idx > parts.size()) {
            return null;
        }
        String part = parts.get(hl7Idx - 1);
        if (part == null || part.isEmpty()) {
            return null;
        }
        return unescape ? unescape(part) : part;
    }
}
