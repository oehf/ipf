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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78;

import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.ResourceType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.queue.AbstractMockedAuditMessageQueue;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqPatient;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 *
 */
public class TestIti78Success extends AbstractTestIti78 {

    private static final String CONTEXT_DESCRIPTOR = "iti-78.xml";

    @BeforeClass
    public static void setUpClass()  {
        startServer(CONTEXT_DESCRIPTOR, false);
        startClient();
    }

    @Test
    public void testGetConformance() {
        assertConformance("Patient");
    }

    @Test
    public void testSendManualPdqmWithBirthdayRange() {
        Bundle result = client.search()
                .forResource(PdqPatient.class)
                .where(PdqPatient.BIRTHDATE.exactly().day("2011-12-31"))
                .and(PdqPatient.FAMILY.matches().value("Miller"))
                .returnBundle(Bundle.class)
                .execute();
        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertTrue(result.hasEntry());
    }

    @Test
    public void testSendManualPdqm() {

        Bundle result = sendManually(familyParameters());

        // printAsXML(result);

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertTrue(result.hasEntry());


        PdqPatient p = (PdqPatient)result.getEntry().get(0).getResource();
        assertEquals("Test", p.getName().get(0).getFamily().get(0).getValue());
        assertEquals("http://localhost:8999/Patient/4711", p.getId());


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
        assertEquals(FhirEventTypeCode.MobilePatientDemographicsQuery, event.getEventIdentification().getEventTypeCode().get(0));

        // ActiveParticipant Source
        ActiveParticipantType source = event.getActiveParticipants().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        ActiveParticipantType destination = event.getActiveParticipants().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/Patient", destination.getUserID());
        assertEquals(AuditUtils.getLocalIPAddress(), destination.getNetworkAccessPointID());

        // Audit Source
        AuditSourceIdentificationType sourceIdentificationType = event.getAuditSourceIdentification();
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());

        // Query
        ParticipantObjectIdentificationType patient = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.System, patient.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Query, patient.getParticipantObjectTypeCodeRole());
        assertEquals("http://localhost:8999/Patient?family=Test",
                new String(patient.getParticipantObjectQuery(), StandardCharsets.UTF_8));

        assertEquals(FhirParticipantObjectIdTypeCode.MobilePatientDemographicsQuery, patient.getParticipantObjectIDTypeCode());

    }

    @Test
    public void testGetResource() {
        PdqPatient p = client.read()
                .resource(PdqPatient.class)
                .withId("4711")
                .execute();
        assertEquals("Test", p.getName().get(0).getFamily().get(0).getValue());
        assertEquals(String.format("http://localhost:%d/Patient/4711", DEMO_APP_PORT), p.getId());
        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0);

        // Patient
        ParticipantObjectIdentificationType patient = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.Person, patient.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Patient, patient.getParticipantObjectTypeCodeRole());
        assertEquals("Patient/4711", patient.getParticipantObjectID());
    }


    @Test
    public void testSendEndpointPdqmCriterion() {
        Bundle result = getProducerTemplate().requestBody("direct:input", familyParameters(), Bundle.class);
        // printAsXML(result);

        // Check ATNA Audit
        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
        // FIXME client-side audit message needs ip addresses, target URL and queryString
    }

    @Test
    public void testSendEndpointPdqmString() {
        Bundle result = getProducerTemplate().requestBody("direct:input", "Patient?family=Test", Bundle.class);
        // printAsXML(result);

        // Check ATNA Audit
        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
        // FIXME client-side audit message needs ip addresses, target URL and queryString
    }




}
