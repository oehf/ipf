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

package org.openehealth.ipf.commons.ihe.xds.iti61;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset.HumanUser;
import org.openehealth.ipf.commons.ihe.xds.atna.XdsAuditorTestBase;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditStrategy30;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

/**
 * @author Christian Ohr
 */
public class Iti61AuditStrategyTest extends XdsAuditorTestBase {

    @Test
    public void testServerSide() {
        testRequest(true, new Iti61ServerAuditStrategy());
    }

    @Test
    public void testClientSide() {
        testRequest(false, new Iti61ClientAuditStrategy());
    }


    private void testRequest(boolean serverSide, XdsSubmitAuditStrategy30 strategy) {
        XdsSubmitAuditDataset auditDataset = getXdsAuditDataset(strategy);
        AuditMessage auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();

        // System.out.println(printAuditMessage(auditMessage));

        assertCommonXdsAuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                serverSide ? EventIdCode.Import : EventIdCode.Export,
                serverSide ? EventActionCode.Create : EventActionCode.Read,
                serverSide,
                true);
    }

    private XdsSubmitAuditDataset getXdsAuditDataset(XdsSubmitAuditStrategy30 strategy) {
        XdsSubmitAuditDataset auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.setPurposesOfUse(PURPOSES_OF_USE);
        auditDataset.setHomeCommunityId(HOME_COMMUNITY_ID);
        auditDataset.setSubmissionSetUuid(SUBMISSION_SET_UUID);
        auditDataset.getPatientIds().addAll(Arrays.asList(PATIENT_IDS));
        auditDataset.getHumanUsers().add(new HumanUser(USER_ID, USER_NAME, USER_ROLES));

        return auditDataset;
    }
}
