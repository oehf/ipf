/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")"
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti43

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-43 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
class TestIti43 extends XdsStandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-43.xml'
    
    def SERVICE1 = "xds-iti43://localhost:${port}/xds-iti43-service1"
    def SERVICE2 = "xds-iti43://localhost:${port}/xds-iti43-service2"
    
    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti43-service2"
    
    def request
    def doc
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }
    
    @BeforeAll
    static void classSetUp() throws Exception {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @BeforeEach
    void setUp() {
        request = SampleData.createRetrieveDocumentSet()
        doc = request.documents[0]
    }
    
    @Test
    void testIti43() {
        def response1 = sendIt(SERVICE1, 'service 1')
        assert SUCCESS == response1.status
        checkForMTOM(response1)
        
        def response2 = sendIt(SERVICE2, 'service 2')
        assert SUCCESS == response2.status
        checkForMTOM(response2)
        assert auditSender.messages.size() == 4
        
        checkAudit(EventOutcomeIndicator.Success, 'service 2')
    }
    
    @Test
    void testIti43FailureAudit() {
        def response2 = sendIt(SERVICE2, 'falsch')
        assert FAILURE == response2.status
        assert auditSender.messages.size() == 2
        
        checkAudit(EventOutcomeIndicator.SeriousFailure, 'falsch')
    }
    
    def checkAudit(EventOutcomeIndicator outcome, String docIdValue) {
        AuditMessage message = getAudit(EventActionCode.Read, SERVICE2_ADDR)[0]

        assert message.activeParticipants.size() == 2
        assert message.participantObjectIdentifications.size() == 2
        
        checkEvent(message.eventIdentification, '110106', 'ITI-43', EventActionCode.Read, outcome)
        checkSource(message.activeParticipants[0], SERVICE2_ADDR, false)
        checkDestination(message.activeParticipants[1], false, false)
        checkAuditSource(message.auditSourceIdentification, 'sourceId')
        checkDocument(message.participantObjectIdentifications[0], docIdValue, 'urn:oid:1.2.3', 'repo1')
        checkDocument(message.participantObjectIdentifications[1], 'doc2', 'urn:oid:1.2.4', 'repo2')
        
        message = getAudit(EventActionCode.Create, SERVICE2_ADDR)[0]

        assert message.activeParticipants.size() == 2
        assert message.participantObjectIdentifications.size() == 2
        
        checkEvent(message.eventIdentification, '110107', 'ITI-43', EventActionCode.Create, outcome)
        checkSource(message.activeParticipants[0], SERVICE2_ADDR, false)
        checkDestination(message.activeParticipants[1], false, false)
        checkDocument(message.participantObjectIdentifications[0], docIdValue, 'urn:oid:1.2.3', 'repo1')
        checkDocument(message.participantObjectIdentifications[1], 'doc2', 'urn:oid:1.2.4', 'repo2')
    }
    
    void checkForMTOM(response) {
        def attachments = response.documents[0].dataHandler.dataSource.attachments
        assert attachments.size() == 2
        assert attachments.iterator().next().xop
    }
    
    def sendIt(endpoint, value) {
        doc.documentUniqueId = value
        send(endpoint, request, RetrievedDocumentSet.class)
    }
}
