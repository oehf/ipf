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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareMethanolRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.SimplifiedPublishDocumentReference;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.URN_IETF_RFC_3986;

/**
 *
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti105WithBalpAudit extends FhirTestContainer {

    private static final String CONTEXT_DESCRIPTOR = "iti-105-balp.xml";

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

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.clearAuditEvents();
    }

    @Test
    public void testSendRemoteSimplifiedPublish() {
        var result = client.create()
            .resource(documentReference())
            .encodedXml()
            .execute();
        assertNotNull(result);
        assertTrue(result.getCreated());

        // Check ATNA Audit
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        var auditEvent = auditEvents.get(0);

        assertEquals("110107", auditEvent.getType().getCode());
        assertEquals("ITI-105", auditEvent.getSubtypeFirstRep().getCode());
        assertEquals("C", auditEvent.getAction().toCode());
        assertEquals("Import", auditEvent.getType().getDisplay());
        assertEquals("0", auditEvent.getOutcome().toCode());
    }

    @Test
    public void testSendOverDirectEndpointSimplifiedPublish() {
        producerTemplate.requestBody("direct:input", documentReference(), MethodOutcome.class);
        assertEquals(2, FhirAuditRepository.getAuditEvents().size());
    }

    private static DocumentReference documentReference()  {
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
        var timestamp = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        var reference = new SimplifiedPublishDocumentReference();
        reference.getMeta().setLastUpdated(timestamp);

        reference
            .setUniqueIdIdentifier(URN_IETF_RFC_3986, "urn:oid:129.6.58.92.88336")
            .setContent("text/plain", documentContent)
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
        reference.getContentFirstRep()
            .setFormat(new Coding("urn:oid:1.3.6.1.4.1.19376.1.2.3", "urn:ihe:pcc:handp:2008", null));
        return reference;
    }

}
