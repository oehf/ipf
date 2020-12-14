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

import lombok.Getter;
import lombok.Setter;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Evaluation context of expressions for payload log file names.
 *
 * @author Dmytro Rud
 */
public class PayloadLoggingContext {
    private static final String PROCESS_ID;

    static {
        RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
        PROCESS_ID = mx.getName().replace("@", "-");
    }

    @Getter @Setter private static String applicationId = "ipf";
    @Getter private final String sequenceId;
    private final Date currentDate;
    @Getter private final String interactionId;

    /**
     * Initializes this context with a sequence ID
     *
     * @param sequenceId sequence ID
     */
    public PayloadLoggingContext(Long sequenceId, String interactionId) {
        this.sequenceId = String.format("%012d", sequenceId);
        this.currentDate = new Date();
        this.interactionId = interactionId;
    }

    /**
     * @return the current process ID
     */
    public String getProcessId() {
        return PROCESS_ID;
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
