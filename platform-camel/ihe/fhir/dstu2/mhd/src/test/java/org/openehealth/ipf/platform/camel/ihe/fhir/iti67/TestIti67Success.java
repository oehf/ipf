/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti67;

import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.DocumentReference;
import org.hl7.fhir.instance.model.ResourceType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.audit.queue.AbstractMockedAuditMessageQueue;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;

import javax.servlet.ServletException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.*;

/**
 *
 */
public class TestIti67Success extends AbstractTestIti67 {

    private static final String CONTEXT_DESCRIPTOR = "iti-67.xml";

    @BeforeClass
    public static void setUpClass() throws ServletException {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testGetConformance() {
        assertConformance("DocumentReference");
    }

    @Test
    public void testSendManualIti67() {

        Bundle result = sendManually(referencePatientIdentifierParameter());

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        DocumentReference p = (DocumentReference) result.getEntry().get(0).getResource();
        assertEquals("63ab1c29-4225-11e6-9b33-0050569b0094", p.getIdElement().getIdPart());

        // Check ATNA Audit

        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0);

        // Event
        assertEquals(
                EventOutcomeIndicator.Success,
                event.getEventIdentification().getEventOutcomeIndicator());
        assertEquals(
                EventActionCode.Execute,
                event.getEventIdentification().getEventActionCode());
        assertEquals(EventIdCode.Query, event.getEventIdentification().getEventID());
        assertEquals(FhirEventTypeCode.MobileDocumentReferenceQuery, event.getEventIdentification().getEventTypeCode().get(0));


        // ActiveParticipant Source
        ActiveParticipantType source = event.getActiveParticipants().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        ActiveParticipantType destination = event.getActiveParticipants().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/DocumentReference", destination.getUserID());
        assertEquals(AuditUtils.getLocalIPAddress(), destination.getNetworkAccessPointID());

        // Audit Source
        AuditSourceIdentificationType sourceIdentificationType = event.getAuditSourceIdentification();
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());

        // Patient
        ParticipantObjectIdentificationType patient = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.Person, patient.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Patient, patient.getParticipantObjectTypeCodeRole());
        assertEquals("urn:oid:2.16.840.1.113883.3.37.4.1.1.2.1.1|1", patient.getParticipantObjectID());

        // Query Parameters
        ParticipantObjectIdentificationType query = event.getParticipantObjectIdentifications().get(1);
        assertEquals(ParticipantObjectTypeCode.System, query.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Query, query.getParticipantObjectTypeCodeRole());
        assertEquals("http://localhost:8999/DocumentReference?patient.identifier=urn%3Aoid%3A2.16.840.1.113883.3.37.4.1.1.2.1.1%7C1",
                new String(Base64.getDecoder().decode(query.getParticipantObjectQuery()), StandardCharsets.UTF_8));

        assertEquals(FhirParticipantObjectIdTypeCode.MobileDocumentReferenceQuery, query.getParticipantObjectIDTypeCode());

    }

    @Test
    public void testSendIti67WithPatientReference() {
        Bundle result = sendManually(referencePatientReferenceParameter());
        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        DocumentReference p = (DocumentReference) result.getEntry().get(0).getResource();
        assertEquals("63ab1c29-4225-11e6-9b33-0050569b0094", p.getIdElement().getIdPart());
    }

    @Test
    public void testGetResource() {
        DocumentReference p = client.read()
                .resource(DocumentReference.class)
                .withId("63ab1c29-4225-11e6-9b33-0050569b0094")
                .execute();
        assertEquals(String.format("http://localhost:%d/DocumentReference/63ab1c29-4225-11e6-9b33-0050569b0094", DEMO_APP_PORT), p.getId());
    }


}
