/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti62

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveMetadata
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-62 transaction with a webservice and client adapter defined via URIs.
 * @author Boris Stanojevic
 */
class TestIti62 extends XdsStandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-62.xml'
    
    def SERVICE1 = "xds-iti62://localhost:${port}/xds-iti62-service1"
    def SERVICE2 = "xds-iti62://localhost:${port}/xds-iti62-service2"

    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti62-service2"

    RemoveMetadata request

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }
    
    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @BeforeEach
    void setUp() {
        request = SampleData.createRemoveMetadata()
    }
    
    @Test
    void testIti62() {
        assert SUCCESS == sendIt(SERVICE1).status
        assert SUCCESS == sendIt(SERVICE2).status
        assert auditSender.messages.size() == 4

        checkAudit(EventOutcomeIndicator.Success)
    }
    
    @Test
    void testIti62FailureAudit() {
        request.getReferences().add(new ObjectReference("wrong-id", "not-at-home"))
        def response = sendIt(SERVICE1)
        assert response.status == FAILURE
        assert response.errors.size() == 1
        assert response.errors[0].errorCode == ErrorCode.UNRESOLVED_REFERENCE_EXCEPTION
        assert response.errors[0].codeContext == 'wrong-id'
        assert auditSender.messages.size() == 2
    }

    @Test
    void testIti62NotValidAudit() {
        request.references.clear()
        assert FAILURE == sendIt(SERVICE1).status
        assert auditSender.messages.size() == 2
    }

    void checkAudit(EventOutcomeIndicator outcome) {
        AuditMessage message = getAudit(EventActionCode.Delete, SERVICE2_ADDR)[0]

        assert message.activeParticipants.size() == 2
        assert message.participantObjectIdentifications.size() == 2

        checkEvent(message.eventIdentification, '110110', 'ITI-62', EventActionCode.Delete, outcome)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], SERVICE2_ADDR, false)
        checkAuditSource(message.auditSourceIdentification, 'sourceId')
        checkRegistryObjectParticipantObjectDetail(message.participantObjectIdentifications[0], 'urn:ihe:iti:2017:ObjectRef', 'urn:uuid:b2632452-1de7-480d-94b1-c2074d79c871')
        checkRegistryObjectParticipantObjectDetail(message.participantObjectIdentifications[1], 'urn:ihe:iti:2017:ObjectRef', 'urn:uuid:b2632df2-1de7-480d-1045-c2074d79aabd')

        message = getAudit(EventActionCode.Delete, SERVICE2_ADDR)[1]

        assert message.activeParticipants.size() == 2
        assert message.participantObjectIdentifications.size() == 2

        checkEvent(message.eventIdentification, '110110', 'ITI-62', EventActionCode.Delete, outcome)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], SERVICE2_ADDR, false)
        checkAuditSource(message.auditSourceIdentification, 'sourceId')
        checkRegistryObjectParticipantObjectDetail(message.participantObjectIdentifications[0], 'urn:ihe:iti:2017:ObjectRef', 'urn:uuid:b2632452-1de7-480d-94b1-c2074d79c871')
        checkRegistryObjectParticipantObjectDetail(message.participantObjectIdentifications[1], 'urn:ihe:iti:2017:ObjectRef', 'urn:uuid:b2632df2-1de7-480d-1045-c2074d79aabd')
    }

    def sendIt(endpoint) {
        return send(endpoint, request, Response.class)
    }

}