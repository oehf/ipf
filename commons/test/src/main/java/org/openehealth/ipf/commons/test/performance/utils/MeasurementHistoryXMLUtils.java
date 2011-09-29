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
package org.openehealth.ipf.commons.test.performance.utils;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.MeasurementLostException;
import org.openehealth.ipf.commons.test.performance.Timestamp;

/**
 * Provides utilities for marshaling and unmarshaling performance measurements.
 * The class is thread-safe.
 * 
 * @author Mitko Kolev
 */
public class MeasurementHistoryXMLUtils {

    private final static Log LOG = LogFactory
            .getLog(MeasurementHistoryXMLUtils.class);

    private final static JAXBContext context;

    private final static ThreadLocal<Marshaller> marshallers;

    private final static ThreadLocal<Unmarshaller> unmarshallers;

    static {
        try {
            // The context is thread-safe
            context = JAXBContext.newInstance(MeasurementHistory.class,
                    Measurement.class, Timestamp.class);

            // Use ThreadLocal to reuse the marshaller and unmarshaller
            // instances
            unmarshallers = new ThreadLocal<Unmarshaller>() {
                @Override
                public Unmarshaller initialValue() {
                    try {
                        return context.createUnmarshaller();
                    } catch (JAXBException e) {
                        throw new IllegalStateException(
                                "Can not create measurement history unmarshaller",
                                e);
                    }
                }
            };
            marshallers = new ThreadLocal<Marshaller>() {
                @Override
                public Marshaller initialValue() {
                    try {
                        return context.createMarshaller();
                    } catch (JAXBException e) {
                        throw new IllegalStateException(
                                "Can not create measurement history marshaller",
                                e);
                    }
                }
            };
        } catch (JAXBException e) {
            throw new RuntimeException("Can not initialize JAXB context", e);
        }
    }

    /**
     * Creates a XML String from the given <code>MeasurementHistory</code>
     * object.
     * 
     * @param measurementHistory
     *            a <code>MeasurementHistory</code> object to marshal
     * @return a String that contains the marshaled
     *         <code>measurementHistory</code>
     * @throws JAXBException
     *             if the marshaling has failed
     */
    public static String marshall(MeasurementHistory measurementHistory) {
        notNull(measurementHistory, "The measurementHistory must not be null!");
        Marshaller marshaller = marshallers.get();
        StringWriter writer = new StringWriter();
        try {
            marshaller.marshal(measurementHistory, writer);
            return writer.toString();
        } catch (JAXBException e) {
            LOG.error("Failed to marshal:" + measurementHistory, e);
            throw new MeasurementLostException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }

    }

    /**
     * Creates a <code>MeasurementHistory</code> object from the given XML
     * String
     * 
     * @param measurementHistory
     *            a <code>String</code> with XML representation of
     *            <code>MeasurementHistory</code>
     * @return a <code>MeasurementHistory</code> object
     * @throws JAXBException
     *             if the unmarshaling has failed
     */
    public static MeasurementHistory unmarshall(String measurementHistory) {
        notNull(measurementHistory, "The measurementHistory must not be null!");
        // If the XML contains white spaces, the unmarshaller will fail
        StringReader reader = new StringReader(measurementHistory.trim());
        try {
            Unmarshaller unmarshaller = unmarshallers.get();
            return (MeasurementHistory) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            LOG.error("Failed to unmarshal:" + measurementHistory, e);
            throw new MeasurementLostException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
}
