/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti51;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.*;

/**
 * Base audit strategy for ITI-51.
 *
 * @author Dmytro Rud
 * @author Michael Ottati
 */
public class Iti51AuditStrategy extends XdsQueryAuditStrategy30 {

    /**
     * Constructs the audit strategy.
     *
     * @param serverSide whether this is a server-side or a client-side strategy.
     */
    public Iti51AuditStrategy(boolean serverSide) {
        super(serverSide);
    }


    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, XdsQueryAuditDataset auditDataset) {
        return auditDataset.getPatientIds().isEmpty() ?
                new AuditMessage[]{doMakeAuditMessage(auditContext, auditDataset, null)} :
                auditDataset.getPatientIds().stream()
                        .map(patientId -> doMakeAuditMessage(auditContext, auditDataset, patientId))
                        .toArray(AuditMessage[]::new);
    }

    private AuditMessage doMakeAuditMessage(AuditContext auditContext, XdsQueryAuditDataset auditDataset, String pid) {
        return new XdsQueryBuilder(auditContext, auditDataset, XdsEventTypeCode.MultiPatientStoredQuery, auditDataset.getPurposesOfUse())
                .addPatients(pid)
                .setQueryParameters(auditDataset, XdsParticipantObjectIdTypeCode.MultiPatientStoredQuery)
                .getMessage();

    }
}
