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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti21

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.PipeParser
import org.apache.camel.*
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.support.DefaultExchange
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.openehealth.ipf.commons.audit.codes.EventIdCode
import org.openehealth.ipf.commons.ihe.core.Constants
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit

import static org.junit.Assert.*

/**
 * Unit tests for the PDQ transaction aka ITI-21.
 * @author Dmytro Rud
 */
class TestIti21 extends MllpTestContainer {

    private static final Logger LOG = LoggerFactory.getLogger(TestIti21)

    def static CONTEXT_DESCRIPTOR = 'iti21/iti-21.xml'

    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }

    @Rule
    public Timeout timeout = new Timeout(5L, TimeUnit.MINUTES)

    @BeforeClass
    static void setUpClass() {
        init(CONTEXT_DESCRIPTOR, false)
    }

    static String getMessageString(String msh9, String msh12, boolean needQpd = true) {
        def s = 'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|' +
                "20081031112704||${msh9}|324406609|P|${msh12}|||ER|||||\n"
        if (needQpd) {
            s += 'QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\n'
        }
        s += 'RCP|I|10^RD|||||\n'
        return s
    }

    @Test
    void testHappyCaseAndAudit1() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18210?timeout=${TIMEOUT}", 2)
    }

    @Test
    void testHappyCaseAndAudit2() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18887?audit=false&timeout=${TIMEOUT}", 0)
    }


    @Test
    void testSSLFailureDueToNonSSLClient() {
        try {
            send("pdq-iti21://localhost:18211?timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
            fail('expected exception: ' + String.valueOf(CamelExchangeException.class))
        } catch (Exception expected) {
            // FIXME: race condition throws one of two possible exceptions
            // 1.) RuntimeIOException: Failed to get the session
            // 2.) CamelExchangeException (expected)
        }

        def messages = auditSender.messages
        assertEquals(2, messages.size())
        assertEquals(EventIdCode.SecurityAlert, messages[0].getEventIdentification().getEventID())
    }

    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('QBP^Q22', '2.5')
        def msg = send(endpointUri, body)
        assertRSP(msg)
        assertEquals(expectedAuditItemsCount, auditSender.messages.size())
    }

    @Test
    void testCustomInterceptorCanThrowAuthenticationException() {
        send("pdq-iti21://localhost:18214?timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
        def messages = auditSender.messages
        assertEquals(3, messages.size())
        LOG.warn("{}", messages)
        assertEquals(EventIdCode.SecurityAlert, messages[0].getEventIdentification().getEventID())
    }

    @Ignore
    void testSendAndReceiveTracingInformation() {
        String msg = getMessageString('QBP^Q22', '2.5')
        HapiContext hapiContext = appContext.getBean(HapiContext.class)
        Message qbp = hapiContext.getPipeParser().parse(msg)
        Map<String, Object> headers = [
                (Constants.TRACE_ID) : 'trace_id',
                (Constants.SPAN_ID) : 'span_id',
                (Constants.PARENT_SPAN_ID) : 'parent_span_id',
                (Constants.SAMPLED) : '1',
                (Constants.FLAGS) : '1'
        ]

        // Expect that the headers being sent arrive at the mock endpoint
        MockEndpoint mockEndpoint = MockEndpoint.resolve(camelContext, "mock:trace")
        mockEndpoint.expectedMessageCount(1)
        mockEndpoint.expectedMessagesMatches(new Predicate() {
            @Override
            boolean matches(final Exchange exchange) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    if (!entry.value.equals(exchange.in.getHeader(entry.key))) {
                        return false
                    }
                }
                return true
            }
        })
        Message response = send("pdq-iti21://localhost:18225?timeout=${TIMEOUT}&interceptorFactories=#sendTracingData", qbp, headers)
        mockEndpoint.assertIsSatisfied(2000)
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
        doTestInacceptanceOnConsumer('MDM^T01', '2.5')
    }

    @Test
    void testInacceptanceOnConsumer2() {
        doTestInacceptanceOnConsumer('QBP^Q21', '2.5')
    }

    @Test
    void testInacceptanceOnConsumer3() {
        doTestInacceptanceOnConsumer('QBP^Q22', '2.3.1')
    }

    @Test
    void testInacceptanceOnConsumer4() {
        doTestInacceptanceOnConsumer('QBP^Q22', '3.1415926')
    }

    @Test
    void testInacceptanceOnConsumer5() {
        doTestInacceptanceOnConsumer('QBP^Q22^QBP_Q26', '2.5')
    }

    def doTestInacceptanceOnConsumer(String msh9, String msh12) {
        def endpointUri = "pdq-iti21://localhost:18210?timeout=${TIMEOUT}"
        def endpoint = camelContext.getEndpoint(endpointUri)
        def consumer = endpoint.createConsumer(
                [process: { Exchange e -> /* nop */ }] as Processor
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
        doTestInacceptanceOnProducer('MDM^T01', '2.5')
    }

    @Test
    void testInacceptanceOnProducer2() {
        doTestInacceptanceOnProducer('QBP^K22', '2.5')
    }

    @Test
    void testInacceptanceOnProducer3() {
        doTestInacceptanceOnProducer('QBP^Q22', '2.3.1')
    }

    @Test
    void testInacceptanceOnProducer4() {
        doTestInacceptanceOnProducer('QBP^Q22', '3.1415926')
    }

    @Test
    void testInacceptanceOnProducer5() {
        doTestInacceptanceOnProducer('QBP^Q22^QBP_Q28', '2.5')
    }

    def doTestInacceptanceOnProducer(String msh9, String msh12) {
        def endpointUri = "pdq-iti21://localhost:18210?timeout=${TIMEOUT}"
        def body = getMessageString(msh9, msh12)
        def failed = true

        try {
            send(endpointUri, body)
        } catch (Exception e) {
            def cause = e.getCause()
            if ((e instanceof HL7Exception) || (cause instanceof HL7Exception)) {
                failed = false
            }
        }
        assertFalse(failed)
        assertEquals(0, auditSender.messages.size())
    }

    /**
     * Auditing in case of automatically generated NAK.
     */
    @Test
    void testAutoNak() throws Exception {
        def body = getMessageString('QBP^Q22', '2.5')
        def endpointUri = "pdq-iti21://localhost:18213?timeout=${TIMEOUT}"
        def msg = send(endpointUri, body)
        assertEquals(2, auditSender.messages.size())
        assertNAKwithQPD(msg, 'RSP', 'K22')
    }

    /**
     * Auditing in case of automatically generated NAK with magic header.
     */
    @Test
    void testMagicNak() throws Exception {
        def body = getMessageString('QBP^Q22', '2.5')
        def endpointUri = "pdq-iti21://localhost:18219?timeout=${TIMEOUT}"
        def msg = send(endpointUri, body)
        assertEquals(2, auditSender.messages.size())
        assertNAKwithQPD(msg, 'RSP', 'K22')
    }


    @Test
    void testCancel() {
        def body =
                'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|' +
                        '20081031112704||QCN^J01|324406609|P|2.5|||ER|||||\n' +
                        'QID|dummy|gummy||\n'
        def endpointUri = "pdq-iti21://localhost:18212?timeout=${TIMEOUT}"
        def msg = send(endpointUri, body)
        assertEquals(0, auditSender.messages.size())
        assertACK(msg)
    }
}
