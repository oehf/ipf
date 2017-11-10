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
package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti9

import ca.uhn.hl7v2.model.Message
import org.openehealth.ipf.commons.ihe.hl7v2.atna.AuditUtils
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset

/**
 * Groovy  audit strategy utils for ITI-9 (PIX Query).
 * @author Dmytro Rud
 */
class Iti9AuditStrategyUtils  {    

    static void enrichAuditDatasetFromRequest(QueryAuditDataset auditDataset, Message msg, Map<String, Object> parameters) {
        if(msg.QPD) {
            def patientId = msg.QPD[3].encode()
            if(patientId) { 
                auditDataset.patientIds = [patientId]
            }
        }

        // request message as String
        auditDataset.payload = AuditUtils.getRequestString(parameters, msg)
    }

    
    static boolean enrichAuditDatasetFromResponse(QueryAuditDataset auditDataset, Message msg) {
        if (msg.MSH[9][1].value == 'RSP' && msg.MSH[9][2].value == 'K23' && !msg.QUERY_RESPONSE?.PID?.empty) {
            if (System.getProperty(QueryAuditDataset.NO_PATIENT_RESULT_IDS) == null) {
                def patientIds = AuditUtils.pidList(msg.QUERY_RESPONSE.PID[3])
                if ((!auditDataset.patientIds) || patientIds.contains(auditDataset.patientIds[0])) {
                    auditDataset.patientIds = patientIds
                } else {
                    patientIds << auditDataset.patientIds[0]
                    auditDataset.patientIds = patientIds
                }
            }
        }
        true
    }


}
