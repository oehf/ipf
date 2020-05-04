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

package org.openehealth.ipf.commons.ihe.hl7v2.audit.iti64;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.Hl7v2AuditorTestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Christian Ohr
 */
public class Iti64AuditStrategyTest extends Hl7v2AuditorTestBase {

    @Test
    public void testServerSide() {
        testRequest(true);
    }

    @Test
    public void testClientSide() {
        testRequest(false);
    }

    private void testRequest(boolean serverSide) {
        var strategy = new Iti64AuditStrategy(serverSide);
        var auditDataset = getHl7v2AuditDataset(strategy);
        var auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();
        assertCommonV2AuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                EventIdCode.PatientRecord,
                EventActionCode.Update,
                serverSide,
                true);

        assertEquals(2, auditMessage.findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectDataLifeCycle() == ParticipantObjectDataLifeCycle.Origination).size());
        assertEquals(2, auditMessage.findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectDataLifeCycle() == ParticipantObjectDataLifeCycle.LogicalDeletion).size());
        assertEquals(1, auditMessage.findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectTypeCode() == ParticipantObjectTypeCode.System).size());

        // System.out.println(printAuditMessage(auditMessage));
    }

    private Iti64AuditDataset getHl7v2AuditDataset(Iti64AuditStrategy strategy) {
        var auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setMessageControlId(MESSAGE_ID);
        auditDataset.setNewPatientId(PATIENT_IDS[0]);
        auditDataset.setPreviousPatientId(PATIENT_IDS[1]);
        auditDataset.setLocalPatientId(PATIENT_IDS[0]);
        auditDataset.setSubsumedLocalPatientId(PATIENT_IDS[1]);
        auditDataset.setSendingFacility(SENDING_FACILITY);
        auditDataset.setSendingApplication(SENDING_APPLICATION);
        auditDataset.setReceivingFacility(RECEIVING_FACILITY);
        auditDataset.setReceivingApplication(RECEIVING_APPLICATION);
        auditDataset.setSubmissionSetUuid(SUBMISSION_SET_UUID);
        return auditDataset;
    }
}
