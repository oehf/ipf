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
package org.openehealth.ipf.platform.camel.ihe.hpd.iti59

import org.apache.commons.io.IOUtils
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.hpd.HPD
import org.openehealth.ipf.commons.ihe.hpd.audit.codes.HpdParticipantObjectIdTypeCode
import org.openehealth.ipf.commons.ihe.hpd.iti59.Iti59PortType
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import javax.xml.bind.JAXB
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import java.nio.charset.StandardCharsets

/**
 * @author Dmytro Rud
 */
class TestIti59 extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'iti-59.xml'

    final String SERVICE1 = "hpd-iti59://localhost:${port}/hpd-service1"

    private static final JAXB_CONTEXT = JAXBContext.newInstance(ObjectFactory.class)

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void testIti59() {
        JAXBElement<BatchRequest> jaxbElement = (JAXBElement) JAXB_CONTEXT.createUnmarshaller().unmarshal(TestIti59.class.classLoader.getResourceAsStream('iti-59-request-message-1.xml'))
        BatchResponse response = sendIt(SERVICE1, jaxbElement.value)
        assert response != null
        def messages = auditSender.messages
        assert messages.size() == 6

        // TODO more tests

        /*
        for (message in messages) {
            if (message.eventIdentification.eventActionCode == EventActionCode.Create) {
                assert message.participantObjectIdentifications.size() == 5
                for (participant in message.participantObjectIdentifications) {
                    assert participant.participantObjectTypeCode in [ParticipantObjectTypeCode.Organization, ParticipantObjectTypeCode.Person]
                    assert participant.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Provider
                    assert participant.participantObjectIDTypeCode == HpdParticipantObjectIdTypeCode.ProviderIdentifier
                    assert participant.participantObjectID.startsWith('hcid-')
                }
            }
            else if (message.eventIdentification.eventActionCode == EventActionCode.Delete) {
                assert message.participantObjectIdentifications.size() == 1
                def participant = message.participantObjectIdentifications[0]
                assert participant.participantObjectTypeCode == ParticipantObjectTypeCode.System
                assert participant.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Resource
                assert participant.participantObjectIDTypeCode == HpdParticipantObjectIdTypeCode.DistinguishedName
                assert participant.participantObjectID == 'dn-2'
            }
            else if (message.eventIdentification.eventActionCode == EventActionCode.Update) {
                assert message.participantObjectIdentifications.size() == 2
                for (participant in message.participantObjectIdentifications) {
                    assert participant.participantObjectTypeCode == ParticipantObjectTypeCode.System
                    assert participant.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Resource
                    assert participant.participantObjectIDTypeCode == HpdParticipantObjectIdTypeCode.DistinguishedName
                }
                assert message.participantObjectIdentifications[0].participantObjectID == 'dn-3'
                assert message.participantObjectIdentifications[1].participantObjectID == 'newrdn-3'
            }
        }   */
    }

    BatchResponse sendIt(String endpoint, BatchRequest request) {
        return send(endpoint, request, BatchResponse.class)
    }


}
