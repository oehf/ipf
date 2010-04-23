/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.async;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.CheckFaultInterceptor;
import org.apache.cxf.interceptor.InFaultChainInitiatorObserver;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * CXF interceptor which inserts a dummy out SOAP message into 
 * the exchange to prevent an NPE on asynchronous fault notifications.
 * 
 * @author Dmytro Rud
 */
public class InFaultHackInterceptor extends AbstractPhaseInterceptor<Message> {
    
    public InFaultHackInterceptor() {
        super(Phase.POST_PROTOCOL);
        addBefore(CheckFaultInterceptor.class.getName());
    }

    /**
     * See {@link InFaultChainInitiatorObserver#initializeInterceptors()}, 
     * line #59 in CXF 2.2.6: NPE on incoming faults without requests 
     * (WSA async response or notification).
     */
    @Override
    public void handleMessage(Message message) {
        message.getExchange().setOutMessage(new SoapMessage(new MessageImpl()));
    }
}
