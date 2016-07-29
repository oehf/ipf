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
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.ResourceType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.atna.MockedSender;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomIHETransactionEventTypeCodes;
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqPatient;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ActiveParticipantCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ActiveParticipantType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditMessage;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditSourceIdentificationType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ParticipantObjectIdentificationType;

import javax.servlet.ServletException;

import static org.junit.Assert.*;

/**
 *
 */
public class TestIti78Success extends AbstractTestIti78 {

    private static final String CONTEXT_DESCRIPTOR = "iti-78.xml";

    @BeforeClass
    public static void setUpClass() throws ServletException {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testGetConformance() {
        Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();

        assertEquals(1, conf.getRest().size());
        Conformance.ConformanceRestComponent component = conf.getRest().iterator().next();
        Conformance.ConformanceRestResourceComponent resource = component.getResource().get(1);
        assertEquals("Patient", resource.getType());

        // printAsXML(conf);
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

        MockedSender sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0).getAuditMessage();

        // Event
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
        CodedValueType expectedEventTypeCode = new CustomIHETransactionEventTypeCodes.PDQMQuery();
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
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/Patient", destination.getUserID());
        assertEquals("localhost", destination.getNetworkAccessPointID());

        // Audit Source
        AuditSourceIdentificationType sourceIdentificationType = event.getAuditSourceIdentification().get(0);
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());

        // Query Parameters
        ParticipantObjectIdentificationType poit = event.getParticipantObjectIdentification().get(0);
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.SYSTEM.getCode(), poit.getParticipantObjectTypeCode());
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.QUERY.getCode(), poit.getParticipantObjectTypeCodeRole());
        assertEquals("http://localhost:8999/Patient?family=Test", new String(poit.getParticipantObjectQuery()));

        CodedValueType poitTypeCode = poit.getParticipantObjectIDTypeCode();
        assertEquals("ITI-78", poitTypeCode.getCode());
        assertEquals("IHE Transactions", poitTypeCode.getCodeSystemName());
        assertEquals("Mobile Patient Demographics Query", poitTypeCode.getOriginalText());
        assertEquals("MobilePatientDemographicsQuery", poit.getParticipantObjectID());
    }

    @Test
    public void testGetResource() {
        PdqPatient p = client.read()
                .resource(PdqPatient.class)
                .withId("4711")
                .execute();
        assertEquals("Test", p.getName().get(0).getFamily().get(0).getValue());
        assertEquals(String.format("http://localhost:%d/Patient/4711", DEMO_APP_PORT), p.getId());
        MockedSender sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0).getAuditMessage();

        // Patient
        ParticipantObjectIdentificationType poit = event.getParticipantObjectIdentification().get(0);
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.PERSON.getCode(), poit.getParticipantObjectTypeCode());
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PATIENT.getCode(), poit.getParticipantObjectTypeCodeRole());
        assertEquals("Patient/4711", new String(poit.getParticipantObjectID()));
    }


    @Test
    public void testSendEndpointPdqmCriterion() {
        Bundle result = getProducerTemplate().requestBody("direct:input", familyParameters(), Bundle.class);
        // printAsXML(result);

        // Check ATNA Audit
        MockedSender sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
        // FIXME client-side audit message needs ip addresses, target URL and queryString
    }

    @Test
    public void testSendEndpointPdqmString() {
        Bundle result = getProducerTemplate().requestBody("direct:input", "Patient?family=Test", Bundle.class);
        // printAsXML(result);

        // Check ATNA Audit
        MockedSender sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
        // FIXME client-side audit message needs ip addresses, target URL and queryString
    }




}
