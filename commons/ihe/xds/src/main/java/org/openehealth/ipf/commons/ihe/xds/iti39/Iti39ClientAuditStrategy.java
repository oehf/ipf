/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti39;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.*;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status;

import java.util.stream.Stream;

/**
 * Client audit strategy for ITI-39.
 *
 * @author Dmytro Rud
 * @author Christian Ohr
 */
public class Iti39ClientAuditStrategy extends XdsRetrieveAuditStrategy30 {

    public Iti39ClientAuditStrategy() {
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
        return new XdsDataImportBuilder(auditContext, auditDataset,
                auditDataset.getEventOutcomeIndicator(status), EventActionCode.Create,
                XdsEventTypeCode.CrossGatewayRetrieve, auditDataset.getPurposesOfUse())
                .setPatient(auditDataset.getPatientId())
                .addDocumentIds(auditDataset, status)
                .getMessage();
    }
}
