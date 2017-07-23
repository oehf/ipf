/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.apache.commons.lang3.StringUtils;
import org.openhealthtools.ihe.atna.auditor.PIXAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionParticipantObjectIDTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.PatientRecordEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;

import java.util.Arrays;

import static org.openehealth.ipf.commons.ihe.core.atna.custom.CustomAuditorUtils.configureEvent;

/**
 * Implementation of ATNA Auditors for the following HL7v2-based transactions:
 * <ul>
 *     <li>ITI-64 -- Notify XAD-PID Link Change</li>
 * </ul>
 *
 * @author Dmytro Rud
 */
public class CustomPixAuditor extends PIXAuditor {

    public static CustomPixAuditor getAuditor() {
        AuditorModuleContext ctx = AuditorModuleContext.getContext();
        return (CustomPixAuditor) ctx.getAuditor(CustomPixAuditor.class);
    }

    public void auditIti64(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String pixManagerIpAddress,
            String sendingFacility,
            String sendingApp,
            String documentRegistryUri,
            String receivingFacility,
            String receivingApp,
            String hl7MessageControlId,
            String localPatientId,
            String subsumedLocalPatientId,
            String newPatientId,
            String previousPatientId,
            String submissionSetUuid)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        CustomIHETransactionEventTypeCodes.NotifyXadPidLinkChange transactionCode =
                new CustomIHETransactionEventTypeCodes.NotifyXadPidLinkChange();

        PatientRecordEvent event = new PatientRecordEvent(
                true,
                eventOutcome,
                RFC3881EventCodes.RFC3881EventActionCodes.UPDATE,
                transactionCode,
                null);

        configureEvent(
                this,
                serverSide,
                event,
                sendingFacility + '|' + sendingApp,
                null,
                receivingFacility + '|' + receivingApp,
                documentRegistryUri,
                pixManagerIpAddress,
                null);

        TypeValuePairType messageIdVP           = event.getTypeValuePair("MSH-10", hl7MessageControlId);
        TypeValuePairType localPatientIdVP      = event.getTypeValuePair("urn:ihe:iti:xpid:2017:patientIdentifierType", "localPatientId");
        TypeValuePairType subsumedPatientIdVP   = event.getTypeValuePair("urn:ihe:iti:xpid:2017:patientIdentifierType", "subsumedPatientId");
        TypeValuePairType newPatientIdIdVP      = event.getTypeValuePair("urn:ihe:iti:xpid:2017:patientIdentifierType", "newPatientId");
        TypeValuePairType previousPatientIdIdVP = event.getTypeValuePair("urn:ihe:iti:xpid:2017:patientIdentifierType", "previousPatientId");

        boolean subsumedLocalPatientIdPresent = StringUtils.isNoneBlank(subsumedLocalPatientId);
        boolean newPatientIdEqualsToPrevious = StringUtils.equals(newPatientId, previousPatientId);

        event.addParticipantObjectIdentification(
                new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.PatientNumber(),
                null,
                null,
                Arrays.asList(messageIdVP, localPatientIdVP),
                localPatientId,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.PERSON,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PATIENT,
                subsumedLocalPatientIdPresent ? RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectDataLifeCycleCodes.ORIGINATION : null,
                null);

        if (subsumedLocalPatientIdPresent) {
            event.addParticipantObjectIdentification(
                    new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.PatientNumber(),
                    null,
                    null,
                    Arrays.asList(messageIdVP, subsumedPatientIdVP),
                    subsumedLocalPatientId,
                    RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.PERSON,
                    RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PATIENT,
                    RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectDataLifeCycleCodes.LOGICAL_DELETION,
                    null);
        }

        event.addParticipantObjectIdentification(
                new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.PatientNumber(),
                null,
                null,
                Arrays.asList(messageIdVP, newPatientIdIdVP),
                newPatientId,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.PERSON,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PATIENT,
                newPatientIdEqualsToPrevious ? null : RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectDataLifeCycleCodes.ORIGINATION,
                null);

        event.addParticipantObjectIdentification(
                new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.PatientNumber(),
                null,
                null,
                Arrays.asList(messageIdVP, previousPatientIdIdVP),
                previousPatientId,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.PERSON,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PATIENT,
                newPatientIdEqualsToPrevious ? null : RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectDataLifeCycleCodes.LOGICAL_DELETION,
                null);

        if (serverSide && StringUtils.isNotBlank(submissionSetUuid)) {
            event.addParticipantObjectIdentification(
                    new IHETransactionParticipantObjectIDTypeCodes.SubmissionSet(),
                    null,
                    null,
                    null,
                    submissionSetUuid,
                    RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.SYSTEM,
                    RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.JOB,
                    null,
                    null);
        }

        audit(event);
    }

}
