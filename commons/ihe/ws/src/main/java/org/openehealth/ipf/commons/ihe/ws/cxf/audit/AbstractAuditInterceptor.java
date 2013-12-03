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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;
import org.openehealth.ipf.commons.ihe.ws.InterceptorUtils;
import org.openehealth.ipf.commons.ihe.ws.cxf.AbstractSafeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;
import java.util.List;

/**
 * Base class for all ATNA audit-related CXF interceptors.
 * @author Dmytro Rud
 */
abstract public class AbstractAuditInterceptor extends AbstractSafeInterceptor {
    private static final transient Logger LOG = LoggerFactory.getLogger(AbstractAuditInterceptor.class);
    
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

    /**
     * XML Namespace URI of SAML 2.0 assertions.
     */
    public static final String SAML2_NS_URI = "urn:oasis:names:tc:SAML:2.0:assertion";

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
        WsAuditDataset auditDataset = InterceptorUtils.findContextualProperty(message, DATASET_CONTEXT_KEY);
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
     * Starting from the given source XML element, goes downwards the tree through
     * the given path, and returns the last element, if it exists.
     * @param source
     *      source element.
     * @param path
     *      path to go &mdash; a chain of qualified element names.
     *      On each level, only the first element with the given name will be considered.
     * @return
     *      last element in the chain, or <code>null</code> when not found.
     */
    private static Element getFirstChildDeep(Element source, QName... path) {
        Element element = source;
        for (int i = 0; (element != null) && (i < path.length); ++i) {
            NodeList nodeList = element.getChildNodes();
            element = null;

            QName qname = path[i];
            for (int j = 0; j < nodeList.getLength(); ++j) {
                Node node = nodeList.item(j);
                if ((node instanceof Element) &&
                    qname.getNamespaceURI().equals(node.getNamespaceURI()) &&
                    qname.getLocalPart().equals(node.getLocalName()))
                {
                    element = (Element) node;
                    break;
                }
            }
        }
        return element;
    }


    /**
     * Extracts ITI-40 XUA user name from the SAML2 assertion contained
     * in the given CXF message, and stores it in the ATNA audit dataset.
     *
     * @param message
     *      source CXF message.
     * @param headerDirection
     *      direction of the header containing the SAML2 assertion.
     * @param auditDataset
     *      target ATNA audit dataset.
     * @throws XPathExpressionException
     *      actually cannot occur.
     */
    protected static void extractXuaUserNameFromSaml2Assertion(
            SoapMessage message,
            Header.Direction headerDirection,
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
        Header header = message.getHeader(new QName(WSSE_NS_URI, "Security"));
        if (! ((header != null) &&
               headerDirection.equals(header.getDirection()) &&
               (header.getObject() instanceof Element)))
        {
            return;
        }

        Element headerElem = (Element) header.getObject();
        Element assertionElem = getFirstChildDeep(headerElem, new QName(SAML2_NS_URI, "Assertion"));
        Element issuerElem = getFirstChildDeep(assertionElem, new QName(SAML2_NS_URI, "Issuer"));
        Element nameElem = getFirstChildDeep(
                assertionElem,
                new QName(SAML2_NS_URI, "Subject"),
                new QName(SAML2_NS_URI, "NameID"));

        if ((nameElem == null) || (issuerElem == null)) {
            return;
        }

        String userName = nameElem.getTextContent();
        String issuer = issuerElem.getTextContent();

        if (StringUtils.isEmpty(issuer) || StringUtils.isEmpty(userName)) {
            return;
        }

        // set ATNA XUA userName element
        StringBuilder sb = new StringBuilder()
                .append(nameElem.getAttribute("SPProvidedID"))
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
