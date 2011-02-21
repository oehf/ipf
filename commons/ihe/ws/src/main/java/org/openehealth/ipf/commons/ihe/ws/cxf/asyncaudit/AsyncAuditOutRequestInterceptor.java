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
package org.openehealth.ipf.commons.ihe.ws.cxf.asyncaudit;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.cxf.ws.addressing.Names;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;

/**
 * CXF interceptor for ATNA auditing in WS-based IHE transactions with
 * WSA asynchrony support.  Handles </b>outgoing</b> requests
 * on <b>producer</b> side.
 *
 * @author Dmytro Rud
 */
public class AsyncAuditOutRequestInterceptor extends AuditInterceptor {
    private final AsynchronyCorrelator correlator;
    private final ItiServiceInfo serviceInfo;

    /**
     * Constructor.
     */
    public AsyncAuditOutRequestInterceptor(
            WsAuditStrategy auditStrategy,
            AsynchronyCorrelator correlator,
            ItiServiceInfo serviceInfo)
    {
        super(Phase.WRITE_ENDING, auditStrategy);
        this.correlator = correlator;
        this.serviceInfo = serviceInfo;
    }

    
    @Override
    protected void process(SoapMessage message) throws Exception {
        WsAuditDataset auditDataset = getAuditDataset(message);
        auditDataset.setServiceEndpointUrl((String) message.get(Message.ENDPOINT_ADDRESS));

        Object request = extractPojo(message);

        // Get request payload, handle different variants thereby:
        //   a) for HL7v3-based transactions, payload corresponds to the "main" message;
        //   b) for ebXML-based transactions, rely on the {@link OutPayloadExtractorInterceptor}.
        if (serviceInfo.isAuditRequestPayload()) {
            String payload = (request instanceof String) ?
                    (String) request :
                    message.getContent(String.class);
            auditDataset.setRequestPayload(payload);
        }

        getAuditStrategy().enrichDatasetFromRequest(request, auditDataset);

        // when the invocation is asynchronous: store audit dataset into the correlator
        AddressingProperties props = (AddressingProperties) message.get(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES_OUTBOUND);
        if (! Names.WSA_ANONYMOUS_ADDRESS.equals(props.getReplyTo().getAddress().getValue())) {
            correlator.storeAuditDataset(props.getMessageID().getValue(), auditDataset);
        }
    }

}
