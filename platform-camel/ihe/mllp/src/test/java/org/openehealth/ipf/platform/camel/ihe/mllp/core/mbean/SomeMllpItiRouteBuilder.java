/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.mbean;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Stefan Ivanov
 */
public class SomeMllpItiRouteBuilder extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        from("some-mllp-iti://0.0.0.0:18181?audit=true&" +
                "secure=true&" +
                "sslContext=#sslContext&" +
                "sslProtocols=SSLv3,TLSv1&" +
                "sslCiphers=SSL_RSA_WITH_NULL_SHA,TLS_RSA_WITH_AES_128_CBC_SHA" +
                "&interceptorFactories=#dummyInterceptor1,#dummyInterceptor2")
            .to("mock:result");
    }

}
