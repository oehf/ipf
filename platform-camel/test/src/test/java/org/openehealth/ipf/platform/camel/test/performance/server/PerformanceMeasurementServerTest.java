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

import static org.apache.camel.component.http.HttpMethods.DELETE;
import static org.apache.camel.component.http.HttpMethods.GET;

import static org.apache.commons.lang.Validate.notNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.test.http.client.Client;
import org.openehealth.ipf.commons.test.http.client.ResponseHandler;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Statistics;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;
import org.openehealth.ipf.commons.test.performance.StatisticsRenderer;
import org.openehealth.ipf.commons.test.performance.Timestamp;
import org.openehealth.ipf.commons.test.performance.dispatcher.MeasurementDispatcher;
import org.openehealth.ipf.commons.test.performance.handler.PerformanceRequestHandler;
import org.openehealth.ipf.commons.test.performance.throughput.ThroughputStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.openehealth.ipf.commons.test.performance.utils.MeasurementHistoryXMLUtils.marshall;

/**
 * Test for the stand-alone performance server.
 * 
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "/context-performance-measurement-server-properties.xml",
        "/context-performance-measurement-server.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class PerformanceMeasurementServerTest {
    /**
     * The tests wait, at the most, the given number of seconds for the server
     * to process the request. Under normal circumstances, the requests should
     * be processed in less than 100ms.
     * 
     */
    private final int MAX_AWAIT_SECONDS = 30;

    private final String EMPTY_BODY = "";

    private final static Log LOG = LogFactory
            .getLog(PerformanceMeasurementServerTest.class);

    @Resource
    ThroughputStatistics throughputStatistics;

    @Autowired
    protected ProducerTemplate producerTemplate;

    @Autowired
    PerformanceRequestHandler performanceRequestHandler;

    @Autowired
    protected StatisticsManager statisticsManager;

    /**
     * Use a HTTP client to send the measurements to the server
     */
    private final Client client;

    // inject the properties from the application context (prior to Spring 3.0)
    @Autowired
    Integer httpPort;

    private CountDownLatch sync;

    public PerformanceMeasurementServerTest() {
        client = new Client();
        client.setContentType("text/xml; Content-Encoding: "
                + MeasurementDispatcher.CONTENT_ENCODING);
        client.setHandler(new ResponseHandler() {
            @Override
            public void handleResponse(InputStream response) throws Exception {
                // do nothing
            }
        });
    }

    @Before
    public void setUp() {
        sync = new CountDownLatch(1);
        performanceRequestHandler.setStatisticsManager(new MyStatisticsManager(
                statisticsManager));
        notNull(httpPort, "The httpPort must not be null!");

    }

    @After
    public void tearDown() {
        throughputStatistics.reset();
        performanceRequestHandler.getStatisticsManager().resetStatistics();
    }

    /**
     * Tests if the synchronization object receives an update. This is required
     * for the other tests.
     * 
     * @throws Exception
     */
    @Test
    public void testSyncIsUpdated() throws Exception {
        postMeasurementHistory();
        sync.await(MAX_AWAIT_SECONDS, TimeUnit.SECONDS);
        String msg = "The synchronization object must be updated on sendMeasurementRequest(). "
                + "Make sure that you do not have a running "
                + PerformanceMeasurementServer.class.getName()
                + " server on the test machine!";
        // Print only the msg with assertTrue
        assertTrue(msg, sync.getCount() == 0);

    }

    @Test
    public void testPOST() throws Exception {
        postMeasurementHistory();
        sync.await(MAX_AWAIT_SECONDS, TimeUnit.SECONDS);
        assertEquals(1, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testGET() throws Exception {
        postMeasurementHistory();
        sync.await(MAX_AWAIT_SECONDS, TimeUnit.SECONDS);
        String response = sendStatisticsGETRequest();

        assertTrue("The throughputStatistics result response must be handled",
                response.toLowerCase().contains("throughput"));
    }

    @Test
    public void testDELETE() throws Exception {
        postMeasurementHistory();
        sync.await(MAX_AWAIT_SECONDS, TimeUnit.SECONDS);
        sendStatisticsDELETERequest();
        assertEquals(0, throughputStatistics.getUpdatesCount());
    }

    @Test
    public void testManyPOSTs() throws Exception {
        int requests = 30;
        sync = new CountDownLatch(requests);
        for (int t = 0; t < requests; t++) {
            postMeasurementHistory();
        }
        sync.await(MAX_AWAIT_SECONDS, TimeUnit.SECONDS);

        assertEquals(0, sync.getCount());
    }

    private void postMeasurementHistory() throws Exception {
        // send the current time
        MeasurementHistory history = new MeasurementHistory(new Date());
        history.add(new Measurement(new Timestamp(System.currentTimeMillis(),
                TimeUnit.MILLISECONDS)));
        history.add(new Measurement(new Timestamp(System.currentTimeMillis(),
                TimeUnit.MILLISECONDS), "finish"));

        String xml = marshall(history);
        client.setServerUrl(new URL(buildURL()));
        client.execute(new ByteArrayInputStream(xml
                .getBytes(MeasurementDispatcher.CONTENT_ENCODING)));
    }

    private void sendStatisticsDELETERequest() {
        producerTemplate.sendBodyAndHeader(buildURL(), EMPTY_BODY,
                Exchange.HTTP_METHOD, DELETE);
    }

    private String sendStatisticsGETRequest() throws IOException {
        Object response = producerTemplate.sendBodyAndHeader(buildURL(),
                ExchangePattern.InOut, EMPTY_BODY, Exchange.HTTP_METHOD, GET);
        return IOUtils.toString((InputStream) response);
    }

    private String buildURL() {
        return "http://localhost:" + httpPort + "/statistics";
    }

    /**
     * A StatisticsManager which delegates to a the StatisticsManager in the
     * configuration context and updates the sync object on
     * {@link #updateStatistics(MeasurementHistory)}
     */
    class MyStatisticsManager extends StatisticsManager {
        private final StatisticsManager delegate;

        private MyStatisticsManager(StatisticsManager delegate) {
            this.delegate = delegate;
        }

        @Override
        public void updateStatistics(MeasurementHistory measurementHistory) {
            delegate.updateStatistics(measurementHistory);

            LOG.info(MyStatisticsManager.class.getName() + " updated!");
            sync.countDown();

        }

        @Override
        public StatisticsRenderer getRenderer(Statistics statistics) {
            return delegate.getRenderer(statistics);
        }

        @Override
        public List<Statistics> getStatistics() {
            return delegate.getStatistics();
        }

        @Override
        public void resetStatistics() {
            delegate.resetStatistics();
        }

    }
}
