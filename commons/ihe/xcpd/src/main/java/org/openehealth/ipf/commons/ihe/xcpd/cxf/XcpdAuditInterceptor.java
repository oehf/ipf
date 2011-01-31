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
package org.openehealth.ipf.commons.ihe.xcpd.cxf;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.ServiceInvokerInterceptor;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.async.InRelatesToHackInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadInjectorInterceptor;


/**
 * CXF interceptor performs ATNA auditing for XCPD transactions.
 * <p>
 * Usable on both client and server sides.
 * 
 * @author Dmytro Rud
 */
public class XcpdAuditInterceptor extends AuditInterceptor {
    private static final transient Log LOG = LogFactory.getLog(XcpdAuditInterceptor.class);
    
    private final AsynchronyCorrelator correlator;
    private final boolean asyncReceiver;
    private final boolean serverSide;
    
    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     * @param serverSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>)  
     * @param correlator
     *      correlator for asynchronous messages.
     * @param asyncReceiver
     *      <code>true</code> when this interceptor is installed
     *      on the asynchronous receiver side. 
     */
    public XcpdAuditInterceptor(
            WsAuditStrategy auditStrategy, 
            boolean serverSide,
            AsynchronyCorrelator correlator,
            boolean asyncReceiver) 
    {
        super(isClient(asyncReceiver, serverSide) ? Phase.INVOKE : Phase.PREPARE_SEND, auditStrategy);
        if (isClient(asyncReceiver, serverSide)) {
            addAfter(InPayloadInjectorInterceptor.class.getName());
            addBefore(ServiceInvokerInterceptor.class.getName());
        }
        this.correlator = correlator;
        this.serverSide = serverSide;
        this.asyncReceiver = asyncReceiver;
    }
    
    
    /**
     * Returns <code>true</code> when the combination of parameters does mean
     * that this interceptor instance is deployed on the client side, i.e.
     * either on producer or on asynchronous response receiver.
     */
    private static boolean isClient(boolean asyncReceiver, boolean serverSide) {
        return (asyncReceiver || (! serverSide));
    }
    

    @Override
    protected void process(SoapMessage message) throws Exception {
        // partial responses are for us out of interest
        if (MessageUtils.isPartialResponse(message)) {
            return;
        }

        List<?> list = message.getContent(List.class);
        String payload = (list == null) ? null : (String) list.get(0);

        String messageId = null;
        WsAuditDataset auditDataset = null;

        // try to get the audit dataset from the asynchrony correlator --
        // will work only when we are on asynchronous receiver, and the WSA
        // RelatesTo header has been properly initialized, and the dataset
        // has not been purged from the asynchrony correlator yet.
        if (asyncReceiver) {
            messageId = InRelatesToHackInterceptor.retrieveMessageId(message.getHeaders());
            if (messageId != null) {
                auditDataset = correlator.getAuditDataset(messageId);
            } else {
                LOG.error("Cannot determine WSA message ID");
            }
        }
        if (auditDataset == null) {
            auditDataset = getAuditDataset(message);
            auditDataset.setRequestPayload(payload);
        }

        // extract user ID from WSA "To" header (not "ReplyTo" due to direction inversion!)
        extractUserIdFromWSAddressing(
                message,
                isClient(asyncReceiver, serverSide),
                serverSide, 
                auditDataset);

        // Perform transaction-specific enrichment of the audit dataset.
        // Depending on the side, the payload will be either request or response one.
        getAuditStrategy().enrichDataset(payload, auditDataset);
        
        // perform transaction-specific auditing
        auditDataset.setEventOutcomeCode(getAuditStrategy().getEventOutcomeCode(payload));
        getAuditStrategy().audit(auditDataset);
    }
    
}
