/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.payload;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Evaluation context of expressions for payload log file names. Expression or templating engines being
 * used should be able to call
 *
 * @author Dmytro Rud
 */
public class PayloadLoggingContext {
    private static final String PROCESS_ID;

    static {
        var mx = ManagementFactory.getRuntimeMXBean();
        PROCESS_ID = mx.getName().replace("@", "-");
    }

    private final String sequenceId;
    private final Date currentDate;

    /**
     * Initializes this context with a sequence ID
     *
     * @param sequenceId sequence ID
     */
    public PayloadLoggingContext(Long sequenceId) {
        this.sequenceId = String.format("%012d", sequenceId);
        this.currentDate = new Date();
    }

    /**
     * @return the current process ID
     */
    public String getProcessId() {
        return PROCESS_ID;
    }

    /**
     * @return the sequence ID
     */
    public String getSequenceId() {
        return sequenceId;
    }

    /**
     * @param formatSpecification date format specification
     * @return the current date/time of this context
     * @see SimpleDateFormat
     */
    public String date(String formatSpecification) {
        return new SimpleDateFormat(formatSpecification).format(currentDate);
    }
}
