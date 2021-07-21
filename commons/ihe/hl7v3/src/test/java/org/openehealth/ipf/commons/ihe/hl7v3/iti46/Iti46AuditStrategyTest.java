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

package org.openehealth.ipf.commons.ihe.hl7v3.iti46;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.hl7v3.atna.HL7v3AuditorTestBase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Christian Ohr
 */
public class Iti46AuditStrategyTest extends HL7v3AuditorTestBase<Iti46AuditStrategy> {

    @Test
    public void testServerSide() {
        testRequest(true);
    }

    @Test
    public void testClientSide() {
        testRequest(false);
    }

    private void testRequest(boolean serverSide) {
        var strategy = new Iti46AuditStrategy(serverSide);
        var auditDataset = getHl7v3AuditDataset(strategy);
        var auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();
        assertCommonV3AuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                EventIdCode.PatientRecord,
                serverSide ? EventActionCode.Update : EventActionCode.Read,
                serverSide,
                true);
    }

}
