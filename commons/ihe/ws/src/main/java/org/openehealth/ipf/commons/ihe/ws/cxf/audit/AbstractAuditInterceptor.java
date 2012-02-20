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

import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.ws.cxf.AbstractSafeInterceptor;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.*;
import java.util.List;

/**
 * Base class for all ATNA audit-related CXF interceptors.
 * @author Dmytro Rud
 */
abstract public class AbstractAuditInterceptor extends AbstractSafeInterceptor {
    private static final transient Log LOG = LogFactory.getLog(AbstractAuditInterceptor.class);
    
    /**
     * Key used to store audit datasets in Web Service contexts.
     */
    public static final String DATASET_CONTEXT_KEY = AbstractAuditInterceptor.class.getName() + ".DATASET";

    /**
     * Key used to find XUA user name tokens in Web Service contexts.
     */
    public static final String XUA_USERNAME_CONTEXT_KEY = AbstractAuditInterceptor.class.getName() + ".XUA_USERNAME";

    /**
     * XML Namespace URI of WS-Security Extensions 1.1.
     */
    public static final String WSSE_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    private static final SimpleNamespaceContext WSS_NS_CONTEXT;
    static {
        WSS_NS_CONTEXT = new SimpleNamespaceContext();
        WSS_NS_CONTEXT.bindNamespaceUri("wsse", WSSE_NS_URI);
        WSS_NS_CONTEXT.bindNamespaceUri("soap", Soap12.SOAP_NAMESPACE);
        WSS_NS_CONTEXT.bindNamespaceUri("saml2", "urn:oasis:names:tc:SAML:2.0:assertion");
    }

    private static final String XUA_EXPRESSION_PREFIX    = "/soap:Envelope/soap:Header/wsse:Security[1]/saml2:Assertion[1]";
    private static final String XUA_NAME_NODE_EXPRESSION = XUA_EXPRESSION_PREFIX + "/saml2:Subject[1]/saml2:NameID[1]";
    private static final String XUA_ISSUES_EXPRESSION    = XUA_EXPRESSION_PREFIX + "/saml2:Issuer[1]/text()";

    private static final ThreadLocal<XPathExpression[]> XUA_XPATH_EXPRESSIONS = new ThreadLocal<XPathExpression[]>() {
        @Override
        protected XPathExpression[] initialValue() {
            try {
                XPath xPath = XPathFactory.newInstance().newXPath();
                xPath.setNamespaceContext(WSS_NS_CONTEXT);
                return new XPathExpression[] {
                    xPath.compile(XUA_NAME_NODE_EXPRESSION),
                    xPath.compile(XUA_ISSUES_EXPRESSION)
                };
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
    };


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
    protected AbstractAuditInterceptor(String phase, WsAuditStrategy auditStrategy) {
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
        WsAuditDataset auditDataset = findContextualProperty(message, DATASET_CONTEXT_KEY);
        if (auditDataset == null) {
            auditDataset = getAuditStrategy().createAuditDataset();
            if (auditDataset == null) {
                LOG.warn("Cannot obtain audit dataset instance, NPE is pending");
                return null;
            }
            message.setContextualProperty(DATASET_CONTEXT_KEY, auditDataset);
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
     * Extracts ITI-40 XUA user name from the SAML2 assertion contained
     * in the given CXF message, and stores it in the ATNA audit dataset.
     *
     * @param message
     *      source CXF message.
     * @param auditDataset
     *      target ATNA audit dataset.
     * @throws XPathExpressionException
     *      actually cannot occur.
     */
    protected static void extractXuaUserNameFromSaml2Assertion(
            SoapMessage message,
            WsAuditDataset auditDataset)
                throws XPathExpressionException
    {
        // check whether someone has already parsed the SAML2 assertion
        // and provided the XUA user name for us via WS message context
        if (message.getContextualProperty(XUA_USERNAME_CONTEXT_KEY) != null) {
            auditDataset.setUserName(message.getContextualProperty(XUA_USERNAME_CONTEXT_KEY).toString());
            return;
        }

        // extract information from SAML2 assertion
        XPathExpression[] expressions = XUA_XPATH_EXPRESSIONS.get();
        Node envelopeNode = message.getContent(Node.class);
        Node nameNode = (Node) expressions[0].evaluate(envelopeNode, XPathConstants.NODE);
        if (nameNode == null) {
            return;
        }

        String issuer = (String) expressions[1].evaluate(envelopeNode, XPathConstants.STRING);
        String userName = nameNode.getTextContent();
        if (issuer.isEmpty() || userName.isEmpty()) {
            return;
        }

        // set ATNA XUA userName element
        StringBuilder sb = new StringBuilder()
                .append(((Element) nameNode).getAttribute("SPProvidedID"))
                .append('<')
                .append(userName)
                .append('@')
                .append(issuer)
                .append('>');
        auditDataset.setUserName(sb.toString());
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
        List<?> list = message.getContent(List.class);
        return ((list == null) || list.isEmpty()) ? null : list.get(0);
    }
}
