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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;

import static org.junit.Assert.assertEquals;

import static org.openehealth.ipf.commons.test.performance.PerformanceMeasurementTestUtils.createMeasurementHistory;
/**
 * @author Mitko Kolev
 */
public class SynchronousMeasurementDispatcherTest {
    int updates = 0;
    private SynchronousMeasurementDispatcher dispatcher;

    @Before
    public void setUp() {
        updates = 0;
        dispatcher = new SynchronousMeasurementDispatcher();
        dispatcher.setStatisticsManager(new MySyncStatisticsManager());
    }

    @Test
    public void testSyncDispatch() throws InterruptedException {
        doTestUpdates(1);
        assertEquals(1, updates);
    }

    @Test
    public void testManySyncDispatches() throws InterruptedException {
        doTestUpdates(23);
        assertEquals(23, updates);
    }

    private void doTestUpdates(int times) throws InterruptedException {
        for (int t = 0; t < times; t++) {
            dispatcher.dispatch(createMeasurementHistory());
        }
    }

    class MySyncStatisticsManager extends StatisticsManager {
        @Override
        public void updateStatistics(MeasurementHistory measurementHistory) {
            updates++;
        }
    }
}
