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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.test.http.client.Client;
import org.openehealth.ipf.commons.test.http.client.ResponseHandler;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.MeasurementLostException;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;
import org.springframework.beans.factory.InitializingBean;

import static org.openehealth.ipf.commons.test.performance.utils.MeasurementHistoryXMLUtils.marshall;

/**
 * Used to dispatch performance measurements to either a
 * <code>StatisticsManager</code>, set with
 * {@link #setStatisticsManager(StatisticsManager)}, a performance measurement
 * server, if such is configured with
 * {@link #setPerformanceMeasurementServerURL(URL)} or both.
 * 
 * @see StatisticsManager
 * @author Mitko Kolev
 */
public abstract class MeasurementDispatcher implements InitializingBean {

    public final static String CONTENT_ENCODING = "UTF-8";

    private final static Log LOG = LogFactory
            .getLog(MeasurementDispatcher.class);

    private StatisticsManager statisticsManager;

    /**
     * The client provides the settings of the HTTP Client
     */
    private final Client client;

    public MeasurementDispatcher() {
        client = new Client();
        client.setContentType("text/xml; charset=" + CONTENT_ENCODING);
        client.setDefaultMaxConnectionsPerHost(5);
        client.setHandler(new ResponseHandler() {
            @Override
            public void handleResponse(InputStream response) throws Exception {
                // do nothing
            }
        });
    }

    /**
     * Dispatches the measurementHistory.
     * 
     * @param measurementHistory
     */
    public abstract void dispatch(MeasurementHistory measurementHistory);

    /**
     * Default implementation
     * 
     * @param measurementHistory
     */
    public void defaultDispatch(MeasurementHistory measurementHistory) {
        updateStatisticsManager(measurementHistory);
        updatePerformanceMeasurementServer(measurementHistory);
    }

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
     * The dispatcher uses the given
     * <code>performanceMeasurementServerURL</code> to dispatch the measurement
     * history to the performance server, sending a HTTP post request to the
     * given <code>performanceMeasurementServerURL</code>.
     * 
     * @param performanceMeasurementServerURL
     *            the performanceMeasurementServerURL to set
     */
    public void setPerformanceMeasurementServerURL(
            URL performanceMeasurementServerURL) {
        notNull(performanceMeasurementServerURL,
                "The performanceMeasurementServerURL must not be null!");

        client.setServerUrl(performanceMeasurementServerURL);
    }

    /**
     * @return the performanceMeasurementServerURL
     */
    public URL getPerformanceMeasurementServerURL() {
        return client.getServerUrl();
    }

    /**
     * @return The max connections per host of the HTTP client used by this
     *         dispatcher, to send measurements to the performance measurement
     *         server.
     * @see HttpConnectionManagerParams#getDefaultMaxConnectionsPerHost()
     */
    public int getDefaultMaxConnectionsPerHost() {
        return client.getDefaultMaxConnectionsPerHost();
    }

    /**
     * The max connections per host of the HTTP client used by this dispatcher,
     * to send measurements to the performance measurement server. The default
     * value is 5
     * 
     * @param numConnections
     *            The number of connections to be used. The number must be
     *            greater than 2
     * 
     * @see HttpConnectionManagerParams#setDefaultMaxConnectionsPerHost(int)
     */
    public void setDefaultMaxConnectionsPerHost(int numConnections) {
        if (numConnections < 2) {
            throw new IllegalArgumentException(
                    "You must set more than 2 default max connections.");
        }
        client.setDefaultMaxConnectionsPerHost(numConnections);
    }

    /**
     * Updates the contained statistics manager with the given
     * <code>measurmentHistory</code>
     * 
     * @param measurementHistory
     *            a <code>MeasurementHistory</code> object.
     */
    protected void updateStatisticsManager(MeasurementHistory measurementHistory) {
        if (!isUsingStatisticsManager()) {
            return;
        }
        notNull(measurementHistory, "The measurementHistory must not be null!");
        statisticsManager.updateStatistics(measurementHistory);
    }

    /**
     * If a performance measurement server is configured (with URL
     * {@link #setPerformanceMeasurementServerURL(String)}), sends an HTTP POST
     * request with body the given <code>measurementHistory</code> to that URL.
     * 
     * @param measurementHistory
     *            a <code>MeasurementHistory</code> object.
     */
    protected void updatePerformanceMeasurementServer(
            MeasurementHistory measurementHistory) {
        if (!isUsingPerformanceMeasurementServer()) {
            return;
        }
        notNull(measurementHistory, "The measurementHistory must not be null!");
        try {
            // marshal and send the current measurement history
            String xml = marshall(measurementHistory);

            client.execute(new ByteArrayInputStream(xml
                    .getBytes(CONTENT_ENCODING)));

        } catch (Exception e) {
            String msg = "Failed to send performance measurement to a performance measurement server at "
                    + getPerformanceMeasurementServerURL();
            LOG.error(msg, e);
            throw new MeasurementLostException(msg, e);

        }
    }

    /**
     * Returns true if the dispatcher will send measurements to a performance
     * measurement server, if such is configured with
     * {@link #setPerformanceMeasurementServerURL(URL)}
     * 
     * @return true if the {@link #getPerformanceMeasurementServerURL()} will
     *         return a not null or empty URL. Returns false otherwise.
     */
    public boolean isUsingPerformanceMeasurementServer() {
        return getPerformanceMeasurementServerURL() != null;
    }

    /**
     * Returns true if the dispatcher will send measurements to a statistics
     * manager. It makes sense to use a statistics manager if the application
     * collects performance statistics. If this is not the case, a performance
     * measurement server should be used.
     * 
     * @return true if there is a statistics manager set with
     *         {@link #setStatisticsManager(StatisticsManager)}, false
     *         otherwise.
     */
    public boolean isUsingStatisticsManager() {
        return statisticsManager != null;
    }

    /**
     * Logs the settings
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (isUsingStatisticsManager()) {
            LOG.info("The " + getClass().getSimpleName()
                    + " is using a statistics manager ");
        } else {
            LOG.info("The " + MeasurementDispatcher.class.getSimpleName()
                    + " is not configured to use a statistics manager ");
        }
        // initialize the performance measurement server client
        if (isUsingPerformanceMeasurementServer()) {
            LOG
                    .info(getClass().getSimpleName()
                            + " is configured to use a performance measurement server with URL "
                            + getPerformanceMeasurementServerURL());
        } else {
            LOG.info("Performance measurement server will not be used, "
                    + "because no URL is configured in "
                    + getClass().getSimpleName());
        }
    }
}
