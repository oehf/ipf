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

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Mitko Kolev
 */
public class MeasurementDispatcherTest {

    private SynchronousMeasurementDispatcher dispatcher;

    @Before
    public void setUp() {
        dispatcher = new SynchronousMeasurementDispatcher();
    }

    @Test
    public void testIsUsingStatisticsManager() {
        SynchronousMeasurementDispatcher d = new SynchronousMeasurementDispatcher();
        assertFalse(d.isUsingStatisticsManager());
    }

    @Test
    public void testIsUsingPerformanceMeasurementServer() throws Exception {
        dispatcher.setPerformanceMeasurementServerURL(new URL(
                "http://localhost"));
        assertTrue(dispatcher.isUsingPerformanceMeasurementServer());
    }

    @Test
    public void testPerformanceMeasurementServerURLIsSet() throws Exception {
        String urlString = "http://localhost";
        dispatcher.setPerformanceMeasurementServerURL(new URL(urlString));
        URL url = dispatcher.getPerformanceMeasurementServerURL();
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testDefaultMaxConnectionsPerHostAreSet() throws Exception {
        int numConnections = 10;
        dispatcher.setDefaultMaxConnectionsPerHost(numConnections);
        assertEquals(numConnections, dispatcher
                .getDefaultMaxConnectionsPerHost());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidDefaultMaxConnectionsPerHost() throws Exception {
        dispatcher.setDefaultMaxConnectionsPerHost(1);
    }

    @Test
    public void testIsNotUsingPerformanceMeasurementServer() throws Exception {
        SynchronousMeasurementDispatcher d = new SynchronousMeasurementDispatcher();
        assertFalse(d.isUsingPerformanceMeasurementServer());
    }

    @Test
    public void testNotUsingStatisticsManager() {
        SynchronousMeasurementDispatcher d = new SynchronousMeasurementDispatcher();
        d.setStatisticsManager(new StatisticsManager());
        assertTrue(d.isUsingStatisticsManager());
    }

    @Test
    public void testAfterPropertiesSet() throws Exception {
        SynchronousMeasurementDispatcher d = new SynchronousMeasurementDispatcher();
        d.afterPropertiesSet();// should only log
    }
    

}
