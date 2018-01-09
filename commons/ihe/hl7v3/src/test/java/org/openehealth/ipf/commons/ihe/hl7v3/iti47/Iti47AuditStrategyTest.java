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

package org.openehealth.ipf.commons.ihe.hl7v3.iti47;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorTestBase;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;

import static org.junit.Assert.assertNotNull;

/**
 * @author Christian Ohr
 */
public class Iti47AuditStrategyTest extends AuditorTestBase {

    @Test
    public void testServerSide() {
        testRequest(true);
    }

    @Test
    public void testClientSide() {
        testRequest(false);
    }

    private void testRequest(boolean serverSide) {
        Iti47AuditStrategy strategy = new Iti47AuditStrategy(serverSide);
        Hl7v3AuditDataset auditDataset = getHl7v3AuditDataset(strategy);
        AuditMessage auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();
        assertCommonV3AuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                EventIdCode.Query,
                EventActionCode.Execute,
                serverSide,
                true);
    }

    private Hl7v3AuditDataset getHl7v3AuditDataset(Iti47AuditStrategy strategy) {
        Hl7v3AuditDataset auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setMessageId(MESSAGE_ID);
        auditDataset.setPatientIds(PATIENT_IDS);
        auditDataset.setUserName(USER_NAME);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.getPurposesOfUse().addAll(NEW_PURPOSES_OF_USE);
        auditDataset.getUserRoles().addAll(NEW_USER_ROLES);
        return auditDataset;
    }
}
