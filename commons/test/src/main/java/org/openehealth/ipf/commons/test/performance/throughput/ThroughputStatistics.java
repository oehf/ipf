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

import java.util.concurrent.TimeUnit;

import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Statistics;
import org.openehealth.ipf.commons.test.performance.Timestamp;

/**
 * @author Mitko Kolev
 */
public class ThroughputStatistics implements Statistics {

    public final static double SECOND = 1000d;

    public final static double MINUTE = 60 * SECOND;

    private final static long VALUE_NOT_SET = 0;

    private long fromTime = VALUE_NOT_SET;

    private long toTime = VALUE_NOT_SET;

    private long updatesCount = VALUE_NOT_SET;

    private String description;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openehealth.ipf.commons.test.performance.tmp.Statistics#update(org
     * .openehealth.ipf.commons.test.performance.tmp.MeasurementHistory)
     */
    @Override
    public void update(MeasurementHistory history) {
        if (fromTime == VALUE_NOT_SET) {
            fromTime = history.getReferenceDate().getTime();
        }
        toTime = history.getReferenceDate().getTime();
        updatesCount++;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openehealth.ipf.commons.test.performance.Statistics#reset()
     */
    @Override
    public void reset() {
        updatesCount = VALUE_NOT_SET;
        fromTime = VALUE_NOT_SET;
        toTime = VALUE_NOT_SET;
    }

    public Throughput getThroughput() {
        Timestamp from = new Timestamp(fromTime, TimeUnit.MILLISECONDS);
        Timestamp to = new Timestamp(toTime, TimeUnit.MILLISECONDS);
        Throughput throughput = new Throughput(updatesCount, from, to);
        return throughput;
    }

    /**
     * @return the updatesCount
     */
    public long getUpdatesCount() {
        return updatesCount;
    }

    /**
     * @return the fromTime
     */
    public long getFromTime() {
        return fromTime;
    }

    /**
     * @return the toTime
     */
    public long getToTime() {
        return toTime;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
