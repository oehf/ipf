package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.UnknownPolicySetIdFaultMessage;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.EpdPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;
import org.opensaml.saml.saml2.core.StatusCode;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmytro Rud
 */
public class ChPpqTest extends StandardTestContainer {

    @BeforeClass
    public static void beforeClass() {

//        PpqAuditor.getAuditor().getConfig().setAuditRepositoryHost("localhost");
//        PpqAuditor.getAuditor().getConfig().setAuditRepositoryPort(514);
//        PpqLegacyAuditor.getAuditor().getConfig().setAuditRepositoryHost("localhost");
//        PpqLegacyAuditor.getAuditor().getConfig().setAuditRepositoryPort(514);
        startServer(new CXFServlet(), "chppq-context.xml");
        Xacml20Utils.initializeHerasaf();
    }

    @Before
    public void before() {
        getAuditSender().clear();
    }

    private static String getUri(String suffix) {
        return "ch-ppq://localhost:" + getPort() + "/ch-ppq-" + suffix;
    }

    private static <T> T loadFile(String fn) throws Exception {
        InputStream stream = ChPpqTest.class.getClassLoader().getResourceAsStream("messages/chppq/" + fn);
        Unmarshaller unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
        Object object = unmarshaller.unmarshal(stream);
        if (object instanceof JAXBElement) {
            object = ((JAXBElement) object).getValue();
        }
        return (T) object;
    }

//    private static void checkCodeValueType(CodedValueType value, String[]... allowedCodes) {
//        for (String[] allowed : allowedCodes) {
//            if (allowed[0].equals(value.getCode()) && allowed[1].equals(value.getCodeSystemName()) && allowed[2].equals(value.getOriginalText())) {
//                return;
//            }
//        }
//        assertTrue(false);
//    }

    @Test
    public void testQueryPerPatientIdSuccess() throws Exception {
        testQueryPerPatientId("success", StatusCode.SUCCESS, 0);
    }

    @Test
    public void testQueryPerPatientIdFailure() throws Exception {
        testQueryPerPatientId("failure", StatusCode.RESPONDER, 8);
    }

    private void testQueryPerPatientId(String suffix, String statusCode, int outcomeIndicator) throws Exception {
        ResponseType response = (ResponseType) send(getUri(suffix), loadFile("query-per-patient-id.xml"), ResponseType.class);
        assertEquals(statusCode, response.getStatus().getStatusCode().getValue());

        List messages = getAuditSender().getMessages();
//        assertEquals(2, messages.size());
//
//        for (Object object : messages) {
//            AuditMessage message = ((AuditEventMessage) object).getAuditMessage();
//
//            EventIdentificationType event = message.getEventIdentification();
//            assertEquals("E", event.getEventActionCode());
//            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
//            checkCodeValueType(event.getEventID(), new String[]{"110112", "DCM", "Query"});
//            assertEquals(1, event.getEventTypeCode().size());
//            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ", "e-health-suisse", "PPQ Privacy Policy Query"});
//
//            assertEquals(0, event.getPurposesOfUse().size());
//
//            assertEquals(2, message.getActiveParticipant().size());
//            assertEquals(1, message.getActiveParticipant().get(0).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(0).getRoleIDCode().get(0), new String[]{"110153", "DCM", "Source"});
//            assertEquals(1, message.getActiveParticipant().get(1).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(1).getRoleIDCode().get(0), new String[]{"110152", "DCM", "Destination"});
//
//            assertEquals(2, message.getParticipantObjectIdentification().size());
//
//            ParticipantObjectIdentificationType patientParticipant = message.getParticipantObjectIdentification().get(0);
//            assertEquals(1, patientParticipant.getParticipantObjectTypeCode().intValue());
//            assertEquals(1, patientParticipant.getParticipantObjectTypeCodeRole().intValue());
//            assertEquals("8901^^^&2.16.756.5.30.1.127.3.10.3&ISO", patientParticipant.getParticipantObjectID());
//
//            ParticipantObjectIdentificationType queryParticipant = message.getParticipantObjectIdentification().get(1);
//            assertEquals(2, queryParticipant.getParticipantObjectTypeCode().intValue());
//            assertEquals(24, queryParticipant.getParticipantObjectTypeCodeRole().intValue());
//            assertEquals("ppq-query-id-1", queryParticipant.getParticipantObjectID());
//            assertTrue(queryParticipant.getParticipantObjectQuery().length > 100);
//            assertEquals(1, queryParticipant.getParticipantObjectDetail().size());
//            assertEquals("QueryEncoding", queryParticipant.getParticipantObjectDetail().get(0).getType());
//            assertTrue(queryParticipant.getParticipantObjectDetail().get(0).getValue().length > 5);
//        }
    }

    @Test
    public void testQueryPerPolicyIdSuccess() throws Exception {
        testQueryPerPolicyId("success", StatusCode.SUCCESS, 0);
    }

    @Test
    public void testQueryPerPolicyIdFailure() throws Exception {
        testQueryPerPolicyId("failure", StatusCode.RESPONDER, 8);
    }

    private void testQueryPerPolicyId(String suffix, String statusCode, int outcomeIndicator) throws Exception {
        ResponseType response = (ResponseType) send(getUri(suffix), loadFile("query-per-policy-id.xml"), ResponseType.class);
        assertEquals(statusCode, response.getStatus().getStatusCode().getValue());

        List messages = getAuditSender().getMessages();
//        assertEquals(2, messages.size());
//
//        for (Object object : messages) {
//            AuditMessage message = ((AuditEventMessage) object).getAuditMessage();
//
//            EventIdentificationType event = message.getEventIdentification();
//            assertEquals("E", event.getEventActionCode());
//            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
//            checkCodeValueType(event.getEventID(), new String[]{"110112", "DCM", "Query"});
//            assertEquals(1, event.getEventTypeCode().size());
//            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ", "e-health-suisse", "PPQ Privacy Policy Query"});
//
//            assertEquals(0, event.getPurposesOfUse().size());
//
//            assertEquals(2, message.getActiveParticipant().size());
//            assertEquals(1, message.getActiveParticipant().get(0).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(0).getRoleIDCode().get(0), new String[]{"110153", "DCM", "Source"});
//            assertEquals(1, message.getActiveParticipant().get(1).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(1).getRoleIDCode().get(0), new String[]{"110152", "DCM", "Destination"});
//
//            assertEquals(2, message.getParticipantObjectIdentification().size());
//
//            ParticipantObjectIdentificationType queryParticipant = message.getParticipantObjectIdentification().get(0);
//            assertEquals(2, queryParticipant.getParticipantObjectTypeCode().intValue());
//            assertEquals(24, queryParticipant.getParticipantObjectTypeCodeRole().intValue());
//            assertEquals("ppq-query-id-2", queryParticipant.getParticipantObjectID());
//            assertTrue(queryParticipant.getParticipantObjectQuery().length > 100);
//            assertEquals(1, queryParticipant.getParticipantObjectDetail().size());
//            assertEquals("QueryEncoding", queryParticipant.getParticipantObjectDetail().get(0).getType());
//            assertTrue(queryParticipant.getParticipantObjectDetail().get(0).getValue().length > 5);
//
//            ParticipantObjectIdentificationType policyParticipant = message.getParticipantObjectIdentification().get(1);
//            assertEquals(2, policyParticipant.getParticipantObjectTypeCode().intValue());
//            assertEquals(13, policyParticipant.getParticipantObjectTypeCodeRole().intValue());
//            assertEquals("urn:e-health-suisse:2015:policies:exclusion-list", policyParticipant.getParticipantObjectID());
//        }
    }

    @Test
    public void testAddPolicySuccess() throws Exception {
        testAddPolicy("success", Xacml20Utils.PPQ_STATUS_SUCCESS, 0);
    }

    @Test
    public void testAddPolicyFailure() throws Exception {
        testAddPolicy("failure", Xacml20Utils.PPQ_STATUS_FAILURE, 8);
    }

    private void testAddPolicy(String suffix, String statusCode, int outcomeIndicator) throws Exception {
        EpdPolicyRepositoryResponse response = (EpdPolicyRepositoryResponse) send(getUri(suffix), loadFile("add-request.xml"), EpdPolicyRepositoryResponse.class);
        assertEquals(statusCode, response.getStatus());

        List messages = getAuditSender().getMessages();
//        assertEquals(2, messages.size());
//
//        for (Object object : messages) {
//            AuditMessage message = ((AuditEventMessage) object).getAuditMessage();
//
//            EventIdentificationType event = message.getEventIdentification();
//            assertEquals("C", event.getEventActionCode());
//            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
//            checkCodeValueType(event.getEventID(), new String[]{"110106", "DCM", "Export"}, new String[]{"110107", "DCM", "Import"});
//            assertEquals(1, event.getEventTypeCode().size());
//            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ", "e-health-suisse", "PPQ Privacy Policy Query"});
//
//            assertEquals(0, event.getPurposesOfUse().size());
//
//            assertEquals(2, message.getActiveParticipant().size());
//            assertEquals(1, message.getActiveParticipant().get(0).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(0).getRoleIDCode().get(0), new String[]{"110153", "DCM", "Source"});
//            assertEquals(1, message.getActiveParticipant().get(1).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(1).getRoleIDCode().get(0), new String[]{"110152", "DCM", "Destination"});
//
//            assertEquals(2, message.getParticipantObjectIdentification().size());
//
//            for (ParticipantObjectIdentificationType participant : message.getParticipantObjectIdentification()) {
//                assertEquals(2, participant.getParticipantObjectTypeCode().intValue());
//                assertEquals(13, participant.getParticipantObjectTypeCodeRole().intValue());
//            }
//
//            assertEquals("1.2.840.113619.20.2.9.0", message.getParticipantObjectIdentification().get(0).getParticipantObjectID());
//            assertEquals("3644dc70-4dec-11e3-8f96-0800200c9a66", message.getParticipantObjectIdentification().get(1).getParticipantObjectID());
//        }
    }

    @Test
    public void testUpdatePolicySuccess() throws Exception {
        testUpdatePolicy("success", Xacml20Utils.PPQ_STATUS_SUCCESS, 0, false);
    }

    @Test
    public void testUpdatePolicyFailure() throws Exception {
        testUpdatePolicy("failure", Xacml20Utils.PPQ_STATUS_FAILURE, 8, false);
    }

    @Test
    public void testUpdatePolicyException() throws Exception {
        testUpdatePolicy("exception", Xacml20Utils.PPQ_STATUS_FAILURE, 8, true);
    }

    private void testUpdatePolicy(String suffix, String statusCode, int outcomeIndicator, boolean expectSoapFault) throws Exception {
        try {
            EpdPolicyRepositoryResponse response = (EpdPolicyRepositoryResponse) send(getUri(suffix), loadFile("update-request.xml"), EpdPolicyRepositoryResponse.class);
            assertEquals(statusCode, response.getStatus());
        } catch (UnknownPolicySetIdFaultMessage exception) {
            if (!expectSoapFault) {
                throw exception;
            }
        }

        List messages = getAuditSender().getMessages();
//        assertEquals(2, messages.size());
//
//        for (Object object : messages) {
//            AuditMessage message = ((AuditEventMessage) object).getAuditMessage();
//
//            EventIdentificationType event = message.getEventIdentification();
//            assertEquals("U", event.getEventActionCode());
//            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
//            checkCodeValueType(event.getEventID(), new String[]{"110106", "DCM", "Export"}, new String[]{"110107", "DCM", "Import"});
//            assertEquals(1, event.getEventTypeCode().size());
//            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ", "e-health-suisse", "PPQ Privacy Policy Query"});
//
//            assertEquals(0, event.getPurposesOfUse().size());
//
//            assertEquals(2, message.getActiveParticipant().size());
//            assertEquals(1, message.getActiveParticipant().get(0).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(0).getRoleIDCode().get(0), new String[]{"110153", "DCM", "Source"});
//            assertEquals(1, message.getActiveParticipant().get(1).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(1).getRoleIDCode().get(0), new String[]{"110152", "DCM", "Destination"});
//
//            assertEquals(2, message.getParticipantObjectIdentification().size());
//
//            for (ParticipantObjectIdentificationType participant : message.getParticipantObjectIdentification()) {
//                assertEquals(2, participant.getParticipantObjectTypeCode().intValue());
//                assertEquals(13, participant.getParticipantObjectTypeCodeRole().intValue());
//            }
//
//            assertEquals("1.2.840.113619.20.2.9.0", message.getParticipantObjectIdentification().get(0).getParticipantObjectID());
//            assertEquals("3644dc70-4dec-11e3-8f96-0800200c9a66", message.getParticipantObjectIdentification().get(1).getParticipantObjectID());
//        }
    }

    @Test
    public void testDeletePolicySuccess() throws Exception {
        testDeletePolicy("success", Xacml20Utils.PPQ_STATUS_SUCCESS, 0, false);
    }

    @Test
    public void testDeletePolicyFailure() throws Exception {
        testDeletePolicy("failure", Xacml20Utils.PPQ_STATUS_FAILURE, 8, false);
    }

    @Test
    public void testDeletePolicyException() throws Exception {
        testDeletePolicy("exception", Xacml20Utils.PPQ_STATUS_FAILURE, 8, true);
    }

    private void testDeletePolicy(String suffix, String statusCode, int outcomeIndicator, boolean expectException) throws Exception {
        try {
            EpdPolicyRepositoryResponse response = (EpdPolicyRepositoryResponse) send(getUri(suffix), loadFile("delete-request.xml"), EpdPolicyRepositoryResponse.class);
            assertEquals(statusCode, response.getStatus());
        } catch (UnknownPolicySetIdFaultMessage exception) {
            if (!expectException) {
                throw exception;
            }
        }

        List messages = getAuditSender().getMessages();
//        assertEquals(2, messages.size());
//
//        for (Object object : messages) {
//            AuditMessage message = ((AuditEventMessage) object).getAuditMessage();
//
//            EventIdentificationType event = message.getEventIdentification();
//            assertEquals("D", event.getEventActionCode());
//            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
//            checkCodeValueType(event.getEventID(), new String[]{"110106", "DCM", "Export"}, new String[]{"110107", "DCM", "Import"});
//            assertEquals(1, event.getEventTypeCode().size());
//            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"PPQ", "e-health-suisse", "PPQ Privacy Policy Query"});
//
//            assertEquals(0, event.getPurposesOfUse().size());
//
//            assertEquals(2, message.getActiveParticipant().size());
//            assertEquals(1, message.getActiveParticipant().get(0).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(0).getRoleIDCode().get(0), new String[]{"110153", "DCM", "Source"});
//            assertEquals(1, message.getActiveParticipant().get(1).getRoleIDCode().size());
//            checkCodeValueType(message.getActiveParticipant().get(1).getRoleIDCode().get(0), new String[]{"110152", "DCM", "Destination"});
//
//            assertEquals(5, message.getParticipantObjectIdentification().size());
//
//            for (int i = 0; i <  5; ++i) {
//                ParticipantObjectIdentificationType participant = message.getParticipantObjectIdentification().get(i);
//                assertEquals(2, participant.getParticipantObjectTypeCode().intValue());
//                assertEquals(13, participant.getParticipantObjectTypeCodeRole().intValue());
//                assertEquals("10a3f268-d9d6-4772-b908-9d852116" + i, participant.getParticipantObjectID());
//            }
//        }
    }

}
