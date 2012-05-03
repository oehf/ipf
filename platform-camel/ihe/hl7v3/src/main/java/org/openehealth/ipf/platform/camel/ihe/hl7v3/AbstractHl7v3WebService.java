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

import groovy.util.slurpersupport.GPathResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.Validate;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.MessageContext;

/**
 * Generic Web Service implementation for HL7 v3-based transactions.
 * @author Dmytro Rud
 */
@Slf4j
abstract public class AbstractHl7v3WebService extends AbstractWebService {

    private final Hl7v3WsTransactionConfiguration wsTransactionConfiguration;

    public AbstractHl7v3WebService(Hl7v3WsTransactionConfiguration wsTransactionConfiguration) {
        Validate.notNull(wsTransactionConfiguration);
        this.wsTransactionConfiguration = wsTransactionConfiguration;
    }
    
    /**
     * The proper message processing method.
     * @param requestString
     *      XML payload of the HL7 v3 request message.
     * @return
     *      XML payload of the HL7 v3 response message or an automatically generated NAK.
     */
    protected String doProcess(String requestString) {
        Exchange result = process(requestString);
        if (result.getException() != null) {
            log.debug("HL7 v3 service failed", result.getException());
            return createNak(requestString, result.getException());
        }
        return Exchanges.resultMessage(result).getBody(String.class);
    }

    /**
     * Creates a transaction-specific NAK message.
     */
    protected String createNak(String requestString, Throwable throwable) {
        return Hl7v3NakFactory.response(
                requestString,
                throwable,
                wsTransactionConfiguration.getNakRootElementName(),
                wsTransactionConfiguration.isNakNeedControlActProcess(),
                false);
    }

    /**
     * Creates a transaction-specific NAK message.
     */
    protected String createNak(GPathResult request, Throwable throwable) {
        return Hl7v3NakFactory.response(
                request, throwable,
                wsTransactionConfiguration.getNakRootElementName(),
                wsTransactionConfiguration.isNakNeedControlActProcess(),
                false);
    }

    public Hl7v3WsTransactionConfiguration getWsTransactionConfiguration() {
        return wsTransactionConfiguration;
    }


    protected Hl7v3AuditDataset startAtnaAuditing(String requestString, Hl7v3AuditStrategy auditStrategy) {
        Hl7v3AuditDataset auditDataset = null;
        if (auditStrategy != null) {
            try {
                auditDataset = auditStrategy.createAuditDataset();
                MessageContext messageContext = new WebServiceContextImpl().getMessageContext();
                HttpServletRequest servletRequest =
                        (HttpServletRequest) messageContext.get(AbstractHTTPDestination.HTTP_REQUEST);
                if (servletRequest != null) {
                    auditDataset.setClientIpAddress(servletRequest.getRemoteAddr());
                }
                auditDataset.setServiceEndpointUrl((String) messageContext.get(Message.REQUEST_URL));

                AddressingProperties apropos = (AddressingProperties) messageContext.get(
                                JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
                if ((apropos != null) && (apropos.getReplyTo() != null) && (apropos.getReplyTo().getAddress() != null)) {
                    auditDataset.setUserId(apropos.getReplyTo().getAddress().getValue());
                }

                if (wsTransactionConfiguration.isAuditRequestPayload()) {
                    auditDataset.setRequestPayload(requestString);
                }

                auditStrategy.enrichDatasetFromRequest(requestString, auditDataset);
            } catch (Exception e) {
                log.error("Phase 1 of server-side ATNA auditing failed", e);
            }
        }
        return auditDataset;
    }


    protected void finalizeAtnaAuditing(
            Object response,
            Hl7v3AuditStrategy auditStrategy,
            Hl7v3AuditDataset auditDataset)
    {
        if (auditStrategy != null) {
            try {
                auditStrategy.enrichDatasetFromResponse(response, auditDataset);
                auditStrategy.audit(auditDataset);
            } catch (Exception e) {
                log.error("Phase 2 of server-side ATNA auditing failed", e);
            }
        }
    }

}
