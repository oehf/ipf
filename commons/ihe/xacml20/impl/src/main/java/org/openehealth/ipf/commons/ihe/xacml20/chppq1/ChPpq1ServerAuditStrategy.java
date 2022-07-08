/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.chppq1;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.core.atna.event.PHIImportBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;

import static org.openehealth.ipf.commons.ihe.xacml20.audit.codes.PpqEventTypeCodes.PrivacyPolicyFeed;

/**
 * @author Dmytro Rud
 */
public class ChPpq1ServerAuditStrategy extends ChPpq1AuditStrategy {

    public ChPpq1ServerAuditStrategy() {
        super(true);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, ChPpqAuditDataset auditDataset) {
        var builder = new PHIImportBuilder<>(auditContext, auditDataset, auditDataset.getAction(), PrivacyPolicyFeed, auditDataset.getPurposesOfUse());
        builder.addSecurityResourceParticipantObjects(ParticipantObjectIdType.of(PrivacyPolicyFeed), auditDataset.getPolicyAndPolicySetIds());
        if (auditDataset.getPatientId() != null) {
            builder.setPatient(auditDataset.getPatientId());
        }
        return builder.getMessages();
    }

}
