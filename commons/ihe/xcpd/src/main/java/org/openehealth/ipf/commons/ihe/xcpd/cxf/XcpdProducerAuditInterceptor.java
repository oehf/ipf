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
package org.openehealth.ipf.commons.ihe.xcpd.cxf;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;


/**
 * CXF interceptor which stores the URL of the target endpoint into 
 * audit dataset.  
 * <p>
 * Usable only for synchronous clients, because asynchronous clients use 
 * correlation, and services get request URLs from HTTP Servlet contexts.
 *
 * @author Dmytro Rud
 */
public class XcpdProducerAuditInterceptor extends AuditInterceptor {

    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     */
    public XcpdProducerAuditInterceptor(WsAuditStrategy auditStrategy) {
        super(Phase.WRITE_ENDING, auditStrategy);
    }

    
    @Override
    protected void process(Message message) throws Exception {
        WsAuditDataset auditDataset = getAuditDataset(message);
        auditDataset.setServiceEndpointUrl((String) message.get(Message.ENDPOINT_ADDRESS));
    }

}
