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
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AbstractAuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.XuaProcessor;
import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistry;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.core.*;
import org.opensaml.soap.wssecurity.WSSecurityConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import java.util.*;

import static org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId.of;
import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.SOAP_NS_URIS;
import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.getElementNS;

/**
 * @author Dmytro Rud
 * @see <a href="http://docs.oasis-open.org/xacml/xspa/v1.0/xacml-xspa-1.0-os.html">Cross-Enterprise Security
 * and Privacy Authorization (XSPA) Profile of XACML v2.0 for Healthcare Version 1.0</a>
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
    private static final String XUA_SAML_ASSERTION = AbstractAuditInterceptor.class.getName() + ".XUA_SAML_ASSERTION";

    private static final Set<String> WSSE_NS_URIS = new HashSet<>(Arrays.asList(
            WSSecurityConstants.WSSE_NS,
            WSSecurityConstants.WSSE11_NS));

    private static final String PURPOSE_OF_USE_ATTRIBUTE_NAME = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    private static final String SUBJECT_NAME_ATTRIBUTE_NAME   = "urn:oasis:names:tc:xspa:1.0:subject:subject-id";
    private static final String SUBJECT_ROLE_ATTRIBUTE_NAME   = "urn:oasis:names:tc:xacml:2.0:subject:role";
    private static final String PATIENT_ID_ATTRIBUTE_NAME     = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";

    private static final QName PURPOSE_OF_USE_ELEMENT_NAME = new QName("urn:hl7-org:v3", "PurposeOfUse");
    private static final QName SUBJECT_ROLE_ELEMENT_NAME   = new QName("urn:hl7-org:v3", "Role");

    /** Map from principal role to assistant role */
    public static final Map<ActiveParticipantRoleId, ActiveParticipantRoleId> PRINCIPAL_ASSISTANT_ROLE_RELATIONSHIPS;
    static {
        PRINCIPAL_ASSISTANT_ROLE_RELATIONSHIPS = new HashMap<>();
        // old (obsolete) and new coding system IDs
        for (String codingSystemId : new String[]{"2.16.756.5.30.1.127.3.10.4", "2.16.756.5.30.1.127.3.10.6"}) {
            PRINCIPAL_ASSISTANT_ROLE_RELATIONSHIPS.put(of("PAT", codingSystemId, ""), of("REP",       codingSystemId, "Representative"));
            PRINCIPAL_ASSISTANT_ROLE_RELATIONSHIPS.put(of("HCP", codingSystemId, ""), of("ASSISTANT", codingSystemId, "Assistant"));
        }
    }

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
                (header.getObject() instanceof Element))) {
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

        WsAuditDataset.HumanUser mainUser = new WsAuditDataset.HumanUser();
        WsAuditDataset.HumanUser assistantUser = new WsAuditDataset.HumanUser();

        // extract information about the main user and the optional assistant
        if (assertion.getSubject() != null) {
            mainUser.setId(createXuaUserId(assertion.getIssuer(), assertion.getSubject().getNameID()));

            // process information about the assistant (if any)
            for (SubjectConfirmation subjectConfirmation : assertion.getSubject().getSubjectConfirmations()) {
                if ((subjectConfirmation.getNameID() != null) && (subjectConfirmation.getSubjectConfirmationData() != null)) {
                    assistantUser.setId(createXuaUserId(assertion.getIssuer(), subjectConfirmation.getNameID()));
                    AttributeStatement statement = new AttributeStatementExtractor().extractAttributeStatement(subjectConfirmation);
                    statement.getAttributes().stream()
                            .filter(attr -> SUBJECT_NAME_ATTRIBUTE_NAME.equals(attr.getName()))
                            .findAny()
                            .ifPresent(attr -> assistantUser.setName(extractSingleStringAttributeValue(attr)));
                }
            }
        }

        // collect purposes of use, user role codes, and the patient ID
        for (AttributeStatement statement : assertion.getAttributeStatements()) {
            for (Attribute attribute : statement.getAttributes()) {
                switch (attribute.getName()) {
                    case PURPOSE_OF_USE_ATTRIBUTE_NAME:
                        auditDataset.setPurposesOfUse(extractPurposeOfUse(attribute, PURPOSE_OF_USE_ELEMENT_NAME));
                        break;
                    case SUBJECT_NAME_ATTRIBUTE_NAME:
                        mainUser.setName(extractSingleStringAttributeValue(attribute));
                        break;
                    case SUBJECT_ROLE_ATTRIBUTE_NAME:
                        extractActiveParticipantRoleId(attribute, SUBJECT_ROLE_ELEMENT_NAME).forEach(mainUserRoleId -> {
                            mainUser.getRoles().add(mainUserRoleId);
                            ActiveParticipantRoleId normalizedMainUserRoleId = of(mainUserRoleId.getCode(), mainUserRoleId.getCodeSystemName(), "");
                            ActiveParticipantRoleId assistantUserRoleId = PRINCIPAL_ASSISTANT_ROLE_RELATIONSHIPS.get(normalizedMainUserRoleId);
                            if (assistantUserRoleId != null) {
                                assistantUser.getRoles().add(assistantUserRoleId);
                            }
                        });
                        break;
                    case PATIENT_ID_ATTRIBUTE_NAME:
                        auditDataset.setXuaPatientId(extractSingleStringAttributeValue(attribute));
                        break;
                }
            }
        }

        if (!mainUser.isEmpty()) {
            auditDataset.getHumanUsers().add(mainUser);
        }
        if (!assistantUser.isEmpty()) {
            auditDataset.getHumanUsers().add(assistantUser);
        }
    }

    private static String createXuaUserId(Issuer issuer, NameID nameID) {
        String userName     = (nameID != null) ? nameID.getValue() : null;
        String issuerName   = (issuer != null) ? issuer.getValue() : null;
        String spProvidedId = (nameID != null) ? StringUtils.stripToEmpty(nameID.getSPProvidedID()) : null;

        return StringUtils.isNoneEmpty(issuerName, userName)
                ? spProvidedId + '<' + userName + '@' + issuerName + '>'
                : null;
    }

    private static String extractSingleStringAttributeValue(Attribute attribute) {
        List<XMLObject> attributeValues = attribute.getAttributeValues();
        return ((attributeValues != null) && (!attributeValues.isEmpty()) && (attributeValues.get(0) != null) && (attributeValues.get(0).getDOM() != null))
                ? attributeValues.get(0).getDOM().getTextContent()
                : null;
    }

    private static PurposeOfUse[] extractPurposeOfUse(Attribute attribute, QName valueElementName) {
        List<PurposeOfUse> targetCollection = new ArrayList<>();
        for (XMLObject value : attribute.getAttributeValues()) {
            if (value.getDOM() != null) {
                NodeList nodeList = value.getDOM().getElementsByTagNameNS(valueElementName.getNamespaceURI(), valueElementName.getLocalPart());
                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Element elem = (Element) nodeList.item(i);
                    targetCollection.add(elementToPurposeOfUse(elem));
                }
            }
        }
        return targetCollection.toArray(new PurposeOfUse[targetCollection.size()]);

    }

    private static PurposeOfUse elementToPurposeOfUse(Element element) {
        return PurposeOfUse.of(
                element.getAttribute("code"),
                element.getAttribute("codeSystem"),
                element.getAttribute("displayName")
        );
    }

    private static List<ActiveParticipantRoleId> extractActiveParticipantRoleId(Attribute attribute, QName valueElementName) {
        List<ActiveParticipantRoleId> result = new ArrayList<>();
        for (XMLObject value : attribute.getAttributeValues()) {
            if (value.getDOM() != null) {
                NodeList nodeList = value.getDOM().getElementsByTagNameNS(valueElementName.getNamespaceURI(), valueElementName.getLocalPart());
                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Element elem = (Element) nodeList.item(i);
                    result.add(elementToActiveParticipantRoleId(elem));
                }
            }
        }
        return result;
    }

    private static ActiveParticipantRoleId elementToActiveParticipantRoleId(Element element) {
        return ActiveParticipantRoleId.of(
                element.getAttribute("code"),
                element.getAttribute("codeSystem"),
                element.getAttribute("displayName")
        );
    }
}
