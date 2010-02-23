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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tracks the split history of a message. Message splits are represented by
 * {@link SplitHistoryEntry}s. Each message split adds a new
 * {@link SplitHistoryEntry} to a message's split history. An aggregation of
 * previously split messages removes {@link SplitHistoryEntry}s from those
 * messages. Split-aggregate pairs are therefore not visible in a message's
 * split history. A split history containing only the
 * {@link SplitHistoryEntry#ROOT} entry means that the message hasn't been split
 * yet (or an aggregation compensated the first split).
 * 
 * @author Martin Krasser
 */
@SuppressWarnings("serial")
public class SplitHistory implements Comparable<SplitHistory>, Serializable {

    /**
     * A singleton representing the history of a non-split message. 
     */
    public static final SplitHistory ROOT = new SplitHistory();
    
    private static final String PATH_OPEN = "[";
    private static final String PATH_CLOSE = "]";
    private static final String PATH_SEP = ",";
    
    private static final String PATTERN = "^\\" + PATH_OPEN + ".*\\" + PATH_CLOSE + "$"; 
    
    private final List<SplitHistoryEntry> entries;

    /**
     * Creates a new split history with a {@link SplitHistoryEntry#ROOT} entry.
     */
    public SplitHistory() {
        this(true);
    }
    
    /**
     * Creates a new split history with a {@link SplitHistoryEntry#ROOT} entry.
     * 
     * @param initialCapacity initial capacity of the entries list.
     */
    public SplitHistory(int initialCapacity) {
        this(initialCapacity, true);
    }
    
    private SplitHistory(boolean addRoot) {
        this(3, addRoot);
    }
    
    private SplitHistory(int initialCapacity, boolean addRoot) {
        entries = new ArrayList<SplitHistoryEntry>(initialCapacity);
        if (addRoot) {
            entries.add(SplitHistoryEntry.ROOT);
        }
    }
    
    /**
     * Returns the number of history entries.
     * 
     * @return the number of history entries.
     */
    public int size() {
        return entries.size();
    }
    
    /**
     * Returns an unmodifiable list of history entries.
     * 
     * @return an unmodifiable list of history entries.
     */
    public List<SplitHistoryEntry> entries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Returns an array containing the index part of the split history entries.
     * 
     * @return an array containing the index part of the split history entries.
     */
    public int[] indexPath() {
        int[] result = new int[entries.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = entries.get(i).getIndex();
        }
        return result;
    }
    
    /**
     * Returns a string representation of the {@link #indexPath()}.
     * 
     * @return a string representation of the {@link #indexPath()}.
     */
    public String indexPathString() {
        StringBuffer result = new StringBuffer();
        int[] ip = indexPath();
        for (int i = 0; i < ip.length - 1; i++) {
            result.append(Integer.toString(ip[i]));
            result.append('.');
        }
        result.append(Integer.toString(ip[ip.length - 1]));
        return result.toString();
    }
    
    /**
     * Splits this history into <code>num</code> result histories. The result
     * histories represent the split of a single message into <code>num</code>
     * split messages.
     * 
     * @param num
     *            number of splits.
     * @throws IllegalArgumentException
     *             if num is less than 2.
     * @return a result split history array of length <code>num</code>.
     */
    public SplitHistory[] split(int num) {
        if (num < 2) {
            throw new IllegalArgumentException("num argument must be greater than 1");
        }
        SplitHistory[] result = new SplitHistory[num];        
        for (int i = 0; i < num; i++) {
            boolean last = (i == num - 1);
            result[i] = split(i, last);
        }
        return result;
    }
    
    /**
     * Splits the history for a single message
     * A split creates multiple messages from a single message. The history
     * returned by this method represents the history for one message created
     * by the split
     * 
     * @param index
     *            split index. The first split result has index 0, the second
     *            has index 1 ...
     * @param last
     *            last in result list. <code>true</code> if the entry is the last
     *            in the split result list.
     *            
     * @return the split history for the sub exchange
     */
    public SplitHistory split(int index, boolean last) {
        SplitHistory result = new SplitHistory(false);
        result.entries.addAll(entries);
        result.entries.add(new SplitHistoryEntry(index, last));
        return result;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SplitHistory history) {
        int ts = size();
        int ps = history.size();
        for (int i = 0; i < Math.min(ts, ps); i++) {
            SplitHistoryEntry tn = entries.get(i);
            SplitHistoryEntry pn = history.entries.get(i);
            if (tn.getIndex() != pn.getIndex()) {
                return tn.getIndex() - pn.getIndex();
            }
        }
        return ts - ps;
    }
    
    /**
     * Returns <code>true</code> if a message represented by this split
     * history is the immediate predecessor of the given split history.
     * 
     * @param history
     *            split history.
     * @return <code>true</code> if this split history is the immediate
     *         predecessor of the given split history.
     */
    public boolean isPredecessor(SplitHistory history) {
        int ts = size();
        int ps = history.size();
        int last = -1;
        SplitHistoryEntry tn = null;
        SplitHistoryEntry pn = null;
        // walk along common path from root
        for (int i = 0; i < Math.min(ts, ps); i++) {
            last = i;
            tn = entries.get(i);
            pn = history.entries.get(i);
            if (tn.getIndex() != pn.getIndex()) {
                break;
            }
        }
        if (!tn.isPredecessor(pn)) {
            return false;
        }
        if (!isLastUpTo(last)) {
            return false;
        }
        if (!history.isFirstUpTo(last)) {
            return false;
        }
        return true;

    }

    /**
     * Returns <code>true</code> if a message represented by this split
     * history is the immediate successor of the given split history.
     * 
     * @param history
     *            split history.
     * @return <code>true</code> if this split history is the immediate
     *         successor of the given split history.
     */
    public boolean isSuccessor(SplitHistory history) {
        int ts = size();
        int ps = history.size();
        int last = -1;
        SplitHistoryEntry tn = null;
        SplitHistoryEntry pn = null;
        // walk along common path from root
        for (int i = 0; i < Math.min(ts, ps); i++) {
            last = i;
            tn = entries.get(i);
            pn = history.entries.get(i);
            if (tn.getIndex() != pn.getIndex()) {
                break;
            }
        }
        if (!tn.isSuccessor(pn)) {
            return false;
        }
        if (!isFirstUpTo(last)) {
            return false;
        }
        if (!history.isLastUpTo(last)) {
            return false;
        }
        return true;
        
    }

    /**
     * Returns <code>true</code> if the sub-history starting at entry
     * <code>level</code> (exclusive) down to the latest entry represents a
     * message that was created first in a series of splits. If the sub-history
     * size is 0 then always <code>true</code> will be returned.
     * 
     * @param level
     *            level or position in the split history (pointing to an entry)
     *            from where to start analysis.
     * @return <code>true</code> if the sub-history represents a message that
     *         was created first in a series of splits.
     */
    public boolean isFirstUpTo(int level) {
        if ((entries.size() - level) < 1) {
            throw new IllegalArgumentException("level must be less than entries size");
        }
        for (int i = entries.size() - 1; i > level; i--) {
            if (!entries.get(i).isFirst()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns <code>true</code> if the sub-history starting at entry
     * <code>level</code> (exclusive) down to the latest entry represents a
     * message that was created last in a series of splits. If the sub-history
     * size is 0 then always <code>true</code> will be returned.
     * 
     * @param level
     *            level or position in the split history (pointing to an entry)
     *            from where to start analysis.
     * @return <code>true</code> if the sub-history represents a message that
     *         was created last in a series of splits.
     */
    public boolean isLastUpTo(int level) {
        if ((entries.size() - level) < 1) {
            throw new IllegalArgumentException("level must be less than entries size");
        }
        for (int i = entries.size() - 1; i > level; i--) {
            if (!entries.get(i).isLast()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns <code>true</code> if this history represents a message that was
     * created first in a series of splits.
     * 
     * @return <code>true</code> if this history represents a message that was
     *         created first in a series of splits.
     */
    public boolean isFirst() {
        return isFirstUpTo(0);
    }
    
    /**
     * Returns <code>true</code> if this history represents a message that was
     * created last in a series of splits.
     * 
     * @return <code>true</code> if this history represents a message that was
     *         created last in a series of splits.
     */
    public boolean isLast() {
        return isLastUpTo(0);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SplitHistory)) {
            return false;
        }
        SplitHistory p = (SplitHistory)obj;
        return entries.equals(p.entries);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    /**
	 * Returns a String in the form [(index/x), ... ], where x is the index of a
	 * num-split (result of method
	 * <code>split(int num)<code>) with num number of splits; 0<= x < num
	 */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(PATH_OPEN);
        for (int i = 0; i < entries.size() - 1; i++) {
            buf.append(entries.get(i));
            buf.append(PATH_SEP);
        }
        buf.append(entries.get(entries.size() - 1));
        buf.append(PATH_CLOSE);
        return buf.toString();
    }
    
    /**
     * Parses the <code>history</code> string and returns a
     * {@link SplitHistory} object.
     * 
     * @param history
     *            history string to parse.
     * @return parsed {@link SplitHistory} object.
     */
    public static SplitHistory parse(String history) {
        if (!history.matches(PATTERN)) {
            throw new SplitHistoryFormatException("History '" + history + "' doesn't match pattern '" + PATTERN + "'");
        }
        String p = history.substring(1, history.length() - 1);
        String[] na = p.split(",");
        SplitHistory result = new SplitHistory(na.length + 2, false);
        for (int i = 0; i < na.length; i++) {
            SplitHistoryEntry n = SplitHistoryEntry.parse(na[i].trim());
            if (i == 0) {
                validateRoot(n);
            }
            result.entries.add(n);
        }
        return result;
    }
    
    private static void validateRoot(SplitHistoryEntry node) {
        if (!node.equals(SplitHistoryEntry.ROOT)) {
            throw new IllegalArgumentException("First entry must be " + SplitHistoryEntry.ROOT.toString());
        }
    }
    
}
