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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17

import static junit.framework.Assert.assertEquals

import org.apache.commons.io.IOUtils
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * Tests the ITI-17 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
class TestIti17 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-17.xml'
    
    def SERVICE1 = "xds-iti17://localhost:${port}/xds-iti17-service1"
    def SERVICE2 = "xds-iti17://localhost:${port}/xds-iti17-service2"
    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti17-service2?service2"
    def SERVICE2_FALSCH_ADDR = "http://localhost:${port}/xds-iti17-service2?falsch"
    
    static void main(args) {
        startServer(new Iti17Servlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUp() {
        startServer(new Iti17Servlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Test
    void testIti17() {
        def response1 = send(SERVICE1, '?service1', InputStream.class)
        def content1 = IOUtils.toString(response1)
        response1.close()
        assertEquals('service1', content1)
        
        def response2 = send(SERVICE2, '?service2', InputStream.class)
        def content2 = IOUtils.toString(response2)
        response2.close()
        assertEquals('service2', content2)
        
        assert auditSender.messages.size() == 4
        
        checkAudit('0', SERVICE2_ADDR)
    }
    
    @Test
    void testIti17FailureAudit() {
        send(SERVICE2, '?falsch', InputStream.class)
        
        assert auditSender.messages.size() == 2
        
        checkAudit('12', SERVICE2_FALSCH_ADDR)
    }
    
    void checkAudit(outcome, addr) {
        def message = getAudit('R', addr)[0]
        
        assert message.AuditSourceIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.ParticipantObjectIdentification.size() == 1
        assert message.children().size() == 5
        
        checkEvent(message.EventIdentification, '110106', 'ITI-17', 'R', outcome)
        checkSource(message.ActiveParticipant[0], 'false')
        checkDestination(message.ActiveParticipant[1], 'true')
        checkAuditSource(message.AuditSourceIdentification, 'repositoryId')
        checkUri(message.ParticipantObjectIdentification[0], addr, '')
        
        message = getAudit('C', addr)[0]
        
        assert message.AuditSourceIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.ParticipantObjectIdentification.size() == 1
        assert message.children().size() == 5
        
        checkEvent(message.EventIdentification, '110107', 'ITI-17', 'C', outcome)
        checkSource(message.ActiveParticipant[0], 'false')
        checkDestination(message.ActiveParticipant[1], 'true')
        checkUri(message.ParticipantObjectIdentification[0], addr, '')
    }
}