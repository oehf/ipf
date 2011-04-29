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

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Producer;
import org.apache.commons.lang.Validate;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;


/**
 * Producer-side Camel interceptor which creates a {@link MessageAdapter} 
 * from various possible request types.
 *  
 * @author Dmytro Rud
 */
public class ProducerAdaptingInterceptor extends AbstractProducerInterceptor {

    private final String charsetName;

    public ProducerAdaptingInterceptor(
            Hl7v2ConfigurationHolder configurationHolder,
            String charsetName,
            Producer wrappedProducer)
    {
        super(configurationHolder, wrappedProducer);
        Validate.notEmpty(charsetName);
        this.charsetName = charsetName;
    }
    
    
    /**
     * Converts outgoing request to a {@link MessageAdapter}  
     * and performs some exchange configuration.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        MessageAdapter msg = Hl7v2MarshalUtils.extractMessageAdapter(
                exchange.getIn(),
                charsetName,
                getTransactionConfiguration().getParser());
        
        if (msg == null) {
            Object body = exchange.getIn().getBody();
            String className = (body == null) ? "null" : body.getClass().getName();
            throw new Hl7v2AdaptingException("Cannot create HL7v2 message from the given " + className);
        }
        
        exchange.getIn().setBody(msg);
        exchange.setPattern(ExchangePattern.InOut);
        
        // run the route
        getWrappedProcessor().process(exchange);
    }
}
