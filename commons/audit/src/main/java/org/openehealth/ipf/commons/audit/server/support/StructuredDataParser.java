/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.server.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.STRUCTURED_DATA_ID;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.STRUCTURED_DATA_PARAMS;

/**
 * Single-pass parser for the STRUCTURED-DATA part of an RFC 5424 syslog message:
 * <pre>
 * STRUCTURED-DATA = NILVALUE / 1*SD-ELEMENT
 * SD-ELEMENT      = "[" SD-ID *(SP SD-PARAM) "]"
 * SD-PARAM        = PARAM-NAME "=" %d34 PARAM-VALUE %d34
 * </pre>
 * Inside PARAM-VALUE, the characters '"', '\' and ']' are escaped with a backslash; these escapes are
 * resolved. Unlike the regular expressions used before, the parser runs in linear time regardless of
 * the input, so that a peer cannot stall the audit receiver with crafted messages.
 *
 * @author Christian Ohr
 * @since 6.1
 */
final class StructuredDataParser {

    private static final char NIL = '-';

    /**
     * @param elements the parsed SD-ELEMENTs, each a map with SD-ID and params
     * @param end      index of the first character after the STRUCTURED-DATA part
     */
    record Result(List<Map<String, Object>> elements, int end) {
    }

    private final String s;
    private int pos;

    private StructuredDataParser(String s, int pos) {
        this.s = s;
        this.pos = pos;
    }

    /**
     * Parses the STRUCTURED-DATA part starting at the given index.
     *
     * @param s     the syslog message
     * @param start index where STRUCTURED-DATA begins
     * @return parsed elements and the index after the STRUCTURED-DATA part
     * @throws IllegalArgumentException if the structured data is malformed
     */
    static Result parse(String s, int start) {
        return new StructuredDataParser(s, start).parse();
    }

    private Result parse() {
        if (pos < s.length() && s.charAt(pos) == NIL) {
            return new Result(List.of(), pos + 1);
        }
        var elements = new ArrayList<Map<String, Object>>();
        do {
            elements.add(element());
        } while (pos < s.length() && s.charAt(pos) == '[');
        return new Result(elements, pos);
    }

    private Map<String, Object> element() {
        expect('[');
        var sdId = name("SD-ID");
        var params = new LinkedHashMap<String, String>();
        while (pos < s.length() && s.charAt(pos) == ' ') {
            pos++;
            var key = name("parameter name");
            expect('=');
            expect('"');
            var value = value();
            if (params.putIfAbsent(key, value) != null) {
                throw new IllegalArgumentException("Duplicate parameter key: " + key + " in SD-ID: " + sdId);
            }
        }
        expect(']');
        var element = new LinkedHashMap<String, Object>();
        element.put(STRUCTURED_DATA_ID, sdId);
        element.put(STRUCTURED_DATA_PARAMS, params);
        return element;
    }

    /**
     * SD-NAME: printable US-ASCII except '=', SP, ']' and '"'. '[' is rejected as well, as before.
     */
    private String name(String what) {
        var begin = pos;
        while (pos < s.length()) {
            var c = s.charAt(pos);
            if (c <= ' ' || c > '~' || c == '=' || c == ']' || c == '"' || c == '[') {
                break;
            }
            pos++;
        }
        if (pos == begin) {
            throw new IllegalArgumentException("Missing " + what + " in structured data at position " + begin);
        }
        return s.substring(begin, pos);
    }

    /**
     * PARAM-VALUE up to and including the closing quote, with escapes resolved.
     */
    private String value() {
        var sb = new StringBuilder();
        while (pos < s.length()) {
            var c = s.charAt(pos++);
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\' && pos < s.length()) {
                var next = s.charAt(pos);
                if (next == '"' || next == '\\' || next == ']') {
                    sb.append(next);
                    pos++;
                    continue;
                }
            }
            sb.append(c);
        }
        throw new IllegalArgumentException("Unterminated parameter value in structured data");
    }

    private void expect(char c) {
        if (pos >= s.length() || s.charAt(pos) != c) {
            throw new IllegalArgumentException("Expected '" + c + "' in structured data at position " + pos);
        }
        pos++;
    }
}
