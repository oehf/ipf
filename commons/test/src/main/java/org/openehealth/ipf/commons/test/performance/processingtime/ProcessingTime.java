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

import java.util.concurrent.TimeUnit;

import org.openehealth.ipf.commons.test.performance.Timestamp;

/**
 * @author Mitko Kolev
 */
public class ProcessingTime {

    private final long value;

    private final TimeUnit unit;

    public ProcessingTime(long value, TimeUnit unit) {
        this.value = value;
        this.unit = unit;
    }
    /**
     * @param targetUnit
     * @return
     */
    public long getValue(TimeUnit unit) {
        return unit.convert(value, this.unit);
    }

    public static ProcessingTime getProcessingTime(Timestamp from, Timestamp to) {
        long durationNanos = to.getNanoValue() - from.getNanoValue();
        return new ProcessingTime(durationNanos, TimeUnit.NANOSECONDS);
    }
}
