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
package org.openehealth.ipf.commons.ihe.fhir.pixpdq;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Patient;
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
import org.openehealth.ipf.commons.ihe.fhir.iti119.Iti119ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti119.Iti119ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti78.Iti78ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti78.Iti78ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti83.Iti83AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.validate.BalpAuditEventValidator;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates the AuditEvent each PIXm and PDQm transaction puts on the wire against the StructureDefinition
 * it claims, using the implementation guide packages IPF already ships.
 * <p>
 * PIXm [ITI-83] is profiled on the BALP PatientQuery pattern, whose patient entity is mandatory. The two
 * PDQm transactions are profiled on the plain BALP Query pattern instead, where the patient entity is
 * optional and used "when one patient is explicitly identified in the query parameters" -- so a
 * demographics search by name, or a $match by candidate attributes, keeps the PDQm profile and simply
 * records no patient. Both outcomes are checked here.
 *
 * @author Christian Ohr
 */
public class PixPdqAuditEventValidationTest {

    private static final String CLIENT_URI = "http://localhost:8080/client";
    private static final String SERVER_URI = "http://localhost:8888/fhir";
    private static final String CLIENT_IP = "192.168.0.1";
    /**
     * What the patient id of a PIXm or PDQm audit dataset looks like: a FHIR token, as the search
     * parameter it was taken from renders it.
     */
    private static final String PATIENT_ID = "urn:oid:1.2.3.4|0815";

    private static final FhirContext FHIR_CONTEXT = FhirContext.forR4Cached();

    private static BalpAuditEventValidator validator;

    @BeforeAll
    public static void setUpClass() {
        validator = BalpAuditEventValidator.sharedInstance(
            PixmValidator.PIXM_PACKAGE_PATH, PdqmValidator.PDQM_PACKAGE_PATH);
    }

    // -------------------------------------------------------------------------------- PIXm [ITI-83]

    @Test
    public void testIti83Manager() {
        validator.assertConformant(iti83(true));
    }

    @Test
    public void testIti83Consumer() {
        validator.assertConformant(iti83(false));
    }

    // -------------------------------------------------------------------------------- PDQm [ITI-78]

    @Test
    public void testIti78Supplier() {
        validator.assertConformant(iti78(true, PATIENT_ID));
    }

    @Test
    public void testIti78Consumer() {
        validator.assertConformant(iti78(false, PATIENT_ID));
    }

    /**
     * A demographics query names no patient. The PDQm profile allows that -- its patient entity is
     * optional -- so the record keeps the profile and just leaves the entity out.
     */
    @Test
    public void testIti78SupplierWithoutPatient() {
        assertConformantWithoutPatient(iti78(true, null), PdqmProfile.PDQM_SUPPLIER_AUDIT_PROFILE);
    }

    @Test
    public void testIti78ConsumerWithoutPatient() {
        assertConformantWithoutPatient(iti78(false, null), PdqmProfile.PDQM_CONSUMER_AUDIT_PROFILE);
    }

    // ------------------------------------------------------------------------------- PDQm [ITI-119]

    @Test
    public void testIti119Supplier() {
        validator.assertConformant(iti119(true, PATIENT_ID));
    }

    @Test
    public void testIti119Consumer() {
        validator.assertConformant(iti119(false, PATIENT_ID));
    }

    /**
     * A $match by candidate demographics resolves to no single patient, which the PDQm profile permits --
     * the IHE consumer example of ITI-119 is exactly such a record.
     */
    @Test
    public void testIti119SupplierWithoutPatient() {
        assertConformantWithoutPatient(iti119(true, null), PdqmProfile.PDQM_MATCH_SUPPLIER_AUDIT_PROFILE);
    }

    @Test
    public void testIti119ConsumerWithoutPatient() {
        assertConformantWithoutPatient(iti119(false, null), PdqmProfile.PDQM_MATCH_CONSUMER_AUDIT_PROFILE);
    }

    /**
     * The ITI-119 query entity carries the $match request body, since the transaction has no query
     * string of its own.
     */
    @Test
    public void testIti119RecordsTheMatchBodyAsQuery() {
        var auditDataset = queryAuditDataset(true, null);
        auditDataset.setFhirContext(FHIR_CONTEXT);
        var patient = new Patient();
        patient.addName().setFamily("Test").addGiven("John");
        new Iti119ServerAuditStrategy().enrichAuditDatasetFromRequest(auditDataset, patient, Map.of());

        var auditEvent = validator.toAuditEvent(
            new Iti119ServerAuditStrategy().makeAuditMessage(auditContext(), auditDataset)[0]);

        var query = auditEvent.getEntity().stream()
            .filter(AuditEvent.AuditEventEntityComponent::hasQuery)
            .findFirst()
            .orElseThrow(() -> new AssertionError("the mandatory query entity is missing"));
        var body = new String(query.getQuery(), StandardCharsets.UTF_8);
        assertTrue(body.contains("\"resourceType\":\"Parameters\""), "not the $match body: " + body);
        assertTrue(body.contains("Test"), "the matched demographics are missing: " + body);
    }

    // ------------------------------------------------------------------------------------ assertions

    private void assertConformantWithoutPatient(AuditMessage auditMessage, String profile) {
        var auditEvent = validator.toAuditEvent(auditMessage);

        assertEquals(List.of(profile), BalpAuditEventValidator.claimedProfiles(auditEvent),
            "a PDQm record without a patient must keep its transaction profile");
        assertTrue(auditEvent.getEntity().stream()
                .noneMatch(entity -> entity.hasRole() && "1".equals(entity.getRole().getCode())),
            "there is no patient, so no patient entity may be recorded");
        validator.assertConformant(auditMessage);
    }

    // ------------------------------------------------------------------------------ audit messages

    private AuditMessage iti83(boolean serverSide) {
        var auditDataset = queryAuditDataset(serverSide, PATIENT_ID);
        auditDataset.setQueryString("sourceIdentifier=urn:oid:1.2.3.4|0815&targetSystem=urn:oid:1.2.3.5");
        return message(new Iti83AuditStrategy(serverSide), auditDataset);
    }

    private AuditMessage iti78(boolean serverSide, String patientId) {
        var auditDataset = queryAuditDataset(serverSide, patientId);
        auditDataset.setQueryString(patientId != null ?
            "identifier=urn:oid:1.2.3.4|0815" :
            "family=Test&given=John");
        return message(serverSide ? new Iti78ServerAuditStrategy() : new Iti78ClientAuditStrategy(), auditDataset);
    }

    private AuditMessage iti119(boolean serverSide, String patientId) {
        var auditDataset = queryAuditDataset(serverSide, patientId);
        auditDataset.setQueryString("$match");
        return message(serverSide ? new Iti119ServerAuditStrategy() : new Iti119ClientAuditStrategy(), auditDataset);
    }

    private FhirQueryAuditDataset queryAuditDataset(boolean serverSide, String patientId) {
        var auditDataset = new FhirQueryAuditDataset(serverSide);
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        auditDataset.setSourceUserId(CLIENT_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRemoteAddress(serverSide ? CLIENT_IP : SERVER_URI);
        auditDataset.setLocalAddress(serverSide ? SERVER_URI : CLIENT_IP);
        auditDataset.setRequestId("6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd", ParticipantObjectIdTypeCode.XRequestId);
        if (patientId != null) {
            auditDataset.getPatientIds().add(patientId);
        }
        return auditDataset;
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
