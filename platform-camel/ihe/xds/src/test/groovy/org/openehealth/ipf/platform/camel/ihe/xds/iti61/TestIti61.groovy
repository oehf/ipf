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
package org.openehealth.ipf.platform.camel.ihe.xds.iti61

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-61 transaction with a webservice and client adapter defined via URIs.
 * @author Dmytro Rud
 */
class TestIti61 extends XdsStandardTestContainer {

    def static CONTEXT_DESCRIPTOR = 'iti-61.xml'

    def SERVICE1 = "xds-iti61://localhost:${port}/xds-iti61-service1"
    def SERVICE2 = "xds-iti61://localhost:${port}/xds-iti61-service2"
    def SERVICE3 = "xds-iti61://localhost:${port}/xds-iti61-service3"

    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti61-service2"

    def request
    def docEntry

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @BeforeEach
    void setUp() {
        request = SampleData.createRegisterDocumentSet()
        request.documentEntries.each { entry ->
            // validation should fail when one of the following lines is deactivated
            entry.type = DocumentEntryType.ON_DEMAND
            entry.setCreationTime((Timestamp) null)
            entry.hash = null
            entry.size = null
            entry.legalAuthenticator = null
        }
        docEntry = request.documentEntries[0]
    }

    @Test
    void testIti61() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        assert auditSender.messages.size() == 4

        checkAudit(EventOutcomeIndicator.Success)
    }

    @Test
    void testIti61FailureAudit() {
        assert FAILURE == sendIt(SERVICE2, 'falsch').status
        assert auditSender.messages.size() == 2

        checkAudit(EventOutcomeIndicator.SeriousFailure)
    }

    void checkAudit(outcome) {
        AuditMessage message = getAudit(EventActionCode.Create, SERVICE2_ADDR)[0]

        assert message.activeParticipants.size() == 2
        assert message.participantObjectIdentifications.size() == 2

        checkEvent(message.eventIdentification, '110107', 'ITI-61', EventActionCode.Create, outcome)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], SERVICE2_ADDR, false)
        checkAuditSource(message.auditSourceIdentification, 'sourceId')
        checkPatient(message.participantObjectIdentifications[0])
        checkSubmissionSet(message.participantObjectIdentifications[1])

        message = getAudit(EventActionCode.Read, SERVICE2_ADDR)[0]

        assert message.activeParticipants.size() == 2
        assert message.participantObjectIdentifications.size() == 2

        checkEvent(message.eventIdentification, '110106', 'ITI-61', EventActionCode.Read, outcome)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], SERVICE2_ADDR, false)
        checkAuditSource(message.auditSourceIdentification, 'sourceId')
        checkPatient(message.participantObjectIdentifications[0])
        checkSubmissionSet(message.participantObjectIdentifications[1])
    }

    def sendIt(endpoint, value) {
        docEntry.comments = new LocalizedString(value)
        return send(endpoint, request, Response.class)
    }
}