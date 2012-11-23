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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8;

import org.apache.camel.Exchange;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

public abstract class Iti8AuditStrategy extends MllpAuditStrategy<Iti8AuditDataset> {

    public Iti8AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    public String[] getNecessaryFields(String messageType) {
        return ("A40".equals(messageType) ?
            new String[] { "PatientId", "OldPatientId" } :
            new String[] { "PatientId" });
    }


    public void enrichAuditDatasetFromRequest(Iti8AuditDataset auditDataset, MessageAdapter<?> msg, Exchange exchange) {
        Iti8AuditStrategyUtils.enrichAuditDatasetFromRequest(auditDataset, msg, exchange);
    }

    
    public void doAudit(RFC3881EventOutcomeCodes eventOutcome, Iti8AuditDataset auditDataset) {
        if("A08".equals(auditDataset.getMessageType())) {
            callUpdateAuditRoutine(eventOutcome, auditDataset, true);
        } else if("A40".equals(auditDataset.getMessageType())) {
            callDeleteAuditRoutine(eventOutcome, auditDataset, false);
            callUpdateAuditRoutine(eventOutcome, auditDataset, true);
        } else {        
            // A01, A04, A05
            callCreateAuditRoutine(eventOutcome, auditDataset, true);
        }
    }

    
    protected abstract void callCreateAuditRoutine(
            RFC3881EventOutcomeCodes eventOutcome,
            Iti8AuditDataset auditDataset,
            boolean newPatientId);

    protected abstract void callUpdateAuditRoutine(
            RFC3881EventOutcomeCodes eventOutcome,
            Iti8AuditDataset auditDataset,
            boolean newPatientId);
    
    protected abstract void callDeleteAuditRoutine(
            RFC3881EventOutcomeCodes eventOutcome,
            Iti8AuditDataset auditDataset,
            boolean newPatientId);    

    @Override
    public Iti8AuditDataset createAuditDataset() {
        return new Iti8AuditDataset(isServerSide());
    }

}
