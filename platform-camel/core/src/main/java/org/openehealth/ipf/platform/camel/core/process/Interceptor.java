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
package org.openehealth.ipf.platform.camel.core.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Provides an interceptor that can wrap the call to the next processor into
 * custom logic.
 */
public interface Interceptor {
    /**
     * Called to process an exchange and to call the next processor in the route.
     * @param exchange
     *          the exchange to process.
     * @param next
     *          the next processor.
     * @throws Exception
     *          any exception that occurred while processing the exchange.
     */
    void process(Exchange exchange, Processor next);
}
