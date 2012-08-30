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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti46

import org.apache.camel.Exchange
import org.apache.camel.impl.DefaultExchange
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * Tests for ITI-46.
 * @author Dmytro Rud
 */
class TestIti46 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-46.xml'
    
    def SERVICE1 = "pixv3-iti46://localhost:${port}/pixv3-iti46-service1"
    def SERVICE_CHARSET = "pixv3-iti46://localhost:${port}/pixv3-iti46-charset"

    private static final String NOTIFICATION =
            readFile('translation/pixfeed/v3/PIX_FEED_REV_Maximal_Request.xml')

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Test
    void testIti46() {
        def response = send(SERVICE1, NOTIFICATION, String.class)
        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventActionCode="R"')
            assert it.toString().contains('EventOutcomeIndicator="0"')
        }
    }
    
    /**
     * Test whether setting and retrieving character sets
     * via Camel exchange property works.
     */
    @Test
    void testCharsets() {
        def exchange = new DefaultExchange(camelContext)
        exchange.in.body = '<PRPA_IN201302UV02 xmlns="urn:hl7-org:v3"/>'
        exchange.properties[Exchange.CHARSET_NAME] = "koi8-r"
        Exchange result = producerTemplate.send(SERVICE_CHARSET, exchange)
        if (result.exception) {
            throw result.exception
        }
        assert result.properties[Exchange.CHARSET_NAME] == "windows-1251"
        def slurper = new XmlSlurper().parseText(Exchanges.resultMessage(result).body)
        assert slurper.@from == 'PIX Consumer'
    }
}
