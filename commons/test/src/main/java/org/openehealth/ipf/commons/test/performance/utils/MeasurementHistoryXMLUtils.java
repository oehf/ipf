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

import static org.apache.commons.lang.Validate.notNull;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Timestamp;

/**
 * @author Mitko Kolev
 */
public class MeasurementHistoryXMLUtils {

    private static JAXBContext context = initContext();
    private final static Log LOG = LogFactory
            .getLog(MeasurementHistoryXMLUtils.class);

    /**
     * Creates a XML String from the given <code>MeasurementHistory</code>
     * object.
     * 
     * @param measurementHistory
     *            a <code>MeasurementHistory</code> object to marshall
     * @return a String that contains the marshalled
     *         <code>measurementHistory</code>
     * @throws JAXBException
     *             if the marshalling has failed
     */
    public static String marshall(MeasurementHistory measurementHistory)
            throws JAXBException {
        notNull(measurementHistory, "The measurementHistory must not be null!");
        Marshaller marshaller = context.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(measurementHistory, writer);
        return writer.toString();
    }

    /**
     * Creates a <code>MeasurementHistory</code> object from the XML data in the
     * given <code>Reader</code>
     * 
     * @param reader
     *            a <code>Reader</code> instance with XML representation of
     *            <code>MeasurementHistory</code>
     * @return a <code>MeasurementHistory</code> object
     * @throws JAXBException
     *             if the unmarshalling has failed
     */
    public static MeasurementHistory unmarshall(Reader reader)
            throws JAXBException {
        notNull(reader, "The reader must not be null!");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (MeasurementHistory) unmarshaller.unmarshal(reader);
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
     *             if the unmarshalling has failed
     */
    public static MeasurementHistory unmarshall(String measurementHistory)
            throws JAXBException {
        notNull(measurementHistory, "The measurementHistory must not be null!");

        return unmarshall(new StringReader(measurementHistory));
    }

    /**
     * Initialize the JAXB context
     */
    private static JAXBContext initContext() {
        try {
            return JAXBContext.newInstance(MeasurementHistory.class,
                    Measurement.class, Timestamp.class);
        } catch (JAXBException e) {
            LOG.error("Can not initialize JAXB context");
            throw new RuntimeException(e);
        }

    }

}
