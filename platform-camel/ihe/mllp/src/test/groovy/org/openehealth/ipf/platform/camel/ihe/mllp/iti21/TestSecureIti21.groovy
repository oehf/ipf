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

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.openehealth.ipf.commons.audit.codes.EventIdCode
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AbstractMllpTest
import org.springframework.test.context.ContextConfiguration

import java.util.concurrent.TimeUnit

import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for the PDQ transaction aka ITI-21.
 * @author Dmytro Rud
 */
@Timeout(value = 1L, unit = TimeUnit.MINUTES)
@ContextConfiguration('/iti21/iti-21-secure.xml')
class TestSecureIti21 extends AbstractMllpTest {


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
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18211?secure=true&sslContextParameters=#iti21SslContextParameters&timeout=${TIMEOUT}", 2)
    }

    @Test
    void testHappyCaseAndAuditSecureWithSslContext() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18211?secure=true&sslContext=#iti21SslContext&timeout=${TIMEOUT}", 2)
    }

    @Test
    void testServerDoesNotNeedToAcceptCertificate() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18215?secure=true&sslContextParameters=#iti21SslContextParametersWithoutKeystore&timeout=${TIMEOUT}", 2)
    }

    @Test
    void testHappyCaseAndAuditSecureTls13() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18216?secure=true&sslContextParameters=#iti21SslContextTls13Parameters&timeout=${TIMEOUT}", 2)
    }

    // Client without certificates (empty key store in SSL context) should fail
    // when trying to access an endpoint with clientAuth=MUST, but should have
    // success when accessing an endpoint with clientAuth=NONE.
    @Test
    void testFailAuditSecureWant() {
        assertThrows(Exception.class, ()->
            send("pdq-iti21://localhost:18211?secure=true&sslContextParameters=#iti21SslContextParametersWithoutKeystore&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
        )
    }

    @Test
    void testHappyCaseAndAuditSecureWant() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18215?secure=true&sslContextParameters=#iti21SslContextParametersWithoutKeystore&timeout=${TIMEOUT}", 2)
    }

    @Test
    void testHappyCaseWithCiphers() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18218?secure=true&sslContextParameters=#iti21SslContextCiphersParameters&timeout=${TIMEOUT}", 2)
    }

    @Test
    void testSSLFailureWithIncompatibleCiphers() {
        try {
            send("pdq-iti21://localhost:18218?secure=true&sslContextParameters=#iti21SslContextOtherCiphersParameters&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
            fail()
        } catch (Exception ignored) {
        }
        assertAuditEvents {
            it.messages.any {
                EventIdCode.SecurityAlert == it.eventIdentification.eventID
            }
        }
    }

    @Test
    void testSSLFailureWithIncompatibleKeystores() {
        assertThrows(Exception.class, ()->
            send("pdq-iti21://localhost:18218?secure=true&sslContextParameters=#iti21OtherSslContextParameters&timeout=${TIMEOUT}", getMessageString('QBP^Q22', '2.5'))
        )
    }

    def doTestHappyCaseAndAudit(String endpointUri, int minExpectedAuditItemsCount) {
        final String body = getMessageString('QBP^Q22', '2.5')
        def msg = send(endpointUri, body)
        assertRSP(msg)
        assertAuditEvents { minExpectedAuditItemsCount <= it.messages.size() }
    }
}
