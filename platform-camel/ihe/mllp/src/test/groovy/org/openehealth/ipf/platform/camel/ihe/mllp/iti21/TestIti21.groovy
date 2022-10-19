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
import ca.uhn.hl7v2.parser.PipeParser
import io.netty.handler.timeout.ReadTimeoutException
import org.apache.camel.*
import org.apache.camel.component.hl7.HL7Charset
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.support.DefaultExchange
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventIdCode
import org.openehealth.ipf.commons.ihe.core.Constants
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AbstractMllpTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.test.context.ContextConfiguration

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.*

import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for the PDQ transaction aka ITI-21.
 * @author Dmytro Rud
 */
@ContextConfiguration('/iti21/iti-21.xml')
class TestIti21 extends AbstractMllpTest {

    private static final Logger LOG = LoggerFactory.getLogger(TestIti21)
    private Random random = new Random(System.currentTimeMillis())

    @EndpointInject("mock:trace")
    private MockEndpoint mockEndpoint

    static String getMessageString(String msh9, String msh12, boolean needQpd = true) {
        def msgId = UUID.randomUUID().toString()
        def s = 'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|' +
                "20081031112704||${msh9}|${msgId}|P|${msh12}|||ER|||||\n"
        if (needQpd) {
            s += 'QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\n'
        }
        s += 'RCP|I|10^RD|||||\n'
        return s
    }

    static byte[] getMessageByteArray(String msh9, String msh12, boolean needQpd = true, Charset charset = StandardCharsets.UTF_8) {
        def msgId = UUID.randomUUID().toString()
        def msh18 = HL7Charset.getHL7Charset(charset.name()).getHL7CharsetName()
        def s = 'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|' +
                "20081031112704||${msh9}|${msgId}|P|${msh12}|||ER|||${msh18}||\n"
        if (needQpd) {
            s += 'QPD|IHE PDQ Query|10|@PID.5.1^ÄÖÜè|||||\n'
        }
        s += 'RCP|I|10^RD|||||\n'
        return s.getBytes(charset)
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
        } catch (CamelExchangeException ignored) {
        }
        assertAuditEvents() {
            it.messages.any {
                it.eventIdentification.eventID == EventIdCode.SecurityAlert
            }
        }
    }

    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('QBP^Q22', '2.5')
        def msg = send(endpointUri, body)
        assertRSP(msg)
        assertAuditEvents { expectedAuditItemsCount <= it.messages.size() }
    }

    String doTestWaitAndAssertCorrectResponse(String endpointUri, int timeout) {
        final String body = getMessageString('QBP^Q22', '2.5')
        final String timeoutString = Integer.toString(timeout)
        def msg = send(endpointUri, body.replace('1402274727', timeoutString))
        // assertRSP(msg)
        // assertEquals(timeoutString, msg.QAK[1].value)
        return msg.QAK[1].value
    }

    def doTestWaitAndAssertTimeout(String endpointUri, int timeout) {
        final String body = getMessageString('QBP^Q22', '2.5')
        final String timeoutString = Integer.toString(timeout)
        try {
            def msg = send(endpointUri, body.replace('1402274727', timeoutString))
            fail("Assuming timeout after $timeout ms")
        } catch (ReadTimeoutException ignored) {
        }
    }

    @Test
    void testCustomInterceptorCanThrowAuthenticationException() {
        send("pdq-iti21://localhost:18214?timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
        assertAuditEvents {
            it.messages.any {
                EventIdCode.SecurityAlert == it.eventIdentification.eventID
            }
        }
    }

    @Disabled
    void testSendAndReceiveTracingInformation() {
        String msg = getMessageString('QBP^Q22', '2.5')
        var qbp = hapiContext.getPipeParser().parse(msg)
        Map<String, Object> headers = [
                (Constants.TRACE_ID)      : 'trace_id',
                (Constants.SPAN_ID)       : 'span_id',
                (Constants.PARENT_SPAN_ID): 'parent_span_id',
                (Constants.SAMPLED)       : '1',
                (Constants.FLAGS)         : '1'
        ]

        // Expect that the headers being sent arrive at the mock endpoint
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
        var response = send("pdq-iti21://localhost:18225?timeout=${TIMEOUT}&interceptorFactories=#sendTracingData", qbp, headers)
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

    @Test
    void testTestTimeoutHandling() {
        // Timeout after 500ms, but response takes 1000ms => Exception
        doTestWaitAndAssertTimeout("pdq-iti21://localhost:18220?producerPoolMaxTotal=1&timeout=500", 1000)
        // Long timeout, response takes 1500ms => check that response is not from previous request, that is handled by now
        doTestWaitAndAssertCorrectResponse("pdq-iti21://localhost:18220?producerPoolMaxTotal=1&timeout=5000", 1500)
    }

    @Test
    void testHappyCaseWithCustomCharset() {
        var body = getMessageByteArray('QBP^Q22', '2.5')
        def msg = send("pdq-iti21://localhost:18220", body)
        assertRSP(msg)
        assertAuditEvents {
            it.messages.every {
                new String(it.participantObjectIdentifications[0].participantObjectQuery, "UTF-8").contains("ÄÖÜè");
            }
        }
    }

    @Test
    void stressTestWithDefaultSetup() {
        stressTest(200, 20, "pdq-iti21://localhost:18220?timeout=10000")
    }

    @Test
    void stressTestWithoutProducerPoolSetup() {
        stressTest(50, 10, "pdq-iti21://localhost:18220?producerPoolEnabled=false&timeout=10000&correlationManager=#hl7CorrelationManager")
    }

    private void stressTest(int numberOfMessages, int threads, String endpoint) {
        def executorService = Executors.newFixedThreadPool(threads)
        def latch = new CountDownLatch(numberOfMessages)
        Map<String, List<Future<String>>> results = new ConcurrentHashMap<>()
        try {
            for (int i = 0; i < numberOfMessages; i++) {
                int delay = random.nextInt(numberOfMessages)
                Future<String> result = executorService.submit(new Callable<String>() {
                    @Override
                    String call() throws Exception {
                        String answer = doTestWaitAndAssertCorrectResponse(endpoint, delay)
                        latch.countDown()
                        return answer
                    }
                })
                results.computeIfAbsent(
                        Integer.toString(delay),
                        r -> new CopyOnWriteArrayList<Future<String>>())
                        .add(result)
            }
            assertTrue(latch.await(10, TimeUnit.SECONDS), () -> 'Responses not arrived: ' + latch.count)
            int numberOfResponses = 0
            for (Map.Entry<String, List<Future<String>>> entry: results) {
                entry.value.forEach(f -> assertEquals(entry.key, f.get()))
                numberOfResponses += entry.value.size()
            }
            assertEquals(numberOfResponses, numberOfMessages)
        } finally {
            executorService.shutdownNow()
            executorService.awaitTermination(5, TimeUnit.SECONDS)
        }

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
        def response = exchange.message.body
        def msg = new PipeParser().parse(response)
        assertNAK(msg)
        assertAuditEvents { it.messages.empty }
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
        assertAuditEvents { it.messages.empty }
    }

    /**
     * Auditing in case of automatically generated NAK.
     */
    @Test
    void testAutoNak() throws Exception {
        def body = getMessageString('QBP^Q22', '2.5')
        def endpointUri = "pdq-iti21://localhost:18213?timeout=${TIMEOUT}"
        def msg = send(endpointUri, body)
        assertAuditEvents { it.messages.size() == 2 }
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
        assertAuditEvents { it.messages.size() == 2 }
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
        assertAuditEvents { it.messages.empty }
        assertACK(msg)
    }
}
