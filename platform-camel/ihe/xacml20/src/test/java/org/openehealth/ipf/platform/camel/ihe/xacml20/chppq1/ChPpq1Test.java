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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq1;

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
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants;
import org.openehealth.ipf.commons.ihe.xacml20.stub.UnknownPolicySetIdFaultMessage;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import jakarta.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Dmytro Rud
 * @since 3.5.1
 */
public class ChPpq1Test extends StandardTestContainer {

    @BeforeAll
    public static void beforeClass() {
        startServer(new CXFServlet(), "chppq1-context.xml");
        Xacml20Utils.initializeHerasaf();
    }

    @BeforeEach
    public void before() {
        getAuditSender().clear();
    }

    private static String getUri(String suffix) {
        return "ch-ppq1://localhost:" + getPort() + "/ch-ppq-" + suffix;
    }

    private static <T> T loadFile(String fn) throws Exception {
        var stream = ChPpq1Test.class.getClassLoader().getResourceAsStream("messages/chppq1/" + fn);
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
        assertEquals(null, value);
    }

    @Test
    public void testAddPolicySuccess1() throws Exception {
        testAddPolicy("success", PpqConstants.StatusCode.SUCCESS, EventOutcomeIndicator.Success, null);
    }

    @Test
    public void testAddPolicySuccess2() throws Exception {
        testAddPolicy("success", PpqConstants.StatusCode.SUCCESS, EventOutcomeIndicator.Success, UUID.randomUUID().toString());
    }

    @Test
    public void testAddPolicyFailure() throws Exception {
        testAddPolicy("failure", PpqConstants.StatusCode.FAILURE, EventOutcomeIndicator.SeriousFailure, UUID.randomUUID().toString());
    }

    private void testAddPolicy(String suffix, String statusCode, EventOutcomeIndicator outcomeIndicator, String traceContextId) throws Exception {
        var httpHeaders = (traceContextId != null)
            ? Map.of(AbstractWsEndpoint.OUTGOING_HTTP_HEADERS, Map.of("TraceParent", traceContextId))
            : null;
        var response = (EprPolicyRepositoryResponse) send(getUri(suffix), loadFile("add-request-ppq.xml"),
            EprPolicyRepositoryResponse.class,
            httpHeaders);
        assertEquals(statusCode, response.getStatus());

        List<AuditMessage> messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());

        for (var message : messages) {
            var event = message.getEventIdentification();
            assertEquals(EventActionCode.Create, event.getEventActionCode());
            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
            checkCodeValueType(event.getEventID(), new String[]{"110106", "DCM", "Export"}, new String[]{"110107", "DCM", "Import"});
            assertEquals(1, event.getEventTypeCode().size());
            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ-1", "e-health-suisse", "Privacy Policy Feed"});

            assertEquals(0, event.getPurposesOfUse().size());

            assertEquals(2, message.getActiveParticipants().size());
            assertEquals(1, message.getActiveParticipants().get(0).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(0).getRoleIDCodes().get(0), new String[]{"110153", "DCM", "Source Role ID"});
            assertEquals(1, message.getActiveParticipants().get(1).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(1).getRoleIDCodes().get(0), new String[]{"110152", "DCM", "Destination Role ID"});

            assertEquals(3, message.getParticipantObjectIdentifications().size());

            var participant = message.getParticipantObjectIdentifications().get(0);
            assertEquals(ParticipantObjectTypeCode.Other, participant.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.ProcessingElement, participant.getParticipantObjectTypeCodeRole());
            assertEquals((traceContextId != null) ? traceContextId : ChPpq1TestRouteBuilder.TRACE_CONTEXT_ID, participant.getParticipantObjectID());

            participant = message.getParticipantObjectIdentifications().get(1);
            assertEquals(ParticipantObjectTypeCode.System, participant.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.SecurityResource, participant.getParticipantObjectTypeCodeRole());
            assertEquals("urn:uuid:58bbfa76-4d65-4fa1-b0af-c862b52a20d4", participant.getParticipantObjectID());

            participant = message.getParticipantObjectIdentifications().get(2);
            assertEquals(ParticipantObjectTypeCode.Person, participant.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.Patient, participant.getParticipantObjectTypeCodeRole());
            assertEquals("761337611194602836^^^&2.16.756.5.30.1.127.3.10.3&ISO", participant.getParticipantObjectID());
        }
    }

    @Test
    public void testUpdatePolicySuccess() throws Exception {
        testUpdatePolicy("success", PpqConstants.StatusCode.SUCCESS, EventOutcomeIndicator.Success, false);
    }

    @Test
    public void testUpdatePolicyFailure() throws Exception {
        testUpdatePolicy("failure", PpqConstants.StatusCode.FAILURE, EventOutcomeIndicator.SeriousFailure, false);
    }

    @Test
    public void testUpdatePolicyException() throws Exception {
        testUpdatePolicy("exception", PpqConstants.StatusCode.FAILURE, EventOutcomeIndicator.SeriousFailure, true);
    }

    private void testUpdatePolicy(String suffix, String statusCode, EventOutcomeIndicator outcomeIndicator, boolean expectSoapFault) throws Exception {
        try {
            var response = (EprPolicyRepositoryResponse) send(getUri(suffix), loadFile("update-request-ppq.xml"),
                EprPolicyRepositoryResponse.class);
            assertEquals(statusCode, response.getStatus());
        } catch (UnknownPolicySetIdFaultMessage exception) {
            if (!expectSoapFault) {
                throw exception;
            }
        }

        List messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());

        for (var object : messages) {
            var message = (AuditMessage) object;

            var event = message.getEventIdentification();
            assertEquals(EventActionCode.Update, event.getEventActionCode());
            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
            checkCodeValueType(event.getEventID(), new String[]{"110106", "DCM", "Export"}, new String[]{"110107", "DCM", "Import"});
            assertEquals(1, event.getEventTypeCode().size());
            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ-1", "e-health-suisse", "Privacy Policy Feed"});

            assertEquals(0, event.getPurposesOfUse().size());

            assertEquals(2, message.getActiveParticipants().size());
            assertEquals(1, message.getActiveParticipants().get(0).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(0).getRoleIDCodes().get(0), new String[]{"110153", "DCM", "Source Role ID"});
            assertEquals(1, message.getActiveParticipants().get(1).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(1).getRoleIDCodes().get(0), new String[]{"110152", "DCM", "Destination Role ID"});

            assertEquals(2, message.getParticipantObjectIdentifications().size());

            var participant = message.getParticipantObjectIdentifications().get(0);
            assertEquals(ParticipantObjectTypeCode.System, participant.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.SecurityResource, participant.getParticipantObjectTypeCodeRole());
            assertEquals("urn:uuid:58bbfa76-4d65-4fa1-b0af-c862b52a20d4", participant.getParticipantObjectID());

            participant = message.getParticipantObjectIdentifications().get(1);
            assertEquals(ParticipantObjectTypeCode.Person, participant.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.Patient, participant.getParticipantObjectTypeCodeRole());
            assertEquals("761337611194602836^^^&2.16.756.5.30.1.127.3.10.3&ISO", participant.getParticipantObjectID());
        }
    }

    @Test
    public void testDeletePolicySuccess() throws Exception {
        testDeletePolicy("success", PpqConstants.StatusCode.SUCCESS, EventOutcomeIndicator.Success, false);
    }

    @Test
    public void testDeletePolicyFailure() throws Exception {
        testDeletePolicy("failure", PpqConstants.StatusCode.FAILURE, EventOutcomeIndicator.SeriousFailure, false);
    }

    @Test
    public void testDeletePolicyException() throws Exception {
        testDeletePolicy("exception", PpqConstants.StatusCode.FAILURE, EventOutcomeIndicator.SeriousFailure, true);
    }

    private void testDeletePolicy(String suffix, String statusCode, EventOutcomeIndicator outcomeIndicator, boolean expectException) throws Exception {
        try {
            var response = (EprPolicyRepositoryResponse) send(getUri(suffix), loadFile("delete-request.xml"), EprPolicyRepositoryResponse.class);
            assertEquals(statusCode, response.getStatus());
        } catch (UnknownPolicySetIdFaultMessage exception) {
            if (!expectException) {
                throw exception;
            }
        }

        List messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());

        for (var object : messages) {
            var message = (AuditMessage) object;

            var event = message.getEventIdentification();
            assertEquals(EventActionCode.Delete, event.getEventActionCode());
            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
            checkCodeValueType(event.getEventID(), new String[]{"110106", "DCM", "Export"}, new String[]{"110107", "DCM", "Import"});
            assertEquals(1, event.getEventTypeCode().size());
            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ-1", "e-health-suisse", "Privacy Policy Feed"});

            assertEquals(0, event.getPurposesOfUse().size());

            assertEquals(2, message.getActiveParticipants().size());
            assertEquals(1, message.getActiveParticipants().get(0).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(0).getRoleIDCodes().get(0), new String[]{"110153", "DCM", "Source Role ID"});
            assertEquals(1, message.getActiveParticipants().get(1).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(1).getRoleIDCodes().get(0), new String[]{"110152", "DCM", "Destination Role ID"});

            assertEquals(5, message.getParticipantObjectIdentifications().size());
            for (var i = 0; i < 5; ++i) {
                var participant = message.getParticipantObjectIdentifications().get(i);
                assertEquals(ParticipantObjectTypeCode.System, participant.getParticipantObjectTypeCode());
                assertEquals(ParticipantObjectTypeCodeRole.SecurityResource, participant.getParticipantObjectTypeCodeRole());
                assertEquals("urn:uuid:10a3f268-d9d6-4772-b908-9d852116" + i, participant.getParticipantObjectID());
            }
        }
    }

}
