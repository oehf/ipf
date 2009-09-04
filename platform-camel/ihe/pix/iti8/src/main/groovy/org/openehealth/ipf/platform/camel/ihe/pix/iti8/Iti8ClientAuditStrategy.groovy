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

import org.openehealth.ipf.commons.ihe.atna.AuditorManager
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset


/**
 * Client (aka Camel producer) audit strategy for ITI-8 (PIX Feed).
 * 
 * @author Dmytro Rud
 */
class Iti8ClientAuditStrategy extends Iti8AuditStrategy {
    
    void callAuditRoutine(action, eventOutcome, auditDataset, newPatientId) {
        AuditorManager.getPIXSourceAuditor()."audit${action}PatientRecordEvent"(
                eventOutcome,
                auditDataset.remoteAddress,
                auditDataset.receivingFacility,
                auditDataset.receivingApplication,
                auditDataset.sendingFacility,
                auditDataset.sendingApplication,
                auditDataset.messageControlId,
                newPatientId ? auditDataset.patientId : auditDataset.oldPatientId)
    }
    
     
    MllpAuditDataset createAuditDataset() {
        new Iti8AuditDataset(false);
    }
}
 
