/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.dispatch;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.handler.logical.LogicalMessageContextImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInRequestInterceptor;

import javax.xml.ws.handler.MessageContext;

/**
 * @author Dmytro Rud
 */
public class DispatchInContextCreatorInterceptor extends AbstractPhaseInterceptor<Message> {

    public DispatchInContextCreatorInterceptor() {
        super(Phase.UNMARSHAL);
        addBefore(AuditInRequestInterceptor.class.getName());
   }

    @Override
    public void handleMessage(Message message) throws Fault {
        var messageContext = new WebServiceContextImpl().getMessageContext();
        if (messageContext == null) {
            messageContext = new LogicalMessageContextImpl(message);
            WebServiceContextImpl.setMessageContext(messageContext);
        }
        messageContext.put(MessageContext.WSDL_OPERATION, message.get(MessageContext.WSDL_OPERATION));
    }
}
