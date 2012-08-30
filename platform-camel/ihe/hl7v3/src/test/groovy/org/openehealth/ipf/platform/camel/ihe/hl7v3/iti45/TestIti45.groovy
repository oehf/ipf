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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti45

import org.apache.cxf.helpers.XMLUtils
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import javax.xml.transform.stream.StreamSource

/**
 * Tests for ITI-45.
 * @author Dmytro Rud
 */
class TestIti45 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-45.xml'
    
    def SERVICE1 = "pixv3-iti45://localhost:${port}/pixv3-iti45-service1";
    def SERVICE2 = "pixv3-iti45://localhost:${port}/pixv3-iti45-service2?audit=false";

    private static final String REQUEST =
        readFile('translation/pixquery/v3/NistPixpdq_Mesa10501-04_Example_01.xml')

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Test
    void testIti45() {
        def response = send(SERVICE1, REQUEST, String.class)
        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventActionCode="E"')
            assert it.toString().contains('EventOutcomeIndicator="0"')
        }
    }


    @Test
    void testIti45XmlProcessing() {
        def response = send(SERVICE2, '<PRPA_IN201309UV02 xmlns="urn:hl7-org:v3"/>', String.class)
        def slurper = new XmlSlurper().parseText(response)
        assert slurper.@from == 'PIX Manager'
    }


    @Test
    void testDatatypes() {
        String request = '<PRPA_IN201309UV02 xmlns="urn:hl7-org:v3"/>'

        // String
        send(SERVICE2, request, String.class)

        // byte[]
        send(SERVICE2, request.bytes, String.class)

        // Stream
        send(SERVICE2, new ByteArrayInputStream(request.bytes), String.class)

        // Reader
        send(SERVICE2, new InputStreamReader(new ByteArrayInputStream(request.bytes)), String.class)

        // DOM Document
        send(SERVICE2, XMLUtils.parse(request))

        // Source
        send(SERVICE2, new StreamSource(new ByteArrayInputStream(request.bytes)), String.class)

        // Unsupported type
        boolean caught = false
        try {
            send(SERVICE2, new Integer(666), String.class)
        } catch (Exception e) {
            caught = true
        }
        assert caught
    }
}
