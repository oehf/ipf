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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti67;

import ca.uhn.fhir.rest.gclient.ICriterion;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdValidator;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.validate.BalpAuditEventValidator;

import static ca.uhn.fhir.context.FhirContext.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti67WithBalpAudit extends AbstractTestIti67 {

    private static final String CONTEXT_DESCRIPTOR = "iti-67-balp.xml";

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String REQUEST_ID = "9a41b47c-2b7a-4b1e-9b5b-3f5f7b0f6d21";
    private static final String B3_TRACE_ID = "80f198ee56343ba864fe8b2a57d3eff7";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.clearAuditEvents();
    }

    @Test
    public void testSendManualIti67() {
        var result = sendManually(referencePatientIdentifierParameter());

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        var p = (DocumentReference) result.getEntry().get(0).getResource();
        assertEquals("63ab1c29-4225-11e6-9b33-0050569b0094", p.getIdElement().getIdPart());

        // Check ATNA Audit
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        var auditEvent = auditEvents.get(0);

        assertEquals("rest", auditEvent.getType().getCode());
        assertEquals("RESTful Operation", auditEvent.getType().getDisplay());
        assertEquals("ITI-67", auditEvent.getSubtypeFirstRep().getCode());
        // MHD fixes the subtype as a pattern of system, code and display
        assertEquals("Find Document References", auditEvent.getSubtypeFirstRep().getDisplay());
        assertEquals("E", auditEvent.getAction().toCode());
        assertEquals("0", auditEvent.getOutcome().toCode());
        var sourceRole = findRoleAgentWithCode(auditEvent, "110153");
        assertTrue(sourceRole.isPresent());
        // the requestor flag is taken from the ATNA record rather than assumed; BALP requires the
        // element on the client and server agents but does not fix its value
        assertTrue(sourceRole.get().getRequestor());
        var destinationRole = findRoleAgentWithCode(auditEvent, "110152");
        assertTrue(destinationRole.isPresent());
        assertFalse(destinationRole.get().getRequestor());
        assertEquals(1, auditEvent.getEntity().stream()
            .filter(event -> event.getType().getCode().equals("2") && event.getRole().getCode().equals("24"))
            .map(AuditEvent.AuditEventEntityComponent::getQuery)
            .filter(Objects::nonNull)
            .count());
    }

    @Test
    public void testSendEndpointIti67() {
        sendViaProducer(referencePatientIdentifierParameter());
        assertEquals(2, FhirAuditRepository.getAuditEvents().size());

        var queries = FhirAuditRepository.getAuditEvents().stream()
            .flatMap(event -> event.getEntity().stream()
            .filter(entity -> entity.getType().getCode().equals("2") && entity.getRole().getCode().equals("24"))
            .map(AuditEvent.AuditEventEntityComponent::getQuery)
            .filter(Objects::nonNull))
            .toList();

        // entity.query is a base64Binary element, so what the parser hands back is the raw query --
        // decoding it again here would mean it had been encoded twice on the wire
        var query = new String(queries.get(0), StandardCharsets.UTF_8);
        assertTrue(query.startsWith("patient.identifier=urn:oid:2.16.840.1.113883.3.37.4.1.1.2.1.1|1"));
    }

    @Test
    public void testRequestIdIsAuditedAsTransactionEntity() {
        client.search()
            .forResource(DocumentReference.class)
            .where(referencePatientIdentifierParameter())
            .returnBundle(Bundle.class)
            .encodedXml()
            .withAdditionalHeader(REQUEST_ID_HEADER, REQUEST_ID)
            .execute();

        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        assertEquals(REQUEST_ID, transactionEntityValue(auditEvents.get(0)).orElse(null));
    }

    @Test
    public void testRequestIdCorrelatesBothEndsOfTheTransaction() {
        producerTemplate.requestBodyAndHeader("direct:input",
            new ICriterion<?>[]{referencePatientIdentifierParameter()},
            Constants.HTTP_OUTGOING_HEADERS,
            Map.of(REQUEST_ID_HEADER, List.of(REQUEST_ID)),
            Bundle.class);

        // the Document Consumer audits the header it sent, the Document Responder the one it received
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(2, auditEvents.size());
        assertEquals(List.of(REQUEST_ID, REQUEST_ID), auditEvents.stream()
            .map(auditEvent -> transactionEntityValue(auditEvent).orElse(null))
            .toList());
    }

    @Test
    public void testB3SingleHeaderIsAuditedByItsTraceId() {
        client.search()
            .forResource(DocumentReference.class)
            .where(referencePatientIdentifierParameter())
            .returnBundle(Bundle.class)
            .encodedXml()
            .withAdditionalHeader("b3", B3_TRACE_ID + "-e457b5a2e4d86bd1-1")
            .execute();

        // the span id differs between the two ends of the transaction, only the trace id correlates them
        assertEquals(B3_TRACE_ID,
            transactionEntityValue(FhirAuditRepository.getAuditEvents().get(0)).orElse(null));
    }

    @Test
    public void testB3MultiHeaderIsAuditedByTheSameTraceId() {
        client.search()
            .forResource(DocumentReference.class)
            .where(referencePatientIdentifierParameter())
            .returnBundle(Bundle.class)
            .encodedXml()
            .withAdditionalHeader("X-B3-TraceId", B3_TRACE_ID)
            .withAdditionalHeader("X-B3-SpanId", "e457b5a2e4d86bd1")
            .execute();

        assertEquals(B3_TRACE_ID,
            transactionEntityValue(FhirAuditRepository.getAuditEvents().get(0)).orElse(null));
    }

    /**
     * An access token the audit source cannot look into -- a reference token, or one encrypted to
     * somebody else -- is still recorded, as the OAuth user agent BALP defines for that case. What the
     * record does not carry is the token: it is a credential.
     */
    @Test
    public void testAnOpaqueAccessTokenIsAuditedWithoutItsValue() {
        var opaqueToken = "8f1e2d3c4b5a69788f1e2d3c4b5a6978";
        client.search()
            .forResource(DocumentReference.class)
            .where(referencePatientIdentifierParameter())
            .returnBundle(Bundle.class)
            .encodedXml()
            .withAdditionalHeader("Authorization", "Bearer " + opaqueToken)
            .execute();

        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);
        var userAgent = auditEvent.getAgent().stream()
            .filter(agent -> agent.getType().getCoding().stream().anyMatch(coding ->
                "https://profiles.ihe.net/ITI/BALP/CodeSystem/UserAgentTypes".equals(coding.getSystem())
                    && "UserOauthAgent".equals(coding.getCode())))
            .findFirst();

        assertTrue(userAgent.isPresent(), "the opaque access token was not audited at all");
        assertTrue(userAgent.get().getRequestor());
        assertFalse(forR4().newJsonParser()
                .encodeResourceToString(auditEvent).contains(opaqueToken),
            "the audit record carries the access token");
    }

    @Test
    public void testNoTransactionEntityWithoutRequestId() {
        sendManually(referencePatientIdentifierParameter());
        assertTrue(transactionEntityValue(FhirAuditRepository.getAuditEvents().get(0)).isEmpty());
    }

    /**
     * The entity type is always BALP's {@code XrequestId}, whichever header the id came from: the
     * profile fixes the transaction entity slice. Only the DICOM record distinguishes the propagation
     * format, in its ParticipantObjectIDTypeCode.
     */
    private Optional<String> transactionEntityValue(AuditEvent auditEvent) {
        return auditEvent.getEntity().stream()
            .filter(entity -> entity.hasType() && "XrequestId".equals(entity.getType().getCode()))
            .findFirst()
            .map(entity -> entity.getWhat().getIdentifier().getValue());
    }

    private Optional<AuditEvent.AuditEventAgentComponent> findRoleAgentWithCode(AuditEvent auditEvent, String code) {
        return auditEvent.getAgent().stream()
            .filter(p -> p.getType().getCodingFirstRep().getCode().equals(code))
            .findFirst();
    }


    /**
     * Whatever a test in this class did, the AuditEvents it caused have to conform to the profiles they
     * claim -- checked here rather than per test, so that a new test is covered without having to say so.
     */
    @AfterEach
    public void validateRecordedAuditEvents() {
        BalpAuditEventValidator.sharedInstance(MhdValidator.MHD_PACKAGE_PATH)
            .assertAllConformant(FhirAuditRepository.getAuditEvents());
    }

}
