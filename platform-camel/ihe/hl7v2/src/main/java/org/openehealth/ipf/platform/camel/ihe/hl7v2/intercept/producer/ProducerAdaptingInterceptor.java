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

import ca.uhn.hl7v2.model.Message;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.commons.lang3.ClassUtils;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;


/**
 * Producer-side Camel interceptor which creates a {@link Message}
 * from various possible request types.
 *  
 * @author Dmytro Rud
 */
public class ProducerAdaptingInterceptor extends InterceptorSupport {

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
     * Converts outgoing request to a {@link Message}
     * and performs some exchange configuration.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        if (charsetName != null) {
            exchange.setProperty(Exchange.CHARSET_NAME, charsetName);
        }
        var msg = Hl7v2MarshalUtils.extractHapiMessage(
                exchange.getIn(),
                characterSet(exchange),
                getEndpoint(HL7v2Endpoint.class).getHl7v2TransactionConfiguration().getParser());
        
        if (msg == null) {
            throw new Hl7v2AdaptingException("Cannot create HL7v2 message from the given " +
                    ClassUtils.getSimpleName(exchange.getIn().getBody(), "<null>"));
        }
        
        exchange.getIn().setBody(msg);
        exchange.setPattern(ExchangePattern.InOut);
        
        // run the route
        getWrappedProcessor().process(exchange);
    }
}
