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
package org.openehealth.ipf.commons.test.performance.throughput;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.stat.Frequency;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Timestamp;

/**
 * @author Mitko Kolev
 */
public class ThroughputDistributionStatistics extends ThroughputStatistics {
    /**
     * Default number of statistical "bins"
     */
    public final static Integer DEFAULT_NUMBER_OF_BINS = 30;

    private int numberOfIntervalBins;

    private final Frequency frequencies;

    public ThroughputDistributionStatistics() {
        numberOfIntervalBins = DEFAULT_NUMBER_OF_BINS;
        frequencies = new Frequency();
    }

    @Override
    public void reset() {
        super.reset();
        frequencies.clear();
    }

    @Override
    public void update(MeasurementHistory history) {
        super.update(history);
        Date referenceDate = history.getReferenceDate();
        frequencies.addValue(referenceDate.getTime());
    }

    public ThroughputDistribution getThroughputDistribution() {
        return new ThroughputDistribution(getThroughput(), getThroughputForIntervals());
    }

    long getElementCount(long date) {
        return frequencies.getCount(date);
    }

    /**
     * Returns the number of "bins" in the frequency scale.
     * 
     * @return the value set with {@link #setNumberOfBins(int)}. if
     *         setNumberOfBins is not invoked, returns
     *         {@link #DEFAULT_NUMBER_OF_BINS}
     */
    public int getNumberOfIntervalBins() {
        return numberOfIntervalBins;
    }

    /**
     * Sets the number of splits the frequency scale to the given number of
     * "bins".
     * 
     * @param numberOfIntervalBins
     *            the number of "splits" for the measurement interval.
     */
    public void setNumberOfIntervalBins(int numberOfIntervalBins) {
        if (numberOfIntervalBins < 1) {
            throw new IllegalArgumentException(
                    "The numberOfIntervalBins must be greater than 1");
        }
        this.numberOfIntervalBins = numberOfIntervalBins;
    }

    protected List<Throughput> getThroughputForIntervals() {

        List<Throughput> bins = new ArrayList<Throughput>(
                this.numberOfIntervalBins);

        long lastTo = getToTime();
        long duration = (lastTo - getFromTime());
        // make the step a little bigger
        long step = duration / (this.numberOfIntervalBins - 1);
        for (int t = 0; t < numberOfIntervalBins; t++) {
            long to = lastTo - t * step;
            long from = to - step;

            double percentageTo = frequencies.getCumPct(to);
            double percentageFrom = frequencies.getCumPct(from);
            double difference;
            if (percentageTo == 1.0 && percentageFrom == 1.0) {
                difference = 1.0;
            } else {
                difference = percentageTo - percentageFrom;
            }

            Throughput binThroughput = calculateThroughputForPercents(
                    difference, new Timestamp(from, TimeUnit.MILLISECONDS),
                    new Timestamp(to, TimeUnit.MILLISECONDS));

            bins.add(0, binThroughput);
        }
        return bins;
    }

    private Throughput calculateThroughputForPercents(
            double msgsAsSizeBetween0and1, Timestamp from, Timestamp to) {
        Throughput throughput = new Throughput(Math.round(getUpdatesCount()
                * msgsAsSizeBetween0and1), from, to);
        return throughput;
    }
}
