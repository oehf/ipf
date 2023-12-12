/*
 * Copyright 2023 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti105;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.NetworkAccessPointTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareMethanolRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.URN_IETF_RFC_3986;
import static org.openehealth.ipf.commons.ihe.fhir.iti105.Iti105Validator.ITI105_PROFILE;

/**
 *
 */
public class TestIti105Success extends FhirTestContainer {

    private static final String CONTEXT_DESCRIPTOR = "iti-105.xml";

    private static final String RESOURCE_NAME = ResourceType.DocumentReference.name();

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    public static void startServer(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");

        var loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setLogRequestSummary(false);
        loggingInterceptor.setLogRequestBody(true);
        loggingInterceptor.setLogResponseBody(true);
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT), fhirContext -> {
            var clientFactory = new SslAwareMethanolRestfulClientFactory(fhirContext);
            clientFactory.setAsync(true);
            fhirContext.setRestfulClientFactory(clientFactory);
        }).registerInterceptor(loggingInterceptor);
    }

    @Test
    public void testGetConformance() {
        var conf = client.capabilities().ofType(CapabilityStatement.class).execute();
        var component = conf.getRest().iterator().next();
        assertTrue(component.getResource().stream().anyMatch(r -> r.getType().equals(RESOURCE_NAME)));
    }

    @Test
    public void testSendRemoteSimplifiedPublish() throws Exception {
        var result = client.create()
            .resource(documentReference())
            .encodedXml()
            .execute();
        assertNotNull(result);
        assertTrue(result.getCreated());

        // Check ATNA Audit
        var sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        var event = sender.getMessages().get(0);

        // Event
        assertEquals(
                EventOutcomeIndicator.Success,
                event.getEventIdentification().getEventOutcomeIndicator());
        assertEquals(
                EventActionCode.Create,
                event.getEventIdentification().getEventActionCode());

        assertEquals(EventIdCode.Import, event.getEventIdentification().getEventID());
        assertEquals(FhirEventTypeCode.SimplifiedPublish, event.getEventIdentification().getEventTypeCode().get(0));

        // ActiveParticipant Source
        var source = event.getActiveParticipants().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        var destination = event.getActiveParticipants().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/" + RESOURCE_NAME, destination.getUserID());
        assertEquals(AuditUtils.getLocalIPAddress(), destination.getNetworkAccessPointID());

        // Patient
        var patient = event.getParticipantObjectIdentifications().get(0);
        assertEquals(ParticipantObjectTypeCode.Person, patient.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Patient, patient.getParticipantObjectTypeCodeRole());
        assertEquals(ParticipantObjectIdTypeCode.PatientNumber, patient.getParticipantObjectIDTypeCode());
        assertEquals("Patient/123", patient.getParticipantObjectID());

        // SubmissionSet
        var poit = event.getParticipantObjectIdentifications().get(1);
        assertEquals(ParticipantObjectTypeCode.System, poit.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Job, poit.getParticipantObjectTypeCodeRole());

        // No real instructions how this should look like, so for now we take the XDS stuff
        var poitTypeCode = poit.getParticipantObjectIDTypeCode();
        assertEquals("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", poitTypeCode.getCode());
        assertEquals("IHE XDS Metadata", poitTypeCode.getCodeSystemName());
    }

    @Test
    public void testSendOverDirectEndpointSimplifiedPublish() throws Exception {
        producerTemplate.requestBody("direct:input", documentReference(), MethodOutcome.class);
        var sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }

    private static DocumentReference documentReference() throws NoSuchAlgorithmException {
        var documentContent = "Hello IHE World".getBytes();
        var practitioner = new Practitioner();
        practitioner.setId("987");
        practitioner.addName(new HumanName()
            .setFamily("Soo")
            .setGiven(Collections.singletonList(new StringType("Yun"))));

        var patient = new Patient();
        patient.setId("123");
        patient.getName().add(new HumanName()
            .setFamily("Foo")
            .setGiven(Collections.singletonList(new StringType("Barbara"))));
        var reference = new DocumentReference();
        reference.getMeta().addProfile(ITI105_PROFILE);
        var timestamp = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        reference.getMeta().setLastUpdated(timestamp);

        reference
            .setMasterIdentifier(
                new Identifier()
                    .setSystem(URN_IETF_RFC_3986)
                    .setValue("urn:oid:129.6.58.92.88336"))
            .addIdentifier(new Identifier()
                .setSystem(URN_IETF_RFC_3986)
                .setValue("urn:oid:129.6.58.92.88336"))
            .setDate(timestamp) // creation of document reference resource
            .setDescription("Physical")
            .setSubject(new Reference(patient))
            .addAuthor(new Reference(practitioner))
            .setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
        reference.getText().setStatus(Narrative.NarrativeStatus.EMPTY);
        reference.getText().setDivAsString("<div>empty</div>");
        reference.getType().addCoding()
            .setSystem("http://ihe.net/connectathon/classCodes")
            .setCode("History and Physical")
            .setDisplay("History and Physical");
        reference.addContent()
            .setAttachment(
                new Attachment()
                    .setContentType("text/plain")
                    .setLanguage("en/us")
                    .setSize(documentContent.length)
                    .setHash(MessageDigest.getInstance("SHA-1").digest(documentContent))
                    .setUrl("urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bb"))
            .setFormat(new Coding("urn:oid:1.3.6.1.4.1.19376.1.2.3", "urn:ihe:pcc:handp:2008", null));
        return reference;
    }

}
