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

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Timestamp;
import org.openehealth.ipf.commons.test.performance.dispatcher.MeasurementDispatcher;

import static org.openehealth.ipf.commons.test.performance.utils.MeasurementHistoryXMLUtils.marshall;
import static org.openehealth.ipf.commons.test.performance.utils.MeasurementHistoryXMLUtils.unmarshall;

/**
 * 
 * Puts a <code>MeasurementHistory</code> in the inbound message header, with a
 * measurement with time the result of {@link #getCurrentTimestamp()}, on
 * {@link #process(Exchange)}. Provides methods for modification and access of
 * the measurement history object, in the inbound message header of the
 * exchange. The measurement history object is stored as a <code>String</code>.
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
            history.add(measurement);
            enrichMessageHeaderWithMeasurementHistory(exchange, history);
        }
    }

    /**
     * @return a
     *         <code>Timestamp<code> object with value System.nanoTime() - the best system precision.
     * 
     */
    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    /**
     * Returns the <code>MeasurementHistory</code> object contained in the
     * inbound message header with name {@link #MEASUREMENT_HISTORY_HEADER_KEY}.
     * The initial <code>MeasurementHistory</code> object in the message header
     * must be created from a <code>TimeProcessor</code>. Note that some Camel
     * components filter some message headers (for example the JMS camel
     * component filters headers with raw Java serializable values).
     * 
     * @param exchange
     *            the exchange that contains the measurement history object.
     * @return a measurement history object, if such exists in the inbound
     *         message header. If no header with name
     *         {@link #MEASUREMENT_HISTORY_HEADER_KEY} is found, returns null
     */
    public MeasurementHistory getMeasurementHistory(Exchange exchange) {
        notNull(exchange, "The exchange must not be null!");
        return getMeasurementHistoryFromMessageHeader(exchange);
    }

    protected void enrichMessageHeaderWithMeasurementHistory(Exchange exchange,
            MeasurementHistory measurementHistory) {
        exchange.getIn().removeHeader(MEASUREMENT_HISTORY_HEADER_KEY);
        String measurementHistoryString = marshall(measurementHistory);
        exchange.getIn().setHeader(MEASUREMENT_HISTORY_HEADER_KEY,
                measurementHistoryString);

    }

    protected void removeMeasurementHistoryHeader(Exchange exchange) {
        exchange.getIn().removeHeader(MEASUREMENT_HISTORY_HEADER_KEY);
    }

    protected MeasurementHistory getMeasurementHistoryFromMessageHeader(
            Exchange exchange) {
        Message inMessage = exchange.getIn();

        String measurementHistoryXML = (String) inMessage
                .getHeader(MEASUREMENT_HISTORY_HEADER_KEY);
        if (measurementHistoryXML == null) {
            return null;
        } else {
            return unmarshall(measurementHistoryXML);
        }
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
