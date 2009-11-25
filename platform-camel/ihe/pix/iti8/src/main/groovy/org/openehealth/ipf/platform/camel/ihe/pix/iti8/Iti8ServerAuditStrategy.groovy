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
 * Server (aka Camel consumer) audit strategy for ITI-8 (PIX Feed).
 * 
 * @author Dmytro Rud
 */
class Iti8ServerAuditStrategy extends Iti8AuditStrategy {

    void callAuditRoutine(action, eventOutcome, auditDataset, newPatientId) {
        AuditorManager.getPIXManagerAuditor()."audit${action}PatientRecordEvent"(
                eventOutcome,
                auditDataset.remoteAddress,
                auditDataset.sendingFacility,
                auditDataset.sendingApplication,
                auditDataset.localAddress,
                auditDataset.receivingFacility,
                auditDataset.receivingApplication,
                auditDataset.messageControlId,
                newPatientId ? auditDataset.patientId : auditDataset.oldPatientId)
    }
    
    
    MllpAuditDataset createAuditDataset() {
        return new Iti8AuditDataset(true);
    }

    public void auditAuthenticationNodeFailure (String hostAddress) {
        AuditorManager.getPIXManagerAuditor().auditNodeAuthenticationFailure(
            true, null, getClass().name, null, hostAddress, null)
    }

}
