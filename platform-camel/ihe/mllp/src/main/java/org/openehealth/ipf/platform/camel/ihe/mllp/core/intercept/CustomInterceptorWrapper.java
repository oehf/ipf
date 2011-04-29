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
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;

/**
 * Wrapper to use a {@link MllpCustomInterceptor} as an
 * {@link org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2Interceptor}.
 */
public class CustomInterceptorWrapper extends AbstractMllpInterceptor {
    private final MllpCustomInterceptor wrappedInterceptor;

    /**
     * Constructor.
     * @param wrappedInterceptor
     *      The custom interceptor that is wrapped by this interceptor.
     * @param endpoint
     *      The Camel endpoint to which this interceptor belongs.
     * @param wrappedProcessor
     *      Original camel-mina processor.
     */
    public CustomInterceptorWrapper(MllpCustomInterceptor wrappedInterceptor, MllpEndpoint endpoint, Processor wrappedProcessor) {
        super(endpoint, wrappedProcessor);
        this.wrappedInterceptor = wrappedInterceptor;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        wrappedInterceptor.process(getWrappedProcessor(), exchange);
    }
}
