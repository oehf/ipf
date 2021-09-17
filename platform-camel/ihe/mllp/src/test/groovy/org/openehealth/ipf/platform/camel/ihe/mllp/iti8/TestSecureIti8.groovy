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


import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer

import static org.junit.jupiter.api.Assertions.assertThrows

/**
 * Unit tests for the PIX Feed transaction a.k.a. ITI-8.
 * @author Dmytro Rud
 */
class TestSecureIti8 extends MllpTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti8/iti-8-secure.xml'
    
    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }
    
    @BeforeAll
    static void setUpClass() {
        init(CONTEXT_DESCRIPTOR, false)
    }

    @Test
    void testSecureEndpoint() {
        final String body = getMessageString('ADT^A01', '2.3.1')
        def endpointUri = "xds-iti8://localhost:18087?secure=true&sslContext=#sslContext&sslProtocols=TLSv1.2&timeout=${TIMEOUT}"
        def msg = send(endpointUri, body)
        assertACK(msg)
    }
    
    @Test
    void testUnsecureProducer() {
        final String body = getMessageString('ADT^A01', '2.3.1')
        def endpointUri = "xds-iti8://localhost:18087?timeout=${TIMEOUT}"
        assertThrows(Exception.class, ()-> send(endpointUri, body));
    }

    @Test
    void testSecureEndpointWithCamelJsseConfigOk() {
        final String body = getMessageString('ADT^A01', '2.3.1')
        def endpointUri = "xds-iti8://localhost:18088?sslContextParameters=#iti8SslContextParameters&timeout=${TIMEOUT}"
        def msg = send(endpointUri, body)
        assertACK(msg)
    }

    @Test
    void testSecureEndpointWithCamelJsseConfigClientFails() {
        final String body = getMessageString('ADT^A01', '2.3.1')
        def endpointUri = "xds-iti8://localhost:18088?timeout=${TIMEOUT}"
        assertThrows(Exception.class, ()-> send(endpointUri, body));
    }
}
