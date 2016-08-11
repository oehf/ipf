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

public class Iti10ServerAuditStrategy extends Iti10AuditStrategy {

    private static class LazyHolder {
        private static final Iti10ServerAuditStrategy INSTANCE = new Iti10ServerAuditStrategy();
    }

    public static Iti10ServerAuditStrategy getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Iti10ServerAuditStrategy() {
        super(true);
    }

    @Override
    public void doAudit(QueryAuditDataset auditDataset) {

        AuditorManager.getPIXConsumerAuditor().auditUpdateNotificationEvent(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getRemoteAddress(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getMessageControlId(),
                auditDataset.getPatientIds());
    }

}
