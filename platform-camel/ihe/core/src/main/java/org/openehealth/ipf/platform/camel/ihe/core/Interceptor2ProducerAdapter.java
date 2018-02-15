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

import org.apache.camel.*;

import static java.util.Objects.requireNonNull;

/**
 * Adapter for HL7v2 interceptors which allows them
 * to be exposed as Camel {@link Producer}s.
 *
 * @author Dmytro Rud
 */
public class Interceptor2ProducerAdapter implements Producer, DelegateProcessor, ServicePoolAware {
    private final Processor interceptor;
    private final Producer originalProducer;

    public Interceptor2ProducerAdapter(Processor interceptor, Producer originalProducer) {
        this.interceptor = requireNonNull(interceptor);
        this.originalProducer = requireNonNull(originalProducer);
    }

    @Override
    public Processor getProcessor() {
        return interceptor;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        interceptor.process(exchange);
    }

    @Override
    public Endpoint getEndpoint() {
        return originalProducer.getEndpoint();
    }

    @Override
    public Exchange createExchange() {
        return originalProducer.getEndpoint().createExchange();
    }

    @Override
    public Exchange createExchange(ExchangePattern pattern) {
        return originalProducer.getEndpoint().createExchange(pattern);
    }

    @Override
    @Deprecated
    public Exchange createExchange(Exchange exchange) {
        return originalProducer.getEndpoint().createExchange(exchange);
    }

    @Override
    public void start() throws Exception {
        originalProducer.start();
    }

    @Override
    public void stop() throws Exception {
        originalProducer.stop();
    }

    @Override
    public boolean isSingleton() {
        return originalProducer.isSingleton();
    }
}
