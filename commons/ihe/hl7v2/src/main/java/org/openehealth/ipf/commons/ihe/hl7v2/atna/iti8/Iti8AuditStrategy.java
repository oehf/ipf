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

import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import java.util.Map;

public abstract class Iti8AuditStrategy extends AuditStrategySupport<Iti8AuditDataset> {

    public Iti8AuditStrategy(boolean serverSide) {
        super(serverSide);
    }


    @Override
    public Iti8AuditDataset enrichAuditDatasetFromRequest(Iti8AuditDataset auditDataset, Object msg, Map<String, Object> parameters) {
        Iti8AuditStrategyUtils.enrichAuditDatasetFromRequest(auditDataset, (Message)msg);
        return auditDataset;
    }


    @Override
    public void doAudit(Iti8AuditDataset auditDataset) {
        if("A08".equals(auditDataset.getMessageType())) {
            callUpdateAuditRoutine(auditDataset, true);
        } else if("A40".equals(auditDataset.getMessageType())) {
            callDeleteAuditRoutine(auditDataset, false);
            callUpdateAuditRoutine(auditDataset, true);
        } else {        
            // A01, A04, A05
            callCreateAuditRoutine(auditDataset, true);
        }
    }


    protected abstract void callCreateAuditRoutine(
            Iti8AuditDataset auditDataset,
            boolean newPatientId);

    protected abstract void callUpdateAuditRoutine(
            Iti8AuditDataset auditDataset,
            boolean newPatientId);
    
    protected abstract void callDeleteAuditRoutine(
            Iti8AuditDataset auditDataset,
            boolean newPatientId);    

    @Override
    public Iti8AuditDataset createAuditDataset() {
        return new Iti8AuditDataset(isServerSide());
    }

}
