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
package org.openehealth.ipf.commons.test.performance.dispatcher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;

import static org.junit.Assert.assertEquals;

import static org.openehealth.ipf.commons.test.performance.PerformanceMeasurementTestUtils.createMeasurementHistory;

/**
 * @author Mitko Kolev
 */
public class AsynchronousMeasurementDispatcherTest {

    private AsynchronousMeasurementDispatcher dispatcher;
    private CountDownLatch sync;
    private final static int MAX_AWAIT_SECONDS = 20;

    @Before
    public void setUp() {
        dispatcher = new AsynchronousMeasurementDispatcher();
    }

    @Test
    public void testAsyncDispatch() throws InterruptedException {
        doTestUpdates(1);
    }

    @Test
    public void testAsyncDispatchManyTimes() throws InterruptedException {
        doTestUpdates(20);
    }

    private void doTestUpdates(int times) throws InterruptedException {
        sync = new CountDownLatch(times);
        dispatcher.setStatisticsManager(new MyStatisticsManager(sync));

        for (int t = 0; t < times; t++) {
            dispatcher.dispatch(createMeasurementHistory());
        }

        sync.await(MAX_AWAIT_SECONDS, TimeUnit.SECONDS);
        // the update has been called
        assertEquals(0, sync.getCount());
    }

    /**
     * Decrements the count of the contained CountDownLatch on
     * {@link #updateStatistics(MeasurementData)}.
     */
    static class MyStatisticsManager extends StatisticsManager {
        private final CountDownLatch latch;

        MyStatisticsManager(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void updateStatistics(MeasurementHistory measurementHistory) {
            latch.countDown();
        }
    }

}
