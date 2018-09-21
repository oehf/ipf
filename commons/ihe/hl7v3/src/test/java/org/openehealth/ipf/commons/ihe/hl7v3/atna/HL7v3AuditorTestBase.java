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

package org.openehealth.ipf.commons.ihe.hl7v3.atna;

import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.EventId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorTestBase;
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditStrategy;

/**
 * @author Christian Ohr
 */
public class HL7v3AuditorTestBase<T extends Hl7v3AuditStrategy> extends AuditorTestBase {

    protected void assertCommonV3AuditAttributes(AuditMessage auditMessage,
                                                 EventOutcomeIndicator eventOutcomeIndicator,
                                                 EventId eventId,
                                                 EventActionCode eventActionCode,
                                                 boolean serverSide,
                                                 boolean requiresPatient) {
        assertCommonAuditAttributes(auditMessage, eventOutcomeIndicator, eventId, eventActionCode,
                REPLY_TO_URI, SERVER_URI, serverSide, requiresPatient);
    }

    protected Hl7v3AuditDataset getHl7v3AuditDataset(T strategy) {
        Hl7v3AuditDataset auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setMessageId(MESSAGE_ID);
        auditDataset.setPatientIds(PATIENT_IDS);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setPurposesOfUse(PURPOSES_OF_USE);
        auditDataset.getHumanUsers().add(new AuditDataset.HumanUser(USER_ID, USER_NAME, USER_ROLES));
        return auditDataset;
    }
}
