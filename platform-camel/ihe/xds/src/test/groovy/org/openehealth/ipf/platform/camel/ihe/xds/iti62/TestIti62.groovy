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
package org.openehealth.ipf.platform.camel.ihe.xds.iti62

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Version
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-62 transaction with a webservice and client adapter defined via URIs.
 * @author Boris Stanojevic
 */
class TestIti62 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-62.xml'
    
    def SERVICE1 = "xds-iti62://localhost:${port}/xds-iti62-service1"
    def SERVICE2 = "xds-iti62://localhost:${port}/xds-iti62-service2"

    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti62-service2"

    RemoveDocumentSet request

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    void setUp() {
        request = SampleData.createRemoveDocumentSet()
    }
    
    @Test
    void testIti62() {
        assert SUCCESS == sendIt(SERVICE1).status
        assert SUCCESS == sendIt(SERVICE2).status
        assert auditSender.messages.size() == 4

        checkAudit("0")
    }
    
    @Test
    void testIti62FailureAudit() {
        request.getReferences().add(new ObjectReference("wrong-id", "not-at-home"))
        assert FAILURE == sendIt(SERVICE1).status
        assert auditSender.messages.size() == 2
    }

    @Test
    void testIti62NotValidAudit() {
        request.setDeletionScope("blah")
        assert FAILURE == sendIt(SERVICE1).status
        assert auditSender.messages.size() == 2
    }

    void checkAudit(outcome) {
        def message = getAudit('D', SERVICE2_ADDR)[0]

        assert message.EventIdentification.size() == 1
        assert message.AuditSourceIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.children().size() == 4

        checkEvent(message.EventIdentification, '110107', 'ITI-62', 'D', outcome)
        checkSource(message.ActiveParticipant[0], 'true')
        checkDestination(message.ActiveParticipant[1], SERVICE2_ADDR, 'false')
        checkAuditSource(message.AuditSourceIdentification, 'customXdsSourceId')

        message = getAudit('D', SERVICE2_ADDR)[1]

        assert message.EventIdentification.size() == 1
        assert message.AuditSourceIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.children().size() == 4

        checkEvent(message.EventIdentification, '110106', 'ITI-62', 'D', outcome)
        checkSource(message.ActiveParticipant[0], 'true')
        checkDestination(message.ActiveParticipant[1], SERVICE2_ADDR, 'false')
        checkAuditSource(message.AuditSourceIdentification, 'customXdsSourceId')
    }

    def sendIt(endpoint) {
        return send(endpoint, request, Response.class)
    }

}