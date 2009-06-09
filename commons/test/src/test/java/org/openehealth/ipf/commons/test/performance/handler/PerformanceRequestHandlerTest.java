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
package org.openehealth.ipf.commons.test.performance.handler;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;
import org.openehealth.ipf.commons.test.performance.throughput.ThroughputStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;

/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-statistics-manager.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class PerformanceRequestHandlerTest {
    @Autowired
    StatisticsManager statisticsManager;

    @Resource
    ThroughputStatistics throughputStatistics;

    private final PerformanceRequestHandler handler = new PerformanceRequestHandler();

    @Before
    public void setUp() {
        handler.setStatisticsManager(statisticsManager);
    }

    @After
    public void tearDown() {
        statisticsManager.resetStatistics();
    }

    @Test
    public void testStatisticsReceiveReset() {
        handler.onRequestAtTime(new Date(), "1");
        assertEquals(1, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testStatisticsReceiveUpdate() {
        handler.onRequestAtTime(new Date(), "1");
        assertEquals(1, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testStatisticsReceiveUpdateAtTime() {
        handler.onRequestAtTime(new Date(), "1");
        handler.onRequestAtTime(new Date(), "1");
        handler.onRequestAtTime(new Date(), "1");
        handler.onRequestAtTime(new Date(), "1");
        assertEquals(4, throughputStatistics.getUpdatesCount());
    }


}
