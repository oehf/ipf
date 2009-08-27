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
package org.openehealth.ipf.platform.camel.lbs.http.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.core.junit.DirtySpringContextJUnit4ClassRunner;
import org.openehealth.ipf.platform.camel.lbs.http.process.ResourceList;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * This test uses {@link DirtySpringContextJUnit4ClassRunner} which is an 
 * alternative to {@link SpringJUnit4ClassRunner} that recreates the Spring
 * application context for the next test class. 
 * <b><p>
 * Do not simply copy this code. It could result it bad performance of tests.
 * Use the standard {@link SpringJUnit4ClassRunner} if you don't need this, 
 * which is usually the case.
 * </b><p>
 * This class requires that the Spring application context is recreated because
 * it creates HTTP endpoints. These endpoints will not be thrown away and use 
 * the tcp/ip ports and endpoint names. Subsequent quests could fail because
 * the ports are in use and exchanges might not be received by the correct 
 * endpoint.
 * <p>
 * This class runs all tests in its base class with a specific configuration
 * 
 * @author Jens Riemschneider
 */
@RunWith(DirtySpringContextJUnit4ClassRunner.class) // DO NOT SIMPLY COPY!!! see above
@ContextConfiguration(locations = { "/context-lbs-route-http-groovy.xml" })
public class GroovyLbsHttpTest extends AbstractLbsHttpTest {
    public static final String ENDPOINT_JMS_QUEUE = 
        "http://localhost:9452/lbstest_jms";

    private final static long CONTENT_SIZE = 1024 * 1024 * 10;
    
    /**
     * Test to verify that example code works 
     */
    @Test
    public void testExample1() throws Exception {        
        PostMethod method = new PostMethod("http://localhost:9452/lbstest_example1");
        method.setRequestEntity(new FileRequestEntity(file, "unknown/unknown"));

        mock.expectedMessageCount(1);
        mock.expectedHeaderReceived("tokenfound", "yes");

        httpClient.executeMethod(method);
        method.releaseConnection();

        mock.assertIsSatisfied();
    }

    /**
     * Test to verify that example code works 
     */
    @Test
    public void testExample2() throws Exception {        
        PostMethod method = new PostMethod("http://localhost:9452/lbstest_example2");
        method.setRequestEntity(new StringRequestEntity("testtext", "text/plain", null));

        mock.expectedMessageCount(1);
        mock.expectedHeaderReceived("textfound", "yes");

        httpClient.executeMethod(method);
        method.releaseConnection();

        mock.assertIsSatisfied();
    }

    /**
     * Test to verify that example code works 
     */
    @Test
    public void testExample3() throws Exception {        
        PostMethod method = new PostMethod("http://localhost:9452/lbstest_example3");
        method.setRequestEntity(new StringRequestEntity("testtext", "text/plain", null));

        mock.expectedMessageCount(1);

        httpClient.executeMethod(method);
        method.releaseConnection();

        mock.assertIsSatisfied();
        Exchange exchange = mock.getReceivedExchanges().get(0);
        ResourceList resourceList = exchange.getIn().getBody(ResourceList.class);
        assertEquals(2, resourceList.size());
    }
    
    @Test
    public void testHugeFileUpload() throws Exception {
        PostMethod method = new PostMethod(ENDPOINT_EXTRACT);
        InputStream inputStream = new HugeContentInputStream();
        InputStreamRequestEntity requestEntity = 
            new InputStreamRequestEntity(inputStream, CONTENT_SIZE, "text/plain");
        method.setRequestEntity(requestEntity);

        final int[] count = { 0 };

        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                InputStream inputStream = exchange.getIn().getBody(InputStream.class);
                int length = getLength(inputStream);
                inputStream.close();
                count[0] = length;
            }
        });

        TrackMemThread memTracker = new TrackMemThread();
        memTracker.start();
        try {
            httpClient.executeMethod(method);
            mock.assertIsSatisfied();
            assertEquals(CONTENT_SIZE, count[0]);
        }
        finally {
            memTracker.waitForStop();
            assertTrue("Memory consumption not constant. Difference was: " + memTracker.getDiff(), memTracker.getDiff() < 10000);
        }
    }
    
    private static class TrackMemThread extends Thread {
        private boolean stopped;
        private long min = Long.MAX_VALUE;
        private long max = Long.MIN_VALUE;
        
        @Override
        public void run() {
            while (!stopped) {
                try {
                    long currentMem = Runtime.getRuntime().totalMemory();
                    min = Math.min(min, currentMem);
                    max = Math.max(max, currentMem);
                    Thread.sleep(10);
                } catch (InterruptedException e) {}
            }
        }

        public void waitForStop() throws InterruptedException {
            stopped = true;
            join();
        }

        public long getDiff() {
            return max - min;
        }
    }
    
    @Test
    public void testHugeFileDownload() throws Exception {
        TrackMemThread memTracker = new TrackMemThread();
        memTracker.start();
        try {
            Object result = producerTemplate.requestBody("direct:lbstest_download", "bla");
            assertTrue(result instanceof InputStream);
            assertEquals(CONTENT_SIZE, getLength((InputStream) result));
        }
        finally {
            memTracker.waitForStop();
            assertTrue("Memory consumption not constant. Difference was: " + memTracker.getDiff(), memTracker.getDiff() < 10000);
        }
    }

    public static int getLength(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length = 0;
        boolean done = false;
        while (!done) {
            int read = inputStream.read(buffer, 0, buffer.length);
            if (read == -1) {
                done = true;
            }
            else {
                length += read;
            }
        }
        return length;
    }
    
    @Test
    public void testFileEndpointJms() throws Exception {
        testFile(ENDPOINT_JMS_QUEUE);
    }

    public static final class HugeContentInputStream extends InputStream {
        private long readBytes;       
        private int count;
        
        @Override
        public int read() throws IOException {
            if (readBytes == CONTENT_SIZE) {
                return -1;
            }
            ++readBytes;
            return 'L';
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (readBytes == CONTENT_SIZE) {
                return -1;
            }

            long sizeToRead = len;
            if (readBytes + sizeToRead > CONTENT_SIZE) {
                sizeToRead = Math.max(0, CONTENT_SIZE - readBytes);
            }
            
            Arrays.fill(b, off, (int)(off + sizeToRead), (byte)65);
            readBytes += sizeToRead;
            if (++count == 1000) {
                count = 0;
            }
            return (int) sizeToRead;
        }
        
        public long getReadBytes() {
            return readBytes;
        }
        
        @Override
        public void close() throws IOException {
            super.close();
            assertEquals(CONTENT_SIZE, readBytes);
            readBytes = 0;
        }
    }
}
