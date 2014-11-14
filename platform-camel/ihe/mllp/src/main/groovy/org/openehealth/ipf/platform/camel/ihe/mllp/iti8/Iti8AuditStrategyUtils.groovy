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

import ca.uhn.hl7v2.model.Message
import org.apache.camel.Exchange

/**
 * Audit Strategy Groovy Utils
 * @author Dmytro Rud
 */
final class Iti8AuditStrategyUtils  {

    static void enrichAuditDatasetFromRequest(Iti8AuditDataset auditDataset, Message msg, Exchange exchange) {
        auditDataset.patientId = msg.findPID()[3].encodeRepetitions()
        if(msg.MSH[9][2].value == 'A40') {
            auditDataset.oldPatientId = msg.PIDPD1MRGPV1.MRG[1].encodeRepetitions()
        }
    }

}
