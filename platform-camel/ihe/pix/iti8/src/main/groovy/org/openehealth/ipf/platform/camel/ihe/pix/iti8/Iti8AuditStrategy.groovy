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
package org.openehealth.ipf.platform.camel.ihe.pix.iti8

import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditStrategy
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditDataset
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.AuditUtils
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes


/**
 * Generic audit strategy for ITI-8 (PIX Feed).
 * 
 * @author Dmytro Rud
 */
abstract class Iti8AuditStrategy implements MllpAuditStrategy {
    
    String[] getNecessaryFields(String messageType) {
        (messageType == 'A40') ? ['PatientId', 'OldPatientId'] : ['PatientId']
    }

    
    void enrichAuditDatasetFromRequest(MllpAuditDataset auditDataset, MessageAdapter msg) {
        auditDataset.patientId = AuditUtils.pidList(msg.PID[3])?.join(msg.MSH[2].value[1])        
        if(msg.MSH[9][2].value == 'A40') {
            auditDataset.oldPatientId = msg.MRG[1].value ?: null
        }
    }

    
    void enrichAuditDatasetFromResponse(MllpAuditDataset auditDataset, MessageAdapter msg) {
        // nop
    }
    
    
    void doAudit(RFC3881EventOutcomeCodes eventOutcome, MllpAuditDataset auditDataset) {
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

    
    abstract void callAuditRoutine(action, eventOutcome, auditDataset, newPatientId)
}
