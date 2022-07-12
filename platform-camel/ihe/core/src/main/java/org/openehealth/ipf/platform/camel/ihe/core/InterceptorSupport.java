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
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.core.chain.ChainableImpl;

import java.nio.charset.Charset;

/**
 * Convenience implementation of {@link Interceptor} for inheritance
 *
 * @since 3.1
 */
public abstract class InterceptorSupport extends ChainableImpl implements Interceptor {

    private Processor wrappedProcessor;
    private Endpoint endpoint;

    @Override
    public Processor getWrappedProcessor() {
        return wrappedProcessor;
    }

    @Override
    public void setWrappedProcessor(Processor wrappedProcessor) {
        this.wrappedProcessor = wrappedProcessor;
    }

    @Override
    public Endpoint getEndpoint() {
        return endpoint;
    }

    @Override
    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Returns character set configured in the given Camel exchange,
     * or, when none found, the system default character set.
     * @param exchange
     *      Camel exchange.
     * @return
     *      character set name.
     */
    protected static String characterSet(Exchange exchange) {
        return exchange.getProperty(Exchange.CHARSET_NAME, Charset.defaultCharset().name(), String.class);
    }
}
