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

package org.openehealth.ipf.commons.ihe.xds.iti86;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.atna.XdsAuditorTestBase;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Christian Ohr
 */
public class Iti86AuditStrategyTest extends XdsAuditorTestBase {

    @Test
    public void testServerSide() {
        testRequest(true);
    }

    @Test
    public void testClientSide() {
        testRequest(false);
    }

    private void testRequest(boolean serverSide) {
        Iti86AuditStrategy strategy = new Iti86AuditStrategy(serverSide);
        XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset = getXdsAuditDataset(strategy);
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

        assertEquals(3, auditMessage.findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectTypeCodeRole() == ParticipantObjectTypeCodeRole.Report).size());
    }

    private XdsNonconstructiveDocumentSetRequestAuditDataset getXdsAuditDataset(Iti86AuditStrategy strategy) {
        XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setUserName(USER_NAME);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.setPurposesOfUse(NEW_PURPOSES_OF_USE);
        auditDataset.getUserRoles().addAll(NEW_USER_ROLES);
        auditDataset.getPatientIds().add(PATIENT_IDS[0]);

        for (int i = 0; i < 3; i++) {
            auditDataset.getDocuments().add(
                    new XdsNonconstructiveDocumentSetRequestAuditDataset.Document(
                            DOCUMENT_OIDS[i],
                            REPOSITORY_OIDS[i],
                            HOME_COMMUNITY_IDS[i],
                            null,
                            null,
                            XdsNonconstructiveDocumentSetRequestAuditDataset.Status.SUCCESSFUL));
        }
        return auditDataset;
    }
}
