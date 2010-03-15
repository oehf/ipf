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
package org.openehealth.ipf.commons.ihe.xds.core.cxf;

import java.util.List;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutPayloadExtractorInterceptor;


/**
 * CXF interceptor that fills the audit dataset with values from 
 * the <b>incoming</b> SOAP message (XML and/or POJO).
 * <p>
 * Usable on both client and server sides.
 * 
 * @author Dmytro Rud
 */
public class XdsAuditDatasetEnrichmentInterceptor extends AuditInterceptor {

    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     * @param serverSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>)  
     */
    public XdsAuditDatasetEnrichmentInterceptor(WsAuditStrategy auditStrategy, boolean serverSide) {
        super(serverSide ? Phase.PRE_INVOKE : Phase.WRITE_ENDING, auditStrategy);
        if (! serverSide) {
            addAfter(OutPayloadExtractorInterceptor.class.getName());
        }
    }

    
    @Override
    protected void process(Message message) throws Exception {
        WsAuditDataset auditDataset = getAuditDataset(message);

        boolean isInbound = isInboundMessage(message);
        extractUserIdFromWSAddressing(message, isInbound, false, auditDataset);
        extractUserNameFromWSSecurity(message, auditDataset);
        
        // determine client IP address and service endpoint URL
        if (isInbound) {
            extractAddressesFromServletRequest(message, auditDataset);
        } else {
            auditDataset.setServiceEndpointUrl((String) message.get(Message.ENDPOINT_ADDRESS));
        }

        // extract value prepared by (Client|Server)PayloadExctactorInterceptor
        auditDataset.setPayload(message.getContent(String.class));
        
        // perform transaction-specific audit dataset enrichment
        Object pojo = message.getContent(List.class).get(0);
        getAuditStrategy().enrichDataset(pojo, auditDataset);
    }

}
