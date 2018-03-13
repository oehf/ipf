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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42

import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.audit.types.CodedValueType
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest
import org.openehealth.ipf.commons.xml.XmlUtils
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.namespace.QName
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-42 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
class TestIti42 extends XdsStandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-42.xml'
    
    def SERVICE1 = "xds-iti42://localhost:${port}/xds-iti42-service1"
    def SERVICE2 = "xds-iti42://localhost:${port}/xds-iti42-service2"
    def SERVICE3 = "xds-iti42://localhost:${port}/xds-iti42-service3"

    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti42-service2"

    RegisterDocumentSet request
    DocumentEntry docEntry

    static Map<String, ?> camelHeaders

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }
    
    @BeforeClass
    static void classSetUp() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
        dbf.setNamespaceAware(true)
        DocumentBuilder db = dbf.newDocumentBuilder()
        Document doc = db.parse(TestIti42.class.classLoader.getResourceAsStream('saml2-assertion-for-xua.xml'))
        Element assertion = doc.documentElement
        SoapHeader header = new SoapHeader(new QName(assertion.namespaceURI, assertion.localName), assertion)
        //header.mustUnderstand = true
        camelHeaders = [(AbstractWsEndpoint.OUTGOING_SOAP_HEADERS) : [header]]

        // By default set by ServiceLoader when ipf-commons-ihe-xua is in classpath
        // AbstractAuditInterceptor.xuaProcessor = new BasicXuaProcessor()

        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    void setUp() {
        request = SampleData.createRegisterDocumentSet()
        docEntry = request.documentEntries[0]
    }
    
    @Test
    void testIti42() {
        request.documentEntries[0].extraMetadata = [
                'urn:oehf:docEntry1' : ['a', 'b', 'c'],
                'invalid' : ['d', 'e', 'f'],
                'urn:oehf:docEntry2' : ['g', 'h']]

        request.submissionSet.extraMetadata = [
                'urn:oehf:submission1' : ['1', '2', '3'],
                'wrong' : ['4', '5', '6'],
                'urn:oehf:submission2' : ['7', '8']]

        request.associations[1].extraMetadata = [
                'urn:oehf:association1' : ['A', 'B', 'C'],
                'improper' : ['D', 'E', 'F'],
                'urn:oehf:association2' : ['G', 'H']]

        request.folders[0].extraMetadata = [
                'urn:oehf:folder1' : ['i', 'ii', 'iii'],
                'bad' : ['iv', 'v', 'vi'],
                'urn:oehf:folder2' : ['vii', 'viii']]

        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        assert auditSender.messages.size() == 4
        
        checkAudit(EventOutcomeIndicator.Success)
    }
    
    @Test
    void testIti42FailureAudit() {
        assert FAILURE == sendIt(SERVICE2, 'falsch').status
        assert auditSender.messages.size() == 2
        
        checkAudit(EventOutcomeIndicator.SeriousFailure)
    }

    @Test
    void checkIti42AdditionalPropertiesSet(){
        XdsEndpoint endpoint = camelContext.getEndpoint(
            'xds-iti42:xds-iti42-service4?schemaLocations=#schemaLocations&properties=#props', XdsEndpoint.class)
        assert endpoint != null
        assert endpoint.schemaLocations.size() == 1
        assert endpoint.properties.size() == 2
        assert Boolean.valueOf(endpoint.properties['mtom-enabled'])
    }

    @Test
    void checkIti42ExtraMetadata() {
        // request with extra metadata
        String submissionSetString = readFile('submission-set.xml')
        JAXBContext jaxbContext = JAXBContext.newInstance(SubmitObjectsRequest.class)
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller()
        def requestWithMetadata = unmarshaller.unmarshal(XmlUtils.source(submissionSetString))
        def response = send(SERVICE3, requestWithMetadata, Response.class)
        assert response.status == SUCCESS

        // request without extra metadata
        response = send(SERVICE3, request, Response.class)
        assert response.status == FAILURE
    }


    void checkAudit(EventOutcomeIndicator outcome) {
        // check registry audit records
        AuditMessage message = getAudit(EventActionCode.Create, SERVICE2_ADDR)[0]

        assert message.activeParticipants.size() == 3
        assert message.participantObjectIdentifications.size() == 2
        
        checkEvent(message.eventIdentification, '110107', 'ITI-42', EventActionCode.Create, outcome)
        checkSource(message.activeParticipants[0], false)
        /* TODO fails with java 9
        checkHumanRequestor(message.activeParticipants[1], 'alias2<lipse@demo.com>', [
                CodedValueType.of('ELE', '1.2.3.4.5.6.777.1', 'Electrician'),
                CodedValueType.of('GYN', '1.2.3.4.5.6.777.2', 'Gynecologist'),
        ])
        */
        checkDestination(message.activeParticipants[2], SERVICE2_ADDR, false)
        checkAuditSource(message.auditSourceIdentification, 'sourceId')
        checkPatient(message.participantObjectIdentifications[0])
        checkSubmissionSet(message.participantObjectIdentifications[1])

        // check repository audit records
        message = getAudit(EventActionCode.Read, SERVICE2_ADDR)[0]

        assert message.activeParticipants.size() == 3
        assert message.participantObjectIdentifications.size() == 2
        
        checkEvent(message.eventIdentification, '110106', 'ITI-42', EventActionCode.Read, outcome)
        checkSource(message.activeParticipants[0], false)
        /* TODO fails with java 9
        checkHumanRequestor(message.activeParticipants[1], 'alias2<lipse@demo.com>', [
                CodedValueType.of('ELE', '1.2.3.4.5.6.777.1', 'Electrician'),
                CodedValueType.of('GYN', '1.2.3.4.5.6.777.2', 'Gynecologist'),
        ])
        */
        checkDestination(message.activeParticipants[2], SERVICE2_ADDR, false)
        checkAuditSource(message.auditSourceIdentification, 'sourceId')
        checkPatient(message.participantObjectIdentifications[0])
        checkSubmissionSet(message.participantObjectIdentifications[1])
    }
    
    def sendIt(endpoint, value) {
        docEntry.comments = new LocalizedString(value)
        return send(endpoint, request, Response.class, camelHeaders)
    }
}