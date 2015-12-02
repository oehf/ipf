/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.core;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.core.chain.Chainable;

/**
 * Generic interceptor interface implementing a Camel {@link Processor}
 * that supports internal ordering via the {@link Chainable} interface
 *
 * @since 3.1
 */
public interface Interceptor<E extends Endpoint> extends Processor, Chainable {

    /**
     * @return the processor instance wrapped by this interceptor.
     */
    Processor getWrappedProcessor();

    /**
     * Lets this interceptor wrap the given processor.
     *
     * @param wrappedProcessor processor instance to be wrapped.
     */
    void setWrappedProcessor(Processor wrappedProcessor);

    /**
     * @return the endpoint that uses this interceptor
     */
    E getEndpoint();

    /**
     * Sets the endpoint that uses this interceptor
     *
     * @param endpoint the endpoint that uses this interceptor
     */
    void setEndpoint(E endpoint);
}
