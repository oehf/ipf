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

package org.openehealth.ipf.commons.ihe.hpd.iti59;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.event.PHIImportBuilder;
import org.openehealth.ipf.commons.ihe.hpd.audit.codes.HpdEventTypeCode;
import org.openehealth.ipf.commons.ihe.hpd.audit.codes.HpdParticipantObjectIdTypeCode;

import java.util.Collections;

/**
 * @author Christian Ohr
 */
public class Iti59ServerAuditStrategy extends Iti59AuditStrategy {

    public Iti59ServerAuditStrategy() {
        super(true);
    }

    protected AuditMessage makeAuditMessage(AuditContext auditContext, Iti59AuditDataset auditDataset, Iti59AuditDataset.RequestItem requestItem) {
        PHIImportBuilder builder = new PHIImportBuilder<>(
                auditContext,
                auditDataset,
                requestItem.getOutcomeCode(),
                requestItem.getOutcomeDescription(),
                requestItem.getActionCode(),
                HpdEventTypeCode.ProviderInformationFeed,
                auditDataset.getPurposesOfUse());

        requestItem.getProviderIds().forEach(providerId ->
                builder.addImportedEntity(
                        providerId,
                        HpdParticipantObjectIdTypeCode.ProviderIdentifier,
                        ParticipantObjectTypeCodeRole.Provider,
                        Collections.emptyList()
                ));

        return builder.getMessage();
    }
}
