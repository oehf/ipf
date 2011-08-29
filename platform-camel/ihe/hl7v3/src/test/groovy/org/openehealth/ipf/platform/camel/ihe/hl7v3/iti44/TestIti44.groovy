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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti44

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

    private static final String ADD_REQUEST =
            readFile('translation/pixfeed/v3/PIX_FEED_REG_Maximal_Request.xml')
    private static final String REVISE_REQUEST =
            readFile('translation/pixfeed/v3/PIX_FEED_REV_Maximal_Request.xml')
    private static final String MERGE_REQUEST =
            readFile('translation/pixfeed/v3/PIX_FEED_MERGE_Maximal_Request.xml')


    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }


    @Test
    void testIti44Add() {
        def response = send(SERVICE1_XDS, ADD_REQUEST, String.class)
        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventActionCode="C"')
            assert it.toString().contains('EventOutcomeIndicator="0"')
        }
    }
    
    @Test
    void testIti44Revise() {
        def response = send(SERVICE1_XDS, REVISE_REQUEST, String.class)
        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventActionCode="U"')
            assert it.toString().contains('EventOutcomeIndicator="0"')
        }
    }

    @Test
    void testIti44Merge() {
        def response = send(SERVICE1_XDS, MERGE_REQUEST, String.class)
        assert auditSender.messages.size() == 4
        int updateCount = 0
        int deleteCount = 0
        auditSender.messages.each {
            if (it.toString().contains('EventActionCode="U"')) {
                ++updateCount
            } else if (it.toString().contains('EventActionCode="D"')) {
                ++deleteCount
            }
            assert it.toString().contains('EventOutcomeIndicator="0"')
        }
        assert updateCount == 2
        assert deleteCount == 2
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
