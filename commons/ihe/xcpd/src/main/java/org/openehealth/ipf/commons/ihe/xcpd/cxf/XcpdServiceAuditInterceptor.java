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

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.xcpd.XcpdAuditDataset;


/**
 * CXF interceptor which stores the URL of the target endpoint, 
 * the client IP address, and the request payload into audit dataset.  
 * <p>
 * Usable only on server-side.
 *
 * @author Dmytro Rud
 */
public class XcpdServiceAuditInterceptor extends AuditInterceptor {
    private final ItiServiceInfo serviceInfo;

    /**
     * Constructor.
     */
    public XcpdServiceAuditInterceptor(WsAuditStrategy auditStrategy, ItiServiceInfo serviceInfo) {
        // must be executed before the splitting of the the message flow into sync ack + async response  
        super(Phase.PRE_STREAM, auditStrategy);
        addAfter(InPayloadExtractorInterceptor.class.getName());
        this.serviceInfo = serviceInfo;
    }

    
    @Override
    protected void process(SoapMessage message) throws Exception {
        // partial responses are for us out of interest
        if (MessageUtils.isPartialResponse(message)) {
            return;
        }

        XcpdAuditDataset auditDataset = (XcpdAuditDataset) getAuditDataset(message);
        extractAddressesFromServletRequest(message, auditDataset);
        
        if (serviceInfo.isAuditRequestPayload()) {
            auditDataset.setRequestPayload(message.getContent(String.class));
        }
    }

}
