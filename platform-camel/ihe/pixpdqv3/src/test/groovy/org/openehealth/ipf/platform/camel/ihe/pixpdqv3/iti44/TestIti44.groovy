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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti44

import static org.junit.Assert.*

import org.apache.camel.Consumer
import org.apache.camel.Route
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.*
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * Tests for ITI-44.
 * @author Dmytro Rud
 */
class TestIti44 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-44.xml'
    
    def SERVICE1_PIX = "pixv3-iti44://localhost:${port}/pixv3-iti44-service1";
    def SERVICE1_XDS = "xds-iti44://localhost:${port}/xds-iti44-service1";
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Test
    void testIti44Xds() {
        def response = send(SERVICE1_XDS, '<PRPA_IN201304UV02 />', String.class)
        def slurper = new XmlSlurper().parseText(response)
        assert slurper.@from == 'Document Registry'
    }
    
    
    @Test @Ignore
    void testRestart() {
        Consumer consumer = ((Route) camelContext.routes.find {
            it.consumer.endpoint.endpointUri == 'pixv3-iti44://pixv3-iti44-service1'
        }).consumer
        
        // consumer is not yet stopped and should be still able to serve requests
        send(SERVICE1_PIX, '<PRPA_IN201301UV02 />', String.class)
        
        // stop and check whether actually deactivated
        consumer.stop()
        boolean failed = false
        try {
            send(SERVICE1_PIX, '<PRPA_IN201301UV02 />', String.class)
        } catch (Exception e) {
            failed = true
        }
        assertTrue('it should be unable to send a message to stopped consumer', failed)
        
        // restart and check whether actually reactivated
        consumer.start()
        send(SERVICE1_PIX, '<PRPA_IN201301UV02 />', String.class)
    }
    
}
