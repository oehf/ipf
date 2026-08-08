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
package org.openehealth.ipf.commons.ihe.fhir.support.audit.model;

import org.hl7.fhir.r4.model.AuditEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.NetworkAccessPointTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType;
import org.openehealth.ipf.commons.audit.model.EventIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.validate.BalpAuditEventValidator;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Every BALP pattern must be able to convert an ATNA audit record of its own shape into a record that
 * conforms to the StructureDefinition it claims.
 * <p>
 * The patterns that no IHE transaction is currently profiled on -- update, delete and the two privacy
 * disclosure ones -- are covered here precisely because nothing else exercises them: {@code initialize}
 * is called <em>instead of</em> the generic translation, so a pattern that filled itself in incompletely
 * would emit a hollow record, and it would only be noticed by whoever wires the next transaction to it.
 *
 * @author Christian Ohr
 */
public class BalpPatternSelfInitializationTest {

    private static final String CLIENT_URI = "http://localhost:8080/client";
    private static final String SERVER_URI = "http://localhost:8888/fhir";
    private static final String CLIENT_IP = "192.168.0.1";
    private static final String PATIENT_ID = "urn:oid:1.2.3.4|0815";
    private static final String RESOURCE_ID = "Observation/1";

    private static BalpAuditEventValidator validator;

    @BeforeAll
    public static void setUpClass() {
        validator = BalpAuditEventValidator.sharedInstance();
    }

    @Test
    public void testUpdate() {
        assertSelfInitializes(UpdateAuditEvent::new, EventActionCode.Update,
            BalpConstants.BALP_UPDATE_AUDIT_PROFILE, false);
    }

    @Test
    public void testPatientUpdate() {
        assertSelfInitializes(PatientUpdateAuditEvent::new, EventActionCode.Update,
            BalpConstants.BALP_PATIENT_UPDATE_AUDIT_PROFILE, true);
    }

    @Test
    public void testDelete() {
        assertSelfInitializes(DeleteAuditEvent::new, EventActionCode.Delete,
            BalpConstants.BALP_DELETE_AUDIT_PROFILE, false);
    }

    @Test
    public void testPatientDelete() {
        assertSelfInitializes(PatientDeleteAuditEvent::new, EventActionCode.Delete,
            BalpConstants.BALP_PATIENT_DELETE_AUDIT_PROFILE, true);
    }

    @Test
    public void testExport() {
        assertSelfInitializes(ExportAuditEvent::new, EventActionCode.Read,
            BalpConstants.BALP_EXPORT_AUDIT_PROFILE, true);
    }

    @Test
    public void testImport() {
        assertSelfInitializes(ImportAuditEvent::new, EventActionCode.Create,
            BalpConstants.BALP_IMPORT_AUDIT_PROFILE, true);
    }

    /**
     * A pattern whose profile requires a patient records one as explicitly unknown when the audit record
     * names none, rather than emitting a record that does not satisfy its own profile.
     */
    @Test
    public void testAPatternRequiringAPatientRecordsAnUnknownOne() {
        var auditEvent = initialized(new ExportAuditEvent(), auditMessage(EventActionCode.Read, null));

        var patient = auditEvent.getEntity().stream()
            .filter(entity -> "1".equals(entity.getType().getCode()) && "1".equals(entity.getRole().getCode()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("the mandatory patient entity is missing"));
        assertEquals("unknown", patient.getWhat()
            .getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/data-absent-reason")
            .getValue().primitiveValue());
        assertTrue(BalpAuditEventValidator.errors(validator.validate(auditEvent)).isEmpty());
    }

    // ------------------------------------------------------------------------------------ assertions

    private void assertSelfInitializes(Supplier<BalpAuditEvent> pattern,
                                       EventActionCode action,
                                       String profile,
                                       boolean withPatient) {
        var auditEvent = initialized(pattern.get(), auditMessage(action, withPatient ? PATIENT_ID : null));

        assertEquals(List.of(profile), BalpAuditEventValidator.claimedProfiles(auditEvent));

        // the parts every pattern takes from the audit record -- what an empty initialize would omit
        assertTrue(auditEvent.hasAgent(), "no agents");
        assertTrue(auditEvent.getAgent().size() >= 2, "the client and server agents are not both there");
        assertTrue(auditEvent.getSource().hasObserver(), "no audit source observer");
        assertTrue(auditEvent.getEntity().stream()
                .anyMatch(entity -> "XrequestId".equals(entity.getType().getCode())),
            "the request id entity is missing");

        validator.assertConformant(auditEvent);
    }

    private AuditEvent initialized(BalpAuditEvent auditEvent, AuditMessage auditMessage) {
        assertTrue(auditEvent.supports(auditMessage));
        auditEvent.initialize(auditMessage);
        return validator.onTheWire(auditEvent);
    }

    // ------------------------------------------------------------------------------- audit messages

    private AuditMessage auditMessage(EventActionCode action, String patientId) {
        var auditMessage = new AuditMessage();

        var eventIdentification = new EventIdentificationType(
            EventIdCode.Export, Instant.now(), EventOutcomeIndicator.Success);
        eventIdentification.setEventActionCode(action);
        auditMessage.setEventIdentification(eventIdentification);

        auditMessage.getActiveParticipants().add(
            participant(CLIENT_URI, ActiveParticipantRoleIdCode.Source, CLIENT_IP, true));
        auditMessage.getActiveParticipants().add(
            participant(SERVER_URI, ActiveParticipantRoleIdCode.Destination, SERVER_URI, false));

        var auditSource = new AuditSourceIdentificationType("IPF");
        auditSource.setAuditEnterpriseSiteID("mysite");
        auditMessage.setAuditSourceIdentification(auditSource);

        auditMessage.setRequestId("6f8d1a5c-6f7e-4d0c-9a56-3a2f9c1e77bd");
        auditMessage.getParticipantObjectIdentifications().add(participantObject(
            RESOURCE_ID, ParticipantObjectIdTypeCode.ReportNumber,
            ParticipantObjectTypeCode.System, ParticipantObjectTypeCodeRole.Report));
        if (patientId != null) {
            auditMessage.getParticipantObjectIdentifications().add(participantObject(
                patientId, ParticipantObjectIdTypeCode.PatientNumber,
                ParticipantObjectTypeCode.Person, ParticipantObjectTypeCodeRole.Patient));
        }
        return auditMessage;
    }

    private ActiveParticipantType participant(String userId, ActiveParticipantRoleIdCode role,
                                              String address, boolean requestor) {
        var participant = new ActiveParticipantType(userId, requestor);
        participant.getRoleIDCodes().add(role);
        participant.setNetworkAccessPointID(address);
        participant.setNetworkAccessPointTypeCode(NetworkAccessPointTypeCode.IPAddress);
        return participant;
    }

    private ParticipantObjectIdentificationType participantObject(String id, ParticipantObjectIdTypeCode idType,
                                                                  ParticipantObjectTypeCode type,
                                                                  ParticipantObjectTypeCodeRole role) {
        var participantObject = new ParticipantObjectIdentificationType(id, idType);
        participantObject.setParticipantObjectTypeCode(type);
        participantObject.setParticipantObjectTypeCodeRole(role);
        return participantObject;
    }

}
