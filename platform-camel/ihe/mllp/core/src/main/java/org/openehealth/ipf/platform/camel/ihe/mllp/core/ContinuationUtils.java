package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import java.util.List;

public class ContinuationUtils {

    /**
     * Appends a splitted segment to the given StringBuilder.
     */
    public static void appendSplittedSegment(StringBuilder sb, List<String> fields, char fieldSeparator) {
        for (String field : fields) {
            sb.append(field).append(fieldSeparator);
        }
        int pos = sb.length() - 1;
        while (sb.charAt(pos) == fieldSeparator) {
            sb.deleteCharAt(pos--);
        }
        sb.append('\r');
    }
 

    /**
     * Appends segments from startIndex to endIndex-1 to the given StringBuider.
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
 
    
    public static String keyString(String... pieces) {
        StringBuilder sb = new StringBuilder(pieces[0]);
        for (int i = 1; i < pieces.length; ++i) {
            sb.append('\0').append(pieces[i]);
        }
        return sb.toString();
    }

}
