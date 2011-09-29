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

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;

/**
 * 
 * @author Mitko Kolev
 */
public class CheckpointProcessor extends TimeProcessor {
    private final String name;

    /**
     * Creates a CheckpointProcessor with the given <code>name</code>
     * 
     * @param name
     */
    public CheckpointProcessor(String name) {
        notNull(name, "The name must not be null!");
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // create measurement data
        Measurement measurement = new Measurement(getCurrentTimestamp(), name);
        registerMeasurement(exchange, measurement);
    }

    /**
     * If the exchange does not contain a measurement history object in the
     * inbound message header with name {@link #MEASUREMENT_HISTORY_HEADER_KEY},
     * a {@link IllegalStateException} is thrown, indicating that the
     * measurement history object is lost, or is not created at all.
     * 
     * @see TimeProcessor#getMeasurementHistory(Exchange)
     * @param exchange
     *            the exchange that contains the measurement history object.
     * @return @return a measurement history object, if such exists in the
     *         inbound message header. If no header with name
     *         {@link #MEASUREMENT_HISTORY_HEADER_KEY} is found, throws
     *         {@link IllegalStateException}
     */
    @Override
    public MeasurementHistory getMeasurementHistory(Exchange exchange) {
        MeasurementHistory history = getMeasurementHistoryFromMessageHeader(exchange);
        if (history == null) {// at this point
            String msg = String
                    .format(
                            "No measurement history can be found, as expected, at location %1$s."
                                    + " The measurement history is created by the first measure().time() of the route,"
                                    + " and is stored in the %2$s header of the in message.",
                            name, MEASUREMENT_HISTORY_HEADER_KEY);
            throw new IllegalStateException(msg);
        }
        return history;
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

        MeasurementHistory history = getMeasurementHistory(exchange);
        history.add(measurement);
        enrichMessageHeaderWithMeasurementHistory(exchange, history);
    }

    /**
     * @return the name of the processor
     */
    public String getName() {
        return name;
    }
}
