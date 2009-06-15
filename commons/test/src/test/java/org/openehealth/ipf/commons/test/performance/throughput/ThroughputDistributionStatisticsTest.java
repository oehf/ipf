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
package org.openehealth.ipf.commons.test.performance.throughput;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;

import static org.openehealth.ipf.commons.test.performance.PerformanceMeasurementTestUtils.createMeasurementHistory;

/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-throughput-distribution-statistics.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class ThroughputDistributionStatisticsTest {

    private final static int LAST_BIN_INDEX = Integer.MAX_VALUE;

    @Autowired
    ThroughputDistributionStatistics statistics;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        statistics.reset();
    }

    @Test
    public void testStatisticsAreUpdated() {
        MeasurementHistory history = createMeasurementHistory();
        statistics.update(history);
        assertEquals(1, statistics.getUpdatesCount());
    }

    @Test
    public void testIntrevalBinsAreSetCorrectly() {
        int intervalBins = 1245;
        statistics.setNumberOfIntervalBins(intervalBins);
        assertEquals(intervalBins, statistics.getNumberOfIntervalBins());
        // restore the default data.
        statistics
                .setNumberOfIntervalBins(ThroughputDistributionStatistics.DEFAULT_NUMBER_OF_BINS);
    }

    @Test
    public void testUpdateAndThenReset() {
        statistics.update(createMeasurementHistory());
        statistics.reset();
        assertEquals(0, statistics.getUpdatesCount());
    }

    @Test
    public void testUpdateAndThenResetTheDistributionIs0() {
        statistics.update(createMeasurementHistory());
        statistics.reset();
        List<Throughput> throughputs = statistics.getThroughputDistribution()
                .getThroughput();
        for (Throughput t : throughputs) {
            assertEquals(0, t.getCount());
        }
    }

    @Test
    public void testStatisticsConsiderAllMeasurements() {
        MeasurementHistory measurementHistory = createMeasurementHistory();
        statistics.update(measurementHistory);
        long elementsInTheFrequencies = statistics.getElementCount(statistics
                .calcuateProcessedSystemTime(measurementHistory));
        assertEquals(1, elementsInTheFrequencies);
    }

    @Test
    public void testOneUpdate() {
        statistics.update(createMeasurementHistory());
        ThroughputDistribution distribution = statistics
                .getThroughputDistribution();
        assertEquals(1, getCountAt(LAST_BIN_INDEX, distribution));
    }

    @Test
    public void testTwoUpdates() {
        statistics.update(createMeasurementHistory());
        statistics.update(createMeasurementHistory(10));
        ThroughputDistribution distribution = statistics
                .getThroughputDistribution();

        assertEquals(1, getCountAt(LAST_BIN_INDEX, distribution));
    }

    @Test
    public void testThreeUpdates() {
        statistics.update(createMeasurementHistory());
        statistics.update(createMeasurementHistory());
        statistics.update(createMeasurementHistory(10));

        ThroughputDistribution distribution = statistics
                .getThroughputDistribution();

        // we have 3 values, so the last bin with the latest time must contain
        // 1/3 of all values
        assertEquals(1, getCountAt(LAST_BIN_INDEX, distribution));
    }

    long getCountAt(int index, ThroughputDistribution distribution) {
        List<Throughput> throughputs = distribution.getThroughput();
        if (index == LAST_BIN_INDEX) {
            return throughputs.get(throughputs.size() - 1).getCount();
        } else {
            return throughputs.get(index).getCount();
        }
    }

}
