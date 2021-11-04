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

import org.apache.camel.CamelExchangeException
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.openehealth.ipf.commons.audit.codes.EventIdCode
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer

import java.util.concurrent.TimeUnit

import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for the PDQ transaction aka ITI-21.
 * @author Dmytro Rud
 */
@Timeout(value = 5L, unit = TimeUnit.MINUTES)
class TestSecureIti21 extends MllpTestContainer {


    def static CONTEXT_DESCRIPTOR = 'iti21/iti-21-secure.xml'

    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }

    @BeforeAll
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
    void testHappyCaseAndAuditSecure() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18211?secure=true&sslContext=#sslContext&timeout=${TIMEOUT}", 2)
    }

    // Client without certificates (empty key store in SSL context) should fail
    // when trying to access an endpoint with clientAuth=MUST, but should have
    // success when accessing an endpoint with clientAuth=WANT.
    @Test
    void testFailAuditSecureWant() {
        assertThrows(Exception.class, ()->
            send("pdq-iti21://localhost:18211?secure=true&sslContext=#sslContextWithoutKeyStore&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
        )
    }

    @Test
    void testHappyCaseAndAuditSecureWant() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18230?secure=true&sslContext=#sslContextWithoutKeyStore&timeout=${TIMEOUT}", 2)
    }

    @Test @Disabled
    void testHappyCaseWithTLSv12AndTLSv13() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18217?secure=true&sslContext=#sslContext&sslProtocols=TLSv1.2,TLSv1.3&timeout=${TIMEOUT}", 2)
    }

    @Test
    void testHappyCaseWithCiphers() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18218?secure=true&sslContext=#sslContext&sslCiphers=SSL_RSA_WITH_NULL_SHA,TLS_RSA_WITH_AES_128_CBC_SHA&timeout=${TIMEOUT}", 2)
    }

    @Test
    void testSSLFailureWithIncompatibleProtocols() {
        assertThrows(Exception.class, ()->
            send("pdq-iti21://localhost:18216?secure=true&sslContext=#sslContext&sslProtocols=TLSv1.2&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
        )
    }

    @Test
    @Disabled("Test runs into JUnit test timeout (see @Rule above)")
    void testSSLFailureWithIncompatibleCiphers() {
        try {
            send("pdq-iti21://localhost:18218?secure=true&sslContext=#sslContext&sslCiphers=TLS_KRB5_WITH_3DES_EDE_CBC_MD5&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
            fail('expected exception: ' + String.valueOf(CamelExchangeException.class))
        } catch (Exception ignored) {
            // FIXME: race condition throws one of two possible exceptions
            // 1.) RuntimeIOException: Failed to get the session
            // 2.) CamelExchangeException (expected)
        }

        def messages = auditSender.messages
        assertEquals(3, messages.size())
        assertEquals(EventIdCode.SecurityAlert, messages[0].getEventIdentification().getEventID())
        assertEquals(EventIdCode.SecurityAlert, messages[1].getEventIdentification().getEventID())
    }

    @Test
    void testSSLFailureWithIncompatibleKeystores() {
        assertThrows(Exception.class, ()->
            send("pdq-iti21://localhost:18218?secure=true&sslContext=#sslContextOther&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
        )
    }

    @Test
    void testServerDoesNotNeedToAcceptCertificate() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18215?secure=true&sslContext=#sslContext&timeout=${TIMEOUT}", 2)
    }

    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('QBP^Q22', '2.5')
        def msg = send(endpointUri, body)
        assertRSP(msg)
        assertEquals(expectedAuditItemsCount, auditSender.messages.size())
    }
}
