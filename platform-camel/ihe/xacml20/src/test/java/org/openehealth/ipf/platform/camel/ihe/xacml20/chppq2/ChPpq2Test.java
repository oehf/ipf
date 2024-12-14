/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq2;

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

import jakarta.xml.bind.JAXBElement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Dmytro Rud
 * @since 3.5.1
 */
public class ChPpq2Test extends StandardTestContainer {

    @BeforeAll
    public static void beforeClass() {
        startServer(new CXFServlet(), "chppq2-context.xml");
        Xacml20Utils.initializeHerasaf();
    }

    @BeforeEach
    public void before() {
        getAuditSender().clear();
    }

    private static String getUri(String suffix) {
        return "ch-ppq2://localhost:" + getPort() + "/ch-ppq-" + suffix;
    }

    private static <T> T loadFile(String fn) throws Exception {
        var stream = ChPpq2Test.class.getClassLoader().getResourceAsStream("messages/chppq2/" + fn);
        var unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
        var object = unmarshaller.unmarshal(stream);
        if (object instanceof JAXBElement) {
            object = ((JAXBElement) object).getValue();
        }
        return (T) object;
    }

    private static void checkCodeValueType(CodedValueType value, String[]... allowedCodes) {
        for (var allowed : allowedCodes) {
            if (allowed[0].equals(value.getCode()) && allowed[1].equals(value.getCodeSystemName()) && allowed[2].equals(value.getOriginalText())) {
                return;
            }
        }
        assertNull(value);
    }

    @Test
    public void testQueryPerPatientIdSuccess() throws Exception {
        testQueryPerPatientId("success", Xacml20Status.SUCCESS, EventOutcomeIndicator.Success, "urn:oid:1.2.3");
    }

    @Test
    public void testQueryPerPatientIdFailure() throws Exception {
        testQueryPerPatientId("failure", Xacml20Status.RESPONDER_ERROR, EventOutcomeIndicator.SeriousFailure, "urn:oid:1.2.4");
    }

    private void testQueryPerPatientId(String suffix, Xacml20Status status, EventOutcomeIndicator outcomeIndicator, String homeCommunityId) throws Exception {
        var response = (ResponseType) send(getUri(suffix), loadFile("query-per-patient-id.xml"), ResponseType.class);
        assertEquals(status.getCode(), response.getStatus().getStatusCode().getValue());
        var assertion = (AssertionType) response.getAssertionOrEncryptedAssertion().get(0);
        assertEquals(homeCommunityId, assertion.getIssuer().getValue());

        List messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());

        for (var object : messages) {
            var message = (AuditMessage) object;

            var event = message.getEventIdentification();
            assertEquals(EventActionCode.Execute, event.getEventActionCode());
            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
            checkCodeValueType(event.getEventID(), new String[]{"110112", "DCM", "Query"});
            assertEquals(1, event.getEventTypeCode().size());
            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ-2", "e-health-suisse", "Privacy Policy Retrieve"});

            assertEquals(0, event.getPurposesOfUse().size());

            assertEquals(2, message.getActiveParticipants().size());
            assertEquals(1, message.getActiveParticipants().get(0).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(0).getRoleIDCodes().get(0), new String[]{"110153", "DCM", "Source Role ID"});
            assertEquals(1, message.getActiveParticipants().get(1).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(1).getRoleIDCodes().get(0), new String[]{"110152", "DCM", "Destination Role ID"});

            assertEquals(2, message.getParticipantObjectIdentifications().size());

            var patientParticipant = message.getParticipantObjectIdentifications().get(0);
            assertEquals(ParticipantObjectTypeCode.Person, patientParticipant.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.Patient, patientParticipant.getParticipantObjectTypeCodeRole());
            assertEquals("8901^^^&2.16.756.5.30.1.127.3.10.3&ISO", patientParticipant.getParticipantObjectID());

            var queryParticipant = message.getParticipantObjectIdentifications().get(1);
            assertEquals(ParticipantObjectTypeCode.System, queryParticipant.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.Query, queryParticipant.getParticipantObjectTypeCodeRole());
            assertEquals("ppq-query-id-1", queryParticipant.getParticipantObjectID());
            assertTrue(queryParticipant.getParticipantObjectQuery().length > 50);
            assertEquals(1, queryParticipant.getParticipantObjectDetails().size());
        }
    }

    @Test
    public void testQueryPerPolicyIdSuccess() throws Exception {
        testQueryPerPolicyId("success", Xacml20Status.SUCCESS, EventOutcomeIndicator.Success);
    }

    @Test
    public void testQueryPerPolicyIdFailure() throws Exception {
        testQueryPerPolicyId("failure", Xacml20Status.RESPONDER_ERROR, EventOutcomeIndicator.SeriousFailure);
    }

    private void testQueryPerPolicyId(String suffix, Xacml20Status status, EventOutcomeIndicator outcomeIndicator) throws Exception {
        var response = (ResponseType) send(getUri(suffix), loadFile("query-per-policy-id.xml"), ResponseType.class);
        assertEquals(status.getCode(), response.getStatus().getStatusCode().getValue());

        List messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());

        for (var object : messages) {
            var message = (AuditMessage) object;

            var event = message.getEventIdentification();
            assertEquals(EventActionCode.Execute, event.getEventActionCode());
            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
            checkCodeValueType(event.getEventID(), new String[]{"110112", "DCM", "Query"});
            assertEquals(1, event.getEventTypeCode().size());
            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ-2", "e-health-suisse", "Privacy Policy Retrieve"});

            assertEquals(0, event.getPurposesOfUse().size());

            assertEquals(2, message.getActiveParticipants().size());
            assertEquals(1, message.getActiveParticipants().get(0).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(0).getRoleIDCodes().get(0), new String[]{"110153", "DCM", "Source Role ID"});
            assertEquals(1, message.getActiveParticipants().get(1).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(1).getRoleIDCodes().get(0), new String[]{"110152", "DCM", "Destination Role ID"});

            assertEquals(1, message.getParticipantObjectIdentifications().size());

            var queryParticipant = message.getParticipantObjectIdentifications().get(0);
            assertEquals(ParticipantObjectTypeCode.System, queryParticipant.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.Query, queryParticipant.getParticipantObjectTypeCodeRole());
            assertEquals("ppq-query-id-2", queryParticipant.getParticipantObjectID());
            assertTrue(queryParticipant.getParticipantObjectQuery().length > 50);
            assertEquals(1, queryParticipant.getParticipantObjectDetails().size());
        }
    }

}
