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

import static org.apache.commons.lang.Validate.notNull;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.filters.StringInputStream;
import org.openehealth.ipf.commons.test.http.client.Client;
import org.openehealth.ipf.commons.test.http.client.ResponseHandler;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;
import org.springframework.beans.factory.InitializingBean;

/**
 * @see StatisticsManager
 * @author Mitko Kolev
 */
public abstract class MeasurementDispatcher implements InitializingBean {

    // TODO 1 review the initial implementation with the test Client
    // TODO 2 write a test for the the setting with the performance server URL
    private final static Log LOG = LogFactory
            .getLog(MeasurementDispatcher.class);

    private StatisticsManager statisticsManager;

    /**
     * The client should provide the settings of the HTTP Client
     */
    private Client client;

    private String performanceServerURL;

    /**
     * Dispatches the measurementHistory.
     * 
     * @param measurementHistory
     */
    public abstract void dispatch(MeasurementHistory measurementHistory);

    /**
     * The target statisticsManager
     * 
     * @return
     */
    public StatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    /**
     * Sets the statisticsManager to which measurement data will be dispatched
     * on {@link #dispatch(MeasurementData)}.
     * 
     * @param statisticsManager
     *            a StatisticsManager instance
     */
    public void setStatisticsManager(StatisticsManager statisticsManager) {
        notNull(statisticsManager, "The statisticsManager must not be null!");
        this.statisticsManager = statisticsManager;
    }

    /**
     * The dispatcher uses the given <code>performanceServerURL</code> to
     * dispatch the measurement history to the performance server as well.
     * 
     * @param performanceServerURL
     *            the performanceServerURL to set
     */
    public void setPerformanceServerURL(String performanceServerURL) {
        this.performanceServerURL = performanceServerURL;
    }

    /**
     * @return the performanceServerURL
     */
    public String getPerformanceServerURL() {
        return performanceServerURL;
    }

    /**
     * Sends an HTTP POST request with the current time to the throughput server to update the
     * throughput.
     */
    protected void updateThroughputServer(MeasurementHistory measurementHistory) {
        if (performanceServerURL == null)
            return;
        try {
            // send a POST request with the current time (the message is processed)
            //TODO decide should the whole measurement history should be sent? 
            InputStream request = new StringInputStream(String
                    .valueOf(new Date().getTime()));
            client.execute(request);
        } catch (Exception e) {
            LOG.error("Can not reach the throughput server", e);
        }
    }

    /**
     * Checks if the single statisticsManager is initialized
     */
    public void afterPropertiesSet() throws Exception {
        if (statisticsManager == null) {
            throw new IllegalArgumentException("The "
                    + MeasurementDispatcher.class.getName()
                    + " msut be configured with a "
                    + StatisticsManager.class.getName());
        }

        // check the throughput server client
        if (performanceServerURL == null) {
            LOG.info("Using no performance server");
        } else {
            client = new Client();
            client.setServerUrl(new URL(performanceServerURL));
            client.setHandler(new ResponseHandler() {
                @Override
                public void handleResponse(InputStream response)
                        throws Exception {
                    // do nothing
                }
            });
            LOG.info("Using a performance server at " + performanceServerURL);
        }
    }
}
