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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti22

import static org.junit.Assert.*

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultExchange
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.parser.PipeParser

/**
 * Unit tests for the PDQ transaction aka ITI-22.
 * @author Dmytro Rud
 */
class TestIti22 extends MllpTestContainer {
    
    
    def static CONTEXT_DESCRIPTOR = 'iti22/iti-22.xml'
    
    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }
    
    @BeforeClass
    static void setUpClass() {
        init(CONTEXT_DESCRIPTOR, false)
    }
    
    static String getMessageString(String msh9, String msh12, boolean needQpd = true) {
        def s = 'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|'+
                "20081031112704||${msh9}|324406609|P|${msh12}|||ER|||||\n"
        if(needQpd) {
            s += 'QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\n'
        }
        s += 'RCP|I|10^RD|||||\n'
        return s
    }
    
    /**
     * Happy case, audit either enabled or disabled.
     * Expected result: ACK response, two or zero audit items.
     */
    @Test
    void testHappyCaseAndAudit1() {
        doTestHappyCaseAndAudit('QBP^ZV1', 'pdq-iti22://localhost:18221', 2)
    }
    @Test
    void testHappyCaseAndAudit2() {
        doTestHappyCaseAndAudit('QBP^ZV1^QBP_Q21', 'pdq-iti22://localhost:18220?audit=false', 0)
    }
    
    def doTestHappyCaseAndAudit(String msh9, String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString(msh9, '2.5')
        def msg = send(endpointUri, body)
        assertRSP(msg)
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
        doTestInacceptanceOnConsumer('QBP^Q21', '2.5')
    }
    @Test
    public void testInacceptanceOnConsumer3() {
        doTestInacceptanceOnConsumer('QBP^ZV1', '2.3.1')
    }
    @Test
    public void testInacceptanceOnConsumer4() {
        doTestInacceptanceOnConsumer('QBP^ZV1', '3.1415926')
    }
    @Test
    public void testInacceptanceOnConsumer5() {
        doTestInacceptanceOnConsumer('QBP^ZV1^QBP_Q26', '2.5')
    }
    
    def doTestInacceptanceOnConsumer(String msh9, String msh12) {
        def endpointUri = 'pdq-iti22://localhost:18221'
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
        doTestInacceptanceOnProducer('MDM^T01', '2.5')
    }
    @Test
    void testInacceptanceOnProducer2() {
        doTestInacceptanceOnProducer('QBP^K22', '2.5')
    }
    @Test
    void testInacceptanceOnProducer3() {
        doTestInacceptanceOnProducer('QBP^ZV1', '2.3.1')
    }
    @Test
    void testInacceptanceOnProducer4() {
        doTestInacceptanceOnProducer('QBP^ZV1', '3.1415926')
    }
    @Test
    void testInacceptanceOnProducer5() {
        doTestInacceptanceOnProducer('QBP^ZV1^QBP_Q28', '2.5')
    }
    
    def doTestInacceptanceOnProducer(String msh9, String msh12) {
        def endpointUri = 'pdq-iti22://localhost:18221'
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
