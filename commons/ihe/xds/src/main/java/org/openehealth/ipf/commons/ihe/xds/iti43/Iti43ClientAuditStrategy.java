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
package org.openehealth.ipf.commons.ihe.xds.iti43;

import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.*;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status;

import java.util.stream.Stream;

/**
 * Client audit strategy for ITI-43.
 *
 * @author Dmytro Rud
 * @author Christian Ohr
 */
public class Iti43ClientAuditStrategy extends XdsRetrieveAuditStrategy30 {

    public Iti43ClientAuditStrategy() {
        super(false);
    }

    @Override
    public AuditMessage[] makeAuditMessage(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset) {
        return Stream.of(Status.values())
                .filter(auditDataset::hasDocuments)
                .map(s -> doMakeAuditMessage(auditDataset, s))
                .toArray(AuditMessage[]::new);
    }

    private AuditMessage doMakeAuditMessage(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset, Status status) {
        return new XdsDataImportBuilder(auditDataset, XdsEventTypeCode.RetrieveDocumentSet, auditDataset.getPurposesOfUse())
                .setPatient(auditDataset.getPatientId())
                .addDocumentIds(auditDataset, status, XdsParticipantObjectIdTypeCode.RetrieveDocumentSet)
                .getMessage();
    }
}
