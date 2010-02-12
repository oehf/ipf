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
package org.openehealth.ipf.platform.camel.ihe.mllp.intercept.producer;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Producer;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpAdaptingException;
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpMarshalUtils;


/**
 * Producer-side Camel interceptor which creates a {@link MessageAdapter} 
 * from various possible request types.
 *  
 * @author Dmytro Rud
 */
public class ProducerAdaptingInterceptor extends AbstractProducerInterceptor {

    public ProducerAdaptingInterceptor(MllpEndpoint endpoint, Producer wrappedProducer) {
        super(endpoint, wrappedProducer);
    }
    
    
    /**
     * Converts outgoing request to a {@link MessageAdapter}  
     * and performs some exchange configuration.
     */
    public void process(Exchange exchange) throws Exception {
        MessageAdapter msg = MllpMarshalUtils.extractMessageAdapter(
                exchange.getIn(), 
                getMllpEndpoint().getConfiguration().getCharsetName(),
                getMllpEndpoint().getParser());
        
        if(msg == null) {
            Object body = exchange.getIn().getBody();
            String className = (body == null) ? "null" : body.getClass().getName();
            throw new MllpAdaptingException("Do not know how to handle " + className);
        }
        
        exchange.getIn().setBody(msg);
        exchange.setPattern(ExchangePattern.InOut);
        
        // run the route
        getWrappedProcessor().process(exchange);
    }
}
