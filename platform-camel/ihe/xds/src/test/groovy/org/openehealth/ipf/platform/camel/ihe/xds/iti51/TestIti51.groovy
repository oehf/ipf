/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti51

import org.apache.camel.RuntimeCamelException
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import static org.junit.Assert.fail
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType

/**
 * Tests the ITI-51 component with the Web Service and the client defined within the URI.
 * @author Jens Riemschneider
 * @author Michael Ottati
 */
class TestIti51 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-51.xml'
    
    def SERVICE1 = "xds-iti51://localhost:${port}/xds-iti51-service1"
    def SERVICE2 = "xds-iti51://localhost:${port}/xds-iti51-service2"
    def SAMPLE_SERVICE = "xds-iti51://localhost:${port}/myIti51Service"
    
    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti51-service2"
    
    def request
    def query
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    void setUp() {
        request = SampleData.createFindDocumentsForMultiplePatientsQuery()
        query = request.query
    }
    
    @Test
    void testComponentCanBeRestarted() {
        camelContext.stopRoute('service1route')
        try {
            sendIt(SERVICE1, 'service 1').status
            fail('Expected exception: ' + RuntimeCamelException.class)
        }
        catch (Exception ignored) {
        }
        
        camelContext.startRoute('service1route')
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
    }
    
    @Test
    void testIti51() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        assert auditSender.messages.size() == 8
        checkAudit('0')
    }
    
    @Test
    void testIti51FailureAudit() {
        assert FAILURE == sendIt(SERVICE2, 'falsch').status
        assert auditSender.messages.size() == 4
        checkAudit('8')
    }
    
    def checkAudit(outcome) {
        def messages = getAudit('E', SERVICE2_ADDR)
        assert messages.size() == 4
        messages.each { message ->
            assert message.AuditSourceIdentification.size() == 1
            assert message.ActiveParticipant.size() == 2
            assert message.ParticipantObjectIdentification.size() == 2
            assert message.children().size() == 6
            checkEvent(message.EventIdentification, '110112', 'ITI-51', 'E', outcome)
            checkSource(message.ActiveParticipant[0], 'true')
            checkDestination(message.ActiveParticipant[1], SERVICE2_ADDR, 'false')
            checkPatient(message.ParticipantObjectIdentification[0], 'id3^^^&1.3&ISO', 'id4^^^&1.4&ISO')
            checkQuery(message.ParticipantObjectIdentification[1], 'ITI-51',
                    QueryType.FIND_DOCUMENTS_MPQ.getId(),
                    QueryType.FIND_DOCUMENTS_MPQ.getId())
        }
    }
    
    @Test
    void testSample() {
        def response =
                send(SAMPLE_SERVICE, SampleData.createFindDocumentsForMultiplePatientsQuery(), QueryResponse.class)
        assert SUCCESS == response.status
        assert 1 == response.references.size()
        assert 'document01' == response.references[0].id
        
        response = send(SAMPLE_SERVICE, SampleData.createGetDocumentsQuery(), QueryResponse.class)
        assert FAILURE == response.status
        
        assert auditSender.messages.size() == 6
        [2, 3].each { i ->
            boolean found = false
            def message = new XmlSlurper().parseText(auditSender.messages[i].auditMessage.toString())
            for (detail in message.ParticipantObjectIdentification.ParticipantObjectDetail) {
                if ((detail.@type.text() == 'ihe:homeCommunityID') && detail.@value.text()) {
                    found = true
                }
            }
            assert found
        }
    }
    
    def sendIt(endpoint, value) {
        query.authorPersons = [value]
        send(endpoint, request, QueryResponse.class)
    }
}
