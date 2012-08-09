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

import org.openhealthtools.ihe.atna.auditor.PIXAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.PatientRecordEvent;

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
            String sourcePatientId,
            String newPatientId,
            String oldPatientId)
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
                transactionCode);

        configureEvent(
                this,
                serverSide,
                event,
                sendingFacility + '|' + sendingApp,
                null,
                receivingFacility + '|' + receivingApp,
                documentRegistryUri,
                pixManagerIpAddress);

        byte[] messageIdBytes = hl7MessageControlId.getBytes();
        event.addPatientParticipantObject(sourcePatientId, messageIdBytes, transactionCode);
        event.addPatientParticipantObject(newPatientId, messageIdBytes, transactionCode);
        event.addPatientParticipantObject(oldPatientId, messageIdBytes, transactionCode);
        audit(event);
    }

}
