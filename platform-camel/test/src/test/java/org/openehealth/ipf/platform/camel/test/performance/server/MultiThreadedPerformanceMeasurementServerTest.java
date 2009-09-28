/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.test.performance.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.test.http.client.Client;
import org.openehealth.ipf.commons.test.http.client.ResponseHandler;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Timestamp;
import org.openehealth.ipf.commons.test.performance.dispatcher.MeasurementDispatcher;

import static org.openehealth.ipf.commons.test.performance.utils.MeasurementHistoryXMLUtils.marshall;

/**
 * Tests to determine the optimal performance settings of the Client and/or the
 * performance measurement server. To before you run this test, run the
 * performance measurement server and comment the @Ignore annotation
 * 
 * @author Mitko Kolev
 */
@RunWith(JUnit4ClassRunner.class)
@Ignore
public class MultiThreadedPerformanceMeasurementServerTest {
    /**
     * Use a HTTP client to send the measurements to the server
     */
    private final Executor executor;

    private final Client client;

    private final CountDownLatch latch;

    private final String performanceMeasurementServerURL = "http://localhost:9191/statistics";

    int executions = 10000;

    int threadsCount = 200;

    int maxConnectionsPerHost = 5;

    public MultiThreadedPerformanceMeasurementServerTest() throws Exception {
        executor = Executors.newFixedThreadPool(threadsCount);
        latch = new CountDownLatch(executions);
        client = new Client();
        client.setServerUrl(new URL(performanceMeasurementServerURL));
        client.setContentType("text/xml; Content-Encoding: "
                + MeasurementDispatcher.CONTENT_ENCODING);
        client.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost);
        client.setHandler(new ResponseHandler() {
            @Override
            public void handleResponse(InputStream response) throws Exception {
                // do nothing
            }
        });

    }

    @Test
    public void testPerformanceServerManyThreads() throws Exception {

        for (int t = 0; t < executions; t++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendMeasurementToServer();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        latch.await();
    }

    private void sendMeasurementToServer() throws Exception {
        // send the current time
        MeasurementHistory history = new MeasurementHistory(new Date());
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS)));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate1"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate2"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate3"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate4"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate5"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate6"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate 7 "));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate 8"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), " intermediate9"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "intermediate 10"));
        history.add(new Measurement(new Timestamp(System.nanoTime(),
                TimeUnit.NANOSECONDS), "finish"));

        String xml = marshall(history);
        client.execute(new ByteArrayInputStream(xml
                .getBytes(MeasurementDispatcher.CONTENT_ENCODING)));
    }

}
