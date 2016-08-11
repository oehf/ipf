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
package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti30;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;

public class Iti30ServerAuditStrategy extends Iti30AuditStrategy {

    private static class LazyHolder {
        private static final Iti30ServerAuditStrategy INSTANCE = new Iti30ServerAuditStrategy();
    }

    public static Iti30ServerAuditStrategy getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Iti30ServerAuditStrategy() {
        super(false);
    }

    @Override
    protected void callCreateAuditRoutine(Iti30AuditDataset auditDataset, boolean newPatientId) {
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
    protected void callUpdateAuditRoutine(Iti30AuditDataset auditDataset, boolean newPatientId) {
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
    protected void callDeleteAuditRoutine(Iti30AuditDataset auditDataset, boolean newPatientId) {
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
