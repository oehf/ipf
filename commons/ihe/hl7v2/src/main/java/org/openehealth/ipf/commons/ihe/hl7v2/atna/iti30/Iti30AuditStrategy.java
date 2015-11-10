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

import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import java.util.Map;

public abstract class Iti30AuditStrategy extends AuditStrategySupport<Iti30AuditDataset> {

    public Iti30AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti30AuditDataset enrichAuditDatasetFromRequest(Iti30AuditDataset auditDataset, Object msg, Map<String, Object> parameters) {
        Iti30AuditStrategyUtils.enrichAuditDatasetFromRequest(auditDataset, (Message)msg);
        return auditDataset;
    }

    @Override
    public void doAudit(Iti30AuditDataset auditDataset) {
        if ("A31".equals(auditDataset.getMessageType()) ||
                "A47".equals(auditDataset.getMessageType()) ||
                "A24".equals(auditDataset.getMessageType()) ||
                "A37".equals(auditDataset.getMessageType())) {
            callUpdateAuditRoutine(auditDataset, true);
        } else if ("A40".equals(auditDataset.getMessageType())) {
            callDeleteAuditRoutine(auditDataset, false);
            callUpdateAuditRoutine(auditDataset, true);
        } else {
            // A28
            callCreateAuditRoutine(auditDataset, true);
        }
    }


    protected abstract void callCreateAuditRoutine(
            Iti30AuditDataset auditDataset,
            boolean newPatientId);

    protected abstract void callUpdateAuditRoutine(
            Iti30AuditDataset auditDataset,
            boolean newPatientId);

    protected abstract void callDeleteAuditRoutine(
            Iti30AuditDataset auditDataset,
            boolean newPatientId);

    @Override
    public Iti30AuditDataset createAuditDataset() {
        return new Iti30AuditDataset(isServerSide());
    }

}
