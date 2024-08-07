/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.audit

import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import org.apache.cxf.binding.soap.Soap11
import org.apache.cxf.binding.soap.Soap12
import org.apache.cxf.binding.soap.SoapMessage
import org.apache.cxf.headers.Header
import org.apache.cxf.staxutils.StaxUtils
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId
import org.openehealth.ipf.commons.audit.types.PurposeOfUse
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset.HumanUser
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import javax.xml.namespace.QName

/**
 * WS audit dataset enricher which fulfills IHE requirements on propagating XUA attributes to ATNA records.
 *
 * @author Dmytro Rud
 */
class XuaWsAuditDatasetEnricher implements WsAuditDatasetEnricher {

    /**
     * If a SAML assertion is stored under this key in the Web Service context,
     * IPF will use it instead of parsing the WS-Security header by itself.
     * If there are no Web Service context element under this key, or if this element
     * does not contain a SAML assertion, IPF will parse the WS-Security header
     * and store the assertion extracted from there (if any) under this key.
     */
    static final String XUA_SAML_ASSERTION = AbstractAuditInterceptor.class.getName() + '.XUA_SAML_ASSERTION'

    static final String WSSE_NS   = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
    static final String SAML20_NS = "urn:oasis:names:tc:SAML:2.0:assertion"

    static final String PURPOSE_OF_USE_ATTRIBUTE_NAME = 'urn:oasis:names:tc:xspa:1.0:subject:purposeofuse'
    static final String SUBJECT_NAME_ATTRIBUTE_NAME   = 'urn:oasis:names:tc:xspa:1.0:subject:subject-id'
    static final String SUBJECT_ROLE_ATTRIBUTE_NAME   = 'urn:oasis:names:tc:xacml:2.0:subject:role'
    static final String PATIENT_ID_ATTRIBUTE_NAME     = 'urn:oasis:names:tc:xacml:2.0:resource:resource-id'

    @Override
    void enrichAuditDatasetFromRequest(SoapMessage message, Header.Direction headerDirection, WsAuditDataset auditDataset) {
        GPathResult xuaToken = extractXuaToken(message, headerDirection)
        if (xuaToken != null) {
            extractXuaTokenElements(xuaToken, auditDataset)
        }
    }

    @Override
    void enrichAuditDatasetFromResponse(SoapMessage message, Header.Direction headerDirection, WsAuditDataset auditDataset) {
        // nop
    }

    protected static GPathResult extractXuaToken(SoapMessage message, Header.Direction headerDirection) {
        Element assertion = null

        // check whether someone has already parsed the SAML2 assertion
        def o = message.getContextualProperty(XUA_SAML_ASSERTION)
        if (o instanceof Element) {
            assertion = (Element) o
        }

        // extract SAML assertion the from WS-Security SOAP header
        if (assertion == null) {
            assertion = extractAssertionFromCxfMessage(message, headerDirection) ?: extractAssertionFromDom(message)
        }
        if (assertion == null) {
            return null
        }

        message.getExchange().put(XUA_SAML_ASSERTION, assertion)
        return new XmlSlurper(false, true).parseText(StaxUtils.toString(assertion))
    }

    private static Element extractAssertionFromCxfMessage(SoapMessage message, Header.Direction headerDirection) {
        Header header = message.getHeader(new QName(WSSE_NS, 'Security'))
        return (header && (headerDirection == header.getDirection()) && (header.getObject() instanceof Element)) ?
                extractAssertionFromHeader((Element) header.getObject()) :
                null
    }

    private static Element extractAssertionFromDom(SoapMessage message) {
        Document document = (Document) message.getContent(Node.class)
        if (document == null) {
            return null
        }
        NodeList nodeList = document.getDocumentElement().getElementsByTagNameNS(Soap12.SOAP_NAMESPACE, 'Header')
        if (nodeList.getLength() == 0) {
            nodeList = document.getDocumentElement().getElementsByTagNameNS(Soap11.SOAP_NAMESPACE, 'Header')
        }
        if (nodeList.getLength() == 0) {
            return null
        }
        Element element = (Element) nodeList.item(0)
        nodeList = element.getElementsByTagNameNS(WSSE_NS, 'Security')
        return (nodeList.getLength() > 0) ? extractAssertionFromHeader((Element) nodeList.item(0)) : null
    }

    private static Element extractAssertionFromHeader(Element headerElement) {
        NodeList nodeList = headerElement.getElementsByTagNameNS(SAML20_NS, 'Assertion')
        return (nodeList.getLength() > 0) ? (Element) nodeList.item(0) : null
    }

    protected static void extractXuaTokenElements(GPathResult xuaToken, WsAuditDataset auditDataset) {
        def purposesOfUse = []
        for (attr in xuaToken.AttributeStatement.Attribute) {
            switch (attr.@Name) {
                case PURPOSE_OF_USE_ATTRIBUTE_NAME:
                    for (pou in attr.AttributeValue.PurposeOfUse) {
                        purposesOfUse << PurposeOfUse.of(pou.@code.text(), pou.@codeSystem.text(), pou.@displayName.text())
                    }
                    break
                case PATIENT_ID_ATTRIBUTE_NAME:
                    auditDataset.xuaPatientId = attr.AttributeValue[0].text()
                    break
            }
        }
        auditDataset.purposesOfUse = purposesOfUse

        conditionallyAddHumanUser(createIheUser(xuaToken), auditDataset)
    }

    private static HumanUser createIheUser(GPathResult gpath) {
        def user = new HumanUser()
        String userName     = gpath.Subject.NameID[0].text()
        String spProvidedId = gpath.Subject.NameID[0].@SPProvidedID.text()
        String issuerName   = gpath.Issuer[0].text()
        if (issuerName && userName) {
            String s = "${spProvidedId}<${userName}@${issuerName}>"
            user.id = s
            user.name = s
        }
        for (role in gpath.AttributeStatement.Attribute.findAll { it.@Name == SUBJECT_ROLE_ATTRIBUTE_NAME }.AttributeValue.Role) {
            user.roles << ActiveParticipantRoleId.of(role.@code.text(), role.@codeSystem.text(), role.@displayName.text())
        }
        return user
    }

    protected static void conditionallyAddHumanUser(HumanUser humanUser, WsAuditDataset auditDataset) {
        if (!humanUser.empty) {
            auditDataset.humanUsers << humanUser
        }
    }

}
