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

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.component.mina.MinaExchange;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpMarshalUtils;

import ca.uhn.hl7v2.parser.PipeParser;


/**
 * Producer-side Camel interceptor which creates a {@link MessageAdapter} 
 * from various possible request types.
 *  
 * @author Dmytro Rud
 */
public class MllpProducerAdaptingInterceptor extends AbstractMllpProducerInterceptor {

    public MllpProducerAdaptingInterceptor(MllpEndpoint endpoint, Producer<MinaExchange> wrappedProducer) {
        super(endpoint, wrappedProducer);
    }
    
    
    /**
     * Converts outgoing request to a {@link MessageAdapter},
     * does nothing with incoming response.
     */
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        Object body = message.getBody(); 
        if(body instanceof MessageAdapter) {
            // already o.k.
        } else if(body instanceof ca.uhn.hl7v2.model.Message) {
            body = new MessageAdapter(new PipeParser(), (ca.uhn.hl7v2.model.Message) body);
        } else {
            // process all other types (String, File, InputStream, ByteBuffer, byte[])
            // by means of the standard routine.  An exception here will be o.k.
            String s = MllpMarshalUtils.marshalStandardTypes(
                    exchange, 
                    getMllpEndpoint().getCharsetName());
            s = s.replace('\n', '\r');
            body = MessageAdapters.make(new PipeParser(), s);
        } 
        exchange.getIn().setBody(body);
        getWrappedProducer().process(exchange);
    }
}
