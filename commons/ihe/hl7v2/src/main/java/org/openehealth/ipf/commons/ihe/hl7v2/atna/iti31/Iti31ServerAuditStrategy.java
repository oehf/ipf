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
package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti31;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;

public class Iti31ServerAuditStrategy extends Iti31AuditStrategy {

    public Iti31ServerAuditStrategy() {
        super(false);
    }

    @Override
    protected void callCreateAuditRoutine(
            Iti31AuditDataset auditDataset, boolean newPatientId) {
        AuditorManager.getPIXManagerAuditor().auditCreatePatientRecordEvent(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getRemoteAddress(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getLocalAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getMessageControlId(),
                newPatientId ? auditDataset.getPatientId() : auditDataset
                        .getOldPatientId());
    }

    @Override
    protected void callUpdateAuditRoutine(
            Iti31AuditDataset auditDataset, boolean newPatientId) {
        AuditorManager.getPIXManagerAuditor().auditUpdatePatientRecordEvent(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getRemoteAddress(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getLocalAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getMessageControlId(),
                newPatientId ? auditDataset.getPatientId() : auditDataset
                        .getOldPatientId());
    }

    @Override
    protected void callDeleteAuditRoutine(
            Iti31AuditDataset auditDataset, boolean newPatientId) {
        AuditorManager.getPIXManagerAuditor().auditDeletePatientRecordEvent(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getRemoteAddress(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getLocalAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getMessageControlId(),
                newPatientId ? auditDataset.getPatientId() : auditDataset
                        .getOldPatientId());
    }

}
