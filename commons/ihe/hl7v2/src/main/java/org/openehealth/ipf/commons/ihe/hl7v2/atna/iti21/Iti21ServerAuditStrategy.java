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
package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti21;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.pdqcore.PdqAuditStrategy;

/**
 * Server (aka Camel consumer) audit strategy for ITI-21 (PDQ).
 * 
 * @author Dmytro Rud
 */
public class Iti21ServerAuditStrategy extends PdqAuditStrategy {

    private static class LazyHolder {
        private static final Iti21ServerAuditStrategy INSTANCE = new Iti21ServerAuditStrategy();
    }

    public static Iti21ServerAuditStrategy getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Iti21ServerAuditStrategy() {
        super(true);
    }

    @Override
    public void doAudit(QueryAuditDataset auditDataset) {
        AuditorManager.getPIXManagerAuditor().auditPDQQueryEvent(
                auditDataset.getEventOutcomeCode(),
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
