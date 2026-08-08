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
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.CodedValueType;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.*;

/**
 * What is needed to build a BALP AuditEvent but is not itself part of a BALP profile: reading the ATNA
 * {@link AuditMessage} of a transaction, and mapping an active participant that stands for a security
 * token to an agent. The profiles themselves are expressed by {@link BalpAuditEvent} and its subclasses.
 *
 * @author Christian Ohr
 */
public abstract class BalpAuditEventHelper {

    /**
     * Looks up the active participant an agent slice is to be built from. It may be missing: an
     * audit dataset is not guaranteed to know both ends of the transaction, and an audit record
     * without one of the agents is still better than none at all.
     *
     * @param auditMessage audit message to search
     * @param roleIdCode   role of the wanted active participant
     * @return the first active participant with that role, if there is one
     */
    public static Optional<ActiveParticipantType> findActiveParticipant(AuditMessage auditMessage,
                                                                        ActiveParticipantRoleIdCode roleIdCode) {
        return auditMessage
            .findActiveParticipants(activeParticipant -> activeParticipant.getRoleIDCodes().contains(roleIdCode))
            .stream()
            .findFirst();
    }

    /**
     * @param activeParticipant active participant
     * @return the network type of the given active participant, or {@code null} if it has none.
     *      The network access point type code is optional in ATNA, so it must not be dereferenced
     *      blindly -- doing so used to fail the transaction being audited.
     */
    public static AuditEvent.AuditEventAgentNetworkType networkType(ActiveParticipantType activeParticipant) {
        var networkAccessPointTypeCode = activeParticipant.getNetworkAccessPointTypeCode();
        return networkAccessPointTypeCode != null ?
            AuditEvent.AuditEventAgentNetworkType.fromCode(String.valueOf(networkAccessPointTypeCode.getValue())) :
            null;
    }

    /** How HL7v2 and XDS name a patient: {@code value^^^&assigningAuthorityOid&ISO}. */
    private static final Pattern CX = Pattern.compile("^([^^]+)\\^{3}&([^&]+)&ISO$");

    /**
     * @param auditMessage audit message to search
     * @return the patient the audit message names, if it names one.
     */
    public static Optional<Reference> patientReference(AuditMessage auditMessage) {
        return auditMessage.findParticipantObjectIdentifications(
                poi -> ParticipantObjectIdTypeCode.PatientNumber == poi.getParticipantObjectIDTypeCode())
            .stream()
            .findFirst()
            .map(poi -> reference(poi.getParticipantObjectID()));
    }

    /**
     * Turns the ID of a participant object into a reference that identifies rather than merely names
     * what it points at. BALP exists for records that hold a well-formed indication of the patient, and
     * PIXm makes the point explicit by requiring {@code entity.what.identifier}; a display string would
     * leave a reader to parse the identifier back out of prose.
     * <p>
     * Four shapes occur:
     * <ul>
     *     <li>a FHIR token, {@code system|value}, which is what the FHIR transactions record</li>
     *     <li>a CX, {@code value^^^&oid&ISO}, which is what HL7v2 and XDS record</li>
     *     <li>a literal reference such as {@code Patient/a2}</li>
     *     <li>a bare id such as a document OID or a SubmissionSet UUID, which has no system to go with
     *     it but is an identifier all the same -- as the generic translation also treats it</li>
     * </ul>
     *
     * @param participantObjectId ID of a participant object, may be null
     * @return a reference to what it identifies
     */
    public static Reference reference(String participantObjectId) {
        if (isBlank(participantObjectId)) {
            return new Reference();
        }
        var cx = CX.matcher(participantObjectId);
        if (cx.matches()) {
            return new Reference().setIdentifier(new Identifier()
                .setSystem("urn:oid:" + cx.group(2))
                .setValue(cx.group(1)));
        }
        var separator = participantObjectId.indexOf('|');
        if (separator >= 0) {
            var system = participantObjectId.substring(0, separator);
            var value = participantObjectId.substring(separator + 1);
            if (isNotBlank(value)) {
                var identifier = new Identifier().setValue(value);
                if (isNotBlank(system)) {
                    identifier.setSystem(system);
                }
                return new Reference().setIdentifier(identifier);
            }
        }
        var literal = new Reference(participantObjectId);
        return literal.getReferenceElement().hasResourceType() ?
            literal :
            new Reference().setIdentifier(new Identifier().setValue(participantObjectId));
    }

    /**
     * The value travels with the message rather than being read back out of the participant object it
     * was encoded into, because which participant object that is depends on the profile that asked for
     * the correlation -- IHE BALP and the Swiss EPR disagree on the code -- while the value does not.
     *
     * @param auditMessage audit message to read
     * @return the id correlating the audit records of the two ends of the transaction, if the request
     *      carried one. BALP reports it as the transaction entity of the AuditEvent.
     */
    public static Optional<String> requestId(AuditMessage auditMessage) {
        return Optional.ofNullable(auditMessage.getRequestId());
    }

    /**
     * Maps an active participant that stands for a security token -- an OAuth user, an OAuth client or
     * an opaque token -- to the user agent of an AuditEvent. Shared with the generic translation so that
     * the profiled AuditEvents report the same user, which their profiles allow as the user agent slice.
     *
     * @param ap active participant
     * @return the agent it maps to, or empty if it does not stand for a token
     */
    public static Optional<AuditEvent.AuditEventAgentComponent> oAuthActiveParticipantToAgent(ActiveParticipantType ap) {
        var oUser = getOAuthAttrFromKnownRoleIdCode(ap.getRoleIDCodes(), OUSER_AGENT_TYPE_SYSTEM_NAME);
        if (oUser.isPresent()) {
            var agent = new AuditEvent.AuditEventAgentComponent()
                .setType(systemAndCodeToCodeableConcept(OUSER_AGENT_TYPE_SYSTEM_NAME, OUSER_AGENT_TYPE_CODE, "information recipient"))
                .addPolicy(oUser.get())
                .setName(ap.getUserName())
                .setWho(
                    new Reference(ap.getUserID())
                        .setIdentifier(new Identifier().setSystem(ap.getAlternativeUserID()).setValue(ap.getUserID()))
                        .setDisplay(ap.getUserName()))
                .setRequestor(ap.isUserIsRequestor());
            getOAuthListAttrFromKnownRoleIdCode(ap.getRoleIDCodes(), OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME)
                .forEach(purpose -> agent.getPurposeOfUse().add(
                    systemAndCodeToCodeableConcept(OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME, purpose, "")));
            getOAuthListAttrFromKnownRoleIdCode(ap.getRoleIDCodes(), OUSER_AGENT_ROLE_SYSTEM_NAME)
                .forEach(purpose -> agent.getRole().add(
                    systemAndCodeToCodeableConcept(OUSER_AGENT_ROLE_SYSTEM_NAME, purpose, "")));
            return Optional.of(agent);
        }
        var oClient = getOAuthAttrFromKnownRoleIdCode(ap.getRoleIDCodes(), DCM_SYSTEM_NAME);
        if (oClient.isPresent()) {
            return Optional.of(new AuditEvent.AuditEventAgentComponent()
                .setType(systemAndCodeToCodeableConcept(DCM_SYSTEM_NAME, DCM_OCLIENT_CODE, "Application"))
                .setRequestor(ap.isUserIsRequestor())
                .setWho(new Reference().setIdentifier(
                    new Identifier().setValue(oClient.get())).setDisplay(ap.getUserName())));
        }
        var opaqueToken = getOAuthAttrFromKnownRoleIdCode(ap.getRoleIDCodes(),
            USER_AGENT_TYPES_SYSTEM_NAME);
        if (opaqueToken.isPresent()) {
            return Optional.of(new AuditEvent.AuditEventAgentComponent()
                .setType(new CodeableConcept(
                    new Coding(USER_AGENT_TYPES_SYSTEM_NAME, USER_OAUTH_AGENT_CODE, "")))
                .setRequestor(true));
        }
        return Optional.empty();
    }

    private static Optional<String> getOAuthAttrFromKnownRoleIdCode(List<ActiveParticipantRoleId> roleCodes,
                                                             String knownCodeSystem) {
        return roleCodes.stream()
            .filter(p -> p.getCodeSystemName().equals(knownCodeSystem))
            .findFirst()
            .map(CodedValueType::getCode);
    }

    private static List<String> getOAuthListAttrFromKnownRoleIdCode(List<ActiveParticipantRoleId> roleCodes,
                                                             String knownCodeSystem) {
        return roleCodes.stream()
            .filter(p -> p.getCodeSystemName().equals(knownCodeSystem))
            .map(CodedValueType::getCode)
            .toList();
    }

    private static CodeableConcept systemAndCodeToCodeableConcept(String codeSystem, String code, String displayName) {
        return new CodeableConcept().addCoding(new Coding(codeSystem, code, displayName));
    }

}
