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
package org.openehealth.ipf.commons.test.performance.processingtime;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.StatisticalSummaryValues;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;

/**
 * Holds {@link DescriptiveStatistics} for every measurement location. The class
 * is not thread-safe. <b>These statistics must be used with caution, because
 * the data values are stored in memory</b>.
 * 
 * @author Mitko Kolev
 */
public class ProcessingTimeDescriptiveStatistics extends
        ProcessingTimeStatistics {

    private final TreeMap<String, DescriptiveStatistics> statisticsByMeasurementName;

    private final static TimeUnit PROCESSING_TIME_UNIT = TimeUnit.MILLISECONDS;

    public ProcessingTimeDescriptiveStatistics() {
        statisticsByMeasurementName = new TreeMap<>();
    }

    @Override
    public List<String> getMeasurementNames() {
        Set<String> keys = statisticsByMeasurementName.keySet();
        return new ArrayList<>(keys);
    }

    /**
     * Returns statistical summary of all the data.
     * 
     * @return a <code>StatisticalSummary</code> object
     */
    @Override
    public StatisticalSummary getStatisticalSummaryByName(String name) {
        DescriptiveStatistics stats = statisticsByMeasurementName.get(name);

        return new StatisticalSummaryValues(stats.getMean(), stats
                .getVariance(), stats.getN(), stats.getMax(), stats.getMin(),
                stats.getSum());
    }

    @Override
    protected void initializeStatisticsIfNecessary(MeasurementHistory history) {
        // initialize summary statistics only for measurements with name
        for (Measurement measurement : history.getMeasurements()) {
            if (!measurement.isNameEmpty()) {
                String name = measurement.getName();
                if (!statisticsByMeasurementName.containsKey(name)) {
                    statisticsByMeasurementName.put(name,
                            new DescriptiveStatistics());
                }
            }
        }
    }

    @Override
    protected void updateStatisticsWithDuration(Measurement from, Measurement to) {
        DescriptiveStatistics statistics = statisticsByMeasurementName
                .get(to.getName());
        ProcessingTime processingTime = ProcessingTime.getProcessingTime(from
                .getTimestamp(), to.getTimestamp());

        statistics.addValue(processingTime.getValue(PROCESSING_TIME_UNIT));
    }

    @Override
    public void reset() {
        for (DescriptiveStatistics s : statisticsByMeasurementName.values()) {
            s.clear();
        }
        statisticsByMeasurementName.clear();
    }

    /**
     * @param measurementLocation
     *            the name of the measurement location for which the measurement
     *            data is requested be queried
     * @return the measurement data that the statistics contain
     */
    public double[] getData(String measurementLocation) {
        notNull(measurementLocation,
                "The measurementLocation must not be null!");
        return statisticsByMeasurementName.get(measurementLocation).getValues();
    }
}
