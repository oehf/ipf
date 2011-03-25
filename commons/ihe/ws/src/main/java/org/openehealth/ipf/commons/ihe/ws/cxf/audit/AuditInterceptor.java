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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.ws.cxf.AbstractSafeInterceptor;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Base class for all ATNA audit-related CXF interceptors.
 * @author Dmytro Rud
 */
abstract public class AuditInterceptor extends AbstractSafeInterceptor {
    private static final transient Log LOG = LogFactory.getLog(AuditInterceptor.class);
    
    /**
     * Key used to store audit datasets in CXF exchanges
     */
    public static final String CXF_EXCHANGE_KEY = "atna.audit.dataset";

    /**
     * Audit strategy associated with this interceptor.  
     */
    private final WsAuditStrategy auditStrategy;

    
    /**
     * Constructor which sets a strategy.
     * 
     * @param phase
     *          the phase in which to use this interceptor.
     * @param auditStrategy
     *          an audit strategy instance. <p><code>null</code> values are
     *          explicitly prohibited. 
     */
    protected AuditInterceptor(String phase, WsAuditStrategy auditStrategy) {
        super(phase);
        Validate.notNull(auditStrategy);
        this.auditStrategy = auditStrategy;
    }
    
    
    /**
     * Returns an audit dataset instance which corresponds to the given message.
     * <p>
     * When no such instance is currently associated with the message, a new one 
     * will be created by means of the corresponding {@link WsAuditStrategy} 
     * and registered in the message's exchange.
     * 
     * @param message
     *      CXF message currently handled by this interceptor.
     * @return      
     *      an audit dataset instance, or <code>null</code> when this instance   
     *      could be neither obtained nor created from scratch.
     */
    protected WsAuditDataset getAuditDataset(SoapMessage message) {
        WsAuditDataset auditDataset = (WsAuditDataset) message.getExchange().get(CXF_EXCHANGE_KEY);
        if (auditDataset == null) {
            auditDataset = getAuditStrategy().createAuditDataset();
            if (auditDataset == null) {
                LOG.warn("Cannot obtain audit dataset instance, NPE is pending");
                return null;
            }
            message.getExchange().put(CXF_EXCHANGE_KEY, auditDataset);
        }
        return auditDataset;
    }
    
    
    /**
     * Returns the audit strategy associated with this interceptor. 
     * 
     * @return
     *      an audit strategy instance or <code>null</code> when none configured.
     */
    protected WsAuditStrategy getAuditStrategy() {
        return auditStrategy;
    }
    
    
    /**
     * Returns <code>true</code> when the given CXF message is an inbound one
     * (i.e. input or input-fault).
     */
    protected static boolean isInboundMessage(SoapMessage message) {
        Exchange exchange = message.getExchange();
        return message == exchange.getInMessage()
                || message == exchange.getInFaultMessage()
                || message.getMessage() == exchange.getInMessage()
                || message.getMessage() == exchange.getInFaultMessage();
    }
    
    
    /**
     * Extracts user ID from an WS-Addressing SOAP header and stores it in the given
     * audit dataset.
     * @param message
     *      CXF message.
     * @param isInbound
     *      <code>true</code> when the CXF message is an inbound one, 
     *      <code>false</code> otherwise. 
     * @param inverseWsaDirection
     *      <code>true</code> when direction is actually inversed, i.e. when the
     *      user ID should be taken not from the "ReplyTo:" WS-Addressing header, 
     *      but from "To:" --- useful for asynchronous responses, where the endpoint
     *      which receives the response is not the endpoint which sent the request.
     * @param auditDataset
     *      target audit dataset.
     */
    protected static void extractUserIdFromWSAddressing(
            SoapMessage message, 
            boolean isInbound, 
            boolean inverseWsaDirection,
            WsAuditDataset auditDataset) 
    {
        AddressingPropertiesImpl wsaProperties = (AddressingPropertiesImpl) message.get(isInbound ? 
                JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES_INBOUND : 
                JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES_OUTBOUND);
        
        if (wsaProperties != null) {
            AttributedURIType address = null;
            if (inverseWsaDirection) {
                address = wsaProperties.getTo();
            } else {
                EndpointReferenceType replyTo = wsaProperties.getReplyTo();
                if (replyTo != null) {
                    address = replyTo.getAddress();
                }
            }
            
            if (address != null) {
                auditDataset.setUserId(address.getValue());
            }
        } else {
            LOG.error("Missing WS-Addressing headers");
        }
    }
    
    
    /**
     * Extracts user name from WS-Security SOAP header, if available.
     * <p>
     * Seems to be of marginal nature.
     */
    protected static void extractUserNameFromWSSecurity(
            SoapMessage message, 
            WsAuditDataset auditDataset) 
    {
        // get <soapenv:Header> element
        Document rootDocument = (Document) message.getContent(org.w3c.dom.Node.class);
        if (rootDocument != null) {
            Element soapHeader = SoapUtils.getElementNS(
                rootDocument.getDocumentElement(), 
                SoapUtils.SOAP_NS_URIS, 
                "Header");
    
            // extract client User Name (as contents of <WSS:Usename>)
            Element elem = SoapUtils.getDeepElementNS(
                soapHeader,
                SoapUtils.WS_SECURITY_NS_URIS, 
                new String[] {"Security", "UsernameToken", "Username"});
            
            if (elem != null) {
                auditDataset.setUserName(elem.getTextContent());
            }
        }
    }
    
    
    /**
     * Extracts service URI and client IP address from the servlet request.
     */
    protected static void extractAddressesFromServletRequest(
            SoapMessage message,
            WsAuditDataset auditDataset) 
    {
        HttpServletRequest request = 
            (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        auditDataset.setClientIpAddress(request.getRemoteAddr());
        auditDataset.setServiceEndpointUrl(request.getRequestURL().toString());
    }


    /**
     * Extracts POJO from the given CXF message.
     * @return
     *      POJO or <code>null</code> when none found.
     */
    protected static Object extractPojo(Message message) {
        List list = message.getContent(List.class);
        return ((list == null) || list.isEmpty()) ? null : list.get(0);
    }
}
