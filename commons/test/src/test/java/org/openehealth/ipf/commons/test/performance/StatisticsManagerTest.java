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
package org.openehealth.ipf.commons.test.performance;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.test.performance.processingtime.ProcessingTimeStatistics;
import org.openehealth.ipf.commons.test.performance.throughput.ThroughputStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.openehealth.ipf.commons.test.performance.PerformanceMeasurementTestUtils.createMeasurementHistory;

/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-statistics-manager.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class StatisticsManagerTest {

    @Autowired
    StatisticsManager statisticsManager;

    @Resource
    ThroughputStatistics throughputStatistics;

    @Autowired
    ProcessingTimeStatistics processingTimeStatistics;

    public StatisticsManagerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        throughputStatistics.reset();
        processingTimeStatistics.reset();
    }

    @Test
    public void testThroughputStatisticsReceiveUpdate() {
        MeasurementHistory measurementHistory = createMeasurementHistory();
        statisticsManager.updateStatistics(measurementHistory);
        assertEquals(1, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testProcessingTimeStatisticsReceiveUpdate() {
        MeasurementHistory measurementHistory = createMeasurementHistory();
        statisticsManager.updateStatistics(measurementHistory);
        List<String> names = processingTimeStatistics.getMeasurementNames();
        StatisticalSummary summary0 = processingTimeStatistics
                .getStatisticalSummaryByName(names.get(0));

        assertEquals(1, summary0.getN());
    }

    @Test
    public void testStatisticsAreCleared() {
        statisticsManager.resetStatistics();
        // test that reset works
        assertEquals(0, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testStatisticsAreUpdatedTwoTimes() {
        MeasurementHistory measurementHistory = createMeasurementHistory();

        statisticsManager.updateStatistics(measurementHistory);
        statisticsManager.updateStatistics(measurementHistory);
        assertEquals(2, throughputStatistics.getUpdatesCount());

    }

    @Test
    public void testRenderingStatistics() {
        MeasurementHistory measurementHistory = createMeasurementHistory();
        statisticsManager.updateStatistics(measurementHistory);
        statisticsManager.updateStatistics(measurementHistory);

        for (Statistics s : statisticsManager.getStatistics()) {
            assertNotNull(statisticsManager.getRenderer(s).render(s));
        }
    }

    @Test
    public void testStatisticsAreUpdatedManyTimes() {
        MeasurementHistory measurementHistory = createMeasurementHistory();
        assertEquals(0, throughputStatistics.getUpdatesCount());

        int times = 10;
        for (int t = 0; t < times; t++)
            statisticsManager.updateStatistics(measurementHistory);
        assertEquals(times, throughputStatistics.getUpdatesCount());

    }
}
