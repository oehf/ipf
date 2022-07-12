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
package org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept;

import ca.uhn.hl7v2.model.Message;
import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;


/**
 * Generic functionality for HL7v2 acceptance checking interceptors.
 * @author Dmytro Rud
 */
public class AcceptanceInterceptorUtils {

    private AcceptanceInterceptorUtils() {
        throw new IllegalStateException("Helper class");
    }

    
    /**
     * Checks acceptance of the input message and calls the route.
     */
    public static void processRequest(
            InterceptorSupport interceptor,
            Exchange exchange) throws Exception
    {
        // check input message
        var config = interceptor.getEndpoint(HL7v2Endpoint.class).getHl7v2TransactionConfiguration();
        config.checkRequestAcceptance(exchange.getIn().getBody(Message.class));
        
        // run the route
        interceptor.getWrappedProcessor().process(exchange);
    }
    
    
    /**
     * Calls the route and checks acceptance of the output message.
     */
    public static void processResponse(
            InterceptorSupport interceptor,
            Exchange exchange) throws Exception 
    {
        // run the route
        interceptor.getWrappedProcessor().process(exchange);

        // check output message
        var config = interceptor.getEndpoint(HL7v2Endpoint.class).getHl7v2TransactionConfiguration();
        config.checkResponseAcceptance(exchange.getMessage().getBody(Message.class));
    }
}
