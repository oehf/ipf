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

import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Parameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.iti83.Iti83Constants;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class TestIti83Success extends AbstractTestIti83 {

    private static final String CONTEXT_DESCRIPTOR = "iti-83.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testGetConformance() {
        var conf = client.capabilities().ofType(CapabilityStatement.class).execute();

        assertEquals(1, conf.getRest().size());
        var component = conf.getRest().iterator().next();
        var operation = component.getOperation().iterator().next();
        assertEquals(Iti83Constants.PIXM_OPERATION_NAME.substring(1), operation.getName());

        // printAsXML(conf);
    }

    @Test
    public void testSendManualPixm() {

        var result = sendManuallyOnType(validQueryParameters());

        // printAsXML(result);

        var parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());

        // Check ATNA Audit
        var sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        var event = sender.getMessages().get(0);

        assertEquals(
                EventOutcomeIndicator.Success,
                event.getEventIdentification().getEventOutcomeIndicator());
        assertEquals(
                EventActionCode.Execute,
                event.getEventIdentification().getEventActionCode());

        assertEquals(EventIdCode.Query, event.getEventIdentification().getEventID());
        assertEquals(FhirEventTypeCode.MobilePatientIdentifierCrossReferenceQuery, event.getEventIdentification().getEventTypeCode().get(0));

        // ActiveParticipant Source
        var source = event.getActiveParticipants().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        var destination = event.getActiveParticipants().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/Patient/$ihe-pix", destination.getUserID());
        assertEquals(AuditUtils.getLocalIPAddress(), destination.getNetworkAccessPointID());

        // Audit Source
        var sourceIdentificationType = event.getAuditSourceIdentification();
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());

        // Patient (going in)
        var patientIn = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.Person, patientIn.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Patient, patientIn.getParticipantObjectTypeCodeRole());
        assertEquals("urn:oid:1.2.3.4|0815", patientIn.getParticipantObjectID());

        // Query
        var patient = event.getParticipantObjectIdentifications().get(1);
        assertEquals(ParticipantObjectTypeCode.System, patient.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Query, patient.getParticipantObjectTypeCodeRole());
        assertEquals("http://localhost:8999/Patient/$ihe-pix?sourceIdentifier=urn:oid:1.2.3.4|0815&targetSystem=urn:oid:1.2.3.4.6&_format=xml",
                new String(patient.getParticipantObjectQuery(), StandardCharsets.UTF_8));

        assertEquals(FhirParticipantObjectIdTypeCode.MobilePatientIdentifierCrossReferenceQuery, patient.getParticipantObjectIDTypeCode());

    }

    @Test
    public void testSendManualRead() {
        var result = sendManuallyOnInstance("0815", validTargetSystemParameters());
        var parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());
    }

    @Test
    public void testSendEndpointPixm() {
        var result = producerTemplate.requestBody("direct:input", validQueryParameters(), Parameters.class);

        var parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());

        // Check ATNA Audit
        var sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }

    @Test
    public void testSendEndpointPixmRead() {
        var result = producerTemplate.requestBody("direct:input", validReadParameters(), Parameters.class);

        var parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());

        // Check ATNA Audit
        var sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }

}
