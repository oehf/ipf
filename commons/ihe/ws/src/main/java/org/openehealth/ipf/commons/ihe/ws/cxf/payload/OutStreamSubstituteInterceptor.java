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

import java.io.OutputStream;

import org.apache.cxf.interceptor.MessageSenderInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * CXF interceptor that substitutes message output stream 
 * with a special wrapper that collects SOAP payload.
 *   
 * @author Dmytro Rud
 */
public class OutStreamSubstituteInterceptor extends AbstractPhaseInterceptor<Message> {

    public OutStreamSubstituteInterceptor() {
        super(Phase.PREPARE_SEND);
        addAfter(MessageSenderInterceptor.class.getName());
    }
    
    @Override
    public void handleMessage(Message message) {
        OutputStream os = message.getContent(OutputStream.class);
        WrappedOutputStream wrapper = new WrappedOutputStream(os);
        message.setContent(OutputStream.class, wrapper);
    }
}
