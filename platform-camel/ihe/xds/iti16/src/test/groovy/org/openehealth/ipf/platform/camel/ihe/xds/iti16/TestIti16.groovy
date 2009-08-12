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
package org.openehealth.ipf.platform.camel.ihe.xds.iti16

import static junit.framework.Assert.assertEquals
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status.*

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.xds.commons.SampleData
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.SqlQuery
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse


/**
 * Tests the ITI-16 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
class TestIti16 extends StandardTestContainer {

    def SERVICE1 = "xds-iti16://localhost:${port}/xds-iti16-service1"
    def SERVICE2 = "xds-iti16://localhost:${port}/xds-iti16-service2"

    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti16-service2"
    
    def request
    def query
    
    @BeforeClass
    static void classSetUp() throws Exception {
        startServer(new CXFServlet(), 'iti-16.xml')
    }

    @Before
    void setUp() {
        request = SampleData.createSqlQuery()      
        query = request.query
    }
    
    @Test
    void testIti16() {
        syslog.expectedPacketCount(4)
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        syslog.assertIsSatisfied()
        checkAudit('0', 'service 2')
    }
    
    @Test
    void testIti16AuditFailure() {
        syslog.expectedPacketCount(2)
        assert FAILURE == sendIt(SERVICE2, 'service falsch').status
        syslog.assertIsSatisfied()
        checkAudit('0', 'service falsch')
    }
    
    void checkAudit(outcome, expectedQueryText) {        
        def messages = getAudit('E', SERVICE2_ADDR)
        assert messages.size() == 2
        
        messages.each { message ->
            assert message.AuditSourceIdentification.size() == 1
            assert message.ActiveParticipant.size() == 2
            assert message.ParticipantObjectIdentification.size() == 1
            assert message.children().size() == 5
            
            checkEvent(message.EventIdentification, '110112', 'ITI-16', 'E', outcome)
            checkSource(message.ActiveParticipant[0], 'true')
            checkDestination(message.ActiveParticipant[1], SERVICE2_ADDR, 'false')
            checkQuery(message.ParticipantObjectIdentification[0], 'ITI-16', expectedQueryText, '')
//            checkPatient(message.ParticipantObjectIdentification[0])
        }
    }
    
    def sendIt(endpoint, value) {
        query.sql = value
        send(endpoint, request, QueryResponse.class)
    }
}
