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

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Server (aka Camel consumer) audit strategy for ITI-8 (PIX Feed).
 * @author Dmytro Rud
 */
class Iti8ServerAuditStrategy extends Iti8AuditStrategy {

    Iti8ServerAuditStrategy() {
        super(true)
    }


    void callAuditRoutine(
            String action,
            RFC3881EventOutcomeCodes eventOutcome,
            Iti8AuditDataset auditDataset,
            boolean newPatientId)
    {
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

}
