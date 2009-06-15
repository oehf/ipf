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
package org.openehealth.ipf.platform.camel.test.performance.extend;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;
import org.openehealth.ipf.commons.test.performance.processingtime.ProcessingTimeStatistics;
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
@ContextConfiguration(locations = { "/context-application.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class PerformanceMeasurementExtensionTest {

    private final static String DEFAULT_BODY = "body";

    @Autowired
    ThroughputStatistics throughputStatistics;

    @Autowired
    ProcessingTimeStatistics processingTimeStatistics;

    @Autowired
    protected ProducerTemplate producerTemplate;

    @Autowired
    protected StatisticsManager statisticsManager;

    @EndpointInject(uri = "mock:output")
    protected MockEndpoint mock;

    @After
    public void tearDown() {
        mock.reset();
        statisticsManager.resetStatistics();
    }

    @Test
    public void testMessageIsDelivered() throws InterruptedException {
        mock.expectedBodiesReceived(DEFAULT_BODY);
        producerTemplate.sendBody("direct:basic", DEFAULT_BODY);
        mock.assertIsSatisfied();
    }

    @Test
    public void testSplitIsSupported() throws InterruptedException {
        mock.expectedBodiesReceived(DEFAULT_BODY);
        producerTemplate.sendBody("direct:split", "a,b,c");
        assertEquals(3, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testThroughputStatisticsAreUpdated()
            throws InterruptedException {
        assertEquals(0, throughputStatistics.getUpdatesCount());
        producerTemplate.sendBody("direct:basic", DEFAULT_BODY);
        assertEquals(1, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testProcessingTimeStatisticsAreUpdated()
            throws InterruptedException {
        assertEquals(0, throughputStatistics.getUpdatesCount());
        producerTemplate.sendBody("direct:basic", DEFAULT_BODY);
        assertEquals(1, processingTimeStatistics.getStatisticalSummaryByName(
                "finish").getN());
    }

    @Test
    public void testUpdatesOneCheckpoint() throws InterruptedException {
        assertEquals(0, throughputStatistics.getUpdatesCount());
        producerTemplate.sendBody("direct:one_checkpoint", DEFAULT_BODY);
        assertEquals(1, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testProcessingStatisticsContainCheckpointData()
            throws InterruptedException {
        assertEquals(0, throughputStatistics.getUpdatesCount());
        producerTemplate.sendBody("direct:one_checkpoint", DEFAULT_BODY);
        producerTemplate.sendBody("direct:one_checkpoint", DEFAULT_BODY);
        StatisticalSummary summary = processingTimeStatistics
                .getStatisticalSummaryByName("checkpoint");
        assertEquals(2, summary.getN());
    }

    @Test
    public void testProcessingStatisticsContainFinishData()
            throws InterruptedException {
        assertEquals(0, throughputStatistics.getUpdatesCount());
        producerTemplate.sendBody("direct:one_checkpoint", DEFAULT_BODY);
        producerTemplate.sendBody("direct:one_checkpoint", DEFAULT_BODY);
        StatisticalSummary summary = processingTimeStatistics
                .getStatisticalSummaryByName("finish");
        assertEquals(2, summary.getN());
    }

    @Test
    public void testThreeExplicitCheckpointsAndFinish()
            throws InterruptedException {
        assertEquals(0, throughputStatistics.getUpdatesCount());
        producerTemplate.sendBody("direct:three_explicit_checkpoints",
                DEFAULT_BODY);
        // one data for checkpoint, one for finish
        StatisticalSummary summary = processingTimeStatistics
                .getStatisticalSummaryByName("finish");
        assertEquals(1, summary.getN());
    }

    @Test
    public void testExplicitCheckpointStatisticsReceiveData()
            throws InterruptedException {
        testCheckpoints("direct:three_explicit_checkpoints");
    }

    @Test
    public void testCheckpointStatisticsReceiveData()
            throws InterruptedException {
        testCheckpoints("direct:three_checkpoints");
    }

    private void testCheckpoints(String endpoint) {
        assertEquals(0, throughputStatistics.getUpdatesCount());
        producerTemplate.sendBody(endpoint, DEFAULT_BODY);
        producerTemplate.sendBody(endpoint, DEFAULT_BODY);

        for (int t = 1; t < 4; t++) {
            StatisticalSummary summary1 = processingTimeStatistics
                    .getStatisticalSummaryByName(String.valueOf(t));
            assertEquals(2, summary1.getN());
        }
    }

}
