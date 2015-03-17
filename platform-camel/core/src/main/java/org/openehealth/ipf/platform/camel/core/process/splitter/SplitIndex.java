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
package org.openehealth.ipf.platform.camel.core.process.splitter;


/**
 * Immutable class specifying an index of a split performed by {@link Splitter}.
 * This class contains all necessary information about the an index of an
 * exchange that was split off. This includes the actual index as well as a
 * flag indicating that the index is the last in the collection of sub exchanges.
 * 
 * - Immutable
 * - fully thread-safe
 * - not for subclassing
 * - uses static factory methods
 * 
 * @author Jens Riemschneider
 */
public final class SplitIndex {
    private final int index;
    private final boolean last;    

    /**
     * Static factory method to return a split index
     * 
     * @param index 
     *          actual Index of an exchange in the corresponding collection of 
     *          sub exchanges 
     * @param last  
     *          {@code true} if this index is the last in the corresponding 
     *          collection of sub exchanges
     *           
     * @return A {@link SplitIndex} corresponding to the index information
     */
    public static SplitIndex valueOf(int index, boolean last) {
        return new SplitIndex(index, last);
    }
    
    /** @return the index in the corresponding collection of sub exchanges
     */
    public int getIndex() {
        return index;
    }

    /** @return {@code true} if this index is the last in the corresponding 
     *          collection of sub exchanges
     */
    public boolean isLast() {
        return last;
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
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SplitIndex other = (SplitIndex) obj;
        if (index != other.index)
            return false;
        return last == other.last;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + Splitter.class.getName() + ": index=" + index + ", last=" + last + ")";
    }

    // Please use factory methods instead of constructor {@link #valueOf}
    private SplitIndex(int index, boolean last) {
        this.index = index;
        this.last = last;
    }
}
