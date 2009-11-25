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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils;


/**
 * Producer-side Hl7 marshalling/unmarshalling Camel interceptor.
 * @author Dmytro Rud
 */
public class ProducerMarshalInterceptor extends AbstractProducerInterceptor {

    public ProducerMarshalInterceptor(MllpEndpoint endpoint, Producer wrappedProducer) {
        super(endpoint, wrappedProducer);
    }

    
    /**
     * Marshals the request, sends it to the route, and unmarshals the response. 
     */
    public void process(Exchange exchange) throws Exception {
        String charset = getMllpEndpoint().getConfiguration().getCharsetName();

        // marshal
        String s = MllpMarshalUtils.marshalStandardTypes(
                exchange.getIn(), 
                charset,
                getMllpEndpoint().getParser());
        exchange.getIn().setBody(s);

        // run the route
        Processor processor = getWrappedProcessor();
        process(exchange, processor);

        // unmarshal
        MllpMarshalUtils.unmarshal(
                Exchanges.resultMessage(exchange),
                charset,
                getMllpEndpoint().getParser());
        exchange.setProperty(Exchange.CHARSET_NAME, charset);
    }

    private void process(Exchange exchange, Processor processor) throws Exception {
        processor.process(exchange);
    }
}
