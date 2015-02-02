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
import org.apache.camel.CamelExchangeException
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.RuntimeCamelException
import org.apache.camel.impl.DefaultExchange
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer
import org.openhealthtools.ihe.atna.auditor.events.dicom.SecurityAlertEvent

import static org.junit.Assert.*

/**
 * Unit tests for the PDQ transaction aka ITI-21.
 * @author Dmytro Rud
 */
class TestIti21 extends MllpTestContainer {
    def static CONTEXT_DESCRIPTOR = 'iti21/iti-21.xml'


    
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
    
    @Test(timeout = TEST_TIMEOUT)
    void testHappyCaseAndAudit1() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18210?timeout=${TIMEOUT}", 2)
    }
    @Test(timeout = TEST_TIMEOUT)
    void testHappyCaseAndAudit2() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18887?audit=false&timeout=${TIMEOUT}", 0)
    }
    
    @Test(timeout = TEST_TIMEOUT)
    void testHappyCaseAndAuditSecure() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18211?secure=true&sslContext=#sslContext&timeout=${TIMEOUT}", 2)
    }

    // Client without certificates (empty key store in SSL context) should fail
    // when trying to access an endpoint with clientAuth=MUST, but should have
    // success when accessing an endpoint with clientAuth=WANT.
    @Test(timeout = TEST_TIMEOUT)
    void testHappyCaseAndAuditSecureWant() {
        boolean failed = false
        try {
            doTestHappyCaseAndAudit("pdq-iti21://localhost:18211?secure=true&sslContext=#sslContextWithoutKeyStore&timeout=${TIMEOUT}", 3)
        } catch (Exception e) {
            failed = true
        }
        assert failed

        auditSender.messages.clear()
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18230?secure=true&sslContext=#sslContextWithoutKeyStore&timeout=${TIMEOUT}", 2)
    }

    @Test(timeout = TEST_TIMEOUT)
    void testHappyCaseWithSSLv3() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18216?secure=true&sslContext=#sslContext&sslProtocols=SSLv3&timeout=${TIMEOUT}", 2)
    }
    
    @Test(timeout = TEST_TIMEOUT)
    void testHappyCaseWithSSLv3AndTLSv1() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18217?secure=true&sslContext=#sslContext&sslProtocols=SSLv3,TLSv1&timeout=${TIMEOUT}", 2)
    }
    
    @Test(timeout = TEST_TIMEOUT)
    void testHappyCaseWithCiphers() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18218?secure=true&sslContext=#sslContext&sslCiphers=SSL_RSA_WITH_NULL_SHA,TLS_RSA_WITH_AES_128_CBC_SHA&timeout=${TIMEOUT}", 2)
    }
    
    @Test(timeout = TEST_TIMEOUT)
    void testSSLFailureWithIncompatibleProtocols() {
        try {
            send("pdq-iti21://localhost:18216?secure=true&sslContext=#sslContext&sslProtocols=TLSv1&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
            fail('expected exception: ' + String.valueOf(CamelExchangeException.class))
        } catch (Exception expected) {
            // FIXME: race condition throws one of two possible exceptions
            // 1.) RuntimeIOException: Failed to get the session
            // 2.) CamelExchangeException (expected)
        }
    }
    
    @Test(timeout = TEST_TIMEOUT)
    void testSSLFailureWithIncompatibleCiphers() {
        try {
            send("pdq-iti21://localhost:18218?secure=true&sslContext=#sslContext&sslCiphers=TLS_KRB5_WITH_3DES_EDE_CBC_MD5&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
            fail('expected exception: ' + String.valueOf(CamelExchangeException.class))
        } catch (Exception expected) {
            // FIXME: race condition throws one of two possible exceptions
            // 1.) RuntimeIOException: Failed to get the session
            // 2.) CamelExchangeException (expected)
        }
        
        def messages = auditSender.messages
        assertEquals(3, messages.size())
        assertTrue(messages[0] instanceof SecurityAlertEvent)
        assertTrue(messages[1] instanceof SecurityAlertEvent)
    }
    
    @Test(timeout = TEST_TIMEOUT)
    void testSSLFailureWithIncompatibleKeystores() {
        try {
            send("pdq-iti21://localhost:18211?secure=true&sslContext=#sslContextOther&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
            fail('expected exception: ' + String.valueOf(RuntimeCamelException.class))
        } catch (Exception expected) {
            // FIXME: race condition throws one of two possible exceptions
            // 1.) RuntimeIOException: Failed to get the session
            // 2.) CamelExchangeException (expected)
        }
    }
    
    @Test(timeout = TEST_TIMEOUT)
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
        assertTrue(messages[0] instanceof SecurityAlertEvent)
    }
    
    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('QBP^Q22', '2.5')
        def msg = send(endpointUri, body)
        assertRSP(msg)
        assertEquals(expectedAuditItemsCount, auditSender.messages.size())
    }
    
    @Test(timeout = TEST_TIMEOUT)
    void testCustomInterceptorCanThrowAuthenticationException() {
        send("pdq-iti21://localhost:18214?timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
        def messages = auditSender.messages
        assertEquals(3, messages.size())
        assertTrue(messages[0] instanceof SecurityAlertEvent)
    }
    
    @Test(timeout = TEST_TIMEOUT)
    void testServerDoesNotNeedToAcceptCertificate() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18215?secure=true&sslContext=#sslContext&timeout=${TIMEOUT}", 2)
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
    @Test(timeout = TEST_TIMEOUT)
    public void testInacceptanceOnConsumer1() {
        doTestInacceptanceOnConsumer('MDM^T01', '2.5')
    }
    @Test(timeout = TEST_TIMEOUT)
    public void testInacceptanceOnConsumer2() {
        doTestInacceptanceOnConsumer('QBP^Q21', '2.5')
    }
    @Test(timeout = TEST_TIMEOUT)
    public void testInacceptanceOnConsumer3() {
        doTestInacceptanceOnConsumer('QBP^Q22', '2.3.1')
    }
    @Test(timeout = TEST_TIMEOUT)
    public void testInacceptanceOnConsumer4() {
        doTestInacceptanceOnConsumer('QBP^Q22', '3.1415926')
    }
    @Test(timeout = TEST_TIMEOUT)
    public void testInacceptanceOnConsumer5() {
        doTestInacceptanceOnConsumer('QBP^Q22^QBP_Q26', '2.5')
    }
    
    def doTestInacceptanceOnConsumer(String msh9, String msh12) {
        def endpointUri = "pdq-iti21://localhost:18210?timeout=${TIMEOUT}"
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
        def msg = new PipeParser().parse(response)
        assertNAK(msg)
        assertEquals(0, auditSender.messages.size())
    }
    
    
    /**
     * Inacceptable messages (wrong message type, wrong trigger event, wrong version),
     * on producer side, audit enabled.
     * Expected results: raise of corresponding HL7-related exceptions, no audit.
     */
    @Test(timeout = TEST_TIMEOUT)
    void testInacceptanceOnProducer1() {
        doTestInacceptanceOnProducer('MDM^T01', '2.5')
    }
    @Test(timeout = TEST_TIMEOUT)
    void testInacceptanceOnProducer2() {
        doTestInacceptanceOnProducer('QBP^K22', '2.5')
    }
    @Test(timeout = TEST_TIMEOUT)
    void testInacceptanceOnProducer3() {
        doTestInacceptanceOnProducer('QBP^Q22', '2.3.1')
    }
    @Test(timeout = TEST_TIMEOUT)
    void testInacceptanceOnProducer4() {
        doTestInacceptanceOnProducer('QBP^Q22', '3.1415926')
    }
    @Test(timeout = TEST_TIMEOUT)
    void testInacceptanceOnProducer5() {
        doTestInacceptanceOnProducer('QBP^Q22^QBP_Q28', '2.5')
    }
    
    def doTestInacceptanceOnProducer(String msh9, String msh12) {
        def endpointUri = "pdq-iti21://localhost:18210?timeout=${TIMEOUT}"
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
    
    
    /**
     * Auditing in case of automatically generated NAK.
     */
    @Test(timeout = TEST_TIMEOUT)
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
    @Test(timeout = TEST_TIMEOUT)
    void testMagicNak() throws Exception {
        def body = getMessageString('QBP^Q22', '2.5')
        def endpointUri = "pdq-iti21://localhost:18219?timeout=${TIMEOUT}"
        def msg = send(endpointUri, body)
        assertEquals(2, auditSender.messages.size())
        assertNAKwithQPD(msg, 'RSP', 'K22')
    }
    
    
    @Test(timeout = TEST_TIMEOUT)
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
