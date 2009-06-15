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

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Mitko Kolev
 */
public class PerformanceMeasurementTestUtils {

    /**
     * Creates measurement history with 2 measurements
     * 
     * @return
     */
    public static MeasurementHistory createMeasurementHistory() {
        return createMeasurementHistory(2);
    }

    public static MeasurementHistory createMeasurementHistory(Date referenceDate) {
        return createMeasurementHistory(referenceDate, 2);
    }

    /**
     * Creates measurement history with the given number of measurements
     * 
     * @param numberOfMeasurementPoints
     * @return
     */
    public static MeasurementHistory createMeasurementHistory(
            int numberOfMeasurementPoints) {
        return createMeasurementHistory(new Date(), numberOfMeasurementPoints);
    }

    public static MeasurementHistory createMeasurementHistory(
            Date referenceDate, int numberOfMeasurementPoints) {

        long initialMeasurementTimestamp = System.currentTimeMillis();

        long[] measurementTimestamps = new long[numberOfMeasurementPoints];

        for (int t = 0; t < numberOfMeasurementPoints; t++) {
            measurementTimestamps[t] = initialMeasurementTimestamp + 1000 * t;
        }
        MeasurementHistory history = new MeasurementHistory(referenceDate);

        // the first one
        history.add(new Measurement(new Timestamp(measurementTimestamps[0],
                TimeUnit.MILLISECONDS)));
        for (int t = 1; t < numberOfMeasurementPoints; t++) {
            Measurement measurement = new Measurement(new Timestamp(
                    measurementTimestamps[t], TimeUnit.MILLISECONDS), String
                    .valueOf(t));
            history.add(measurement);
        }

        return history;
    }
}
