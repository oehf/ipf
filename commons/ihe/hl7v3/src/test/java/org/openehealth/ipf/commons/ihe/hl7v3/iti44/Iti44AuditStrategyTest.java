/*
 * Copyright 2017 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.hl7v3.iti44;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.hl7v3.atna.HL7v3AuditorTestBase;
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditDataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Christian Ohr
 */
public class Iti44AuditStrategyTest extends HL7v3AuditorTestBase<Iti44AuditStrategy> {

    @Test
    public void testCreateServerSide() {
        testRequest(true, EventActionCode.Create);
    }

    @Test
    public void testCreateClientSide() {
        testRequest(false, EventActionCode.Create);
    }

    @Test
    public void testUpdateServerSide() {
        testRequest(true, EventActionCode.Update);
    }

    @Test
    public void testUpdateClientSide() {
        testRequest(false, EventActionCode.Update);
    }


    private void testRequest(boolean serverSide, EventActionCode eventActionCode) {
        Iti44AuditStrategy strategy = new Iti44AuditStrategy(serverSide);
        Hl7v3AuditDataset auditDataset = getHl7v3AuditDataset(strategy);
        switch (eventActionCode) {
            case Create: auditDataset.setRequestType("PRPA_IN201301UV02"); break;
            case Update: auditDataset.setRequestType("PRPA_IN201302UV02"); break;
            case Delete: auditDataset.setRequestType("PRPA_IN201302UV03"); break;
        }
        AuditMessage auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();
        assertCommonV3AuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                EventIdCode.PatientRecord,
                eventActionCode,
                serverSide,
                true);

        assertEquals(1, auditMessage.getParticipantObjectIdentifications().size());
    }

}
