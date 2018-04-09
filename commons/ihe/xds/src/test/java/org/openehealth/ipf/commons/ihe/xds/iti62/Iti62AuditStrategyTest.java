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

package org.openehealth.ipf.commons.ihe.xds.iti62;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset.HumanUser;
import org.openehealth.ipf.commons.ihe.xds.atna.XdsAuditorTestBase;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRemoveMetadataAuditDataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Christian Ohr
 */
public class Iti62AuditStrategyTest extends XdsAuditorTestBase {

    @Test
    public void testServerSide() {
        testRequest(true);
    }

    @Test
    public void testClientSide() {
        testRequest(false);
    }

    private void testRequest(boolean serverSide) {
        Iti62AuditStrategy strategy = new Iti62AuditStrategy(serverSide);
        XdsRemoveMetadataAuditDataset auditDataset = getXdsAuditDataset(strategy);
        AuditMessage auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();

        // System.out.println(printAuditMessage(auditMessage));

        assertCommonXdsAuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                EventIdCode.PatientRecord,
                EventActionCode.Delete,
                serverSide,
                true);

        assertEquals(OBJECT_UUIDS.length, auditMessage.findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectTypeCodeRole() == ParticipantObjectTypeCodeRole.Report).size());
    }

    private XdsRemoveMetadataAuditDataset getXdsAuditDataset(Iti62AuditStrategy strategy) {
        XdsRemoveMetadataAuditDataset auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.setPurposesOfUse(PURPOSES_OF_USE);
        auditDataset.getPatientIds().add(PATIENT_IDS[0]);
        auditDataset.setObjectIds(OBJECT_UUIDS);
        auditDataset.getHumanUsers().add(new HumanUser(USER_ID, USER_NAME, USER_ROLES));

        return auditDataset;
    }
}
