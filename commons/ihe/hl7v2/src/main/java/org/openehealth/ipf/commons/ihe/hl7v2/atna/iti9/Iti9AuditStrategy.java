/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti9;

import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEQueryBuilder;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.MllpEventTypeCode;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.hl7v2.atna.MllpParticipantObjectIdTypeCode.PIXQuery;

public class Iti9AuditStrategy extends AuditStrategySupport<QueryAuditDataset> {

    public Iti9AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public QueryAuditDataset createAuditDataset() {
        return new QueryAuditDataset(isServerSide());
    }

    @Override
    public QueryAuditDataset enrichAuditDatasetFromRequest(QueryAuditDataset auditDataset,
                                                           Object msg, Map<String, Object> parameters) {
        Iti9AuditStrategyUtils.enrichAuditDatasetFromRequest(auditDataset, (Message) msg, parameters);
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(QueryAuditDataset auditDataset,
                                                  Object msg) {
        return Iti9AuditStrategyUtils.enrichAuditDatasetFromResponse(auditDataset, (Message) msg);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, QueryAuditDataset auditDataset) {
        return new IHEQueryBuilder(auditContext,auditDataset, MllpEventTypeCode.PIXQuery)
                .setQueryParameters(
                        auditDataset.getMessageControlId(),
                        PIXQuery,
                        auditDataset.getPayload(),
                        "MSH-10", auditDataset.getMessageControlId())
                .addPatients(auditDataset.getPatientIds())
                .getMessages();
    }

}
