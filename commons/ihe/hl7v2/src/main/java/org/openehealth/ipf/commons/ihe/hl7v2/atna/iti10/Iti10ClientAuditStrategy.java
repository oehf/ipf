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
package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti10;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset;

public class Iti10ClientAuditStrategy extends Iti10AuditStrategy {

    public  Iti10ClientAuditStrategy() {
        super(false);
    }

    @Override
    public void doAudit(QueryAuditDataset auditDataset) {
        AuditorManager.getPIXManagerAuditor().auditUpdateNotificationEvent(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getLocalAddress(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getRemoteAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getMessageControlId(),
                auditDataset.getPatientIds());
    }

}
