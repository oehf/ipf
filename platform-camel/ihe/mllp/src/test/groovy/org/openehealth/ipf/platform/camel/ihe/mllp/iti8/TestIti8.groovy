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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.parser.PipeParser
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.support.DefaultExchange
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer
import zipkin2.Span

import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for the PIX Feed transaction a.k.a. ITI-8.
 * @author Dmytro Rud
 */
class TestIti8 extends MllpTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti8/iti-8.xml'
    
    static void main(args) {
        System.setProperty(PayloadLoggerBase.PROPERTY_DISABLED, 'true')
        init(CONTEXT_DESCRIPTOR, true)
    }
    
    @BeforeAll
    static void setUpClass() {
        System.setProperty(PayloadLoggerBase.PROPERTY_DISABLED, 'true')
        init(CONTEXT_DESCRIPTOR, false)
    }
    
    /**
     * Happy case, audit either enabled or disabled.
     * Expected result: ACK response, two or zero audit items.
     */
    @Test
    void testHappyCaseAndAudit1() {
        doTestHappyCaseAndAudit("xds-iti8://localhost:18082?timeout=${TIMEOUT}&interceptorFactories=#clientInLogger,#clientOutLogger", 2)
    }
    @Test
    void testHappyCaseAndAudit2() {
        doTestHappyCaseAndAudit("pix-iti8://localhost:18082?audit=true&timeout=${TIMEOUT}&interceptorFactories=#clientInLogger,#clientOutLogger", 2)
    }
    @Test
    void testHappyCaseAndAudit3() {
        doTestHappyCaseAndAudit("xds-iti8://localhost:18081?audit=false&timeout=${TIMEOUT}&interceptorFactories=#clientInLogger,#clientOutLogger", 0)
    }
    @Test
    void testHappyCaseAndTrace() {
        doTestHappyCaseAndAudit("pix-iti8://localhost:18083?interceptorFactories=#producerTracingInterceptor,#clientInLogger,#clientOutLogger&timeout=${TIMEOUT}", 2)
        MockReporter reporter = appContext.getBean(MockReporter)
        assertEquals(2, reporter.spans.size())

        Span clientSpan = reporter.spans.find { span -> span.kind() == Span.Kind.CLIENT}
        Span serverSpan = reporter.spans.find { span -> span.kind() == Span.Kind.SERVER}

        assertFalse(clientSpan.tags().isEmpty())
        assertEquals(clientSpan.tags(), serverSpan.tags())
        assertNotEquals(clientSpan.id(), serverSpan.id())
        assertEquals(clientSpan.id(), serverSpan.parentId())
        assertTrue(clientSpan.durationAsLong() > serverSpan.durationAsLong())

    }
    
    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('ADT^A01', '2.3.1')
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
    void testInacceptanceOnConsumer1() {
        doTestInacceptanceOnConsumer('MDM^T01', '2.3.1')
    }
    @Test
    void testInacceptanceOnConsumer2() {
        doTestInacceptanceOnConsumer('ADT^A02', '2.3.1')
    }
    @Test
    void testInacceptanceOnConsumer3() {
        doTestInacceptanceOnConsumer('ADT^A01', '2.5')
    }
    @Test
    void testInacceptanceOnConsumer4() {
        doTestInacceptanceOnConsumer('ADT^A01', '3.1415926')
    }
    @Test
    void testInacceptanceOnConsumer5() {
        doTestInacceptanceOnConsumer('ADT^A01^ADT_A02', '2.3.1')
    }
    
    def doTestInacceptanceOnConsumer(String msh9, String msh12) {
        def endpointUri = 'pix-iti8://localhost:18084'
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
        doTestInacceptanceOnProducer('MDM^T01', '2.3.1')
    }
    @Test
    void testInacceptanceOnProducer2() {
        doTestInacceptanceOnProducer('ADT^A02', '2.3.1')
    }
    @Test
    void testInacceptanceOnProducer3() {
        doTestInacceptanceOnProducer('ADT^A01', '2.4')
    }
    @Test
    void testInacceptanceOnProducer4() {
        doTestInacceptanceOnProducer('ADT^A01', '3.1415926')
    }
    @Test
    void testInacceptanceOnProducer5() {
        doTestInacceptanceOnProducer('ADT^A01^ADT_A02', '2.3.1')
    }
    
    def doTestInacceptanceOnProducer(String msh9, String msh12) {
        def endpointUri = "xds-iti8://localhost:18084?timeout=${TIMEOUT}&interceptorFactories=#clientInLogger,#clientOutLogger"
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
        def body = getMessageString('ADT^A01', '2.3.1')
        doTestException("pix-iti8://localhost:18085?timeout=${TIMEOUT}&interceptorFactories=#clientInLogger,#clientOutLogger", body, 'you cry')
        doTestException("pix-iti8://localhost:18086?timeout=${TIMEOUT}&interceptorFactories=#clientInLogger,#clientOutLogger", body, 'lazy dog')
    }

    @Disabled
    void testWrongEncoding() {
        String isoMessage = this.getClass().classLoader.getResource('./iti8/iti8-a40-iso-8859-1.hl7')?.getText('iso-8859-1')
        doTestException("pix-iti8://localhost:18089?timeout=${TIMEOUT}", isoMessage, "java.nio.charset.MalformedInputException")
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
        def endpointUri1 = 'pix-iti8://fake.address.no.uri:180?decoders=#alternativeDecoder&encoders=#alternativeEncoder'
        def endpointUri2 = 'xds-iti8://localhost:18085'
        def endpoint1 = camelContext.getEndpoint(endpointUri1)
        def endpoint2 = camelContext.getEndpoint(endpointUri2)
        assertEquals('UTF-8', endpoint1.charsetName)
        assertEquals('ISO-8859-1', endpoint2.charsetName)
    }

}
