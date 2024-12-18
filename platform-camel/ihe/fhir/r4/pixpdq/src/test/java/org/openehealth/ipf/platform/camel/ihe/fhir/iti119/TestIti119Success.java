/*
 * Copyright 2024 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti119;

import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.MatchGrade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.marshal.BalpJsonSerializationStrategy;

import static org.junit.jupiter.api.Assertions.*;
import static org.openehealth.ipf.commons.ihe.fhir.iti119.Iti119Constants.COUNT;
import static org.openehealth.ipf.commons.ihe.fhir.iti119.Iti119Constants.RESOURCE;
import static org.openehealth.ipf.commons.ihe.fhir.iti119.MatchGradeEnumInterceptor.MATCH_GRADE_EXTENSION_URL;

/**
 *
 */
public class TestIti119Success extends AbstractTestIti119 {

    private static final String CONTEXT_DESCRIPTOR = "iti-119.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR, false);
        startClient();
    }

    @Test
    public void getConformance() {
        assertConformance("Patient");
    }

    @Test
    public void sendManualPdqmMatch() {

        var p = new Parameters();
        p.addParameter()
            .setName(RESOURCE)
            .setResource(new Patient().addName(new HumanName().setFamily("Test")));
        p.addParameter()
            .setName(COUNT)
            .setValue(new IntegerType(1));
        var result = sendManually(p);

        // printAsXML(result);

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertTrue(result.hasEntry());
        var entry = result.getEntryFirstRep();
        assertEquals(Bundle.SearchEntryMode.MATCH, entry.getSearch().getMode());
        assertEquals(MatchGrade.PROBABLE.toCode(), ((Coding)entry.getSearch().getExtensionByUrl(MATCH_GRADE_EXTENSION_URL).getValue()).getCode());

        var patient = (Patient)result.getEntry().get(0).getResource();
        assertEquals("Test", patient.getName().get(0).getFamily());
        assertEquals("http://localhost:8999/Patient/4711", patient.getId());


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
        assertEquals(FhirEventTypeCode.PatientDemographicsMatch, event.getEventIdentification().getEventTypeCode().get(0));

        // ActiveParticipant Source
        var source = event.getActiveParticipants().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        var destination = event.getActiveParticipants().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/Patient/$match", destination.getUserID());
        assertEquals(AuditUtils.getLocalIPAddress(), destination.getNetworkAccessPointID());

        // Audit Source
        var sourceIdentificationType = event.getAuditSourceIdentification();
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());

        // Query
        var query = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.System, query.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Query, query.getParticipantObjectTypeCodeRole());

        assertEquals(FhirParticipantObjectIdTypeCode.PatientDemographicsMatch, query.getParticipantObjectIDTypeCode());

        var fhir = new BalpJsonSerializationStrategy(serverFhirContext).marshal(event, true);
        // System.out.println(fhir);

    }

    @Test
    public void sendEndpointParametersResource() {
        var p = new Parameters();
        p.addParameter()
            .setName(RESOURCE)
            .setResource(new Patient().addName(new HumanName().setFamily("Test")));
        p.addParameter()
            .setName(COUNT)
            .setValue(new IntegerType(1));
        var result = sendViaProducer(p);
        // printAsXML(result);

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertTrue(result.hasEntry());

        // Check ATNA Audit
        var sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());

        // Check the client-side audit
        var event = sender.getMessages().get(1);

        // ActiveParticipant Source
        var source = event.getActiveParticipants().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals(AuditUtils.getLocalIPAddress(), source.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        var destination = event.getActiveParticipants().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/Patient/$match", destination.getUserID());
        assertEquals("localhost", destination.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.MachineName, destination.getNetworkAccessPointTypeCode());

        // Query Parameters
        var query = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.System, query.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Query, query.getParticipantObjectTypeCodeRole());

        // Audit Source
        var sourceIdentificationType = event.getAuditSourceIdentification();
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());
    }


}
