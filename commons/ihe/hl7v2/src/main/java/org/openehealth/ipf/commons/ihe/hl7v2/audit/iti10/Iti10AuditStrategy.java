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
package org.openehealth.ipf.commons.ihe.hl7v2.audit.iti10;

import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.event.DefaultPatientRecordEventBuilder;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.QueryAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.codes.MllpEventTypeCode;

import java.util.Map;

public class Iti10AuditStrategy extends AuditStrategySupport<QueryAuditDataset> {

    public Iti10AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public QueryAuditDataset createAuditDataset() {
        return new QueryAuditDataset(isServerSide());
    }

    @Override
    public QueryAuditDataset enrichAuditDatasetFromRequest(QueryAuditDataset auditDataset, Object msg, Map<String, Object> parameters) {
        Iti10AuditStrategyUtils.enrichAuditDatasetFromRequest(auditDataset, (Message) msg);
        return auditDataset;
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, QueryAuditDataset auditDataset) {
        return new DefaultPatientRecordEventBuilder(
                auditContext,
                auditDataset,
                isServerSide() ? EventActionCode.Update : EventActionCode.Read,
                MllpEventTypeCode.PIXUpdateNotification)

                // Type=MSH-10 (the literal string), Value=the value of MSH-10 (from the message content, base64 encoded)
                .addPatients("MSH-10", auditDataset.getMessageControlId(), auditDataset.getPatientIds())
                .getMessages();

    }


}
