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
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.AcceptanceCheckUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpTransactionConfiguration;

import ca.uhn.hl7v2.parser.Parser;


/**
 * Producer-side acceptance checking Camel interceptor.
 *  
 * @author Dmytro Rud
 */
public class MllpProducerAcceptanceInterceptor extends AbstractMllpProducerInterceptor {

    public MllpProducerAcceptanceInterceptor(MllpEndpoint endpoint, Producer wrappedProducer) {
        super(endpoint, wrappedProducer);
    }
    
    /**
     * Checks whether request and response messages are acceptable.
     * <p>
     * Expects {@link MessageAdapter} contents
     * both on the way there and on the way back. 
     */
    public void process(Exchange exchange) throws Exception {
        MllpTransactionConfiguration config = getMllpEndpoint().getTransactionConfiguration();
        Parser parser = getMllpEndpoint().getParser();
        MessageAdapter msg;
        
        msg = exchange.getIn().getBody(MessageAdapter.class);
        AcceptanceCheckUtils.checkRequestAcceptance(msg, config, parser); 

        getWrappedProducer().process(exchange);

        msg = Exchanges.resultMessage(exchange).getBody(MessageAdapter.class);
        AcceptanceCheckUtils.checkResponseAcceptance(msg, config, parser);
    }
}
