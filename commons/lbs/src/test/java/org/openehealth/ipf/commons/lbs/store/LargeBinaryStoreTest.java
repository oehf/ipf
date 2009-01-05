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
package org.openehealth.ipf.commons.lbs.store;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.commons.lbs.store.ResourceIOException;
import org.openehealth.ipf.commons.lbs.store.ResourceNotFoundException;

/**
 * Base test for all store implementations
 * This test is used by sub classes to test concrete sub classes. 
 * @author Jens Riemschneider
 */
public abstract class LargeBinaryStoreTest {
    private LargeBinaryStore store;

    /**
     * @param store
     *          store that should be used for the next test
     */
    public void setStore(LargeBinaryStore store) {
        this.store = store;
    }
    
    /**
     * @return store that is used for the current test
     */
    public LargeBinaryStore getStore() {
        return store;
    }
    
    @Test
    public void testAddAndGetByteArray() throws Exception {
        byte[] binary = new byte[] { 0, 1, 2, 3, 4, 5 };
        URI resourceUri = store.add(binary);
        assertTrue(store.contains(resourceUri));
        assertNotNull(resourceUri);
        byte[] resource = store.getByteArray(resourceUri);
        assertNotNull(resource);
        assertArrayEquals(binary, resource);
    }
    
    @Test
    public void testAddAndGetStream() throws Exception {
        InputStream originalStream = createStream("Hello World");        
        URI resourceUri = store.add(originalStream);
        assertTrue(store.contains(resourceUri));
        assertNotNull(resourceUri);
        InputStream resultStream = store.getInputStream(resourceUri);
        assertStream("Hello World", resultStream);
        assertNotNull(resultStream);
        originalStream.close();
        resultStream.close();
    }
    
    @Test
    public void testAddAndStreamContent() throws Exception {
        InputStream originalStream = createStream("Hello World");        
        URI resourceUri = store.add();
        assertNotNull(resourceUri);
        OutputStream outputStream = store.getOutputStream(resourceUri);
        assertNotNull(outputStream);
        IOUtils.copy(originalStream, outputStream);
        outputStream.close();
        InputStream resultStream = store.getInputStream(resourceUri);
        assertStream("Hello World", resultStream);
        assertNotNull(resultStream);
        originalStream.close();
        resultStream.close();
    }
    
    @Test
    public void testChangeExistingResource() throws Exception {
        byte[] binary = new byte[] { 0, 1, 2, 3, 4, 5 };
        URI resourceUri = store.add(binary);        
        OutputStream outputStream = store.getOutputStream(resourceUri);
        IOUtils.copy(createStream("Something completely different"), outputStream);
        outputStream.close();        
        InputStream resultStream = store.getInputStream(resourceUri);
        assertStream("Something completely different", resultStream);
        resultStream.close();
    }
    
    @Test
    public void testGetSize() throws Exception {
        byte[] binary = new byte[] { 0, 1, 2, 3, 4, 5 };
        URI resourceUri = store.add(binary);
        assertEquals(binary.length, store.getSize(resourceUri));
    }
    
    @Test
    public void testMultipleAddsAndGets() throws Exception {
        byte[] binary1 = new byte[] { 0, 1, 2, 3, 4, 5 };
        byte[] binary2 = new byte[] { 5, 4, 3, 2, 1, 0 };
        URI resourceUri1 = store.add(binary1);
        URI resourceUri2 = store.add(binary2);
        assertTrue(store.contains(resourceUri1));
        assertTrue(store.contains(resourceUri2));
        assertArrayEquals(binary1, store.getByteArray(resourceUri1));
        assertArrayEquals(binary2, store.getByteArray(resourceUri2));
    }
    
    @Test(expected = ResourceNotFoundException.class)
    public void testGetStreamForWrongUri() throws Exception {
        URI resourceUri = getUriWithWrongScheme();
        assertFalse(store.contains(resourceUri));
        store.getInputStream(resourceUri);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetStreamForNonExistingUri() throws Exception {
        URI resourceUri = getUriForNonExisitingResource();
        assertFalse(store.contains(resourceUri));
        store.getInputStream(resourceUri);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetByteArrayForWrongUri() throws Exception {
        URI resourceUri = getUriWithWrongScheme();
        assertFalse(store.contains(resourceUri));
        store.getByteArray(resourceUri);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetByteArrayForNonExistingUri() throws Exception {
        URI resourceUri = getUriForNonExisitingResource();
        assertFalse(store.contains(resourceUri));
        store.getByteArray(resourceUri);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteForWrongUri() throws Exception {
        URI resourceUri = getUriWithWrongScheme();
        assertFalse(store.contains(resourceUri));
        store.delete(resourceUri);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteForNonExistingUri() throws Exception {
        URI resourceUri = getUriForNonExisitingResource();
        assertFalse(store.contains(resourceUri));
        store.delete(resourceUri);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetSizeForWrongUri() throws Exception {
        URI resourceUri = getUriWithWrongScheme();
        assertFalse(store.contains(resourceUri));
        store.getSize(resourceUri);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetSizeForNonExistingUri() throws Exception {
        URI resourceUri = getUriForNonExisitingResource();
        assertFalse(store.contains(resourceUri));
        store.getSize(resourceUri);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetOutputStreamForNonExistingUri() throws Exception {
        URI resourceUri = getUriForNonExisitingResource();
        assertFalse(store.contains(resourceUri));
        store.getOutputStream(resourceUri);
    }

    @Test
    public void testDelete() throws Exception {
        byte[] binary = new byte[] { 0, 1, 2, 3, 4, 5 };
        URI resourceUri = store.add(binary);
        assertTrue(store.contains(resourceUri));
        store.delete(resourceUri);
        assertFalse(store.contains(resourceUri));
    }
    
    @Test(expected = ResourceIOException.class)
    public void testAddWithCorruptedInputStream() throws Exception {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("for testing");
            }            
        };
        store.add(inputStream);
    }
    
    @Test
    public void testDeleteAll() throws Exception {
        byte[] binary = new byte[] { 0, 1, 2, 3, 4, 5 };
        URI resourceUri1 = store.add(binary);
        URI resourceUri2 = store.add(binary);
        assertTrue(store.contains(resourceUri1));
        assertTrue(store.contains(resourceUri2));
        store.deleteAll();
        assertFalse(store.contains(resourceUri1));
        assertFalse(store.contains(resourceUri2));
    }
    
    // Performs add, get and delete in a loop in multiple threads
    @Test
    public void testThreadSafety() throws Exception {
        final int NUM_THREADS = 4;
        final int ITERATIONS = 100;

        final CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS);
        StoreTestThread[] threads = new StoreTestThread[NUM_THREADS];
        for (int idx = 0; idx < NUM_THREADS; ++idx) {
            threads[idx] = new StoreTestThread(barrier, ITERATIONS);
            threads[idx].start();
        }
        
        for (int idx = 0; idx < NUM_THREADS; ++idx) {
            threads[idx].join();
            if (threads[idx].isFailed()) {
                throw threads[idx].getException();
            }
        }
    }
    
    private class StoreTestThread extends Thread {
        private CyclicBarrier barrier;
        private int numberOfIterations;
        private Exception exception;
        
        public StoreTestThread(CyclicBarrier barrier, int numberOfIterations) {
            this.barrier = barrier;
            this.numberOfIterations = numberOfIterations;
        }
        
        @Override
        public void run() {
            noExceptionsAwait();            
            try {
                for (int iter = 0; iter < numberOfIterations; ++iter) {
                    doWork();
                }
            }
            catch (Exception e) {
                this.exception = e;
            }
        }

        private void doWork() throws IOException {
            InputStream originalStream = createStream("Hello World");        
            URI resourceUri = store.add(originalStream);
            assertTrue(store.contains(resourceUri));
            assertNotNull(resourceUri);
            InputStream resultStream = store.getInputStream(resourceUri);
            assertStream("Hello World", resultStream);
            assertEquals(11, store.getSize(resourceUri));
            assertNotNull(resultStream);
            originalStream.close();
            resultStream.close();
            store.delete(resourceUri);
            assertFalse(store.contains(resourceUri));
        }
        
        public boolean isFailed() {
            return exception != null;
        }
        
        public Exception getException() {
            return exception;
        }

        // Only for test purposes. Don't copy this.
        private void noExceptionsAwait() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
            } catch (BrokenBarrierException e) {
            }
        }       
    }
    
    @Test
    public void testNullSafety() throws Exception {
        assertIllegalArg(new Code() { public void run() {
            store.add((byte[])null);
        }});
        
        assertIllegalArg(new Code() { public void run() throws IOException {
            store.add((InputStream)null);
        }});
        
        assertIllegalArg(new Code() { public void run() {
            store.getByteArray(null);
        }});
    
        assertIllegalArg(new Code() { public void run() {
            store.getInputStream(null);
        }});
    
        assertIllegalArg(new Code() { public void run() {
            store.delete(null);
        }});

        assertIllegalArg(new Code() { public void run() {
            store.contains(null);
        }});

        assertIllegalArg(new Code() { public void run() {
            store.getSize(null);
        }});

        assertIllegalArg(new Code() { public void run() {
            store.getOutputStream(null);
        }});
    }

    private URI getUriWithWrongScheme() throws URISyntaxException {
        return new URI("dummy://schnuff");
    }

    private URI getUriForNonExisitingResource() {
        byte[] binary = new byte[] { 0, 1 };
        URI resourceUri = store.add(binary);
        store.delete(resourceUri);
        return resourceUri;
    }

    static void assertStream(String expected, InputStream stream) throws IOException {        
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        assertEquals(expected, bufferedReader.readLine());
    }

    static InputStream createStream(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

    interface Code {
        public void run() throws Exception;
    }
    
    static <T extends Exception> void assertException(Class<T> expectedException, Code code) {
        try {
            code.run();
        }
        catch (Exception e) {
            assertEquals(expectedException, e.getClass());
            return;
        }
        
        fail("Expected Exception: " + expectedException);
    }
    
    static void assertIllegalArg(Code code) {
        assertException(IllegalArgumentException.class, code);
    }
}