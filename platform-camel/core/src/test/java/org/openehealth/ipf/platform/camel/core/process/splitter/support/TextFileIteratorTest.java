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
package org.openehealth.ipf.platform.camel.core.process.splitter.support;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Jens Riemschneider
 */
public class TextFileIteratorTest {
    private File file; 

    @BeforeEach
    public void setUp() throws Exception {
        file = File.createTempFile("TextFileIteratorTest", "txt");

        var writer = new BufferedWriter(new FileWriter(file));
        writer.write("a,b,17,4\n");            
        writer.write("c,d,e,dotter");            
        writer.close();
    }
    
    @AfterEach
    public void tearDown() {
        assertTrue(file.delete());      // If this fails the last test did not
                                        // close the FileReader in the 
                                        // TextFileIterator
    }
    
    @Test
    public void testSimpleIteration() throws Exception {
        var iterator = new TextFileIterator(file.getAbsolutePath());
        assertTrue(iterator.hasNext());
        assertEquals("a,b,17,4", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c,d,e,dotter", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testLineSplitLogic() throws Exception {
        var iterator = new TextFileIterator(
                file.getAbsolutePath(),
                new SplitStringLineSplitterLogic(","));
        
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("17", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("4", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("e", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("dotter", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHugeFile() throws Exception {
        final var NUMBER_OF_LINES = 10000;

        var hugeFile = File.createTempFile("testHugeFile", "txt");
        try {
            var writer = new BufferedWriter(new FileWriter(hugeFile));
            for (var idx = 1; idx <= NUMBER_OF_LINES; ++idx) {
                writer.write("Line " + idx + "\n");            
            }
            writer.close();
            
            System.out.println("Test file written");

            var iterator = new TextFileIterator(hugeFile.getAbsolutePath());
            var idx = 1;
            while (iterator.hasNext()) {
                var line = iterator.next();
                assertEquals("Line " + idx, line);
                ++idx;
            }
        }
        finally {
            assertTrue(hugeFile.delete());
        }
    }
    
    @Test
    public void testRemoveNotSupported() throws Exception {
        var iterator = new TextFileIterator(file.getAbsolutePath());
        try {
            assertThrows(UnsupportedOperationException.class, iterator::remove);
        }
        finally {
            assertTrue(iterator.isClosed());
        }
    }    
    
    @Test
    public void testFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> new TextFileIterator("isnotthere"));
    }
    
    @Test
    public void testSafeAbortOfIteration() throws Exception {
        var iterator = new TextFileIterator(file.getAbsolutePath());
        iterator.close();
        assertThrows(IllegalStateException.class, iterator::next);
    }
}

