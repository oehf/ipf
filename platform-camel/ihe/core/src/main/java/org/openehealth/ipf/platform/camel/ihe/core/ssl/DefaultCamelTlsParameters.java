/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.core.ssl;

import org.apache.camel.support.jsse.CipherSuitesParameters;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextClientParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.SSLContextServerParameters;
import org.apache.camel.support.jsse.SecureSocketProtocolsParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.openehealth.ipf.commons.audit.CustomTlsParameters;

import java.util.Arrays;

/**
 * Provider of SSLContextParameters that default to the global settings based on the
 * system properties javax.net.ssl....
 */
public class DefaultCamelTlsParameters extends CustomTlsParameters implements CamelTlsParameters {

    public DefaultCamelTlsParameters() {
        super();
    }

    @Override
    public SSLContextParameters getSSLContextParameters() {
        var sslContextParameters = new SSLContextParameters();

        var keyStoreParameters = new KeyStoreParameters();
        keyStoreParameters.setResource(keyStoreFile);
        keyStoreParameters.setPassword(keyStorePassword);
        keyStoreParameters.setType(keyStoreType);
        var keyManagers = new KeyManagersParameters();
        keyManagers.setKeyStore(keyStoreParameters);
        sslContextParameters.setKeyManagers(keyManagers);

        var trustStoreParameters = new KeyStoreParameters();
        keyStoreParameters.setResource(trustStoreFile);
        keyStoreParameters.setPassword(trustStorePassword);
        keyStoreParameters.setType(trustStoreType);
        var trustManagersParameters = new TrustManagersParameters();
        trustManagersParameters.setKeyStore(trustStoreParameters);
        sslContextParameters.setTrustManagers(trustManagersParameters);

        sslContextParameters.setProvider(provider);
        sslContextParameters.setSecureSocketProtocol(tlsProtocol);
        if (certAlias != null) {
            sslContextParameters.setCertAlias(certAlias);
        }
        if (sessionTimeout > 0) {
            sslContextParameters.setSessionTimeout(Integer.toString(sessionTimeout));
        }

        if (enabledCipherSuites != null) {
            var cipherSuitesParameters = new CipherSuitesParameters();
            cipherSuitesParameters.setCipherSuite(Arrays.asList(split(enabledCipherSuites)));
            sslContextParameters.setCipherSuites(cipherSuitesParameters);
        }

        if (clientAuthentication != null) {
            var sslContextServerParameters = new SSLContextServerParameters();
            sslContextServerParameters.setClientAuthentication(clientAuthentication);
        }

        if (sniHostnames != null && !sniHostnames.isEmpty()) {
            var sslContextClientParameters = new SSLContextClientParameters();
            sslContextClientParameters.addAllSniHostNames(getSniHostnames());
        }

        var secureSocketProtocolsParameters = new SecureSocketProtocolsParameters();
        secureSocketProtocolsParameters.setSecureSocketProtocol(Arrays.asList(split(tlsProtocol)));
        sslContextParameters.setSecureSocketProtocols(secureSocketProtocolsParameters);
        return sslContextParameters;
    }
}
