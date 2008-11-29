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

import static org.junit.Assert.*;

import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.process.splitter.SplitIndex;
import org.openehealth.ipf.platform.camel.core.process.splitter.Splitter;


/**
 * @author Jens Riemschneider
 */
public class SplitIndexTest {
    @Test
    public void testValueOf() {
        SplitIndex index = SplitIndex.valueOf(14, true);
        assertEquals(14, index.getIndex());
        assertEquals(true, index.isLast());
    }
    
    @Test
    public void testEquals() {
        SplitIndex index1a = SplitIndex.valueOf(4, false);
        SplitIndex index1b = SplitIndex.valueOf(4, false);
        SplitIndex index2 = SplitIndex.valueOf(5, false);
        SplitIndex index3 = SplitIndex.valueOf(4, true);
        
        assertTrue("Should be equal", index1a.equals(index1b));
        assertFalse("Should not be equal", index1a.equals(index2));
        assertFalse("Should not be equal", index1a.equals(index3));
        
        assertFalse("Null is never equal", index1a.equals(null));
        assertFalse("Unrelated class is never equal", index1a.equals(this));
        
        assertTrue("Same instances should be equal", index1a.equals(index1a));
    }
    
    @Test
    public void testHashCode() {
        SplitIndex index1a = SplitIndex.valueOf(4, false);
        SplitIndex index1b = SplitIndex.valueOf(4, false);
        SplitIndex index2 = SplitIndex.valueOf(5, false);
        SplitIndex index3 = SplitIndex.valueOf(4, true);
        
        assertEquals(index1a.hashCode(), index1b.hashCode());
        
        // Not necessary, but hash code shouldn't be too weak 
        assertTrue(index1a.hashCode() != index2.hashCode());
        assertTrue(index1a.hashCode() != index3.hashCode());
    }
    
    @Test
    public void testToString() {
        SplitIndex index = SplitIndex.valueOf(14, true);
        String str = index.toString();
        assertTrue("Class name missing", str.contains(Splitter.class.getName()));
        assertTrue("index missing", str.contains("14"));
        assertTrue("last flag missing", str.contains("true"));
    }
}
