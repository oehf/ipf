/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti62;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRemoveMetadataAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRemoveMetadataAuditStrategy30;
import org.openehealth.ipf.commons.ihe.xds.core.audit.codes.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.event.XdsPatientRecordEventBuilder;

import java.util.Arrays;
import java.util.Collections;

/**
 * Client audit strategy for ITI-62.
 *
 * @author Boris Stanojevic
 */
public class Iti62AuditStrategy extends XdsRemoveMetadataAuditStrategy30 {

    public Iti62AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, XdsRemoveMetadataAuditDataset auditDataset) {
        return new XdsPatientRecordEventBuilder(auditContext, auditDataset, EventActionCode.Delete,
                XdsEventTypeCode.RemoveMetadata, auditDataset.getPurposesOfUse())
                        .addPatients(auditDataset.getPatientIds())
                        .addObjectIds(auditDataset.getObjectIds() != null ? Arrays.asList(auditDataset.getObjectIds())
                                : Collections.emptyList(), ParticipantObjectDataLifeCycle.PermanentErasure)
                        .getMessages();
    }
}
