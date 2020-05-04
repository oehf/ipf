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
package org.openehealth.ipf.commons.ihe.ws.cxf.audit;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.ServiceInvokerInterceptor;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.VersionTransformer;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadInjectorInterceptor;
import org.w3c.dom.Element;


/**
 * CXF interceptor for ATNA auditing in WS-based IHE transactions with
 * WSA asynchrony support.  Handles <b>response</b> messages.
 * Usable on client, server, and asynchronous response receiver sides.
 * 
 * @author Dmytro Rud
 */
public class AuditResponseInterceptor<T extends WsAuditDataset> extends AbstractAuditInterceptor<T> {
    private static final transient Logger LOG = LoggerFactory.getLogger(AuditResponseInterceptor.class);

    private final AsynchronyCorrelator<T> correlator;
    private final boolean asyncReceiver;
    private final boolean serverSide;
    
    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance.
     * @param serverSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>).
     *      Server side is where the response is generated.
     * @param correlator
     *      correlator for asynchronous messages (<code>null</code> on server side).
     * @param asyncReceiver
     *      <code>true</code> when this interceptor is installed
     *      on the asynchronous receiver side. 
     */
    public AuditResponseInterceptor(
            AuditStrategy<T> auditStrategy,
            AuditContext auditContext,
            boolean serverSide,
            AsynchronyCorrelator<T> correlator,
            boolean asyncReceiver) 
    {
        super(isClient(asyncReceiver, serverSide) ? Phase.INVOKE : Phase.PREPARE_SEND, auditStrategy, auditContext);
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
    protected void process(SoapMessage message) {
        if (isGET(message)) {
            return;
        }

        // partial responses are for us out of interest
        if (MessageUtils.isPartialResponse(message)) {
            return;
        }

        // check whether the response is relevant for ATNA audit finalization
        var response = extractPojo(message);
        var auditStrategy = getAuditStrategy();
        if (! auditStrategy.isAuditableResponse(response)) {
            return;
        }

        T auditDataset = null;

        // try to get the audit dataset from the asynchrony correlator --
        // will work only when we are on asynchronous receiver, and the WSA
        // RelatesTo header has been properly initialized, and the dataset
        // has not been purged from the asynchrony correlator yet.
        if (asyncReceiver) {
            String messageId = null;
            for (var header : message.getHeaders()) {
                if ("RelatesTo".equals(header.getName().getLocalPart())
                        && VersionTransformer.isSupported(header.getName().getNamespaceURI()))
                {
                    messageId = ((Element) header.getObject()).getTextContent();
                    break;
                }
            }
            if (messageId != null) {
                auditDataset = correlator.getAuditDataset(messageId);
                // message.getExchange().put(CXF_EXCHANGE_KEY, auditDataset);
            } else {
                LOG.error("Cannot determine WSA message ID");
            }
        }
        if (auditDataset == null) {
            auditDataset = getAuditDataset(message);
        }

        // extract user ID from WSA "To" header (not "ReplyTo" due to direction inversion!)
        extractUserIdFromWSAddressing(
                message,
                isClient(asyncReceiver, serverSide),
                serverSide, 
                auditDataset);

        // check whether the response POJO is available and
        // perform transaction-specific enrichment of the audit dataset
        var exchange = message.getExchange();
        if ((message == exchange.getInFaultMessage())
                || (message == exchange.getOutFaultMessage())
                || (response == null))
        {
            auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.SeriousFailure);
        } else {
            auditStrategy.enrichAuditDatasetFromResponse(auditDataset, response, getAuditContext());
        }
        
        // perform transaction-specific auditing
        auditStrategy.doAudit(getAuditContext(), auditDataset);
    }
    
}
