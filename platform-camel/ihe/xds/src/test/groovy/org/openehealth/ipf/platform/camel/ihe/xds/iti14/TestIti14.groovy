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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * Tests the auditing of ITI-14.
 * @author Jens Riemschneider
 */
class TestIti14 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-14.xml'
    
    def SERVICE1 = "xds-iti14://localhost:${port}/xds-iti14-service1";
    def SERVICE2 = "xds-iti14://localhost:${port}/xds-iti14-service2";
    
    def SERVICE_FT = "xds-iti14://localhost:${port}/xds-iti14-service12?audit=false&allowIncompleteAudit=true";
    def SERVICE_DT = "xds-iti14://localhost:${port}/xds-iti14-service13?allowIncompleteAudit=true";
    
    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti14-service2"
    
    def request
    def docEntry
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    void setUp() {
        request = SampleData.createRegisterDocumentSet();
        docEntry = request.documentEntries[0]
    }
    
    @Test
    void testIti14() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        assert auditSender.messages.size() == 4
        checkPackets('0')
    }
    
    @Test
    void testIti14_FailureAudit() {
        assert FAILURE == sendIt(SERVICE2, 'service falsch').status
        assert auditSender.messages.size() == 2
        checkPackets('8')
    }
    
    void checkPackets(outcome) {
        def message = getAudit('C', SERVICE2_ADDR)[0]
        
        assert message.AuditSourceIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.ParticipantObjectIdentification.size() == 2
        assert message.children().size() == 6
        
        checkEvent(message.EventIdentification, '110107', 'ITI-14', 'C', outcome)
        checkSource(message.ActiveParticipant[0], 'true')
        checkDestination(message.ActiveParticipant[1], SERVICE2_ADDR, 'false')
        checkAuditSource(message.AuditSourceIdentification, 'registryId')
        checkPatient(message.ParticipantObjectIdentification[0])
        checkSubmissionSet(message.ParticipantObjectIdentification[1])
        
        message = getAudit('R', SERVICE2_ADDR)[0]
        
        assert message.AuditSourceIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.ParticipantObjectIdentification.size() == 2
        assert message.children().size() == 6
        
        checkEvent(message.EventIdentification, '110106', 'ITI-14', 'R', outcome)
        checkSource(message.ActiveParticipant[0], 'true')
        checkDestination(message.ActiveParticipant[1], SERVICE2_ADDR, 'false')
        checkAuditSource(message.AuditSourceIdentification, 'repositoryId')
        checkPatient(message.ParticipantObjectIdentification[0])
        checkSubmissionSet(message.ParticipantObjectIdentification[1])
    }
    
    @Test
    void testIti14_AuditDisabled() {
        sendIt(SERVICE_FT, 'service 12')
        assert auditSender.messages.size() == 0
    }
    
    @Test
    void testIti14_Incomplete_IncompleteAuditingNotAllowed()  {
        request.submissionSet = null
        sendIt(SERVICE2, 'service 2')
        assert auditSender.messages.size() == 0
    }
    
    @Test
    void testIti14_Incomplete_IncompleteAuditingAllowed() {
        request.submissionSet = null
        sendIt(SERVICE_DT, 'service 13')
        assert auditSender.messages.size() == 2
        
        // No assumption on what is actually logged, as it is faulty anyway and highly
        // depends on what OHT does.
    }
    
    def sendIt(endpoint, value) {
        docEntry.setComments(new LocalizedString(value))
        send(endpoint, request, Response.class)
    }
}
