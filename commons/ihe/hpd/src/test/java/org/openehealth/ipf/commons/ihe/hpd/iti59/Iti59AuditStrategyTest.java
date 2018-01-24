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

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.hpd.atna.HpdAuditorTestBase;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * @author Christian Ohr
 */
public class Iti59AuditStrategyTest extends HpdAuditorTestBase {

    @Test
    public void testServerSide() {
        testRequest(true, new Iti59ServerAuditStrategy());
    }

    @Test
    public void testClientSide() {
        testRequest(false, new Iti59ClientAuditStrategy());
    }

    private void testRequest(boolean serverSide, Iti59AuditStrategy strategy) {
        Iti59AuditDataset auditDataset = getHpdAuditDataset(strategy);
        AuditMessage[] auditMessages = makeAuditMessages(strategy, auditContext, auditDataset);

        assertEquals(2, auditMessages.length);

        for (AuditMessage auditMessage : auditMessages) {
            auditMessage.validate();
            // System.out.println(printAuditMessage(auditMessage));
        }

        assertCommonHpdAuditAttributes(auditMessages[0],
                EventOutcomeIndicator.Success,
                serverSide ? EventIdCode.Import : EventIdCode.Export,
                EventActionCode.Update,
                serverSide);

        assertEquals(PROVIDER_IDS.length, auditMessages[0].findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectTypeCodeRole() == ParticipantObjectTypeCodeRole.Provider).size());
    }

    private Iti59AuditDataset getHpdAuditDataset(Iti59AuditStrategy strategy) {
        Iti59AuditDataset auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setUserName(USER_NAME);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.setPurposesOfUse(NEW_PURPOSES_OF_USE);
        auditDataset.getUserRoles().addAll(NEW_USER_ROLES);

        Iti59AuditDataset.RequestItem requestItem1 = new Iti59AuditDataset.RequestItem(
                QUERY_ID,
                EventActionCode.Update,
                new HashSet<>(Arrays.asList(PROVIDER_IDS)));
        requestItem1.setDn("oldDn");
        requestItem1.setNewRdn("newDn");
        requestItem1.setOutcomeCode(EventOutcomeIndicator.Success);
        Iti59AuditDataset.RequestItem requestItem2 = new Iti59AuditDataset.RequestItem(
                QUERY_ID,
                EventActionCode.Create,
                new HashSet<>(Arrays.asList(PROVIDER_IDS)));
        requestItem2.setOutcomeCode(EventOutcomeIndicator.Success);
        auditDataset.setRequestItems(new Iti59AuditDataset.RequestItem[]{requestItem1, requestItem2});

        return auditDataset;
    }
}
