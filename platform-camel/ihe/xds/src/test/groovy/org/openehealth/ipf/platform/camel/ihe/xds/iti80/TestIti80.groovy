/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti80

import groovy.util.logging.Slf4j
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer

@Slf4j
class TestIti80 extends XdsStandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-80.xml'
    
    def SERVICE1 = "xcdr-iti80://localhost:${port}/xcdr-iti80-service1"

    ProvideAndRegisterDocumentSet request

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }
    
    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @BeforeEach
    void setUp() {
        request = SampleData.createProvideAndRegisterDocumentSet()
    }

    @Test
    void test1() {
        request.targetHomeCommunityId = null
        def response = sendIt(SERVICE1)
        assert response.status == Status.FAILURE
        assert response.errors[0].errorCode == ErrorCode.MISSING_HOME_COMMUNITY_ID
        def auditMessages = auditSender.messages
        assert auditMessages.size() == 2
    }

    @Test
    void test2() {
        def response = sendIt(SERVICE1)
        assert response.status == Status.SUCCESS
        def auditMessages = auditSender.messages
        assert auditMessages.size() == 2
        for (auditMessage in auditMessages) {
            assert auditMessage.participantObjectIdentifications.size() == 2
            def ssParticipants = auditMessage.findParticipantObjectIdentifications { it.participantObjectIDTypeCode.originalText == 'submission set classificationNode' }
            assert ssParticipants.size() == 1
            assert ssParticipants[0].participantObjectDetails.size() == 1
            ssParticipants[0].participantObjectDetails[0].with {
                assert type == 'urn:ihe:iti:xca:2010:homeCommunityId'
                assert value == 'urn:oid:1.2.3.4.5.6.2333.23'.bytes
            }
        }
    }

    def sendIt(endpoint) {
        return (Response) send(endpoint, request, Response.class)
    }

}