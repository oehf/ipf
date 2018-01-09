/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.Identifier;
import org.hl7.fhir.instance.model.Parameters;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.atna.MockedAuditMessageQueue;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomIHETransactionEventTypeCodes;
import org.openehealth.ipf.commons.ihe.fhir.iti83.Iti83Constants;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ActiveParticipantCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ActiveParticipantType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditMessage;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ParticipantObjectIdentificationType;

import javax.servlet.ServletException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class TestIti83Success extends AbstractTestIti83 {

    private static final String CONTEXT_DESCRIPTOR = "iti-83.xml";

    @BeforeClass
    public static void setUpClass() throws ServletException {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testGetConformance() {
        Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();

        assertEquals(1, conf.getRest().size());
        Conformance.ConformanceRestComponent component = conf.getRest().iterator().next();
        Conformance.ConformanceRestOperationComponent operation = component.getOperation().iterator().next();
        assertEquals(Iti83Constants.PIXM_OPERATION_NAME, operation.getName());

        // printAsXML(conf);
    }

    @Test
    public void testSendManualPixm() {

        Parameters result = sendManuallyOnType(validQueryParameters());

        // printAsXML(result);

        Parameters.ParametersParameterComponent parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());

        // Check ATNA Audit
        MockedAuditMessageQueue sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0).getAuditMessage();
        assertEquals(
                RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS.getCode().intValue(),
                event.getEventIdentification().getEventOutcomeIndicator());
        assertEquals(
                RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE.getCode(),
                event.getEventIdentification().getEventActionCode());
        CodedValueType eventId = event.getEventIdentification().getEventID();
        CodedValueType expectedEventId = new DICOMEventIdCodes.Query();
        assertEquals(expectedEventId.getCode(), eventId.getCode());
        assertEquals(expectedEventId.getCodeSystemName(), eventId.getCodeSystemName());
        assertEquals(expectedEventId.getOriginalText(), eventId.getOriginalText());
        CodedValueType eventTypeCode = event.getEventIdentification().getEventTypeCode().get(0);
        CodedValueType expectedEventTypeCode = new CustomIHETransactionEventTypeCodes.PIXMQuery();
        assertEquals(expectedEventTypeCode.getCode(), eventTypeCode.getCode());
        assertEquals(expectedEventTypeCode.getCodeSystemName(), eventTypeCode.getCodeSystemName());
        assertEquals(expectedEventTypeCode.getOriginalText(), eventTypeCode.getOriginalText());

        // ActiveParticipant Source
        ActiveParticipantType source = event.getActiveParticipant().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(RFC3881ActiveParticipantCodes.RFC3881NetworkAccessPointTypeCodes.IP_ADDRESS.getCode(), source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        ActiveParticipantType destination = event.getActiveParticipant().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/Patient/$ihe-pix", destination.getUserID());
        assertEquals("localhost", destination.getNetworkAccessPointID());

        // Patient (going in)
        ParticipantObjectIdentificationType patientIn = event.getParticipantObjectIdentification().get(0);
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.PERSON.getCode(), patientIn.getParticipantObjectTypeCode());
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PATIENT.getCode(), patientIn.getParticipantObjectTypeCodeRole());
        assertEquals("urn:oid:1.2.3.4|0815", new String(patientIn.getParticipantObjectID()));

        // Patient (going out)
//        ParticipantObjectIdentificationType patientOut = event.getParticipantObjectIdentification().get(1);
//        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.PERSON.getCode(), patientOut.getParticipantObjectTypeCode());
//        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PATIENT.getCode(), patientOut.getParticipantObjectTypeCodeRole());
//        assertEquals("http://org.openehealth/ipf/commons/ihe/fhir/2|4711", new String(patientOut.getParticipantObjectID()));

        ParticipantObjectIdentificationType query = event.getParticipantObjectIdentification().get(1);
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.SYSTEM.getCode(), query.getParticipantObjectTypeCode());
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.QUERY.getCode(), query.getParticipantObjectTypeCodeRole());
        assertEquals("http://localhost:8999/Patient/$ihe-pix?sourceIdentifier=urn%3Aoid%3A1.2.3.4%7C0815&targetSystem=urn%3Aoid%3A1.2.3.4.6", new String(query.getParticipantObjectQuery()));
        CodedValueType poitTypeCode = query.getParticipantObjectIDTypeCode();
        assertEquals("ITI-83", poitTypeCode.getCode());
        assertEquals("IHE Transactions", poitTypeCode.getCodeSystemName());
        assertEquals("Mobile Patient Identifier Cross-reference Query", poitTypeCode.getOriginalText());
        assertEquals("PIXmQuery", query.getParticipantObjectID());
    }

    @Test
    public void testSendManualRead() {
        Parameters result = sendManuallyOnInstance("0815", validTargetSystemParameters());
        Parameters.ParametersParameterComponent parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());
    }

    @Test
    public void testSendEndpointPixm() {
        Parameters result = getProducerTemplate().requestBody("direct:input", validQueryParameters(), Parameters.class);

        Parameters.ParametersParameterComponent parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());

        // Check ATNA Audit
        MockedAuditMessageQueue sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }

    @Test
    public void testSendEndpointPixmRead() {
        Parameters result = getProducerTemplate().requestBody("direct:input", validReadParameters(), Parameters.class);

        Parameters.ParametersParameterComponent parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());

        // Check ATNA Audit
        MockedAuditMessageQueue sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }

}
