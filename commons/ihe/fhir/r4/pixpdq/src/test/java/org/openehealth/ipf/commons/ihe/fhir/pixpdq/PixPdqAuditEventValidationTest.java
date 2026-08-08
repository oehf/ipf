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
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpConstants;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.validate.BalpAuditEventValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates the AuditEvent each PIXm and PDQm transaction puts on the wire against the StructureDefinition
 * it claims, using the implementation guide packages IPF already ships.
 * <p>
 * All three transactions are profiled on the BALP PatientQuery pattern, whose patient entity is mandatory.
 * A query that identifies a patient claims the transaction profile; one that does not -- a demographics
 * search by name, a $match by candidate attributes -- steps down to the plain BALP query pattern, and both
 * outcomes are checked here.
 *
 * @author Christian Ohr
 */
public class PixPdqAuditEventValidationTest {

    private static final String PIXM_PACKAGE_PATH = "classpath:META-INF/profiles/pixm/v310/ihe.iti.pixm.tgz";
    private static final String PDQM_PACKAGE_PATH = "classpath:META-INF/profiles/pdqm/v320/ihe.iti.pdqm.tgz";

    private static final String CLIENT_URI = "http://localhost:8080/client";
    private static final String SERVER_URI = "http://localhost:8888/fhir";
    private static final String CLIENT_IP = "192.168.0.1";
    /**
     * What the patient id of a PIXm or PDQm audit dataset looks like: a FHIR token, as the search
     * parameter it was taken from renders it.
     */
    private static final String PATIENT_ID = "urn:oid:1.2.3.4|0815";

    private static BalpAuditEventValidator validator;

    @BeforeAll
    public static void setUpClass() {
        validator = new BalpAuditEventValidator(PIXM_PACKAGE_PATH, PDQM_PACKAGE_PATH);
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
     * A demographics query names no patient, so it cannot claim the PDQm profile and is recorded as the
     * BALP query pattern -- still conformant, and still naming the transaction.
     */
    @Test
    public void testIti78SupplierWithoutPatient() {
        assertStepsDownToTheQueryPattern(iti78(true, null), "ITI-78");
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

    @Test
    public void testIti119SupplierWithoutPatient() {
        assertStepsDownToTheQueryPattern(iti119(true, null), "ITI-119");
    }

    // ------------------------------------------------------------------------------------ assertions

    private void assertStepsDownToTheQueryPattern(AuditMessage auditMessage, String transaction) {
        var auditEvent = validator.toAuditEvent(auditMessage);

        assertEquals(List.of(BalpConstants.BALP_QUERY_AUDIT_PROFILE),
            BalpAuditEventValidator.claimedProfiles(auditEvent));
        assertTrue(auditEvent.getSubtype().stream()
                .anyMatch(subtype -> transaction.equals(subtype.getCode())),
            "the " + transaction + " subtype is missing");
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
