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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Interface for custom interceptors that are plugged into the processing of the MLLP
 * endpoint.<p>
 * Custom interceptors allow processing an exchange before it is processed by the route.
 */
public interface MllpCustomInterceptor {
    /**
     * Called while processing an exchange to execute the interceptor logic.<p>
     * This method allows to plug in logic before and after the actual processing of
     * the exchange by the route.
     * @param nextProcessor
     *          the processor that should be called by this method.
     * @param exchange
     *          the exchange being processed.
     * @throws Exception
     *          any exception that occurred while processing the exchange.
     */
    void process(Processor nextProcessor, Exchange exchange) throws Exception;
}
