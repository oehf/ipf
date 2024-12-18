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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Jens Riemschneider
 */
public class SplitIndexTest {
    @Test
    public void testValueOf() {
        var index = SplitIndex.valueOf(14, true);
        assertEquals(14, index.getIndex());
        assertTrue(index.isLast());
    }
    
    @Test
    public void testEquals() {
        var index1a = SplitIndex.valueOf(4, false);
        var index1b = SplitIndex.valueOf(4, false);
        var index2 = SplitIndex.valueOf(5, false);
        var index3 = SplitIndex.valueOf(4, true);

        assertEquals(index1a, index1b, "Should be equal");
        assertNotEquals(index1a, index2, "Should not be equal");
        assertNotEquals(index1a, index3, "Should not be equal");

        assertNotEquals(null, index1a, "Null is never equal");
        assertNotEquals(this, index1a, "Unrelated class is never equal");
    }
    
    @Test
    public void testHashCode() {
        var index1a = SplitIndex.valueOf(4, false);
        var index1b = SplitIndex.valueOf(4, false);
        var index2 = SplitIndex.valueOf(5, false);
        var index3 = SplitIndex.valueOf(4, true);
        
        assertEquals(index1a.hashCode(), index1b.hashCode());
        
        // Not necessary, but hash code shouldn't be too weak 
        assertTrue(index1a.hashCode() != index2.hashCode());
        assertTrue(index1a.hashCode() != index3.hashCode());
    }
    
    @Test
    public void testToString() {
        var index = SplitIndex.valueOf(14, true);
        var str = index.toString();
        assertTrue(str.contains(Splitter.class.getName()), "Class name missing");
        assertTrue(str.contains("14"), "index missing");
        assertTrue(str.contains("true"), "last flag missing");
    }
}
