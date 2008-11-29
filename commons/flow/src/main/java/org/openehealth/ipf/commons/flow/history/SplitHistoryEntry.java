/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.flow.history;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a single message split as part of a history of splits. 
 * 
 * @author Martin Krasser
 * 
 * @see SplitHistory
 */
@SuppressWarnings("serial")
public class SplitHistoryEntry implements Serializable {

    /**
     * A singleton representing the root entry of a {@link SplitHistory}. 
     */
    public static final SplitHistoryEntry ROOT = new SplitHistoryEntry(0, 1); 
    
    private static final String NODE_OPEN = "(";
    private static final String NODE_CLOSE = ")";
    private static final String NODE_SEP = "/";
    private static final String NODE_LAST = "L";
    
    private static final String OLD_PATTERN = 
        "^\\" + NODE_OPEN + "[0-9]+" + NODE_SEP + "[0-9]+\\" + NODE_CLOSE + "$";
    
    private static final String CUR_PATTERN =  
        "^\\" + NODE_OPEN + "([0-9]+)" + "(" + NODE_LAST + "?)\\" + NODE_CLOSE + "$";
    
    private int index;
    private boolean last;
    
    /**
     * Creates a new {@link SplitHistoryEntry}.
     * 
     * @param index
     *            split index. The first split result has index 0, the second
     *            has index 1 ... the last split result has index
     *            <code>numSiblings</code> - 1.
     * @param numSiblings
     *            number of siblings. This represents the number of messages
     *            split from a (parent) message.
     */
    public SplitHistoryEntry(int index, int numSiblings) {
        if (index < 0) {
            throw new IllegalArgumentException("condition index >= 0 violated");
        }
        if (index >= numSiblings) {
            throw new IllegalArgumentException("condition index < numSiblings violated");
        }
        this.index = index;
        this.last = (index + 1) == numSiblings;
    }
    
    /**
     * Creates a new {@link SplitHistoryEntry}.
     * 
     * @param index
     *            split index. The first split result has index 0, the second
     *            has index 1 ...
     * @param last
     *            last in result list. <code>true</code> if the entry is the last
     *            in the split result list.
     */
    public SplitHistoryEntry(int index, boolean last) {
        if (index < 0) {
            throw new IllegalArgumentException("condition index >= 0 violated");
        }
        this.index = index;
        this.last = last;
    }

    /**
     * Returns the split index of this entry.
     * 
     * @return the split index of this entry.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns <code>true</code> if this entry is the first compared to its
     * siblings.
     * 
     * @return <code>true</code> if this entry is the first compared to its
     * siblings.
     */
    public boolean isFirst() {
        return index == 0;
    }
    
    /**
     * Returns <code>true</code> if this entry is the last compared to its
     * siblings.
     * 
     * @return <code>true</code> if this entry is the last compared to its
     * siblings.
     */
    public boolean isLast() {
        return last;
    }
    
    /**
     * Returns <code>true</code> if this entry is the predecessor of the given
     * entry. Only the entry's <code>index</code> fields are compared.
     * 
     * @param entry a split history entry
     * @return <code>true</code> if this entry is the predecessor of the given
     * entry. 
     */
    public boolean isPredecessor(SplitHistoryEntry entry) {
        return (index + 1) == entry.index;
    }
    
    /**
     * Returns <code>true</code> if this entry is the successor of the given
     * entry. Only the entry's <code>index</code> fields are compared.
     * 
     * @param entry a split history entry
     * @return <code>true</code> if this entry is the successor of the given
     * entry. 
     */
    public boolean isSuccessor(SplitHistoryEntry entry) {
        return (index - 1) == entry.index;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SplitHistoryEntry)) {
            return false;
        }
        SplitHistoryEntry n = (SplitHistoryEntry)obj;
        return (index == n.index) && (last == n.last);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + (last ? 1231 : 1237);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer()
            .append(NODE_OPEN)
            .append(index)
            .append(last ? NODE_LAST : "")
            .append(NODE_CLOSE).toString();
    }
    
    /**
     * Parses the <code>entry</code> string and returns a
     * {@link SplitHistoryEntry} object.
     * 
     * @param history
     *            entry string to parse.
     * @return parsed {@link SplitHistoryEntry} object.
     */
    public static SplitHistoryEntry parse(String entry) {
        if (entry.matches(CUR_PATTERN)) {
            return parseCurrentVersion(entry);
        }
        
        if (!entry.matches(OLD_PATTERN)) {
            throw new SplitHistoryFormatException("Entry '" + entry + "' doesn't match pattern '" + CUR_PATTERN + "' or any previous version");
        }
        int idx = entry.indexOf('/');
        int n1 = Integer.parseInt(entry.substring(1, idx));        
        int n2 = Integer.parseInt(entry.substring(idx + 1, entry.length() - 1)); 
        return new SplitHistoryEntry(n1, n2);
    }

    private static SplitHistoryEntry parseCurrentVersion(String entry) {
        Pattern pattern = Pattern.compile(CUR_PATTERN);        
        Matcher matcher = pattern.matcher(entry);
        if (!matcher.matches() || matcher.groupCount() > 2) {
            return null;
        }
        
        int index = Integer.parseInt(matcher.group(1));
        boolean last = matcher.group(2).equals(NODE_LAST);        
        
        return new SplitHistoryEntry(index, last);
    }    
}
