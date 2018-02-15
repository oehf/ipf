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
package org.openehealth.ipf.platform.camel.ihe.xds.iti18

import org.apache.camel.RuntimeCamelException
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer

import static org.junit.Assert.fail
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-18 component with the webservice and the client defined within the URI.
 * @author Jens Riemschneider
 */
class TestIti18 extends XdsStandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-18.xml'
    
    def SERVICE1 = "xds-iti18://localhost:${port}/xds-iti18-service1"
    def SERVICE2 = "xds-iti18://localhost:${port}/xds-iti18-service2"
    def SAMPLE_SERVICE = "xds-iti18://localhost:${port}/myIti18Service?features=#loggingFeature"

    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti18-service2"
    
    QueryRegistry request
    FindDocumentsQuery query
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }
    
    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    void setUp() {
        request = SampleData.createFindDocumentsQuery()
        query = request.query

        QueryList<String> params1 = new QueryList<>()
        params1.outerList << ['a', 'b', 'c']
        params1.outerList << ['d', 'e', 'f']
        params1.outerList << ['h', 'i', 'j']

        QueryList<String> params2 = new QueryList<>()
        params2.outerList << ['12', '34']
        params2.outerList << ['56', '78', '90']

        query.extraParameters['$urn:oehf:test1'] = params1
        query.extraParameters['$urn:oehf:test2'] = params2
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
    void testIti18() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        assert auditSender.messages.size() == 4
        checkAudit(EventOutcomeIndicator.Success)
    }
    
    @Test
    void testIti18FailureAudit() {
        assert FAILURE == sendIt(SERVICE2, 'falsch').status
        assert auditSender.messages.size() == 2
        checkAudit(EventOutcomeIndicator.SeriousFailure)
    }
    
    def checkAudit(EventOutcomeIndicator outcome) {
        List<AuditMessage> messages = getAudit(EventActionCode.Execute, SERVICE2_ADDR)
        assert messages.size() == 2
        messages.each { message ->
            assert message.activeParticipants.size() == 2
            assert message.participantObjectIdentifications.size() == 2
            
            checkEvent(message.eventIdentification, '110112', 'ITI-18', EventActionCode.Execute, outcome)
            checkSource(message.activeParticipants[0], true)
            checkDestination(message.activeParticipants[1], SERVICE2_ADDR, false)
            checkPatient(message.participantObjectIdentifications[0])
            checkQuery(message.participantObjectIdentifications[1], 'ITI-18', 'urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d', 'urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d')
        }
    }
    
    @Test
    void testSample() {
        def response =
                send(SAMPLE_SERVICE, SampleData.createFindDocumentsQuery(), QueryResponse.class)
        assert SUCCESS == response.status
        assert 1 == response.references.size()
        assert 'document01' == response.references[0].id
        
        response = send(SAMPLE_SERVICE, SampleData.createGetDocumentsQuery(), QueryResponse.class)
        assert FAILURE == response.status
        
        assert auditSender.messages.size() == 4
        [2, 3].each { i ->
            boolean found = false
            AuditMessage message = auditSender.messages[i]
            for (detail in message.participantObjectIdentifications[0].participantObjectDetails) {
                if ((detail.type == 'urn:ihe:iti:xca:2010:homeCommunityId') && detail.value) {
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
