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

import javax.xml.bind.JAXBException;

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
 * Dispatches performance measurements to a <code>StatisticsManager</code>.
 * Additionally dispatches to a performance measurement server, if such is
 * configured with {@link #setPerformanceMeasurementServerURL(String)}
 * 
 * @see StatisticsManager
 * @author Mitko Kolev
 */
public abstract class MeasurementDispatcher implements InitializingBean {

    public static String CONTENT_ENCODING = "UTF-8";

    // TODO 1 review the initial implementation with the test Client
    // TODO 2 write a test for the the setting with the performance server URL
    // TODO 3 Expose the configuration of the test client
    private final static Log LOG = LogFactory
            .getLog(MeasurementDispatcher.class);

    private StatisticsManager statisticsManager;

    /**
     * The client should provide the settings of the HTTP Client
     */
    private Client client;

    private String performanceMeasurementServerURL;

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
     * The dispatcher uses the given
     * <code>performanceMeasurementServerURL</code> to dispatch the measurement
     * history to the performance server, sending a HTTP post request to the
     * given <code>performanceMeasurementServerURL</code>.
     * 
     * @param performanceMeasurementServerURL
     *            the performanceMeasurementServerURL to set
     */
    public void setPerformanceMeasurementServerURL(
            String performanceMeasurementServerURL) {
        this.performanceMeasurementServerURL = performanceMeasurementServerURL;
    }

    /**
     * @return the performanceMeasurementServerURL
     */
    public String getPerformanceMeasurementServerURL() {
        return performanceMeasurementServerURL;
    }

    /**
     * Updates the contained statistics manager with the given
     * <code>measurmentHistory</code>
     * 
     * @param measurementHistory
     *            a <code>MeasurementHistory</code> object.
     */
    protected void updateStatisticsManager(MeasurementHistory measurementHistory) {
        notNull(measurementHistory, "The measurementHistory must not be null!");
        this.statisticsManager.updateStatistics(measurementHistory);
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
        notNull(measurementHistory, "The measurementHistory must not be null!");
        if (!isUsingPerformanceMeasurementServer()) {
            return;
        }
        try {
            // marshal and send the current measurement history
            String xml = marshall(measurementHistory);

            client.execute(new ByteArrayInputStream(xml
                    .getBytes(CONTENT_ENCODING)));

        } catch (JAXBException e) {
            String msg = "Unable to marshal the measurement history to XML";
            LOG.error(msg, e);
            throw new MeasurementLostException(msg, e);
        } catch (Exception e) {
            String msg = "Failed to send performance measurement to a performance measurement server at "
                    + performanceMeasurementServerURL;
            LOG.error(msg, e);
            throw new MeasurementLostException(msg, e);

        }
    }

    /**
     * Returns true if a performance measurement server has been configured.
     * 
     * @return true if the {@link #getPerformanceMeasurementServerURL()} will
     *         return a not null or empty URL String. Returns false otherwise.
     */
    public boolean isUsingPerformanceMeasurementServer() {
        if (performanceMeasurementServerURL == null
                || performanceMeasurementServerURL.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the single statisticsManager is set and initializes the HTTP
     * client, if performance measurement server is used.
     */
    public void afterPropertiesSet() throws Exception {
        if (statisticsManager == null) {
            throw new IllegalArgumentException("The "
                    + MeasurementDispatcher.class.getName()
                    + " msut be configured with a "
                    + StatisticsManager.class.getName());
        }
        // initialize the preformane measurement server client
        if (isUsingPerformanceMeasurementServer()) {
            client = new Client();
            client.setServerUrl(new URL(performanceMeasurementServerURL));
            client.setContentType("text/xml; charset=" + CONTENT_ENCODING);
            client.setHandler(new ResponseHandler() {
                @Override
                public void handleResponse(InputStream response)
                        throws Exception {
                    // do nothing
                }
            });
            LOG
                    .info(this.getClass().getSimpleName()
                            + " is configured to use a performance measurement server with URL "
                            + performanceMeasurementServerURL);
        } else {
            LOG.info("Performance measurement server will not be used, "
                    + "because no URL is configured in "
                    + this.getClass().getSimpleName());
        }
    }
}
