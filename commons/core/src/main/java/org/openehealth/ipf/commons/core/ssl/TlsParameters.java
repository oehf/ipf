/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.core.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.NoSuchAlgorithmException;

/**
 * Abstraction for setting TLS parameters to be used by other IPF components such as TLS audit sender etc.
 * <p>
 * The default uses the default as defined by javax.net.ssl.* system properties.
 *
 * @author Christian Ohr
 */
public interface TlsParameters {

    String JAVAX_NET_DEBUG = "javax.net.debug";
    String JAVAX_NET_SSL_TRUSTSTORE = "javax.net.ssl.trustStore";
    String JAVAX_NET_SSL_TRUSTSTORE_TYPE = "javax.net.ssl.trustStoreType";
    String JAVAX_NET_SSL_TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword";
    String JAVAX_NET_SSL_KEYSTORE = "javax.net.ssl.keyStore";
    String JAVAX_NET_SSL_KEYSTORE_TYPE = "javax.net.ssl.keyStoreType";
    String JAVAX_NET_SSL_KEYSTORE_PASSWORD = "javax.net.ssl.keyStorePassword";
    String JAVAX_TLS_CLIENT_CIPHERSUITES = "jdk.tls.client.cipherSuites";
    String JAVAX_TLS_SERVER_CIPHERSUITES = "jdk.tls.server.cipherSuites";
    String HTTPS_CIPHERSUITES = "https.ciphersuites";
    String JDK_TLS_CLIENT_PROTOCOLS = "jdk.tls.client.protocols";

    static TlsParameters getDefault() {
        return (serverSide) -> {
            try {
                return SSLContext.getDefault();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * @return the {@link SSLContext}
     */
    SSLContext getSSLContext(boolean serverSide);

    /**
     * @return the {@link SSLSocketFactory}
     */
    default SSLSocketFactory getSSLSocketFactory(boolean serverSide) {
        return getSSLContext(serverSide).getSocketFactory();
    }

}
