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
package org.openehealth.ipf.platform.camel.test.performance.process;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.test.performance.Measurement;

/**
 * 
 * @author Mitko Kolev
 */
public class CheckpointProcessor extends TimeProcessor {
    private final String name;

    /**
     * Creates a CheckpointProcessor with the given <code>name</code>
     * 
     * @param name
     */
    public CheckpointProcessor(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // create measurement data
        Measurement measurement = new Measurement(getCurrentTimestamp(), name);
        registerMeasurement(exchange, measurement);
    }

}
