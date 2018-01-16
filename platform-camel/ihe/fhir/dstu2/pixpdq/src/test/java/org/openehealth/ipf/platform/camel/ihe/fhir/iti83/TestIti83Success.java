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
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AbstractMockedAuditMessageQueue;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCodes;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCodes;
import org.openehealth.ipf.commons.ihe.fhir.iti83.Iti83Constants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.*;

/**
 *
 */
public class TestIti83Success extends AbstractTestIti83 {

    private static final String CONTEXT_DESCRIPTOR = "iti-83.xml";

    @BeforeClass
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testGetConformance() {
        Conformance conf = client.capabilities().ofType(Conformance.class).execute();

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
        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0);

        assertEquals(
                EventOutcomeIndicator.Success,
                event.getEventIdentification().getEventOutcomeIndicator());
        assertEquals(
                EventActionCode.Execute,
                event.getEventIdentification().getEventActionCode());

        assertEquals(EventIdCode.Query, event.getEventIdentification().getEventID());
        assertEquals(FhirEventTypeCodes.MobilePatientIdentifierCrossReferenceQuery, event.getEventIdentification().getEventTypeCode().get(0));

        // ActiveParticipant Source
        ActiveParticipantType source = event.getActiveParticipants().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        ActiveParticipantType destination = event.getActiveParticipants().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/Patient/$ihe-pix", destination.getUserID());
        assertEquals(AuditUtils.getLocalIPAddress(), destination.getNetworkAccessPointID());

        // Audit Source
        AuditSourceIdentificationType sourceIdentificationType = event.getAuditSourceIdentification();
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());

        // Patient (going in)
        ParticipantObjectIdentificationType patientIn = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.Person, patientIn.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Patient, patientIn.getParticipantObjectTypeCodeRole());
        assertEquals("urn:oid:1.2.3.4|0815", new String(patientIn.getParticipantObjectID()));

        // Query
        ParticipantObjectIdentificationType patient = event.getParticipantObjectIdentifications().get(1);
        assertEquals(ParticipantObjectTypeCode.System, patient.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Query, patient.getParticipantObjectTypeCodeRole());
        assertEquals("http://localhost:8999/Patient/$ihe-pix?sourceIdentifier=urn%3Aoid%3A1.2.3.4%7C0815&targetSystem=urn%3Aoid%3A1.2.3.4.6",
                new String(Base64.getDecoder().decode(patient.getParticipantObjectQuery()), StandardCharsets.UTF_8));

        assertEquals(FhirParticipantObjectIdTypeCodes.MobilePatientIdentifierCrossReferenceQuery, patient.getParticipantObjectIDTypeCode());

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
        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }

    @Test
    public void testSendEndpointPixmRead() {
        Parameters result = getProducerTemplate().requestBody("direct:input", validReadParameters(), Parameters.class);

        Parameters.ParametersParameterComponent parameter = result.getParameter().iterator().next();
        assertEquals(ResponseCase.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());

        // Check ATNA Audit
        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }

}
