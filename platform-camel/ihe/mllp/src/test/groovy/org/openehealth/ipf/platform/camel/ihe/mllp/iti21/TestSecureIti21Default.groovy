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


import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AbstractMllpTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * Unit tests for the PDQ transaction aka ITI-21.
 * @author Dmytro Rud
 */
@ContextConfiguration("/iti21/iti-21-secure-default.xml")
class TestSecureIti21Default extends AbstractMllpTest {

    static Properties sysProperties

    @BeforeAll
    static void setUpClass() {
        sysProperties = System.properties
        System.setProperty("javax.net.ssl.keyStore", "keystore.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "keystore.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
    }

    @AfterAll
    static void tearDownClass() {
        System.setProperty("javax.net.ssl.keyStore", sysProperties.get("javax.net.ssl.keyStore").toString())
        System.setProperty("javax.net.ssl.trustStore", sysProperties.get("javax.net.ssl.trustStore").toString())
        System.setProperty("javax.net.ssl.keyStorePassword", sysProperties.get("javax.net.ssl.keyStorePassword").toString())
        System.setProperty("javax.net.ssl.trustStorePassword", sysProperties.get("javax.net.ssl.trustStorePassword").toString());
        System.setProperty("jdk.tls.client.protocols", sysProperties.get("jdk.tls.client.protocols").toString());
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

    @Test @Disabled
    void testHappyCaseAndAuditSecureDefaultSslContext() {
        doTestHappyCaseAndAudit("pdq-iti21://localhost:18217?secure=true", 2)
    }

    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('QBP^Q22', '2.5')
        def msg = send(endpointUri, body)
        assertRSP(msg)
        assertEquals(expectedAuditItemsCount, auditSender.messages.size())
    }

}
