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
package org.openehealth.ipf.commons.ihe.xds.iti63;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.*;
import org.openehealth.ipf.commons.ihe.xds.core.audit.codes.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.codes.XdsParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.event.XdsQueryInformationBuilder;

/**
 * Audit strategy for the XCF ITI-63 transaction.
 *
 * @author Dmytro Rud
 * @author Christian Ohr
 */
public class Iti63AuditStrategy extends XdsQueryAuditStrategy30 {

    public Iti63AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, XdsQueryAuditDataset auditDataset) {
        return new XdsQueryInformationBuilder(auditContext, auditDataset, XdsEventTypeCode.CrossCommunityFetch, auditDataset.getPurposesOfUse())
                .addPatients(auditDataset.getPatientId())
                .setQueryParameters(auditDataset, XdsParticipantObjectIdTypeCode.CrossCommunityFetch)
                .getMessages();
    }

}
