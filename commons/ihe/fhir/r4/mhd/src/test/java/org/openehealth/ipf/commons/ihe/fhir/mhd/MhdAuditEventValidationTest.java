/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.mhd;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti105.Iti105AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti105.Iti105ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti105.Iti105ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.validate.BalpAuditEventValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates the AuditEvent each MHD transaction puts on the wire against the StructureDefinition it
 * claims, using the MHD implementation guide package IPF already ships.
 * <p>
 * Where IPF is known not to satisfy a profile yet, the gap is named in the test rather than ignored, so
 * that it is visible here and disappears from here when it is closed. See
 * {@link BalpAuditEventValidator#assertConformantApartFrom(AuditMessage, String...)}.
 *
 * @author Christian Ohr
 */
public class MhdAuditEventValidationTest {

    private static final String CLIENT_URI = "http://localhost:8080/client";
    private static final String SERVER_URI = "http://localhost:8888/fhir";
    private static final String CLIENT_IP = "192.168.0.1";
    private static final String PATIENT_ID = "Patient/a2";

    private static BalpAuditEventValidator validator;

    @BeforeAll
    public static void setUpClass() {
        validator = BalpAuditEventValidator.sharedInstance(MhdValidator.MHD_PACKAGE_PATH);
    }

    /**
     * The two ends of Provide Document Bundle do not record the same event: the Document Source exports
     * the PHI, the Document Recipient imports it. Both are otherwise conformant apart from the audit
     * source, whose invariant this profile states on its own agent slices.
     */
    @Test
    public void testIti65Recipient() {
        validator.assertConformant(iti65(true));
    }

    @Test
    public void testIti65Source() {
        validator.assertConformant(iti65(false));
    }

    @Test
    public void testIti66Responder() {
        validator.assertConformant(iti66(true));
    }

    @Test
    public void testIti66Consumer() {
        validator.assertConformant(iti66(false));
    }

    @Test
    public void testIti67Responder() {
        validator.assertConformant(iti67(true, PATIENT_ID));
    }

    @Test
    public void testIti67Consumer() {
        validator.assertConformant(iti67(false, PATIENT_ID));
    }

    /**
     * A Find Document References that does not name a patient -- searching by author or by status, say.
     * The MHD profile requires a patient entity, so one is recorded as explicitly unknown and the record
     * keeps the profile of its transaction.
     */
    @Test
    public void testIti67ResponderWithoutPatient() {
        assertPatientRecordedAsUnknown(iti67(true, null), MhdProfile.FIND_DOCUMENT_REFERENCES_RESPONDER_AUDIT_PROFILE, "ITI-67");
    }

    @Test
    public void testIti68Responder() {
        validator.assertConformant(iti68(PATIENT_ID));
    }

    /**
     * The ordinary Retrieve Document: it downloads a binary from a URL and names no patient at all. The
     * mandatory patient entity of the MHD profile is therefore recorded as explicitly unknown, which
     * keeps the record on the profile its transaction prescribes instead of dropping it to a weaker one.
     */
    @Test
    public void testIti68ResponderWithoutPatient() {
        assertPatientRecordedAsUnknown(iti68(null), MhdProfile.RETRIEVE_DOCUMENT_RESPONDER_AUDIT_PROFILE, "ITI-68");
    }

    @Test
    public void testIti105Recipient() {
        validator.assertConformant(iti105(true, PATIENT_ID));
    }

    @Test
    public void testIti105Source() {
        validator.assertConformant(iti105(false, PATIENT_ID));
    }

    /**
     * A Simplified Publish of a DocumentReference without a subject: the patient entity the MHD profile
     * requires is recorded as explicitly unknown.
     */
    @Test
    public void testIti105RecipientWithoutPatient() {
        assertPatientRecordedAsUnknown(iti105(true, null), MhdProfile.SIMPLIFIED_PUBLISH_RECIPIENT_AUDIT_PROFILE, "ITI-105");
    }

    // ------------------------------------------------------------------------------------ assertions

    /**
     * Asserts that a transaction whose profile requires a patient, but whose audit message names none,
     * keeps that profile and records the patient as explicitly unknown rather than omitting it.
     *
     * @param auditMessage the audit message of the transaction
     * @param profile      the profile the record is expected to claim
     * @param transaction  the IHE transaction code the record is expected to keep naming
     */
    private void assertPatientRecordedAsUnknown(AuditMessage auditMessage, String profile, String transaction) {
        var auditEvent = validator.toAuditEvent(auditMessage);

        assertEquals(List.of(profile), BalpAuditEventValidator.claimedProfiles(auditEvent),
            "an unknown patient must not cost the record its transaction profile");
        assertTrue(auditEvent.getSubtype().stream().anyMatch(subtype -> transaction.equals(subtype.getCode())),
            "the " + transaction + " subtype is missing");

        var patient = auditEvent.getEntity().stream()
            .filter(entity -> "1".equals(entity.getType().getCode()) && "1".equals(entity.getRole().getCode()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("the mandatory patient entity is missing"));
        var dataAbsentReason = patient.getWhat()
            .getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/data-absent-reason");
        assertNotNull(dataAbsentReason, "the patient reference is neither filled in nor marked absent");
        assertEquals("unknown", dataAbsentReason.getValue().primitiveValue());

        validator.assertConformant(auditMessage);
    }

    // ------------------------------------------------------------------------------ audit messages

    private AuditMessage iti65(boolean serverSide) {
        var auditDataset = new Iti65AuditDataset(serverSide);
        common(auditDataset, serverSide);
        auditDataset.getPatientIds().add(PATIENT_ID);
        auditDataset.setSubmissionSetUuid("urn:uuid:6b1a1b0e-1f6c-4a1e-9b0e-2c9f4b6a1c11");
        return message(serverSide ? new Iti65ServerAuditStrategy() : new Iti65ClientAuditStrategy(), auditDataset);
    }

    private AuditMessage iti66(boolean serverSide) {
        var auditDataset = new FhirQueryAuditDataset(serverSide);
        common(auditDataset, serverSide);
        auditDataset.getPatientIds().add(PATIENT_ID);
        auditDataset.setQueryString("patient.identifier=urn:oid:1.2.3.4|1&status=current");
        return message(new Iti66AuditStrategy(serverSide), auditDataset);
    }

    private AuditMessage iti67(boolean serverSide, String patientId) {
        var auditDataset = new FhirQueryAuditDataset(serverSide);
        common(auditDataset, serverSide);
        if (patientId != null) {
            auditDataset.getPatientIds().add(patientId);
        }
        auditDataset.setQueryString("author.given=John&status=current");
        return message(new Iti67AuditStrategy(serverSide), auditDataset);
    }

    private AuditMessage iti68(String patientId) {
        var auditDataset = new Iti68AuditDataset(true);
        common(auditDataset, true);
        if (patientId != null) {
            auditDataset.getPatientIds().add(patientId);
        }
        auditDataset.setDocumentUniqueId("1.2.3.4.5.6.7.8.9");
        auditDataset.setRepositoryUniqueId("1.2.3.4.5");
        return message(new Iti68ServerAuditStrategy(), auditDataset);
    }

    private AuditMessage iti105(boolean serverSide, String patientId) {
        var auditDataset = new Iti105AuditDataset(serverSide);
        common(auditDataset, serverSide);
        if (patientId != null) {
            auditDataset.getPatientIds().add(patientId);
        }
        auditDataset.setDocumentReferenceId("DocumentReference/1");
        return message(serverSide ? new Iti105ServerAuditStrategy() : new Iti105ClientAuditStrategy(), auditDataset);
    }

    private void common(org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset auditDataset, boolean serverSide) {
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        auditDataset.setSourceUserId(CLIENT_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRemoteAddress(serverSide ? CLIENT_IP : SERVER_URI);
        auditDataset.setLocalAddress(serverSide ? SERVER_URI : CLIENT_IP);
        auditDataset.setRequestId("6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd", ParticipantObjectIdTypeCode.XRequestId);
    }

    private <T extends AuditDataset> AuditMessage message(AuditStrategySupport<T> strategy, T auditDataset) {
        return strategy.makeAuditMessage(auditContext(), auditDataset)[0];
    }

    private AuditContext auditContext() {
        var auditContext = new DefaultAuditContext();
        auditContext.setAuditSourceId("IPF");
        auditContext.setAuditEnterpriseSiteId("mysite");
        return auditContext;
    }

}
