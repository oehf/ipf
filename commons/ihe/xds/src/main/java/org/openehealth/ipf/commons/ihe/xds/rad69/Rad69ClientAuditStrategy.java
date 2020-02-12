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
package org.openehealth.ipf.commons.ihe.xds.rad69;

import java.util.stream.Stream;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsIRetrieveAuditStrategy30;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status;
import org.openehealth.ipf.commons.ihe.xds.core.audit.codes.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.event.DicomInstancesAccessedEventBuilder;

/**
 * Audit strategy for Retrieve Imaging Document Set [RAD-69] as "Imaging Document Consumer" actor
 *
 * @author Clay Sebourn
 * @author Christian Ohr
 * @author Eugen Fischer
 */
public class Rad69ClientAuditStrategy extends XdsIRetrieveAuditStrategy30 {

    public Rad69ClientAuditStrategy() {
        super(false);
    }

    @Override
    public AuditMessage[] makeAuditMessage(
            final AuditContext auditContext,
            final XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset) {

        return Stream.of(Status.values())
                .filter(auditDataset::hasDocuments)
                .map(s -> doMakeAuditMessage(auditContext, auditDataset, s))
                .toArray(AuditMessage[]::new);
    }

    private static AuditMessage doMakeAuditMessage(
            final AuditContext auditContext,
            final XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset,
            final Status status) {

        return new DicomInstancesAccessedEventBuilder(auditContext, auditDataset,
                auditDataset.getEventOutcomeIndicator(status), null,
                EventActionCode.Create,
                XdsEventTypeCode.RetrieveImagingDocumentSet, auditDataset.getPurposesOfUse())
                .setPatient(auditDataset.getPatientId())
                .addTransferredStudy(auditDataset, status, false)
                .getMessage();
    }

}
