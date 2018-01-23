/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v2.audit.iti9

import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.Parser
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.DefaultAuditContext
import org.openehealth.ipf.commons.ihe.hl7v2.audit.QueryAuditDataset
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions

/**
 * Test for #144
 */
class Iti9AuditStrategyEnrichTest {

    private static final Parser PARSER = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pix", "2.5"),
            PixPdqTransactions.ITI9).pipeParser
    private String msg
    private AuditContext auditContext

    @Before
    void setup() {
        msg = getClass().getResourceAsStream("/rsp.hl7").text
        auditContext = new DefaultAuditContext();
    }

    @Test
    void testEnrichResponse() {
        Iti9AuditStrategy strategy = new Iti9AuditStrategy(false)
        QueryAuditDataset dataset = new QueryAuditDataset(false)
        Message message = PARSER.parse(msg)
        strategy.enrichAuditDatasetFromResponse(dataset, message, auditContext)
        // println dataset
    }
}
