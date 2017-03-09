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
package org.openehealth.ipf.platform.camel.ihe.xds.itiY1

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocuments
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.PARTIAL_SUCCESS
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-Y1 transaction with a webservice and client adapter defined via URIs.
 * @author Dmytro Rud
 */
class TestItiY1 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-Y1.xml'
    
    def SERVICE2 = "xds-itiY1://localhost:${port}/xds-itiY1-service2"
    def SERVICE2_ADDR = "http://localhost:${port}/xds-itiY1-service2"

    RemoveDocuments request

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void classSetUp() throws Exception {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    void setUp() {
        request = SampleData.createRemoveDocuments()
    }

    @Test
    void testItiY1Success() {
        def response = sendIt(SERVICE2, 'success')
        assert SUCCESS == response.status
        assert auditSender.messages.size() == 2
        checkAudit(['success', '2.1.2', '2.1.3'], null)
    }

    @Test
    void testItiY1Failure() {
        def response = sendIt(SERVICE2, 'failure')
        assert FAILURE == response.status
        assert auditSender.messages.size() == 2
        checkAudit(null, ['failure', '2.1.2', '2.1.3'])
    }

    @Test
    void testItiY1Partial() {
        def response = sendIt(SERVICE2, 'partial')
        assert PARTIAL_SUCCESS == response.status
        assert auditSender.messages.size() == 4
        checkAudit(['partial', '2.1.2'], ['2.1.3'])
    }

    def checkAudit(List<String> successfulDocuments, List<String> failedDocuments) {
        def messages = auditSender.messages.collect { getMessage(it) }

        messages.each {
            assert it.AuditSourceIdentification.size() == 1
            assert it.EventIdentification.size() == 1
            assert it.ActiveParticipant.size() == 2
            assert it.EventIdentification.@EventActionCode == 'D'
            assert it.ActiveParticipant[1].@UserID.text() == SERVICE2_ADDR
            checkCode(it.EventIdentification.EventTypeCode, 'ITI-Y1', 'IHE Transactions')
        }

        checkDocumentUids(messages, successfulDocuments, '0')
        checkDocumentUids(messages, failedDocuments, '8')
    }

    private void checkDocumentUids(List allMessages, List<String> documentUids, String outcomeCode) {
        if (documentUids) {
            def messages = allMessages.findAll { it.EventIdentification.@EventOutcomeIndicator.text() == outcomeCode }
            assert messages.size() == 2

            messages.each { message ->
                assert message.ParticipantObjectIdentification.size() == documentUids.size()
                documentUids.eachWithIndex { documentUid, i ->
                    assert message.ParticipantObjectIdentification[i].@ParticipantObjectID.text() == documentUid
                }
            }
        }
    }

    def sendIt(endpoint, firstDocumentUid) {
        request.documents[0].documentUniqueId = firstDocumentUid
        return send(endpoint, request, Response.class)
    }
}
