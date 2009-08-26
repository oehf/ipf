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
import org.apache.camel.Producer;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpMarshalUtils;


/**
 * Producer-side Hl7 marshalling/unmarshalling Camel interceptor.
 *  
 * @author Dmytro Rud
 */
public class MllpProducerMarshalInterceptor extends AbstractMllpProducerInterceptor {

    public MllpProducerMarshalInterceptor(MllpEndpoint endpoint, Producer wrappedProducer) {
        super(endpoint, wrappedProducer);
    }

    
    /**
     * Marshals the request, sends it, and unmarshals the response. 
     */
    public void process(Exchange exchange) throws Exception {
        String charset = getMllpEndpoint().getConfiguration().getCharsetName();
        marshal(exchange, charset);
        getWrappedProducer().process(exchange);
        MllpMarshalUtils.unmarshal(Exchanges.resultMessage(exchange), charset);
        exchange.setProperty(Exchange.CHARSET_NAME, charset);
    }
    
    
    /**
     * Converts contents of the given Camel exchange into a String.
     */
    private void marshal(Exchange exchange, String charset) throws Exception {
        String s = MllpMarshalUtils.marshalStandardTypes(
                exchange.getIn(), 
                getMllpEndpoint().getConfiguration().getCharsetName());
        
        exchange.getIn().setBody(s);
    }
    
}
