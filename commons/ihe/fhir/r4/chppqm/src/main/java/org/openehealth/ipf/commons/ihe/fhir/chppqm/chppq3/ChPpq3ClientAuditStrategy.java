/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq3;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.core.atna.event.PHIExportBuilder;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmAuditDataset;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode.MobilePrivacyPolicyFeed;

public class ChPpq3ClientAuditStrategy extends ChPpq3AuditStrategy {

    public ChPpq3ClientAuditStrategy() {
        super(false);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, ChPpqmAuditDataset auditDataset) {
        return new PHIExportBuilder<>(auditContext, auditDataset, MobilePrivacyPolicyFeed)
                .setPatient(auditDataset.getPatientId())
                .addSecurityResourceParticipantObjects(ParticipantObjectIdType.of(MobilePrivacyPolicyFeed), auditDataset.getPolicyAndPolicySetIds())
                .getMessages();
    }
}
