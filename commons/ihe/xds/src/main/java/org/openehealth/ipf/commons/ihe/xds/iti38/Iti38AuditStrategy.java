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

package org.openehealth.ipf.commons.ihe.xds.iti38;

import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEQueryBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.audit.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christian Ohr
 */
public class Iti38AuditStrategy extends XdsQueryAuditStrategy30 {

    public Iti38AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(XdsQueryAuditDataset auditDataset) {
        return new XdsQueryBuilder(auditDataset, XdsEventTypeCode.CrossGatewayQuery, auditDataset.getPurposesOfUse())
                .addPatients(auditDataset.getPatientId())
                .setQueryParameters(auditDataset, XdsParticipantObjectIdTypeCode.CrossGatewayQuery)
                .getMessages();
    }
}
