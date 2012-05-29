/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8

import org.apache.camel.Exchange;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AuditUtils;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Generic audit strategy for ITI-8 (PIX Feed).
 * @author Dmytro Rud
 */
abstract class Iti8AuditStrategy extends MllpAuditStrategy<Iti8AuditDataset> {
    
    Iti8AuditStrategy(boolean serverSide) {
        super(serverSide)
    }


    String[] getNecessaryFields(String messageType) {
        return (messageType == 'A40') ?
            ['PatientId', 'OldPatientId'] as String[] :
            ['PatientId'] as String[]
    }


    void enrichAuditDatasetFromRequest(Iti8AuditDataset auditDataset, MessageAdapter msg, Exchange exchange) {
        def pidSegment
        if(msg.MSH[9][2].value == 'A40') {
            def group = msg.PIDPD1MRGPV1
            pidSegment = group.PID[3]
            auditDataset.oldPatientId = group.MRG[1].value ?: null
        } else {
            pidSegment = msg.PID[3]
        }
        auditDataset.patientId = AuditUtils.pidList(pidSegment)?.join(msg.MSH[2].value[1])
    }

    
    void doAudit(RFC3881EventOutcomeCodes eventOutcome, Iti8AuditDataset auditDataset) {
        if(auditDataset.messageType == 'A08') {
            callAuditRoutine('Update', eventOutcome, auditDataset, true)
        } else if(auditDataset.messageType == 'A40') {
            callAuditRoutine('Delete', eventOutcome, auditDataset, false)
            callAuditRoutine('Update', eventOutcome, auditDataset, true)
        } else {        
            // A01, A04, A05
            callAuditRoutine('Create', eventOutcome, auditDataset, true)
        }
    }

    
    abstract void callAuditRoutine(
            String action,
            RFC3881EventOutcomeCodes eventOutcome,
            Iti8AuditDataset auditDataset,
            boolean newPatientId)


    @Override
    Iti8AuditDataset createAuditDataset() {
        return new Iti8AuditDataset(serverSide)
    }
}
