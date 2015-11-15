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

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.ws.InterceptorUtils;
import org.openehealth.ipf.commons.ihe.ws.cxf.AbstractSafeInterceptor;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistry;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
     * If a SAML assertion is stored under this key in the Web Service context,
     * IPF will use it instead of parsing the WS-Security header by itself.
     * If there are no Web Service context element under this key, or if this element
     * does not contain a SAML assertion, IPF will parse the WS-Security header
     * and store the assertion extracted from there (if any) under this key.
     */
    public static final String XUA_SAML_ASSERTION = AbstractAuditInterceptor.class.getName() + ".XUA_SAML_ASSERTION";

    /**
     * XML Namespace URI of WS-Security Extensions 1.1.
     */
    public static final String WSSE_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    /**
     * Audit strategy associated with this interceptor.  
     */
    private final WsAuditStrategy auditStrategy;


    private static final UnmarshallerFactory SAML_UNMARSHALLER_FACTORY;

    static {
        try {
            InitializationService.initialize();
            XMLObjectProviderRegistry registry = ConfigurationService.get(XMLObjectProviderRegistry.class);
            SAML_UNMARSHALLER_FACTORY = registry.getUnmarshallerFactory();
        } catch (InitializationException e) {
            throw new RuntimeException(e);
        }
    }


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
            message.getExchange().put(DATASET_CONTEXT_KEY, auditDataset);
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
        AddressingProperties wsaProperties = (AddressingProperties) message.get(isInbound ?
                JAXWSAConstants.ADDRESSING_PROPERTIES_INBOUND :
                JAXWSAConstants.ADDRESSING_PROPERTIES_OUTBOUND);
        
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
     * @param headerDirection
     *      direction of the header containing the SAML2 assertion.
     * @param auditDataset
     *      target ATNA audit dataset.
     */
    protected static void extractXuaUserNameFromSaml2Assertion(
            SoapMessage message,
            Header.Direction headerDirection,
            WsAuditDataset auditDataset)
    {
        Assertion assertion = null;

        // check whether someone has already parsed the SAML2 assertion
        Object o = message.getContextualProperty(XUA_SAML_ASSERTION);
        if (o instanceof Assertion) {
            assertion = (Assertion) o;
        }

        // extract SAML assertion the from WS-Security SOAP header
        if (assertion == null) {
            Header header = message.getHeader(new QName(WSSE_NS_URI, "Security"));
            if (! ((header != null) &&
                   headerDirection.equals(header.getDirection()) &&
                   (header.getObject() instanceof Element)))
            {
                return;
            }

            Element headerElem = (Element) header.getObject();
            NodeList nodeList = headerElem.getElementsByTagNameNS(SAMLConstants.SAML20_NS, "Assertion");
            Element assertionElem = (Element) nodeList.item(0);
            if (assertionElem == null) {
                return;
            }

            Unmarshaller unmarshaller = SAML_UNMARSHALLER_FACTORY.getUnmarshaller(assertionElem);
            try {
                assertion = (Assertion) unmarshaller.unmarshall(assertionElem);
            } catch (UnmarshallingException e) {
                LOG.warn("Cannot extract SAML assertion from the WS-Security SOAP header", e);
                return;
            }

            message.getExchange().put(XUA_SAML_ASSERTION, assertion);
        }

        // set ATNA XUA userName element
        String userName = ((assertion.getSubject() != null) && (assertion.getSubject().getNameID() != null))
                ? assertion.getSubject().getNameID().getValue() : null;

        String issuer = (assertion.getIssuer() != null)
                ? assertion.getIssuer().getValue() : null;

        if (StringUtils.isNotEmpty(issuer) && StringUtils.isNotEmpty(userName)) {
            auditDataset.setUserName(assertion.getSubject().getNameID().getSPProvidedID() + '<' + userName + '@' + issuer + '>');
        }

        // collect purposes of use
        for (AttributeStatement statement : assertion.getAttributeStatements()) {
            for (Attribute attribute : statement.getAttributes()) {
                if ("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse".equals(attribute.getName())) {
                    for (XMLObject value : attribute.getAttributeValues()) {
                        NodeList purposeElemList = value.getDOM().getElementsByTagNameNS("urn:hl7-org:v3", "PurposeOfUse");
                        for (int i = 0; i < purposeElemList.getLength(); ++i) {
                            Element purposeElem = (Element) purposeElemList.item(i);
                            CodedValueType cvt = new CodedValueType();
                            cvt.setCode(purposeElem.getAttribute("code"));
                            cvt.setCodeSystemName(purposeElem.getAttribute("codeSystem"));
                            cvt.setOriginalText(purposeElem.getAttribute("displayName"));
                            auditDataset.getPurposesOfUse().add(cvt);
                        }
                    }
                }
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
        List<?> list = message.getContent(List.class);
        return ((list == null) || list.isEmpty()) ? null : list.get(0);
    }
}
