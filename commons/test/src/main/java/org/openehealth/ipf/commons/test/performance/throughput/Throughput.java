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

import org.openehealth.ipf.commons.test.performance.Timestamp;
import org.openehealth.ipf.commons.test.performance.processingtime.ProcessingTime;

/**
 * @author Mitko Kolev
 */
public class Throughput {

    private final long count;
    private final Timestamp from;
    private final Timestamp to;

    /**
     * @param count
     * @param from
     * @param to
     */
    public Throughput(long count, Timestamp from, Timestamp to) {
        super();
        this.count = count;
        this.from = from;
        this.to = to;
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("from must be before to");
        }
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * The method exists to provide double precision for the seconds duration
     * 
     * @return the duration
     */
    public double getDurationSeconds() {
        return ProcessingTime.getProcessingTime(from, to).getValue(
                TimeUnit.MILLISECONDS) / 1000d;
    }

    public double getValueSeconds() {
        return count / getDurationSeconds();
    }

    /**
     * @return the from
     */
    public Timestamp getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public Timestamp getTo() {
        return to;
    }
}
