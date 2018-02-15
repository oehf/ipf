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
package org.openehealth.ipf.platform.camel.ihe.mllp.custom

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.parser.PipeParser
import org.apache.camel.CamelExchangeException
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultExchange
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer

import static org.junit.Assert.*

/**
 * Unit tests for the PIX Feed transaction a.k.a. ITI-8.
 * @author Dmytro Rud
 */
class TestMdm extends MllpTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'custom/mdm.xml'
    
    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }
    
    @BeforeClass
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
    //   6. Alternative HL7 codec factory
    //   7. Secure Endpoint
    // -----------------------------------
    

    @Test
    void testHappyCaseAndAudit1() {
        doTestHappyCaseAndAudit("mdm://localhost:19081?audit=false&timeout=${TIMEOUT}", 0)
    }
    
    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('MDM^T01', '2.5')
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
        doTestInacceptanceOnConsumer('ADT^A01', '2.5')
    }
    @Test
    public void testInacceptanceOnConsumer2() {
        doTestInacceptanceOnConsumer('MDM^T01', '2.3.1')
    }
    @Test
    public void testInacceptanceOnConsumer3() {
        doTestInacceptanceOnConsumer('MDM^T11', '2.5')
    }
    @Test
    public void testInacceptanceOnConsumer4() {
        doTestInacceptanceOnConsumer('MDM^T01', '3.1415926')
    }
    @Test
    public void testInacceptanceOnConsumer5() {
        doTestInacceptanceOnConsumer('MDM^T01^MDM_T02', '2.5')
    }
    
    def doTestInacceptanceOnConsumer(String msh9, String msh12) {
        def endpointUri = 'mdm://localhost:19081?audit=false'
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
        doTestInacceptanceOnProducer('ADT^A01', '2.5')
    }
    @Test
    void testInacceptanceOnProducer2() {
        doTestInacceptanceOnProducer('MDM^T11', '2.5')
    }
    @Test
    void testInacceptanceOnProducer3() {
        doTestInacceptanceOnProducer('MDM^T01', '2.3.1')
    }
    @Test
    void testInacceptanceOnProducer4() {
        doTestInacceptanceOnProducer('MDM^T01', '3.1415926')
    }
    @Test
    void testInacceptanceOnProducer5() {
        doTestInacceptanceOnProducer('MDM^T01^MDM^T02', '2.5')
    }
    
    def doTestInacceptanceOnProducer(String msh9, String msh12) {
        def endpointUri = 'mdm://localhost:19081?audit=false'
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
     * Tests how the exceptions in the route are handled.
     */
    @Test
    void testExceptions() {
        def body = getMessageString('MDM^T01', '2.5')
        doTestException('mdm://localhost:19085?audit=false', body, 'you cry')
        doTestException('mdm://localhost:19086?audit=false', body, 'lazy dog')
    }
    
    def doTestException(String endpointUri, String body, String wantedOutputContent) {
        def msg = send(endpointUri, body)
        assertNAK(msg)
        assertTrue(msg.toString().contains(wantedOutputContent))
    }
    
    
    /**
     * Checks whether alternative HL7 codec factories can be used.
     */
    @Test
    void testAlterativeHl7CodecFactory() {
        def endpointUri1 = 'mdm://fake.address.no.uri:180?codec=#alternativeCodec&audit=false'
        def endpointUri2 = 'mdm://localhost:19085?audit=false'
        def endpoint1 = camelContext.getEndpoint(endpointUri1)
        def endpoint2 = camelContext.getEndpoint(endpointUri2)
        assertEquals('UTF-8', endpoint1.charsetName)
        assertEquals('ISO-8859-1', endpoint2.charsetName)
    }

    @Test
    void testSecureEndpoint() {
        final String body = getMessageString('MDM^T01', '2.5')
        def endpointUri = 'mdm://localhost:19087?secure=true&sslContext=#sslContext&sslProtocols=TLSv1&audit=false'
        def msg = send(endpointUri, body)
        assertACK(msg)
    }
    
    @Test(expected=CamelExchangeException.class)
    void testUnsecureProducer() {
        final String body = getMessageString('MDM^T01', '2.5')
        def endpointUri = 'mdm://localhost:19087?audit=false'
        send(endpointUri, body)
        fail()
    }

    @Test
    void testSecureEndpointWithCamelJsseConfigOk() {
        final String body = getMessageString('MDM^T01', '2.5')
        def endpointUri = 'mdm://localhost:19088?sslContextParameters=#sslContextParameters&audit=false'
        def msg = send(endpointUri, body)
        assertACK(msg)
    }

    @Test(expected=CamelExchangeException.class)
    void testSecureEndpointWithCamelJsseConfigClientFails() {
        final String body = getMessageString('MDM^T01', '2.5')
        def endpointUri = 'mdm://localhost:19088?audit=false'
        send(endpointUri, body)
        fail()
    }
}
