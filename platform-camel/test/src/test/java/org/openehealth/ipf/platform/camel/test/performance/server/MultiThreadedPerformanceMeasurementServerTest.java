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

import java.net.URL;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Timestamp;
import org.openehealth.ipf.commons.test.performance.dispatcher.SynchronousMeasurementDispatcher;

/**
 * Tests to determine the optimal performance settings of the HTTP Client and/or
 * the performance measurement server. To before you run this test, run the
 * performance measurement server and comment the @Ignore annotation
 * 
 * @author Mitko Kolev
 */
@RunWith(BlockJUnit4ClassRunner.class)
@Ignore
public class MultiThreadedPerformanceMeasurementServerTest {
    /**
     * Use a HTTP client to send the measurements to the server
     */
    private final Executor executor;

    private final SynchronousMeasurementDispatcher dispatcher;

    private final CountDownLatch latch;

    private final String performanceMeasurementServerURL = "http://localhost:9191/statistics";

    int executions = 10000;

    int threadsCount = 100;

    int maxConnectionsPerHost = 5;

    public MultiThreadedPerformanceMeasurementServerTest() throws Exception {
        executor = Executors.newFixedThreadPool(threadsCount);
        latch = new CountDownLatch(executions);
        dispatcher = new SynchronousMeasurementDispatcher();
        dispatcher.setPerformanceMeasurementServerURL(new URL(
                performanceMeasurementServerURL));
        dispatcher.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost);

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

        dispatcher.dispatch(history);
    }

}
