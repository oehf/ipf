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
import org.openehealth.ipf.commons.ihe.core.atna.event.PHIExportBuilder;
import org.openehealth.ipf.commons.ihe.hpd.audit.codes.HpdEventTypeCode;
import org.openehealth.ipf.commons.ihe.hpd.audit.codes.HpdParticipantObjectIdTypeCode;

import java.util.Collections;

import static org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle.Translation;

/**
 * @author Christian Ohr
 */
public class Iti59ClientAuditStrategy extends Iti59AuditStrategy {

    public Iti59ClientAuditStrategy() {
        super(false);
    }

    protected AuditMessage makeAuditMessage(AuditContext auditContext,
                                            Iti59AuditDataset auditDataset,
                                            Iti59AuditDataset.RequestItem requestItem) {
        PHIExportBuilder builder = new PHIExportBuilder<>(
                auditContext,
                auditDataset,
                requestItem.getOutcomeCode(),
                null,
                requestItem.getActionCode(),
                HpdEventTypeCode.ProviderInformationFeed,
                auditDataset.getPurposesOfUse()
        );
        builder.addExportedEntity(
                requestItem.getUid(),
                HpdParticipantObjectIdTypeCode.RelativeDistinguishedName,
                requestItem.getParticipantObjectTypeCode(),
                ParticipantObjectTypeCodeRole.Provider,
                requestItem.getParticipantObjectDataLifeCycle(),
                Translation.equals(requestItem.getParticipantObjectDataLifeCycle())
                        ? Collections.emptyList()
                        : Collections.singletonList(builder.getTypeValuePair("old uid", requestItem.getUid()))
        );
        return builder.getMessage();
    }

}
