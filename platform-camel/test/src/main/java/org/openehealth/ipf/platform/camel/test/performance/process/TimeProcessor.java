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

import javax.xml.bind.JAXBException;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Timestamp;
import org.openehealth.ipf.commons.test.performance.dispatcher.MeasurementDispatcher;
import org.openehealth.ipf.commons.test.performance.utils.MeasurementHistoryXMLUtils;

/**
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
     * Registers the measurement in the measurement history of the given
     * exchange. If the given <code>exchange</code> does not contain a
     * measurement history object in the inbound message header, a
     * {@link IllegalStateException} is thrown, indicating that the measurement
     * history object is lost, or is not created at all.
     * 
     * @param exchange
     *            the exchange, whose <code>MeasurementHistory</code> object
     *            will be updated
     * @param measurement
     *            the measurement to add to the <code>MeasurementHistory</code>
     *            object of the given <code>exchange</code>
     */
    public void registerMeasurement(Exchange exchange, Measurement measurement) {
        notNull(exchange, "The exchange must not be null!");
        notNull(measurement, "The measurement must not be null!");

        MeasurementHistory history = checkExistsAndGetMeasurementHistory(exchange);
        history.add(measurement);
        enrichMessageHeaderWithMeasurementHistory(exchange, history);
    }

    /**
     * Returns the <code>MeasurementHistory</code> object contained in the
     * inbound message header. The initial <code>MeasurementHistory</code>
     * object in the message header must be created from a
     * <code>TimeProcessor</code>. The history object is then updated with
     * {@link #registerMeasurement(Exchange, Measurement)}. Note that some Camel
     * components filter some message headers (for example the JMS camel
     * component filters headers with raw Java serializable values. If the
     * exchange does not contain a measurement history object in the inbound
     * message header, a {@link IllegalStateException} is thrown, indicating
     * that the measurement history object is lost, or is not created at all.
     * 
     * @param exchange
     *            the exchange that contains the measurement history object.
     * @return a measurement history object, if such exists in the inbound
     *         message header. If the exchange does not contain a measurement
     *         history object, throws {@link IllegalStateException}
     */
    public MeasurementHistory getMeasurementHistory(Exchange exchange) {
        notNull(exchange, "The exchange must not be null!");
        return checkExistsAndGetMeasurementHistory(exchange);
    }

    private MeasurementHistory checkExistsAndGetMeasurementHistory(
            Exchange exchange) {
        MeasurementHistory history = getMeasurementHistoryFromMessageHeader(exchange);
        if (history == null) {// at this point
            String msg = String
                    .format(
                            "No measurement history can be found as expected at this point."
                                    + " Make sure the inbound message headers are not lost for exchange %1$s",
                            exchange.toString());
            throw new IllegalStateException(msg);
        }
        return history;
    }

    private void enrichMessageHeaderWithMeasurementHistory(Exchange exchange,
            MeasurementHistory measurementHistory) {
        exchange.getIn().removeHeader(MEASUREMENT_HISTORY_HEADER_KEY);
        exchange.getIn().setHeader(MEASUREMENT_HISTORY_HEADER_KEY,
                marshal(measurementHistory));

    }

    private String marshal(MeasurementHistory measurementHistory) {
        try {
            String marshaled = MeasurementHistoryXMLUtils
                    .marshall(measurementHistory);
            return marshaled;
        } catch (JAXBException e) {
            throw new IllegalStateException(
                    "Marshaling the measurement history failed!", e);
        }
    }

    private MeasurementHistory getMeasurementHistoryFromMessageHeader(
            Exchange exchange) {
        Message inMessage = exchange.getIn();

        String header = (String) inMessage
                .getHeader(MEASUREMENT_HISTORY_HEADER_KEY);
        if (header == null) {
            return null;
        } else {
            return unmarshal(header);
        }
    }

    private MeasurementHistory unmarshal(String measurementHistoryXML) {
        try {
            MeasurementHistory history = MeasurementHistoryXMLUtils
                    .unmarshall(measurementHistoryXML);
            return history;
        } catch (JAXBException e) {
            throw new IllegalStateException(
                    "Unmarshaling the measurement history failed!", e);
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
