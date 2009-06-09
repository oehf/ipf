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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Statistics;

/**
 * @author Mitko Kolev
 */
public class ProcessingTimeStatistics implements Statistics {

    private final TreeMap<String, SummaryStatistics> statisticsByMeasurementName;

    private static TimeUnit PROCESSING_TIME_UNIT = TimeUnit.MILLISECONDS;

    public ProcessingTimeStatistics() {
        statisticsByMeasurementName = new TreeMap<String, SummaryStatistics>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openehealth.ipf.commons.test.performance.tmp.Statistics#update(org
     * .openehealth.ipf.commons.test.performance.tmp.MeasurementHistory)
     */
    @Override
    public void update(MeasurementHistory history) {
        // TODO: collect delta1, delta2 ... + update statistics
        initializeStatisticsIfNecessary(history);
        updateStatisticsWithHistory(history);
    }

    public List<String> getMeasurementNames() {
        return new ArrayList<String>(statisticsByMeasurementName.keySet());
    }

    public StatisticalSummary getStatisticalSummaryByName(String name) {
        return statisticsByMeasurementName.get(name).getSummary();
    }

    private void initializeStatisticsIfNecessary(MeasurementHistory history) {
        // initialize summary statistics only for measurements with name
        for (Measurement measurement : history.getMeasurements()) {
            if (!measurement.isNameEmpty()) {
                String name = measurement.getName();
                if (!statisticsByMeasurementName.containsKey(name)) {
                    statisticsByMeasurementName.put(name,
                            new SummaryStatistics());
                }
            }
        }
    }

    private void updateStatisticsWithHistory(MeasurementHistory history) {
        List<Measurement> measurements = history.getMeasurements();
        Measurement firstMeasurement = measurements.get(0);

        int lastIndex = measurements.size() - 1;
        for (int t = 0; t < measurements.size(); t++) {
            Measurement measurement = measurements.get(t);
            if (measurement.isNameEmpty()) {// skip times
                continue;
            } else if (t != lastIndex) {
                // checkpoints
                int prevIndex = t - 1;
                if (prevIndex == -1) {
                    continue;
                }
                Measurement prev = measurements.get(prevIndex);
                updateStatisticsWithDuration(prev, measurement);
            } else if (t == lastIndex) {
                // finish
                updateStatisticsWithDuration(firstMeasurement, measurement);
            }
        }
    }

    protected void updateStatisticsWithDuration(Measurement from, Measurement to) {
        SummaryStatistics statistics = this.statisticsByMeasurementName.get(to
                .getName());
        ProcessingTime processingTime = ProcessingTime.getProcessingTime(from
                .getTimestamp(), to.getTimestamp());

        statistics.addValue(processingTime.getValue(PROCESSING_TIME_UNIT));
    }

    @Override
    public void reset() {
        for (SummaryStatistics s : statisticsByMeasurementName.values()) {
            s.clear();
        }
        statisticsByMeasurementName.clear();
    }

}
