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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti22;

import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.QueryAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.pdqcore.PdqAuditStrategy;

/**
 * Server (aka Camel consumer) audit strategy for ITI-21 and ITI-22 (PDQ).
 * 
 * @author Dmytro Rud
 */
public class Iti22ServerAuditStrategy extends PdqAuditStrategy {

    public Iti22ServerAuditStrategy() {
        super(true);
    }

    public void doAudit(RFC3881EventOutcomeCodes eventOutcome,
            QueryAuditDataset auditDataset) {
        AuditorManager.getPIXManagerAuditor().auditPDVQQueryEvent(eventOutcome,
                auditDataset.getRemoteAddress(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getLocalAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getMessageControlId(), auditDataset.getPayload(),
                auditDataset.getPatientIds());

    }

}
