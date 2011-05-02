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
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;


/**
 * Producer-side Hl7 marshalling/unmarshalling interceptor.
 * @author Dmytro Rud
 */
public class ProducerMarshalInterceptor extends AbstractProducerInterceptor {

    public ProducerMarshalInterceptor(
            Hl7v2ConfigurationHolder configurationHolder,
            Producer wrappedProducer)
    {
        super(configurationHolder, wrappedProducer);
    }

    
    /**
     * Marshals the request, sends it to the route, and unmarshals the response. 
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        Message message;
        
        // marshal
        message = exchange.getIn();
        MessageAdapter request = message.getBody(MessageAdapter.class);
        message.setBody(request.toString());

        // run the route
        getWrappedProcessor().process(exchange);

        // unmarshal
        message = Exchanges.resultMessage(exchange);
        String responseString = message.getBody(String.class);
        message.setBody(MessageAdapters.make(getTransactionConfiguration().getParser(), responseString));
    }
}
