/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8

import org.junit.Test
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters

import static org.junit.Assert.assertEquals

/**
 *
 */
class Iti8AuditStrategyUtilsTest {

    @Test
    void testExtractPatientId(){
        MessageAdapter message = MessageAdapters.load('iti8/iti8-a40.hl7')
        Iti8AuditDataset dataset = new Iti8AuditDataset(true);
        Iti8AuditStrategyUtils.enrichAuditDatasetFromRequest(dataset, message, null);
        assertEquals('305014^^^MPI-NS-P&2.16.840.1.113883.3.37.4.1.1.2.1.1&ISO'
                + '~7200117317^^^BBB&2.16.840.1.113883.3.37.4.1.1.2.611.1&ISO'
                + '~7200117355^^^CCC&2.16.840.1.113883.3.37.4.1.1.2.711.1&ISO', dataset.patientId)
        assertEquals('305010~7200117359^^^BBB&2.16.840.1.113883.3.37.4.1.1.2.611.1&ISO', dataset.oldPatientId)
    }
}
