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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti64

import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer
import static org.junit.Assert.assertEquals
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultExchange
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters
import ca.uhn.hl7v2.parser.PipeParser
import ca.uhn.hl7v2.HL7Exception
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import static org.junit.Assert.assertFalse

/**
 * Unit tests for the Notify XAD-PID Link Change transaction a.k.a. ITI-64.
 * @author Boris Stanojevic
 */
class TestIti64 extends MllpTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti64/iti-64.xml'
    
    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }
    
    @BeforeClass
    static void setUpClass() {
        init(CONTEXT_DESCRIPTOR, false)
    }
    
    static String getMessageString(msh9, msh12) {
        def s = 'MSH|^~\\&|REPOSITORY|ENT|RSP1P8|GOOD HEALTH HOSPITAL|200701051530|' +
                "SEC|${msh9}|0000009|P|${msh12}\n" +
                'EVN|A43|200701051530\n' +
                'PID|1|E2|new^^^&1.2.3.4&ISO~source^^^&1.2.3.4&ISO|||EVERYWOMAN^EVE|\n' +
                'MRG|old^^^&1.2.3.4&ISO|||E1\n'
        s
    }
    
    /**
     * Happy case, audit either enabled or disabled.
     * Expected result: ACK response, two or zero audit items.
     */
    @Test
    void testHappyCase() {
        doTestHappyCaseAndAudit('xpid-iti64://localhost:18491', 2)
    }
    
    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('ADT^A43^ADT_A43', '2.5')
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
        doTestInacceptanceOnConsumer('MDM^T01', '2.5')
    }
    @Test
    public void testInacceptanceOnConsumer2() {
        doTestInacceptanceOnConsumer('ADT^A43', '2.3.1')
    }
    @Test
    public void testInacceptanceOnConsumer3() {
        doTestInacceptanceOnConsumer('ADT^A43^ADT_A44', '2.5')
    }

    def doTestInacceptanceOnConsumer(msh9, msh12) {
        def endpointUri = 'xpid-iti64://localhost:18090'
        def endpoint = camelContext.getEndpoint(endpointUri)
        def consumer = endpoint.createConsumer(
                [process : { Exchange e -> /* nop */ }] as Processor
        )
        def processor = consumer.processor

        def body = getMessageString(msh9, msh12);
        def exchange = new DefaultExchange(camelContext)
        exchange.in.body = body

        processor.process(exchange)
        def response = Exchanges.resultMessage(exchange).body
        def msg = MessageAdapters.make(new PipeParser(), response)
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
        doTestInacceptanceOnProducer('ADT^A43^ADT_A44', '2.5')
    }
    @Test
    void testInacceptanceOnProducer3() {
        doTestInacceptanceOnProducer('ADT^A43', '2.3.1')
    }

    def doTestInacceptanceOnProducer(String msh9, String msh12) {
        def endpointUri = 'xpid-iti64://localhost:18490'
        def body = getMessageString(msh9, msh12)
        def failed = true;

        try {
            send(endpointUri, body)
        } catch (Exception e) {
            def cause = e.getCause()
            if((e instanceof HL7Exception) || (cause instanceof HL7Exception) ||
                    (e instanceof AbstractHL7v2Exception) || (cause instanceof AbstractHL7v2Exception)) {
                failed = false
            }
        }
        assertFalse(failed)
        assertEquals(0, auditSender.messages.size())
    }
}
