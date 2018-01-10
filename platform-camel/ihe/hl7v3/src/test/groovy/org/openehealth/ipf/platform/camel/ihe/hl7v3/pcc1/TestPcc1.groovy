/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.pcc1

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.platform.camel.ihe.hl7v3.HL7v3StandardTestContainer

/**
 * Tests for PCC-1.
 * @author Dmytro Rud
 */
class TestPcc1 extends HL7v3StandardTestContainer {

    def static CONTEXT_DESCRIPTOR = 'pcc-1.xml'

    def SERVICE1 = "qed-pcc1://localhost:${port}/qed-pcc1-service1"

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void testPcc1() {
        String request = readFile('pcc1/pcc1-sample-request.xml')
        String response = send(SERVICE1, request, String.class)

        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.eventIdentification.eventActionCode == EventActionCode.Execute
            assert it.eventIdentification.eventOutcomeIndicator == EventOutcomeIndicator.Success
            assert it.participantObjectIdentifications.size() == 3
        }
    }
}
