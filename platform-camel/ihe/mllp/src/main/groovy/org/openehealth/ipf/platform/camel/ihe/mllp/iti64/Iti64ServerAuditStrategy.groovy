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

import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes

/**
 * Server (aka Camel consumer, aka XPID Consumer) audit strategy for ITI-64 (Notify XAD-PID Link Change).
 * @author Boris Stanojevic
 */
class Iti64ServerAuditStrategy extends Iti64AuditStrategy {

    Iti64ServerAuditStrategy() {
        super(true)
    }


    void doAudit(RFC3881EventOutcomeCodes eventOutcome, MllpAuditDataset auditDataset) {
        // TODO: implement ATNA audit for ITI-64 component
    }

}
