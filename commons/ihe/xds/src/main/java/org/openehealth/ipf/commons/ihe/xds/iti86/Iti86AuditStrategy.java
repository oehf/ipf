/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.iti86;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.codes.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status;
import org.openehealth.ipf.commons.ihe.xds.core.audit.event.XdsPatientRecordEventBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRemoveDocumentAuditStrategy30;

import java.util.stream.Stream;

/**
 * @author Dmytro Rud
 * @since 3.3
 */
public class Iti86AuditStrategy extends XdsRemoveDocumentAuditStrategy30 {

    public Iti86AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset) {
        return Stream.of(Status.values())
                .filter(auditDataset::hasDocuments)
                .map(s -> doMakeAuditMessage(auditContext, auditDataset, s))
                .toArray(AuditMessage[]::new);
    }

    private AuditMessage doMakeAuditMessage(AuditContext auditContext, XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset, Status status) {
        return new XdsPatientRecordEventBuilder(auditContext, auditDataset,
                auditDataset.getEventOutcomeIndicator(status),
                null,
                EventActionCode.Delete,
                XdsEventTypeCode.RemoveDocuments,
                auditDataset.getPurposesOfUse())
                .addPatients(auditDataset.getPatientIds())
                .addDocumentIds(auditDataset,
                        status,
                        ParticipantObjectIdTypeCode.ReportNumber,
                        ParticipantObjectDataLifeCycle.PermanentErasure)
                .getMessage();
    }

}
