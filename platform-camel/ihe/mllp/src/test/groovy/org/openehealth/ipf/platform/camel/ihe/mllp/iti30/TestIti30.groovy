/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.iti30

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.parser.PipeParser
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.support.DefaultExchange
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer

import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for the PAM "Patient Identity Management" transaction ITI-30.
 */
class TestIti30 extends MllpTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti30/iti-30.xml'
    
    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }
    
    @BeforeAll
    static void setUpClass() {
        init(CONTEXT_DESCRIPTOR, false)
    }
    
    // -----------------------------------
    // Test program:
    //   1. Happy case
    //   2. Inacceptance on Consumer
    //   3. Inacceptance on Producer
    //   4. Incomplete audit datasets
    //   5. Exceptions in the route
    // -----------------------------------
    
    
    /**
     * Happy case, audit either enabled or disabled.
     * Expected result: ACK response, two or zero audit items.
     */
    @Test
    void testHappyCaseAndAudit1() {
        doTestHappyCaseAndAudit('ADT^A28^ADT_A05', "pam-iti30://localhost:18100?timeout=${TIMEOUT}", 2)
    }
    @Test
    void testHappyCaseAndAudit2() {
        doTestHappyCaseAndAudit('ADT^A28^ADT_A05', "pam-iti30://localhost:18100?audit=true&timeout=${TIMEOUT}", 2)
    }
    @Test
    void testHappyCaseAndAudit3() {
        doTestHappyCaseAndAudit('ADT^A28^ADT_A05', "pam-iti30://localhost:18100?audit=false&timeout=${TIMEOUT}", 1)
    }

    @Test
    void testHappyCaseAndAuditLink() {
        doTestHappyCaseAndAudit('ADT^A24^ADT_A24', "pam-iti30://localhost:18103?audit=false&timeout=${TIMEOUT}&iheOptions=LINK_UNLINK", 1)
    }
    
    def doTestHappyCaseAndAudit(String trigger, String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString(trigger, '2.5')
        def msg = send(endpointUri, body)
        assertACK(msg)
        assertEquals(expectedAuditItemsCount, auditSender.messages.size())
    }


    
    /**
     * Inacceptable messages (wrong message type, wrong trigger event, wrong version), 
     * on consumer side, audit enabled.
     * Expected results: NAK responses, no audit.
     * <p>
     * We do not use MLLP producers, because they perform their own acceptance
     * tests and do not pass inacceptable messages to the consumers
     * (it is really a feature, not a bug! ;-)) 
     */
    @Test
    public void testInacceptanceOnConsumer1() {
        doTestInacceptanceOnConsumer('MDM^T01^MDM_T01', '2.5')
    }
    @Test
    public void testInacceptanceOnConsumer2() {
        doTestInacceptanceOnConsumer('ADT^A02^ADT_A02', '2.5')
    }
    @Test
    public void testInacceptanceOnConsumer3() {
        doTestInacceptanceOnConsumer('ADT^A28^ADT_A05', '2.3.1')
    }
    @Test
    public void testInacceptanceOnConsumer4() {
        doTestInacceptanceOnConsumer('ADT^A28^ADT_A05', '3.1415926')
    }
    @Test
    public void testInacceptanceOnConsumer5() {
        doTestInacceptanceOnConsumer('ADT^A28^ADT_A02', '2.5')
    }
    
    def doTestInacceptanceOnConsumer(String msh9, String msh12) {
        def endpointUri = 'pam-iti30://localhost:18101'
        def endpoint = camelContext.getEndpoint(endpointUri)
        def consumer = endpoint.createConsumer(
                [process : { Exchange e -> /* nop */ }] as Processor
                )
        def processor = consumer.processor
        
        def body = getMessageString(msh9, msh12)
        def exchange = new DefaultExchange(camelContext)
        exchange.in.body = body
        
        processor.process(exchange)
        def response = Exchanges.resultMessage(exchange).body
        def msg = new PipeParser().parse(response)
        assertNAK(msg)
        assertEquals(0, auditSender.messages.size())
    }
    
    
    /**
     * Inacceptable messages (wrong message type, wrong trigger event, wrong version), 
     * on producer side, audit enabled.
     * Expected results: raise of corresponding HL7-related exceptions, no audit.
     */
    @Test
    void testInacceptanceOnProducer1() {
        doTestInacceptanceOnProducer('MDM^T01^MDM_T01', '2.5')
    }
    @Test
    void testInacceptanceOnProducer2() {
        doTestInacceptanceOnProducer('ADT^A02^ADT_A02', '2.5')
    }
    @Test
    void testInacceptanceOnProducer3() {
        doTestInacceptanceOnProducer('ADT^A28^ADT_A05', '2.4')
    }
    @Test
    void testInacceptanceOnProducer4() {
        doTestInacceptanceOnProducer('ADT^A28^ADT_A05', '3.1415926')
    }
    @Test
    void testInacceptanceOnProducer5() {
        doTestInacceptanceOnProducer('ADT^A28^ADT_A02', '2.5')
    }
    
    def doTestInacceptanceOnProducer(String msh9, String msh12) {
        def endpointUri = "pam-iti30://localhost:18101?timeout=${TIMEOUT}"
        def body = getMessageString(msh9, msh12)
        def failed = true
        
        try {
            send(endpointUri, body)
        } catch (Exception e) {
            def cause = e.getCause()
            if((e instanceof HL7Exception) || (cause instanceof HL7Exception))
            {
                failed = false
            }
        }
        assertFalse(failed)
        assertEquals(0, auditSender.messages.size())
    }
    

    /**
     * Tests how the exceptions in tte route are handled.
     */
    @Test
    void testExceptions() {
        def body = getMessageString('ADT^A28^ADT_A05', '2.5')
        doTestException("pam-iti30://localhost:18102?timeout=${TIMEOUT}", body, 'you fox')
    }
    
    def doTestException(String endpointUri, String body, String wantedOutputContent) {
        def msg = send(endpointUri, body)
        assertNAK(msg)
        assertTrue(msg.toString().contains(wantedOutputContent))
    }


}
