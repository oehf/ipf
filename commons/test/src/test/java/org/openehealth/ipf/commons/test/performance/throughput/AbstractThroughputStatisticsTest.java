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

import org.junit.Test;
import org.openehealth.ipf.commons.test.performance.StatisticsRenderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.openehealth.ipf.commons.test.performance.PerformanceMeasurementTestUtils.createMeasurementHistory;

/**
 * @author Mitko Kolev
 */
public abstract class AbstractThroughputStatisticsTest {

    private static int UPDATES_COUNT = 30;

    public abstract ThroughputStatistics getStatistics();

    public abstract StatisticsRenderer getRenderer();

    @Test
    public void testUpdate() {
        getStatistics().update(createMeasurementHistory());
        assertEquals(1, getStatistics().getUpdatesCount());
    }

    @Test
    public void testUpdateAndThenClear() {
        getStatistics().update(createMeasurementHistory());
        getStatistics().reset();
        assertEquals(0, getStatistics().getUpdatesCount());
    }

    @Test
    public void testDescriptionIsSet() {
        String testDescription = "test";
        getStatistics().setDescription(testDescription);
        assertEquals(testDescription, getStatistics().getDescription());
    }

    @Test
    public void testManyUpdates() {
        for (int updates = 0; updates < UPDATES_COUNT; updates++) {
            getStatistics().update(createMeasurementHistory());
        }

        assertEquals(UPDATES_COUNT, getStatistics().getUpdatesCount());
    }

    @Test
    public void testRendererContainsUpdatesCount() {
        for (int updates = 0; updates < UPDATES_COUNT; updates++) {
            getStatistics().update(createMeasurementHistory());
        }
        String report = getRenderer().render(getStatistics());
        assertTrue(report.contains(String.valueOf(UPDATES_COUNT)));
    }
}
