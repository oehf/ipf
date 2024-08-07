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
package org.openehealth.ipf.commons.ihe.ws.cxf.audit;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.cxf.ws.addressing.Names;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder;

/**
 * CXF interceptor for ATNA auditing in WS-based IHE transactions with
 * WSA asynchrony support.  Handles <b>outgoing</b> requests
 * on <b>producer</b> side.
 *
 * @author Dmytro Rud
 */
public class AuditOutRequestInterceptor<T extends WsAuditDataset> extends AbstractAuditInterceptor<T> {
    private final AsynchronyCorrelator<T> correlator;
    private final WsTransactionConfiguration<T> wsTransactionConfiguration;

    /**
     * Constructor.
     */
    public AuditOutRequestInterceptor(
            AuditStrategy<T> auditStrategy,
            AuditContext auditContext,
            AsynchronyCorrelator<T> correlator,
            WsTransactionConfiguration<T> wsTransactionConfiguration)
    {
        super(Phase.PRE_PROTOCOL_ENDING, auditStrategy, auditContext);
        addAfter(OutPayloadExtractorInterceptor.class.getName());
        this.correlator = correlator;
        this.wsTransactionConfiguration = wsTransactionConfiguration;
    }

    
    @Override
    protected void process(SoapMessage message) {
        if (isGET(message)) {
            return;
        }

        var auditDataset = getAuditDataset(message);
        auditDataset.setRemoteAddress((String) message.get(Message.ENDPOINT_ADDRESS));
        auditDataset.setDestinationUserId((String) message.get(Message.ENDPOINT_ADDRESS));
        enrichAuditDatasetFromRequest(message, Header.Direction.DIRECTION_OUT, auditDataset);

        var request = extractPojo(message);

        // Get request payload, handle different variants thereby:
        //   a) for HL7v3-based transactions, payload corresponds to the "main" message;
        //   b) for ebXML-based transactions, rely on the {@link OutPayloadExtractorInterceptor}.
        if (wsTransactionConfiguration.isAuditRequestPayload()) {
            if (request instanceof String) {
                auditDataset.setRequestPayload((String) request);
            } else {
                auditDataset.setRequestPayload(message.getContent(StringPayloadHolder.class));
            }
        }

        getAuditStrategy().enrichAuditDatasetFromRequest(auditDataset, request, message);

        // when the invocation is asynchronous: store audit dataset into the correlator
        var props = (AddressingProperties) message.get(JAXWSAConstants.ADDRESSING_PROPERTIES_OUTBOUND);
        if (props != null && (Boolean.TRUE.equals(message.getContextualProperty(AsynchronyCorrelator.FORCE_CORRELATION)) ||
            ! Names.WSA_ANONYMOUS_ADDRESS.equals(props.getReplyTo().getAddress().getValue())))
        {
            correlator.storeAuditDataset(props.getMessageID().getValue(), auditDataset);
        }
    }

}
