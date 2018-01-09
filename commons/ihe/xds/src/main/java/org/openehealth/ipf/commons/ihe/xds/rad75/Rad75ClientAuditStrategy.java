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
package org.openehealth.ipf.commons.ihe.xds.rad75;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsDataImportBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsIRetrieveAuditStrategy30;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status;

import java.util.stream.Stream;

/**
 * Audit strategy for RAD-75.
 *
 * @author Clay Sebourn
 * @author Christian Ohr
 */
public class Rad75ClientAuditStrategy extends XdsIRetrieveAuditStrategy30 {

    public Rad75ClientAuditStrategy() {
        super(false);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset) {
        return Stream.of(Status.values())
                .filter(auditDataset::hasDocuments)
                .map(s -> doMakeAuditMessage(auditContext, auditDataset, s))
                .toArray(AuditMessage[]::new);
    }

    private AuditMessage doMakeAuditMessage(AuditContext auditContext, XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset, Status status) {
        return new XdsDataImportBuilder(auditContext, auditDataset, XdsEventTypeCode.CrossGatewayRetrieveImagingDocumentSet, auditDataset.getPurposesOfUse())
                .setPatient(auditDataset.getPatientId())
                .addDocumentIds(auditDataset, status, ParticipantObjectIdTypeCode.ReportNumber)
                .getMessage();
    }


}
