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
package org.openehealth.ipf.commons.test.performance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.openehealth.ipf.commons.core.io.IOUtils;
import org.openehealth.ipf.commons.test.performance.utils.MeasurementHistoryXMLUtils;

import static org.junit.Assert.assertEquals;

import static org.openehealth.ipf.commons.test.performance.PerformanceMeasurementTestUtils.createMeasurementHistory;

/**
 * @author Mitko Kolev
 */
public class MeasurementHistoryTest {

    @Test
    public void testReferenceDateIsSerialized() throws Exception {
        MeasurementHistory history = createMeasurementHistory();
        byte[] bytes = IOUtils.serialize(history);
        MeasurementHistory deserialized = (MeasurementHistory) IOUtils
                .deserialize(bytes);
        assertEquals(history.getReferenceDate(), deserialized
                .getReferenceDate());
    }

    @Test
    public void testMeasurementsAreSerialized() throws Exception {
        MeasurementHistory history = createMeasurementHistory(20);
        byte[] bytes = IOUtils.serialize(history);
        MeasurementHistory deserialized = (MeasurementHistory) IOUtils
                .deserialize(bytes);
        List<Measurement> measurements = history.getMeasurements();
        List<Measurement> deserializedMeasurements = deserialized
                .getMeasurements();

        for (int t = 0; t < deserializedMeasurements.size(); t++) {
            assertEquals(measurements.get(t), deserializedMeasurements.get(t));
        }

    }

    @Test
    public void testXMLSerialization() throws Exception {
        MeasurementHistory history = createMeasurementHistory(20);
        JAXBContext context = JAXBContext.newInstance(MeasurementHistory.class,
                Measurement.class, Timestamp.class);
        Marshaller marshaller = context.createMarshaller();
        Unmarshaller unmarshaller = context.createUnmarshaller();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        marshaller.marshal(history, outStream);

        MeasurementHistory unmarshalledHistory = (MeasurementHistory) unmarshaller
                .unmarshal(new ByteArrayInputStream(outStream.toByteArray()));

        assertEquals(history, unmarshalledHistory);

    }

    @Test
    public void testXMLSerializationWithXMLUtils() throws Exception {
        // test unmarshal with string
        MeasurementHistory history = createMeasurementHistory(20);
        String marshalled = MeasurementHistoryXMLUtils.marshall(history);
        MeasurementHistory unmarshalled = MeasurementHistoryXMLUtils
                .unmarshall(marshalled);
        assertEquals(history, unmarshalled);
    }

    @Test
    public void testXMLSerializationWithXMLUtils2() throws Exception {
        // test unmarshal with a string reader
        MeasurementHistory history = createMeasurementHistory(20);
        String marshalled = MeasurementHistoryXMLUtils.marshall(history);
        MeasurementHistory unmarshalled = MeasurementHistoryXMLUtils
                .unmarshall(marshalled);
        assertEquals(history, unmarshalled);
    }
}
