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
package org.openehealth.ipf.platform.camel.test.performance.process;

import static org.apache.commons.lang.Validate.notNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Timestamp;
import org.openehealth.ipf.commons.test.performance.dispatcher.MeasurementDispatcher;

/**
 * Enriches the exchange inbound message with the current time, provided by
 * {@link #getCurrentTimestamp()}
 * 
 * @author Mitko Kolev
 */
public class TimeProcessor implements Processor {

    public static final String MEASUREMENT_HISTORY_HEADER_KEY = "performance.measurement.history";

    private MeasurementDispatcher dispatcher;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        notNull(exchange, "The exchange must not be null!");
        Date now = new Date();
        Measurement measurement = new Measurement(getCurrentTimestamp());

        MeasurementHistory history = getMeasurementHistoryFromMessageHeader(exchange);
        if (history == null) {// the first time
            history = new MeasurementHistory(now);
            history.add(measurement);
            enrichMessageHeaderWithMeasurementHistory(exchange, history);
        } else {
            MeasurementHistory historyCopy = new MeasurementHistory(history);
            historyCopy.add(measurement);
            enrichMessageHeaderWithMeasurementHistory(exchange, historyCopy);
        }
    }

    public MeasurementHistory getMeasurementHistory(Exchange exchange) {
        MeasurementHistory history = getMeasurementHistoryFromMessageHeader(exchange);
        if (history == null) {
            throw new IllegalStateException(
                    "No measurement history can be found in "
                            + MEASUREMENT_HISTORY_HEADER_KEY + " header!");
        }
        return history;
    }

    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.nanoTime(), TimeUnit.NANOSECONDS);
        // return new Timestamp(System.nanoTime(), TimeUnit.MILLISECONDS);
    }

    public void registerMeasurement(Exchange exchange, Measurement measurement) {
        notNull(exchange, "The exchange must not be null!");
        MeasurementHistory history = getMeasurementHistoryFromMessageHeader(exchange);
        MeasurementHistory historyCopy = new MeasurementHistory(history);
        historyCopy.add(measurement);
        enrichMessageHeaderWithMeasurementHistory(exchange, historyCopy);
    }

    private void enrichMessageHeaderWithMeasurementHistory(Exchange exchange,
            MeasurementHistory history) {
        exchange.getIn().removeHeader(MEASUREMENT_HISTORY_HEADER_KEY);
        exchange.getIn().setHeader(MEASUREMENT_HISTORY_HEADER_KEY, history);
    }

    private MeasurementHistory getMeasurementHistoryFromMessageHeader(
            Exchange exchange) {
        Message inMessage = exchange.getIn();

        MeasurementHistory history = (MeasurementHistory) inMessage
                .getHeader(MEASUREMENT_HISTORY_HEADER_KEY);
        return history;
    }

    /**
     * Sets the <code>MeasurementDispatcher</code>, which will be used by the
     * processor subclasses to dispatch their measurement histories.
     * 
     * @param dispatcher
     *            a <code>MeasurementDispatcher</code> instance
     */
    public void setMeasurementDispatcher(MeasurementDispatcher dispatcher) {
        notNull(dispatcher, "The dispatcher must not be null!");
        this.dispatcher = dispatcher;
    }

    /**
     * @return the contained <code>MeasurementDispatcher</code> instance.
     */
    public MeasurementDispatcher getMeasurementDispatcher() {
        return dispatcher;
    }
}
