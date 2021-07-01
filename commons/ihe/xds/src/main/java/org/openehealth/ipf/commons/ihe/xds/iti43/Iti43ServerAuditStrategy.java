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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.xds.core.audit.*;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status;
import org.openehealth.ipf.commons.ihe.xds.core.audit.codes.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.event.XdsPHIExportBuilder;

import java.util.stream.Stream;

/**
 * Server audit strategy for ITI-43.
 *
 * @author Dmytro Rud
 * @author Christian Ohr
 */
public class Iti43ServerAuditStrategy extends XdsRetrieveAuditStrategy30 {

    public Iti43ServerAuditStrategy() {
        super(true);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset) {
        return Stream.of(Status.values())
                .filter(auditDataset::hasDocuments)
                .map((Status s) -> {
                    final var auditMessage = doMakeAuditMessage(auditContext, auditDataset, s);
                    // Fix the @AlternativeUserID (the process ID) that shall go into the source, not the destination
                    auditMessage.getActiveParticipants().get(0).setAlternativeUserID(AuditUtils.getProcessId());
                    auditMessage.getActiveParticipants().get(1).setAlternativeUserID(null);
                    return auditMessage;
                })
                .toArray(AuditMessage[]::new);
    }

    private AuditMessage doMakeAuditMessage(AuditContext auditContext, XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset, Status status) {
        return new XdsPHIExportBuilder(auditContext, auditDataset,
                auditDataset.getEventOutcomeIndicator(status), null,
                EventActionCode.Read,
                XdsEventTypeCode.RetrieveDocumentSet, auditDataset.getPurposesOfUse())
                .setPatient(auditDataset.getPatientId())
                .addDocumentIds(auditDataset, status, false)
                .getMessage();
    }

}