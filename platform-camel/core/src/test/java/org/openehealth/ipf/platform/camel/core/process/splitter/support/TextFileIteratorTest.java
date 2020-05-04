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

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Jens Riemschneider
 */
public class TextFileIteratorTest {
    private File file; 

    @Before
    public void setUp() throws Exception {
        file = File.createTempFile("TextFileIteratorTest", "txt");

        var writer = new BufferedWriter(new FileWriter(file));
        writer.write("a,b,17,4\n");            
        writer.write("c,d,e,dotter");            
        writer.close();
    }
    
    @After
    public void tearDown() throws Exception {
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
    
    @Test(expected=UnsupportedOperationException.class)
    public void testRemoveNotSupported() throws Exception {
        var iterator = new TextFileIterator(file.getAbsolutePath());
        try {
            iterator.remove();
        }
        finally {
            assertTrue(iterator.isClosed());
        }
    }    
    
    @Test(expected=FileNotFoundException.class)
    public void testFileNotFound() throws Exception {
        new TextFileIterator("isnotthere");
    }
    
    @Test(expected=IllegalStateException.class)
    public void testSafeAbortOfIteration() throws Exception {
        var iterator = new TextFileIterator(file.getAbsolutePath());
        iterator.close();
        iterator.next();
    }
}

