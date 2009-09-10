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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept;

import org.apache.camel.Exchange;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AcceptanceCheckUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;

import ca.uhn.hl7v2.parser.Parser;


/**
 * Generic functionality for PIX/PDQ acceptance checking 
 * interceptors, a kind of Visitor.
 * @author Dmytro Rud
 */
public class AcceptanceInterceptorUtils {

    private AcceptanceInterceptorUtils() {
        throw new IllegalStateException("Helper class");
    }

    
    /**
     * Checks acceptance of the input message and calls the route.
     * For synchronous message types, checks acceptance of the 
     * output message as well.
     */
    public static void doProcess(
            AcceptanceInterceptor interceptor, 
            Exchange exchange) throws Exception 
    {
        MllpTransactionConfiguration config = 
            interceptor.getMllpEndpoint().getTransactionConfiguration();
        Parser parser = interceptor.getMllpEndpoint().getParser();
        MessageAdapter msg;

        // check input message
        msg = exchange.getIn().getBody(MessageAdapter.class);
        AcceptanceCheckUtils.checkRequestAcceptance(msg, config, parser); 
        
        // run the route
        interceptor.getWrappedProcessor().process(exchange);

        // check output message
        msg = Exchanges.resultMessage(exchange).getBody(MessageAdapter.class);
        AcceptanceCheckUtils.checkResponseAcceptance(msg, config, parser);
    }
}
