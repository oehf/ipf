/*
 * Copyright 2011 InterComponentWare AG.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Helper methods for segment fragmentation, unsolicited request fragmentation,
 * and interactive response continuation in HL7v2-based transactions.
 *
 * @author Dmytro Rud
 */
public class FragmentationUtils {

    /**
     * Splits the given String at occurrences of the given character.
     */
    public static List<String> splitString(String s, char c) {
        List<String> result = new ArrayList<String>();
        int startPos = 0;
        while (true) {
            int pos = s.indexOf(c, startPos);
            if (pos == -1) {
                break;
            }
            result.add(s.substring(startPos, pos));
            startPos = pos + 1;
        }
        if (startPos != s.length()) {
            result.add(s.substring(startPos, s.length()));
        }
        return result;
    }


    /**
     * Ensures that all segments in the given HL7 message string representation
     * are not longer than the given value (-1 means positive infinity).
     * If needed, splits long segments by means of ADD segments, as described
     * in paragraph 2.10.2.1 of the HL7 v.2.5 specification.
     * <p>
     * <code>'\r'<code> characters are not considered in the length computation.
     * @param message
     *      string representation of the source HL7 message.
     * @param maxLength
     *      maximal segment length, must be either -1 or greater than 4.
     * @return
     *      string representation of a semantically equivalent message,
     *      whose segments are not longer than the given value.
     */
    public static String ensureMaximalSegmentsLength(String message, int maxLength) {
        if (maxLength == -1) {
            return message;
        }
        if (maxLength <= 4) {
            throw new IllegalArgumentException("maximal length must be greater than 4");
        }
        List<String> segments = splitString(message, '\r');

        // check whether we have to do anything
        boolean needProcess = false;
        for (String segment : segments) {
            if (segment.length() > maxLength) {
                needProcess = true;
                break;
            }
        }
        if ( ! needProcess) {
            return message;
        }

        // process segments
        StringBuilder sb = new StringBuilder();
        String prefix = "ADD" + message.charAt(3);
        int restLength = maxLength - prefix.length();
        for (String segment : segments) {
            // short segment
            if (segment.length() <= maxLength) {
                sb.append(segment).append('\r');
                continue;
            }

            // first part of the long segment
            sb.append(segment.substring(0, maxLength)).append('\r');
            // parts 2..n-1 of the long segment
            int startPos;
            for (startPos = maxLength; startPos + restLength <= segment.length(); startPos += restLength) {
                sb.append(prefix)
                  .append(segment.substring(startPos, startPos + restLength))
                  .append('\r');
            }
            // part n of the long segment
            if (startPos < segment.length()) {
                sb.append(prefix).append(segment.substring(startPos)).append('\r');
            }
        }
        return sb.toString();
    }


    /**
     * Appends a split segment to the given StringBuilder.
     */
    public static void appendSplitSegment(StringBuilder sb, List<String> fields, char fieldSeparator) {
        for (String field : fields) {
            sb.append(field).append(fieldSeparator);
        }
        for (int len = sb.length(); sb.charAt(--len) == fieldSeparator; ) {
            sb.setLength(len);
        }
        sb.append('\r');
    }


    /**
     * Appends segments from startIndex to endIndex-1 to the given StringBuilder.
     */
    public static void appendSegments(StringBuilder sb, List<String> segments, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; ++i) {
            sb.append(segments.get(i)).append('\r');
        }
    }


    /**
     * Joins segments from startIndex to endIndex-1.
     */
    public static CharSequence joinSegments(List<String> segments, int startIndex, int endIndex) {
        StringBuilder sb = new StringBuilder();
        appendSegments(sb, segments, startIndex, endIndex);
        return sb;
    }


    /**
     * Creates a single key string from the given key pieces.
     */
    public static String keyString(String... pieces) {
        StringBuilder sb = new StringBuilder(pieces[0]);
        for (int i = 1; i < pieces.length; ++i) {
            sb.append('\0').append(pieces[i]);
        }
        return sb.toString();
    }


    /**
     * Returns an unique value which can be used, for example, as an HL7v2 message ID.
     */
    public static String uniqueId() {
        return UUID.randomUUID().toString();
    }
}
