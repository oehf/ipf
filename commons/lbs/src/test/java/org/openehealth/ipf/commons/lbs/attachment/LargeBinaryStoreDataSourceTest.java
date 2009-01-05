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
package org.openehealth.ipf.commons.lbs.attachment;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.junit.Test;
import org.openehealth.ipf.commons.lbs.store.MemoryStore;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;

/**
 * @author Jens Riemschneider
 */
public class LargeBinaryStoreDataSourceTest {
    @Test
    public void testGetters() throws Exception {
        MemoryStore store = new MemoryStore();
        URI uri = store.add(new byte[] { 1, 2 });
        LargeBinaryStoreDataSource dataSource = 
            new LargeBinaryStoreDataSource(store, uri, "test/plain", "test");
        
        assertEquals("test/plain", dataSource.getContentType());
        assertEquals("test", dataSource.getName());
        assertEquals(uri, dataSource.getResourceUri());
        InputStream inputStream = dataSource.getInputStream();
        try {            
            assertEquals(1, inputStream.read());
            assertEquals(2, inputStream.read());
            assertEquals(-1, inputStream.read());
        }
        finally {
            inputStream.close();
        }        
    }
    
    @Test
    public void testGetOutputStreamFails() throws Exception {
        MemoryStore store = new MemoryStore();
        URI uri = store.add(new byte[] { 1, 2 });
        LargeBinaryStoreDataSource dataSource = 
            new LargeBinaryStoreDataSource(store, uri, "test/plain", "test");
        
        OutputStream outputStream = dataSource.getOutputStream();
        assertNotNull(outputStream);
        try {
            outputStream.write(new byte[] { 2, 3, 4 });
        }
        finally {
            outputStream.close();
        }

        InputStream inputStream = dataSource.getInputStream();
        try {            
            assertEquals(2, inputStream.read());
            assertEquals(3, inputStream.read());
            assertEquals(4, inputStream.read());
            assertEquals(-1, inputStream.read());
        }
        finally {
            inputStream.close();
        }        
    }
    
    @Test
    public void testGetContentLength() {
        MemoryStore store = new MemoryStore();
        URI uri = store.add(new byte[] { 1, 2 });
        LargeBinaryStoreDataSource dataSource = 
            new LargeBinaryStoreDataSource(store, uri, "test/plain", "test");
        
        assertEquals(2, dataSource.getContentLength());
    }
    
    @Test
    public void testResourceIsDeletedAfterInputStreamClosed() throws Exception {
        MemoryStore store = new MemoryStore();
        URI uri = store.add(new byte[] { 1, 2 });
        LargeBinaryStoreDataSource dataSource = 
            new LargeBinaryStoreDataSource(store, uri, "test/plain", "test");
        
        dataSource.deleteAfterNextUsage();
        dataSource.getInputStream().close();
        
        assertFalse(store.contains(uri));
    }
    
    @Test
    public void testResourceIsNotDeletedAfterInputStreamClosed() throws Exception {
        MemoryStore store = new MemoryStore();
        URI uri = store.add(new byte[] { 1, 2 });
        LargeBinaryStoreDataSource dataSource = 
            new LargeBinaryStoreDataSource(store, uri, "test/plain", "test");
        
        // No call to deleteAfterNextUsage()
        dataSource.getInputStream().close();
        
        assertTrue(store.contains(uri));
    }
    
    @Test
    public void testNullNameIsOk() {
        MemoryStore store = new MemoryStore();
        URI uri = store.add(new byte[] { 1, 2 });
        LargeBinaryStoreDataSource dataSource = 
            new LargeBinaryStoreDataSource(store, uri, "test/plain", null);

        assertNull(dataSource.getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullSafetyConstructorStore() throws Exception {
        new LargeBinaryStoreDataSource(null, new URI("http://blubla"), "test/plain", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSafetyConstructorUri() throws Exception {
        MemoryStore store = new MemoryStore();
        new LargeBinaryStoreDataSource(store, null, "test/plain", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSafetyConstructorContentType() throws Exception {
        MemoryStore store = new MemoryStore();
        new LargeBinaryStoreDataSource(store, new URI("http://blubla"), null, "test");
    }
    
    @Test
    public void testNiceClass() throws Exception {
        MemoryStore store = new MemoryStore();
        URI uri = store.add(new byte[] { 1, 2 });
        LargeBinaryStoreDataSource dataSource = 
            new LargeBinaryStoreDataSource(store, uri, "test/plain", "test");

        NiceClass.checkToString(dataSource, store, uri, "test/plain", "test");
        NiceClass.checkNullSafety(dataSource, 
                asList(store, uri, "bla"), 
                asList("LargeBinaryStoreDataSource:4"));
    }
}
