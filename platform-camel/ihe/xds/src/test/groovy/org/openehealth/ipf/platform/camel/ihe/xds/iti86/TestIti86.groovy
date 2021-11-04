/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.iti86

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocuments
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*

/**
 * Tests the ITI-86 transaction with a webservice and client adapter defined via URIs.
 * @author Dmytro Rud
 */
class TestIti86 extends XdsStandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-86.xml'
    
    def SERVICE2 = "rmd-iti86://localhost:${port}/rmd-iti86-service2?inInterceptors=#inLogger&outInterceptors=#outLogger"
    def SERVICE2_ADDR = "http://localhost:${port}/rmd-iti86-service2"

    RemoveDocuments request

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }
    
    @BeforeAll
    static void classSetUp() throws Exception {
        System.setProperty(PayloadLoggerBase.PROPERTY_CONSOLE, 'true')
        System.setProperty(PayloadLoggerBase.PROPERTY_DISABLED, 'true')
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @BeforeEach
    void setUp() {
        request = SampleData.createRemoveDocuments()
    }

    @Test
    void testIti86Success() {
        def response = sendIt(SERVICE2, 'success')
        assert SUCCESS == response.status
        assert auditSender.messages.size() == 2
        checkAudit(['success', '2.1.2', '2.1.3'], null)
    }

    @Test
    void testIti86Failure() {
        def response = sendIt(SERVICE2, 'failure')
        assert FAILURE == response.status
        assert auditSender.messages.size() == 2
        checkAudit(null, ['failure', '2.1.2', '2.1.3'])
    }

    @Test
    void testIti86Partial() {
        def response = sendIt(SERVICE2, 'partial')
        assert PARTIAL_SUCCESS == response.status
        assert auditSender.messages.size() == 4
        checkAudit(['partial', '2.1.2'], ['2.1.3'])
    }

    void checkAudit(List<String> successfulDocuments, List<String> failedDocuments) {
        auditSender.messages.each { AuditMessage message ->
            assert message.activeParticipants.size() == 2
            assert message.eventIdentification.eventActionCode == EventActionCode.Delete
            assert message.activeParticipants[1].userID == SERVICE2_ADDR
            checkCode(message.eventIdentification.eventTypeCode[0], 'ITI-86', 'IHE Transactions')
        }

        checkDocumentUids(auditSender.messages, successfulDocuments, EventOutcomeIndicator.Success)
        checkDocumentUids(auditSender.messages, failedDocuments, EventOutcomeIndicator.SeriousFailure)
    }

    private void checkDocumentUids(List<AuditMessage> allMessages, List<String> documentUids, EventOutcomeIndicator outcomeCode) {
        if (documentUids) {
            def messages = allMessages.findAll { it.eventIdentification.eventOutcomeIndicator == outcomeCode }
            assert messages.size() == 2

            messages.each { message ->
                assert message.participantObjectIdentifications.size() == documentUids.size()
                documentUids.eachWithIndex { documentUid, i ->
                    assert message.participantObjectIdentifications[i].participantObjectID == documentUid
                }
            }
        }
    }

    def sendIt(endpoint, String firstDocumentUid) {
        request.documents[0].setDocumentUniqueId(firstDocumentUid)
        return send(endpoint, request, Response.class)
    }
}
