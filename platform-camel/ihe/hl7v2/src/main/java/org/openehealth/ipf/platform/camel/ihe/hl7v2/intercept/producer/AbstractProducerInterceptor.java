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
package org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer;

import org.apache.camel.*;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.AbstractHl7v2Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;


/**
 * Abstract producer-side Camel interceptor for HL7v2 transactions.
 * @author Dmytro Rud
 */
public abstract class AbstractProducerInterceptor 
       extends AbstractHl7v2Interceptor
       implements Producer, ServicePoolAware 
{
    /**
     * Constructor.
     * @param configurationHolder
     *      Configuration.
     * @param wrappedProducer
     *      The producer to be wrapped.
     */
    protected AbstractProducerInterceptor(
            Hl7v2ConfigurationHolder configurationHolder,
            Processor wrappedProducer)
    {
        super(configurationHolder, wrappedProducer);
    }

    
    // ----- dumb delegation, nothing interesting below -----

    private Producer getWrappedProducer() {
        return (Producer) getWrappedProcessor();
    }
    
    @Override
    public Endpoint getEndpoint() {
        return getWrappedProducer().getEndpoint();
    }

    @Override
    public Exchange createExchange() {
        return getWrappedProducer().createExchange();
    }

    @Override
    public Exchange createExchange(ExchangePattern pattern) {
        return getWrappedProducer().createExchange(pattern);
    }

    @Override
    public Exchange createExchange(Exchange exchange) {
        return getWrappedProducer().createExchange(exchange);
    }

    @Override
    public void start() throws Exception {
        getWrappedProducer().start();
    }

    @Override
    public void stop() throws Exception {
        getWrappedProducer().stop();
    }

    @Override
    public boolean isSingleton() {
        return getWrappedProducer().isSingleton();
    }
}
