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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chadr;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.CodedValueType;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Status;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import javax.xml.bind.JAXBElement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class ChAdrTest extends StandardTestContainer {

    @BeforeAll
    public static void beforeClass() {
        startServer(new CXFServlet(), "chadr-context.xml");
        Xacml20Utils.initializeHerasaf();
    }

    @BeforeEach
    public void before() {
        getAuditSender().clear();
    }

    private static String getUri(String suffix) {
        return "ch-adr://localhost:" + getPort() + "/ch-adr-" + suffix;
    }

    private static <T> T loadFile(String fn) throws Exception {
        var stream = ChAdrTest.class.getClassLoader().getResourceAsStream("messages/chadr/" + fn);
        var unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
        var object = unmarshaller.unmarshal(stream);
        if (object instanceof JAXBElement) {
            object = ((JAXBElement<?>) object).getValue();
        }
        return (T) object;
    }

    private static void checkCodeValueType(CodedValueType value, String[]... allowedCodes) {
        for (var allowed : allowedCodes) {
            if (allowed[0].equals(value.getCode()) && allowed[1].equals(value.getCodeSystemName()) && allowed[2].equals(value.getOriginalText())) {
                return;
            }
        }
        assertEquals(null, value);
    }

    @Test
    public void testQuerySuccess() throws Exception {
        doTestQuery("success", Xacml20Status.SUCCESS, EventOutcomeIndicator.Success, "Permit", "urn:oid:1.44.567");
    }

    @Test
    public void testQueryFailure() throws Exception {
        doTestQuery("failure", Xacml20Status.RESPONDER_ERROR, EventOutcomeIndicator.SeriousFailure, "[unknown]", "urn:oid:1.2.4");
    }

    private void doTestQuery(
        String suffix,
        Xacml20Status status,
        EventOutcomeIndicator outcomeIndicator,
        String decision,
        String issuerId) throws Exception
    {
        var response = (ResponseType) send(getUri(suffix), loadFile("chadr-request-1.xml"), ResponseType.class);
        assertEquals(status.getCode(), response.getStatus().getStatusCode().getValue());
        var assertion = (AssertionType) response.getAssertionOrEncryptedAssertion().get(0);
        assertEquals(issuerId, assertion.getIssuer().getValue());

        List<AuditMessage> messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());

        for (var object : messages) {
            var message = (AuditMessage) object;

            var event = message.getEventIdentification();
            assertEquals(EventActionCode.Execute, event.getEventActionCode());
            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
            checkCodeValueType(event.getEventID(), new String[]{"110112", "DCM", "Query"});
            assertEquals(1, event.getEventTypeCode().size());
            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"ADR", "e-health-suisse", "Authorization Decisions Query"});

            assertEquals(0, event.getPurposesOfUse().size());

            assertEquals(2, message.getActiveParticipants().size());
            assertEquals(1, message.getActiveParticipants().get(0).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(0).getRoleIDCodes().get(0), new String[]{"110153", "DCM", "Source Role ID"});
            assertEquals(1, message.getActiveParticipants().get(1).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(1).getRoleIDCodes().get(0), new String[]{"110152", "DCM", "Destination Role ID"});

            assertEquals(2, message.getParticipantObjectIdentifications().size());

            {
                var requesterParticipant = message.getParticipantObjectIdentifications().get(0);
                assertEquals(ParticipantObjectTypeCode.Person, requesterParticipant.getParticipantObjectTypeCode());
                assertEquals(ParticipantObjectTypeCodeRole.SecurityUserEntity, requesterParticipant.getParticipantObjectTypeCodeRole());
                checkCodeValueType(requesterParticipant.getParticipantObjectIDTypeCode(), new String[]{"HCP", "2.16.756.5.30.1.127.3.10.6", "Healthcare Professional"});
                assertEquals("7600000000000", requesterParticipant.getParticipantObjectID());
                assertEquals(0, requesterParticipant.getParticipantObjectQuery().length);
                assertTrue(requesterParticipant.getParticipantObjectDetails().isEmpty());
            }
            {
                var resourceParticipant = message.getParticipantObjectIdentifications().get(1);
                assertEquals(ParticipantObjectTypeCode.System, resourceParticipant.getParticipantObjectTypeCode());
                assertEquals(ParticipantObjectTypeCodeRole.SecurityResource, resourceParticipant.getParticipantObjectTypeCodeRole());
                checkCodeValueType(resourceParticipant.getParticipantObjectIDTypeCode(), new String[]{"ADR", "e-health-suisse", "Authorization Decisions Query"});
                assertEquals("urn:uuid:5a478b92-0b20-40a9-9bee-30ce7d831ca2", resourceParticipant.getParticipantObjectID());
                assertEquals(0, resourceParticipant.getParticipantObjectQuery().length);
                assertEquals(1, resourceParticipant.getParticipantObjectDetails().size());
                assertEquals("decision", resourceParticipant.getParticipantObjectDetails().get(0).getType());
                assertEquals(decision, new String(resourceParticipant.getParticipantObjectDetails().get(0).getValue()));
            }
        }
    }

}
