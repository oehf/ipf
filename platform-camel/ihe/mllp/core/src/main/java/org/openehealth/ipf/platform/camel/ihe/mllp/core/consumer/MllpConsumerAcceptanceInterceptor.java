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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.consumer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AcceptanceCheckUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;

import ca.uhn.hl7v2.parser.Parser;


/**
 * Consumer-side acceptance checking Camel interceptor.
 * @author Dmytro Rud
 */
public class MllpConsumerAcceptanceInterceptor extends AbstractMllpConsumerInterceptor {
    
    public MllpConsumerAcceptanceInterceptor(MllpEndpoint endpoint, Processor wrappedProcessor) {
        super(endpoint, wrappedProcessor);
    }

    /**
     * Checks whether the request and response messages are acceptable. 
     */
    public void process(Exchange exchange) throws Exception {
        MllpTransactionConfiguration config = getMllpEndpoint().getTransactionConfiguration();
        Parser parser = getMllpEndpoint().getParser();
        MessageAdapter msg;

        msg = exchange.getIn().getBody(MessageAdapter.class);
        AcceptanceCheckUtils.checkRequestAcceptance(msg, config, parser); 
        
        getWrappedProcessor().process(exchange);

        msg = Exchanges.resultMessage(exchange).getBody(MessageAdapter.class);
        AcceptanceCheckUtils.checkResponseAcceptance(msg, config, parser);
    }
}
