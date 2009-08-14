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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons.producer;

import org.apache.camel.Endpoint;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Producer;
import org.apache.camel.component.mina.MinaExchange;
import org.apache.commons.lang.Validate;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;


/**
 * Abstract producer-side Camel interceptor.
 *  
 * @author Dmytro Rud
 */
public abstract class AbstractMllpProducerInterceptor implements Producer<MinaExchange> {

    private final MllpEndpoint endpoint;
    private final Producer<MinaExchange> wrappedProducer;


    /**
     * Constructor.
     * @param endpoint
     *      The Camel endpoint to which this producer belongs.
     * @param wrappedProducer
     *      The producer to be wrapped.
     */
    public AbstractMllpProducerInterceptor(MllpEndpoint endpoint, Producer<MinaExchange> wrappedProducer) 
    {
        Validate.notNull(endpoint);
        Validate.notNull(wrappedProducer);

        this.endpoint = endpoint;
        this.wrappedProducer = wrappedProducer;
    }


    public Producer<MinaExchange> getWrappedProducer() {
        return wrappedProducer;
    }

    public Endpoint<MinaExchange> getEndpoint() {
        return endpoint;
    }
    
    public MllpEndpoint getMllpEndpoint() {
        return endpoint;
    }
    
    // ----- dumb delegation, nothing interesting below -----
    
    public MinaExchange createExchange() {
        return getWrappedProducer().createExchange();
    }

    public MinaExchange createExchange(ExchangePattern pattern) {
        return getWrappedProducer().createExchange(pattern);
    }

    public MinaExchange createExchange(MinaExchange exchange) {
        return getWrappedProducer().createExchange(exchange);
    }

    public void start() throws Exception {
        getWrappedProducer().start();
    }

    public void stop() throws Exception {
        getWrappedProducer().stop();
    }
}
