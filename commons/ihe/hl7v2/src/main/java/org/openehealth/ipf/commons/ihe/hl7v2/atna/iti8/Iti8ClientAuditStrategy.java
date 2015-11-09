/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti8;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

public class Iti8ClientAuditStrategy extends Iti8AuditStrategy {

    public Iti8ClientAuditStrategy() {
        super(false);
    }

    @Override
    protected void callCreateAuditRoutine(
            RFC3881EventOutcomeCodes eventOutcome,
            Iti8AuditDataset auditDataset, boolean newPatientId) {
        AuditorManager.getPIXSourceAuditor().auditCreatePatientRecordEvent(
                eventOutcome,
                auditDataset.getRemoteAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getMessageControlId(),
                newPatientId ? auditDataset.getPatientId() : auditDataset
                        .getOldPatientId());
    }

    @Override
    protected void callUpdateAuditRoutine(
            RFC3881EventOutcomeCodes eventOutcome,
            Iti8AuditDataset auditDataset, boolean newPatientId) {
        AuditorManager.getPIXSourceAuditor().auditUpdatePatientRecordEvent(
                eventOutcome,
                auditDataset.getRemoteAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getMessageControlId(),
                newPatientId ? auditDataset.getPatientId() : auditDataset
                        .getOldPatientId());
    }

    @Override
    protected void callDeleteAuditRoutine(
            RFC3881EventOutcomeCodes eventOutcome,
            Iti8AuditDataset auditDataset, boolean newPatientId) {
        AuditorManager.getPIXSourceAuditor().auditDeletePatientRecordEvent(
                eventOutcome,
                auditDataset.getRemoteAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getMessageControlId(),
                newPatientId ? auditDataset.getPatientId() : auditDataset
                        .getOldPatientId());
    }

}
