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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Jens Riemschneider
 */
public class SplitHistoryEntryCurrentVersionTest {

    private SplitHistoryEntry lastEntry;
    private SplitHistoryEntry entry;
    
    @Before
    public void setUp() throws Exception {
        lastEntry = new SplitHistoryEntry(2, 3);
        entry = new SplitHistoryEntry(1, 3);
    }

    @Test
    public void testHashcode() {
        SplitHistoryEntry entry1a = new SplitHistoryEntry(1, false);
        SplitHistoryEntry entry1b = new SplitHistoryEntry(1, false);
        SplitHistoryEntry entry2 = new SplitHistoryEntry(1, true);
        SplitHistoryEntry entry3 = new SplitHistoryEntry(2, false);
        
        assertEquals(entry1a.hashCode(), entry1b.hashCode());   

        // Hash codes do not necessarily have to be different, but the algorithm
        // should be good enough that the test values lead to different hashcodes
        assertFalse("Two different entries should have different hash codes", 
                entry1a.hashCode() == entry2.hashCode());
        
        assertFalse("Two different entries should have different hash codes",
                entry1a.hashCode() == entry3.hashCode());
    }

    @Test
    public void testEquals() {
        SplitHistoryEntry entry1a = new SplitHistoryEntry(1, false);
        SplitHistoryEntry entry1b = new SplitHistoryEntry(1, false);
        SplitHistoryEntry entry2 = new SplitHistoryEntry(1, true);
        SplitHistoryEntry entry3 = new SplitHistoryEntry(2, false);
        SplitHistoryEntry entry4 = new SplitHistoryEntry(1, 3);
        SplitHistoryEntry entry5 = new SplitHistoryEntry(1, 2);
        
        assertTrue("instance should be equal to itself", 
                entry1a.equals(entry1a));
        assertTrue("different instances with same content should be equal", 
                entry1a.equals(entry1b));
        assertFalse("different content should not be equal", 
                entry1a.equals(entry2));
        assertFalse("different content should not be equal", 
                entry1a.equals(entry3));
        assertTrue("same content should be equal", 
                entry1a.equals(entry4));
        assertFalse("different content should not be equal", 
                entry4.equals(entry5));       
        assertTrue("same content should be equal", 
                entry2.equals(entry5)); 
        assertFalse("null should not be equal to anything", 
                entry1a.equals(null));
        assertFalse("an unrelated type should never be equal", 
                entry1a.equals(this));
    }

    @Test
    public void testParse() {
        assertEquals(lastEntry, SplitHistoryEntry.parse("(2L)"));
        assertEquals(entry, SplitHistoryEntry.parse("(1)"));
    }

    @Test(expected = SplitHistoryFormatException.class)
    public void testParseFailNumber() {
        SplitHistoryEntry.parse("(m)");
    }

    @Test(expected = SplitHistoryFormatException.class)
    public void testParseFailLastNode() {
        SplitHistoryEntry.parse("(3m)");
    }
}
