/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit;

import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.JdkSslContext;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SupportedCipherSuiteFilter;

import java.util.stream.Stream;

public class NettyUtils {

    public static SslContext initSslContext(TlsParameters tlsParameters, boolean serverSide) {
        var allowedProtocols = System.getProperty("jdk.tls.client.protocols", "TLSv1.2");
        var protocols = Stream.of(allowedProtocols.split("\\s*,\\s*")).toArray(String[]::new);
        return new JdkSslContext(
                tlsParameters.getSSLContext(serverSide),
                !serverSide,
                null, // use default
                SupportedCipherSuiteFilter.INSTANCE,
                ApplicationProtocolConfig.DISABLED,
                ClientAuth.REQUIRE, // require mutual authentication
                protocols,
                false
        );
    }

}
