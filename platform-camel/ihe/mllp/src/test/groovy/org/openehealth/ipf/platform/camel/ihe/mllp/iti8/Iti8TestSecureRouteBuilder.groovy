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

import org.apache.camel.builder.RouteBuilder

import static org.openehealth.ipf.platform.camel.hl7.HL7v2.ack

/**
 * Camel route for generic unit tests.
 * @author Dmytro Rud
 */
class Iti8TestSecureRouteBuilder extends RouteBuilder {

    void configure() throws Exception {

        from('xds-iti8://0.0.0.0:18087?audit=false&' +
                'secure=true&sslContext=#sslContext&' +
                'sslProtocols=TLSv1.2,TLSv1.3&' +
                'sslCiphers=TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_AES_128_GCM_SHA256')
                .transform(ack())

        from('xds-iti8://0.0.0.0:18088?audit=false&' +
                'sslContextParameters=#sslContextParameters')
                .transform(ack())

    }
}

