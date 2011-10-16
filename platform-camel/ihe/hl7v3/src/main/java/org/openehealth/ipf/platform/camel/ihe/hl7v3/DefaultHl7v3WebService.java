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
import org.apache.camel.Exchange;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.MessageContext;

/**
 * Generic Web Service implementation for HL7 v3-based transactions.
 * @author Dmytro Rud
 */
public class DefaultHl7v3WebService extends DefaultItiWebService {
    private static final transient Log LOG = LogFactory.getLog(DefaultHl7v3WebService.class);

    private final Hl7v3WsTransactionConfiguration wsTransactionConfiguration;

    public DefaultHl7v3WebService(Hl7v3WsTransactionConfiguration wsTransactionConfiguration) {
        Validate.notNull(wsTransactionConfiguration);
        this.wsTransactionConfiguration = wsTransactionConfiguration;
    }
    
    /**
     * The proper message processing method.
     * @param request
     *      XML payload of the HL7 v3 request message, actually always a String from CXF.
     * @return
     *      XML payload of the HL7 v3 response message or an automatically generated NAK.
     */
    protected Object doProcess(Object request) {
        String requestString = (String) request;
        Exchange result = process(request);
        return (result.getException() != null)
                ? createNak(requestString, result.getException())
                : prepareBody(result);
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



    protected WsAuditDataset startAtnaAuditing(String requestString, WsAuditStrategy auditStrategy) {
        WsAuditDataset auditDataset = null;
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
                LOG.error("Error while starting manual ATNA auditing", e);
            }
        }
        return auditDataset;
    }


    protected void finalizeAtnaAuditing(
            Object response,
            WsAuditStrategy auditStrategy,
            WsAuditDataset auditDataset)
    {
        if (auditStrategy != null) {
            try {
                auditStrategy.enrichDatasetFromResponse(response, auditDataset);
                auditStrategy.audit(auditDataset);
            } catch (Exception e) {
                LOG.error("Error while finalizing manual ATNA auditing", e);
            }
        }
    }

}
