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
package org.openehealth.ipf.platform.camel.ihe.xds.iti41

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import org.apache.camel.impl.DefaultExchange
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.xml.XmlUtils
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.openehealth.ipf.platform.camel.ihe.xds.MyRejectionHandlingStrategy
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-41 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
class TestIti41 extends StandardTestContainer {

    def static CONTEXT_DESCRIPTOR = 'iti-41.xml'

    def SERVICE1 = "xds-iti41://localhost:${port}/xds-iti41-service1"
    def SERVICE2 = "xds-iti41://localhost:${port}/xds-iti41-service2"
    def SERVICE3 = "xds-iti41://localhost:${port}/xds-iti41-service3"
    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti41-service2"

    def request
    def docEntry

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }

    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Before
    void setUp() {
        request = SampleData.createProvideAndRegisterDocumentSet()
        docEntry = request.documents[0].documentEntry
    }

    @Test
    void testIti41() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        assert auditSender.messages.size() == 4
        checkAudit('0')
    }

    @Test
    void testIti41FailureAudit() {
        assert FAILURE == sendIt(SERVICE2, 'falsch').status
        assert auditSender.messages.size() == 2
        checkAudit('8')
    }

    @Test
    void testIti41ExtraMetadata() {
        // request with extra metadata
        String submissionSetString = readFile('submission-set.xml')
        String pnrRequestString = """
            <urn:ProvideAndRegisterDocumentSetRequest xmlns:urn="urn:ihe:iti:xds-b:2007">
                ${submissionSetString}
                <urn:Document id="Document01">SGVsbG8gTWFyZWshIDotKQ0K</urn:Document>
            </urn:ProvideAndRegisterDocumentSetRequest>
        """
        JAXBContext jaxbContext = JAXBContext.newInstance(ProvideAndRegisterDocumentSetRequestType.class)
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller()
        def requestWitmMetadata = unmarshaller.unmarshal(XmlUtils.source(pnrRequestString))
        def response = send(SERVICE3, requestWitmMetadata, Response.class)
        assert response.status == SUCCESS

        // request without extra metadata
        response = send(SERVICE3, request, Response.class)
        assert response.status == FAILURE
    }


    /**
     * Send some garbage to an XDS endpoint (via raw HTTP, because we want to test
     * consumer behaviour and therefore should avoid checks on the producer side),
     * and check whether the rejection handling strategy works well.
     */
    @Test
    void testRejectionHandling() {
        def exchange = new DefaultExchange(camelContext)
        exchange.in.body = '< some ill-formed XML !'
        producerTemplate.send("http://localhost:${port}/xds-iti41-service1", exchange)
        assert MyRejectionHandlingStrategy.count == 1
    }

    void checkAudit(outcome) {
        def message = getAudit('C', SERVICE2_ADDR)[0]
        assert message.AuditSourceIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.ParticipantObjectIdentification.size() == 2
        assert message.children().size() == 6
        
        checkEvent(message.EventIdentification, '110107', 'ITI-41', 'C', outcome)
        checkSource(message.ActiveParticipant[0], 'true')
        checkDestination(message.ActiveParticipant[1], SERVICE2_ADDR, 'false')
        checkAuditSource(message.AuditSourceIdentification, 'repositoryId')
        checkPatient(message.ParticipantObjectIdentification[0])
        checkSubmissionSet(message.ParticipantObjectIdentification[1])
        
        message = getAudit('R', SERVICE2_ADDR)[0]
        assert message.AuditSourceIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.ParticipantObjectIdentification.size() == 2
        assert message.children().size() == 6
        
        checkEvent(message.EventIdentification, '110106', 'ITI-41', 'R', outcome)
        checkSource(message.ActiveParticipant[0], 'true')
        checkDestination(message.ActiveParticipant[1], SERVICE2_ADDR, 'false')
        checkPatient(message.ParticipantObjectIdentification[0])
        checkSubmissionSet(message.ParticipantObjectIdentification[1])
    }

    def sendIt(String endpoint, String value) {
        docEntry.comments = new LocalizedString(value)
        send(endpoint, request, Response.class)
    }
}
