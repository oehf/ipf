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
package org.openehealth.ipf.platform.camel.ihe.xdsb.commons.cxf.audit;

import java.io.OutputStream;

import org.apache.cxf.interceptor.MessageSenderInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;


/**
 * CXF client-side interceptor that substitutes message output stream 
 * with a special wrapper that collects SOAP payload.  
 * <p>
 * Usable on client side only. 
 * 
 * @author Dmytro Rud
 */
public class ClientOutputStreamSubstituteInterceptor extends AuditInterceptor {

    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     */
    public ClientOutputStreamSubstituteInterceptor(AuditStrategy auditStrategy) {
        super(Phase.PREPARE_SEND, auditStrategy);
        addAfter(MessageSenderInterceptor.class.getName());
    }
    
    
    public void handleMessage(Message message) {
        // check whether we should process
        if(isAuditDisabled() || ( ! getAuditStrategy().needSavePayload())) {
            return;
        }

        // wrap the default output stream into a payload-collecting proxy
        OutputStream os = message.getContent(OutputStream.class);
        WrappedOutputStream wrapper = new WrappedOutputStream(os);
        message.setContent(OutputStream.class, wrapper);
    }
}
