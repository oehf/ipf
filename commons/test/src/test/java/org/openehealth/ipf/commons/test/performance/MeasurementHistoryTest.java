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

import java.util.List;

import org.junit.Test;
import org.openehealth.ipf.commons.core.io.IOUtils;

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

}
