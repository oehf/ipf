/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xua;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AbstractAuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.XuaProcessor;
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
import org.opensaml.soap.wssecurity.WSSecurityConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.*;

import javax.xml.namespace.QName;
import java.util.*;

/**
 * @see <a href="http://docs.oasis-open.org/xacml/xspa/v1.0/xacml-xspa-1.0-os.html">Cross-Enterprise Security
 * and Privacy Authorization (XSPA) Profile of XACML v2.0 for Healthcare Version 1.0</a>
 *
 * @author Dmytro Rud
 */
@Slf4j
public class BasicXuaProcessor implements XuaProcessor {

    /**
     * If a SAML assertion is stored under this key in the Web Service context,
     * IPF will use it instead of parsing the WS-Security header by itself.
     * If there are no Web Service context element under this key, or if this element
     * does not contain a SAML assertion, IPF will parse the WS-Security header
     * and store the assertion extracted from there (if any) under this key.
     */
    public static final String XUA_SAML_ASSERTION = AbstractAuditInterceptor.class.getName() + ".XUA_SAML_ASSERTION";

    public static final Set<String> WSSE_NS_URIS = new HashSet<>(Arrays.asList(
            WSSecurityConstants.WSSE_NS,
            WSSecurityConstants.WSSE11_NS));

    public static final String PURPOSE_OF_USE_ATTRIBUTE_NAME = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String SUBJECT_ROLE_ATTRIBUTE_NAME   = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String PATIENT_ID_ATTRIBUTE_NAME     = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";

    public static final QName PURPOSE_OF_USE_ELEMENT_NAME = new QName("urn:hl7-org:v3", "PurposeOfUse");
    public static final QName SUBJECT_ROLE_ELEMENT_NAME   = new QName("urn:hl7-org:v3", "Role");

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

    private static Element extractAssertionElementFromCxfMessage(SoapMessage message, Header.Direction headerDirection) {
        Header header = message.getHeader(new QName(WSSecurityConstants.WSSE_NS, "Security"));
        if (!((header != null) &&
                headerDirection.equals(header.getDirection()) &&
                (header.getObject() instanceof Element)))
        {
            return null;
        }

        Element headerElem = (Element) header.getObject();
        NodeList nodeList = headerElem.getElementsByTagNameNS(SAMLConstants.SAML20_NS, "Assertion");
        return (Element) nodeList.item(0);
    }

    private static Element extractAssertionElementFromDom(SoapMessage message) {
        Document document = (Document) message.getContent(Node.class);
        if (document == null) {
            return null;
        }
        Element element = getElementNS(document.getDocumentElement(), SOAP_NS_URIS, "Header");
        element = getElementNS(element, WSSE_NS_URIS, "Security");
        return getElementNS(element, Collections.singleton(SAMLConstants.SAML20_NS), "Assertion");
    }

    /**
     * Extracts ITI-40 XUA user name from the SAML2 assertion contained
     * in the given CXF message, and stores it in the ATNA audit dataset.
     *
     * @param message         source CXF message.
     * @param headerDirection direction of the header containing the SAML2 assertion.
     * @param auditDataset    target ATNA audit dataset.
     */
    public void extractXuaUserNameFromSaml2Assertion(
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
            Element assertionElem = extractAssertionElementFromCxfMessage(message, headerDirection);
            if (assertionElem == null) {
                assertionElem = extractAssertionElementFromDom(message);
            }
            if (assertionElem == null) {
                return;
            }

            Unmarshaller unmarshaller = SAML_UNMARSHALLER_FACTORY.getUnmarshaller(assertionElem);
            try {
                assertion = (Assertion) unmarshaller.unmarshall(assertionElem);
            } catch (UnmarshallingException e) {
                log.warn("Cannot extract SAML assertion from the WS-Security SOAP header", e);
                return;
            }

            message.getExchange().put(XUA_SAML_ASSERTION, assertion);
        }

        // set ATNA XUA userName element
        String userName = ((assertion.getSubject() != null) && (assertion.getSubject().getNameID() != null))
                ? assertion.getSubject().getNameID().getValue() : null;

        String issuer = (assertion.getIssuer() != null)
                ? assertion.getIssuer().getValue() : null;

        if (StringUtils.isNoneEmpty(issuer, userName)) {
            String spProvidedId = StringUtils.stripToEmpty(assertion.getSubject().getNameID().getSPProvidedID());
            auditDataset.setUserName(spProvidedId + '<' + userName + '@' + issuer + '>');
        }

        // collect purposes of use, user role codes, and the patient ID
        for (AttributeStatement statement : assertion.getAttributeStatements()) {
            for (Attribute attribute : statement.getAttributes()) {
                if (PURPOSE_OF_USE_ATTRIBUTE_NAME.equals(attribute.getName())) {
                    extractCodes(attribute, PURPOSE_OF_USE_ELEMENT_NAME, auditDataset.getPurposesOfUse());
                } else if (SUBJECT_ROLE_ATTRIBUTE_NAME.equals(attribute.getName())) {
                    extractCodes(attribute, SUBJECT_ROLE_ELEMENT_NAME, auditDataset.getUserRoles());
                } else if (PATIENT_ID_ATTRIBUTE_NAME.equals(attribute.getName())) {
                    List<XMLObject> attributeValues = attribute.getAttributeValues();
                    if ((attributeValues != null)
                            && (!attributeValues.isEmpty())
                            && (attributeValues.get(0) != null)
                            && (attributeValues.get(0).getDOM() != null))
                    {
                        auditDataset.setXuaPatientId(attributeValues.get(0).getDOM().getTextContent());
                    }
                }
            }
        }
    }

    private static void extractCodes(Attribute attribute, QName valueElementName, List<CodedValueType> targetCollection) {
        for (XMLObject value : attribute.getAttributeValues()) {
            if (value.getDOM() != null) {
                NodeList nodeList = value.getDOM().getElementsByTagNameNS(valueElementName.getNamespaceURI(), valueElementName.getLocalPart());
                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Element elem = (Element) nodeList.item(i);
                    targetCollection.add(elementToCode(elem));
                }
            }
        }
    }

    private static CodedValueType elementToCode(Element element) {
        CodedValueType cvt = new CodedValueType();
        cvt.setCode(element.getAttribute("code"));
        cvt.setCodeSystemName(element.getAttribute("codeSystemName"));
        cvt.setOriginalText(element.getAttribute("displayName"));
        return cvt;
    }
}
