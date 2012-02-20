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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import java.util.List;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.SOAP_BODY;

import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInRequestInterceptor;

/**
 * CXF interceptor which inserts data of String content type  
 * (it is supposed to be the XML payload of the incoming message)
 * into the list of an operation's parameters or response values.
 * 
 * @author Dmytro Rud
 */
public class InPayloadInjectorInterceptor extends AbstractPhaseInterceptor<Message> {
    private final int position;
    
    /**
     * Constructs an interceptor instance.
     * @param position
     *      position in the parameter list at which the collected 
     *      message payload should be inserted.
     */
    public InPayloadInjectorInterceptor(int position) {
        super(Phase.UNMARSHAL);
        addBefore(AuditInRequestInterceptor.class.getName());
        addAfter(InNamespaceMergeInterceptor.class.getName());
        this.position = position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message message) {
        if (isGET(message)) {
            return;
        }

        List list = message.getContent(List.class);
        StringPayloadHolder payloadHolder = message.getContent(StringPayloadHolder.class);
        if ((list != null) && (payloadHolder != null)) {
            list.set(position, payloadHolder.get(SOAP_BODY));
        }
    }
}

