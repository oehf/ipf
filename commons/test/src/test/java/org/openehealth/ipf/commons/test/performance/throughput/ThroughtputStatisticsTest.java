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

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.StatisticsRenderer;
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
@ContextConfiguration(locations = { "/context-throughput-statistics.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class ThroughtputStatisticsTest extends AbstractThroughputStatisticsTest {

    @Autowired
    private ThroughputStatistics statistics;

    @Autowired
    private StatisticsRenderer renderer;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        statistics.reset();
    }

    @Override
    public ThroughputStatistics getStatistics() {
        return statistics;
    }

    @Override
    public StatisticsRenderer getRenderer() {
        return renderer;
    }

    @Test
    public void testFromIsTheReferenceDate() {
        MeasurementHistory measurementHistory = createMeasurementHistory();
        statistics.update(measurementHistory);
        assertEquals(measurementHistory.getReferenceDate(), new Date(statistics
                .getFromTime()));
    }

    @Test
    public void testToIsTheReferenceDateIncremented() {
        MeasurementHistory measurementHistory = createMeasurementHistory();
        statistics.update(measurementHistory);
        assertEquals(
                statistics.calcuateProcessedSystemTime(measurementHistory),
                statistics.getToTime());
    }

    @Test
    public void testThroughputHasValidUpdateCounts() {
        int maxUpdates = 30;
        for (int updates = 0; updates < maxUpdates; updates++) {
            statistics.update(createMeasurementHistory());
        }
        Throughput t = statistics.getThroughput();
        assertEquals(maxUpdates, t.getCount());
    }
}
