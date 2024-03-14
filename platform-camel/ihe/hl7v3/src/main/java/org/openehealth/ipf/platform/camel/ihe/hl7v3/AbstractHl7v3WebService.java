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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import groovy.xml.slurpersupport.GPathResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.hl7v3.*;
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditStrategy;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

import jakarta.servlet.http.HttpServletRequest;

import static java.util.Objects.requireNonNull;

/**
 * Generic Web Service implementation for HL7 v3-based transactions.
 *
 * @author Dmytro Rud
 */
@Slf4j
abstract public class AbstractHl7v3WebService extends AbstractWebService {

    private final Hl7v3WsTransactionConfiguration wsTransactionConfiguration;

    public AbstractHl7v3WebService(Hl7v3InteractionId<? extends Hl7v3WsTransactionConfiguration> hl7v3InteractionId) {
        this.wsTransactionConfiguration = requireNonNull(hl7v3InteractionId.getWsTransactionConfiguration(), "TransactionConfiguration is null");
    }
    
    /**
     * The proper message processing method.
     * @param requestString
     *      XML payload of the HL7 v3 request message.
     * @return
     *      XML payload of the HL7 v3 response message or an automatically generated NAK.
     */
    protected String doProcess(String requestString) {
        var result = process(requestString);
        var exception = Exchanges.extractException(result);
        if (exception != null) {
            log.info("HL7 v3 service failed", exception);
            return createNak(requestString, exception);
        }
        return result.getMessage().getBody(String.class);
    }

    /**
     * Creates a transaction-specific NAK message.
     */
    protected String createNak(String requestString, Throwable throwable) {
        return Hl7v3NakFactory.response(
                requestString,
                throwable,
                wsTransactionConfiguration.getNakRootElementName(),
                wsTransactionConfiguration.getControlActProcessCode(),
                false,
                wsTransactionConfiguration.isIncludeQuantities());
    }

    /**
     * Creates a transaction-specific NAK message.
     */
    protected String createNak(GPathResult request, Throwable throwable) {
        return Hl7v3NakFactory.response(
                request, throwable,
                wsTransactionConfiguration.getNakRootElementName(),
                wsTransactionConfiguration.getControlActProcessCode(),
                false,
                wsTransactionConfiguration.isIncludeQuantities());
    }

    public Hl7v3WsTransactionConfiguration getWsTransactionConfiguration() {
        return wsTransactionConfiguration;
    }


    protected Hl7v3AuditDataset startAtnaAuditing(String requestString, Hl7v3AuditStrategy auditStrategy) {
        Hl7v3AuditDataset auditDataset = null;
        if (auditStrategy != null) {
            try {
                auditDataset = auditStrategy.createAuditDataset();
                var messageContext = new WebServiceContextImpl().getMessageContext();
                var servletRequest =
                        (HttpServletRequest) messageContext.get(AbstractHTTPDestination.HTTP_REQUEST);
                if (servletRequest != null) {
                    auditDataset.setRemoteAddress(servletRequest.getRemoteAddr());
                }

                // The SOAP endpoint URL
                auditDataset.setDestinationUserId((String) messageContext.get(Message.REQUEST_URL));

                var apropos = (AddressingProperties) messageContext.get(
                                JAXWSAConstants.ADDRESSING_PROPERTIES_INBOUND);
                if ((apropos != null) && (apropos.getReplyTo() != null) && (apropos.getReplyTo().getAddress() != null)) {
                    auditDataset.setSourceUserId(apropos.getReplyTo().getAddress().getValue());
                }

                if (auditDataset.getSourceUserId() == null) {
                    auditDataset.setSourceUserId("unknown");
                }

                if (wsTransactionConfiguration.isAuditRequestPayload()) {
                    auditDataset.setRequestPayload(requestString);
                }

                auditStrategy.enrichAuditDatasetFromRequest(auditDataset, requestString, null);
            } catch (Exception e) {
                log.error("Phase 1 of server-side ATNA auditing failed", e);
            }
        }
        return auditDataset;
    }


    protected void finalizeAtnaAuditing(
            Object response,
            Hl7v3AuditStrategy auditStrategy,
            AuditContext auditContext,
            Hl7v3AuditDataset auditDataset) {
        if (auditStrategy != null) {
            auditStrategy.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
            auditStrategy.doAudit(auditContext, auditDataset);
        }
    }

}
