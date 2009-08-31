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
package org.openehealth.ipf.platform.camel.ihe.pix.iti9

import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditStrategy
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditDataset
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.AuditUtils
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7.message.MessageUtils

/**
 * Generic audit strategy for ITI-9 (PIX Query).
 * @author Dmytro Rud
 */
abstract class Iti9AuditStrategy implements MllpAuditStrategy {
    
    String[] getNecessaryFields(String messageType) {
        ['QpdPayload', 'PatientIds']
    }

    
    void enrichAuditDataset(MllpAuditDataset auditDataset, MessageAdapter msg) {
        if(msg.QPD.value) {
            def patientId = MessageUtils.pipeEncode(msg.QPD[3].target)
            auditDataset.patientIds = (patientId ? [patientId] : null)
            auditDataset.qpdPayload = MessageUtils.pipeEncode(msg.QPD.target)
        }
    }
}
