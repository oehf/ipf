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
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.AbstractHl7v2Interceptor;


/**
 * Producer-side Camel interceptor which creates a {@link MessageAdapter} 
 * from various possible request types.
 *  
 * @author Dmytro Rud
 */
public class ProducerAdaptingInterceptor extends AbstractHl7v2Interceptor {

    private final String charsetName;


    /**
     * Constructor which enforces the use of a particular character set.
     * The given value will be propagated to the Camel exchange property
     * {@link Exchange#CHARSET_NAME}, rewriting its old content.
     *
     * @param charsetName
     *      character set to use in all data transformations.
     */
    public ProducerAdaptingInterceptor(String charsetName) {
        super();
        this.charsetName = charsetName;
    }


    /**
     * Constructor which does not enforce the use of a particular character set.
     * When the Camel exchange does not contain property {@link Exchange#CHARSET_NAME},
     * the default system character set will be used.
     */
    public ProducerAdaptingInterceptor() {
        this(null);
    }


    /**
     * Converts outgoing request to a {@link MessageAdapter}  
     * and performs some exchange configuration.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        if (charsetName != null) {
            exchange.setProperty(Exchange.CHARSET_NAME, charsetName);
        }
        MessageAdapter<?> msg = Hl7v2MarshalUtils.extractMessageAdapter(
                exchange.getIn(),
                characterSet(exchange),
                getHl7v2TransactionConfiguration().getParser());
        
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
