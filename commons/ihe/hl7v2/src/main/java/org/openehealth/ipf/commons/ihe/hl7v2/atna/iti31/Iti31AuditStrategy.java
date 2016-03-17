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

import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;

import java.util.Map;

public abstract class Iti31AuditStrategy extends AuditStrategySupport<Iti31AuditDataset> {


    public Iti31AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti31AuditDataset enrichAuditDatasetFromRequest(Iti31AuditDataset auditDataset, Object msg, Map<String, Object> parameters) {
        Iti31AuditStrategyUtils.enrichAuditDatasetFromRequest(auditDataset, (Message)msg);
        return auditDataset;
    }

    @Override
    public void doAudit(Iti31AuditDataset auditDataset) {
        if (auditDataset.isUpdate()) {
            callUpdateAuditRoutine(auditDataset, true);
        } else if (auditDataset.isDelete()) {
            callDeleteAuditRoutine(auditDataset, false);
        } else if (auditDataset.isMerge()) {
            callDeleteAuditRoutine(auditDataset, false);
            callUpdateAuditRoutine(auditDataset, true);
        } else if (auditDataset.isCreate()) {
            callCreateAuditRoutine(auditDataset, true);
        }
    }

    protected abstract void callCreateAuditRoutine(
            Iti31AuditDataset auditDataset,
            boolean newPatientId);

    protected abstract void callUpdateAuditRoutine(
            Iti31AuditDataset auditDataset,
            boolean newPatientId);

    protected abstract void callDeleteAuditRoutine(
            Iti31AuditDataset auditDataset,
            boolean newPatientId);

    @Override
    public Iti31AuditDataset createAuditDataset() {
        return new Iti31AuditDataset(isServerSide());
    }

}
