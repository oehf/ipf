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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti64

import org.apache.camel.Exchange
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AuditUtils
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy

/**
 * Generic audit strategy for ITI-64 (Notify XAD-PID Link Change).
 * @author Christian Ohr
 * @author Boris Stanojevic
 */
abstract class Iti64AuditStrategy extends MllpAuditStrategy {

    public Iti64AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public String[] getNecessaryFields(String eventTrigger) {
        return ['PatientId', 'OldPatientId'] as String[]
    }

    @Override
    public void enrichAuditDatasetFromRequest(MllpAuditDataset auditDataset,
                                              MessageAdapter<?> msg, Exchange exchange) {
        def group = msg.PATIENT
        def pidSegment = group.PID[3]
        auditDataset.oldPatientId = group.MRG[1].value ?: null
        auditDataset.patientId = AuditUtils.pidList(pidSegment)?.join(msg.MSH[2].value[1])

    }
}
