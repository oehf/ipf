/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.phase.Phase;

import static java.util.Objects.requireNonNull;


/**
 * CXF interceptor which sends all rejected CXF exchanges
 * to the user-defined failure handler.
 * <p>
 * Usable in outgoing chains (normal and fault) on server side only.
 *
 * @author Dmytro Rud
 */
public class RejectionHandlerInterceptor extends AbstractSoapInterceptor {
    private final WsRejectionHandlingStrategy strategy;

    public RejectionHandlerInterceptor(WsRejectionHandlingStrategy strategy) {
        super(Phase.MARSHAL);
        requireNonNull(strategy);
        this.strategy = strategy;
    }


    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        Exchange exchange = message.getExchange();
        if (strategy.isRejected(exchange)) {
            strategy.handleRejectedExchange(exchange);
        }
    }
}
