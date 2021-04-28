/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.custom

import org.apache.camel.builder.RouteBuilder

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage
import static org.openehealth.ipf.platform.camel.hl7.HL7v2.ack


class MdmTestRouteBuilder extends RouteBuilder {
    
    void configure() throws Exception {
        
        // normal processing without auditing
        from('mdm://0.0.0.0:19081?audit=false')
                .transform(ack())

        // fictive route to test producer-side acceptance checking
        from('mdm://0.0.0.0:19084?audit=false')
                .process {
                    resultMessage(it).body.MSH[9][1] = 'DOES NOT MATTER'
                    resultMessage(it).body.MSH[9][2] = 'SHOULD FAIL IN INTERCEPTORS'
                }

        // route with normal exception
        from('mdm://0.0.0.0:19085?audit=false')
                .onException(Exception.class)
                    .maximumRedeliveries(0)
                    .end()
                .process { throw new Exception('Why do you cry, Willy?') }

        // route with runtime exception
        from('mdm://0.0.0.0:19086?audit=false')
                .onException(Exception.class)
                    .maximumRedeliveries(0)
                    .end()
                .process { throw new RuntimeException('Jump over the lazy dog, you fox.') }

        from('mdm://0.0.0.0:19087?audit=false&'+
                'secure=true&sslContext=#sslContext&' +
                'sslProtocols=TLSv1.2,TLSv1.3&' +
                'sslCiphers=TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_AES_128_GCM_SHA256')
                .transform(ack())

        from('mdm://0.0.0.0:19088?audit=false&'+
                'sslContextParameters=#sslContextParameters')
                .transform(ack())
    }
}

