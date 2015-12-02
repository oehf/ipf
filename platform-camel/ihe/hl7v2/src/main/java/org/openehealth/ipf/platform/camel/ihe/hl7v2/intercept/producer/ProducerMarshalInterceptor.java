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
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;


/**
 * Producer-side Hl7 marshalling/unmarshalling interceptor.
 * @author Dmytro Rud
 */
public class ProducerMarshalInterceptor extends InterceptorSupport<HL7v2Endpoint> {

    /**
     * Marshals the request, sends it to the route, and unmarshals the response. 
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // marshal
        Message message = exchange.getIn();
        ca.uhn.hl7v2.model.Message request = message.getBody(ca.uhn.hl7v2.model.Message.class);
        message.setBody(request.toString());

        // run the route
        getWrappedProcessor().process(exchange);

        // unmarshal
        message = Exchanges.resultMessage(exchange);
        String responseString = message.getBody(String.class);
        message.setBody(getEndpoint().getHl7v2TransactionConfiguration().getParser().parse(responseString));
    }
}
