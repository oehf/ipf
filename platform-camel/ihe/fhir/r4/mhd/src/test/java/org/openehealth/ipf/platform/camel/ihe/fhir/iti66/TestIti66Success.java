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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti66;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class TestIti66Success extends AbstractTestIti66 {

    private static final String CONTEXT_DESCRIPTOR = "iti-66.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testGetConformance() {
        assertConformance("DocumentManifest");
    }

    @Test
    public void testSendManualIti66() {
        var result = sendManually(manifestPatientIdentifierParameter(), statusParameter());

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        var p = (DocumentManifest) result.getEntry().get(0).getResource();
        assertEquals("9bc72458-49b0-11e6-8a1c-3c1620524153", p.getIdElement().getIdPart());

        // Check ATNA Audit

        var sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        var event = sender.getMessages().get(0);

        // Event
        assertEquals(
                EventOutcomeIndicator.Success,
                event.getEventIdentification().getEventOutcomeIndicator());
        assertEquals(
                EventActionCode.Execute,
                event.getEventIdentification().getEventActionCode());

        assertEquals(EventIdCode.Query, event.getEventIdentification().getEventID());
        assertEquals(FhirEventTypeCode.MobileDocumentManifestQuery, event.getEventIdentification().getEventTypeCode().get(0));

        // ActiveParticipant Source
        var source = event.getActiveParticipants().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        var destination = event.getActiveParticipants().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/DocumentManifest", destination.getUserID());
        assertEquals(AuditUtils.getLocalIPAddress(), destination.getNetworkAccessPointID());

        // Audit Source
        var sourceIdentificationType = event.getAuditSourceIdentification();
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());

        // Patient
        var patient = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.Person, patient.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Patient, patient.getParticipantObjectTypeCodeRole());
        assertEquals("urn:oid:2.16.840.1.113883.3.37.4.1.1.2.1.1|1", patient.getParticipantObjectID());

        // Query Parameters
        var query = event.getParticipantObjectIdentifications().get(1);
        assertEquals(ParticipantObjectTypeCode.System, query.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Query, query.getParticipantObjectTypeCodeRole());
        assertEquals("http://localhost:8999/DocumentManifest?patient.identifier=urn:oid:2.16.840.1.113883.3.37.4.1.1.2.1.1|1&status=active&_format=xml",
                new String(query.getParticipantObjectQuery(), StandardCharsets.UTF_8));

        assertEquals(FhirParticipantObjectIdTypeCode.MobileDocumentManifestQuery, query.getParticipantObjectIDTypeCode());
    }

    @Test
    public void testSendIti66WithPatientReference() {
        var result = sendManually(manifestPatientReferenceParameter(), statusParameter());
        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        var p = (DocumentManifest) result.getEntry().get(0).getResource();
        assertEquals("9bc72458-49b0-11e6-8a1c-3c1620524153", p.getIdElement().getIdPart());
    }

    @Test
    public void testGetResource() {
        var p = client.read()
                .resource(DocumentManifest.class)
                .withId("9bc72458-49b0-11e6-8a1c-3c1620524153")
                .execute();
        assertEquals(String.format("http://localhost:%d/DocumentManifest/9bc72458-49b0-11e6-8a1c-3c1620524153", DEMO_APP_PORT), p.getId());
    }


}
