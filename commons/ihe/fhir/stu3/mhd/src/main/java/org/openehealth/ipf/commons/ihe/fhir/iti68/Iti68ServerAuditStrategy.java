/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti68;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.event.PHIExportBuilder;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;

import java.util.Map;


/**
 * @author Christian Ohr
 */
public class Iti68ServerAuditStrategy extends AuditStrategySupport<Iti68AuditDataset> {

    public Iti68ServerAuditStrategy() {
        super(true);
    }

    @Override
    public Iti68AuditDataset enrichAuditDatasetFromRequest(Iti68AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        return auditDataset;
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, Iti68AuditDataset auditDataset) {
        PHIExportBuilder builder = new PHIExportBuilder<>(auditContext, auditDataset,
                EventActionCode.Create,
                FhirEventTypeCode.MobileDocumentRetrieval)
                .setPatient(auditDataset.getPatientId());
        if (auditDataset.getDocumentUniqueId() != null) {
            builder.addExportedEntity(
                    auditDataset.getDocumentUniqueId(),
                    ParticipantObjectIdTypeCode.ReportNumber,
                    ParticipantObjectTypeCodeRole.Report,
                    builder.documentDetails(
                            auditDataset.getRepositoryUniqueId(),
                            auditDataset.getHomeCommunityId(),
                            null, null, false));
        }
        return builder.getMessages();
    }

    @Override
    public Iti68AuditDataset createAuditDataset() {
        return new Iti68AuditDataset(true);
    }
}
