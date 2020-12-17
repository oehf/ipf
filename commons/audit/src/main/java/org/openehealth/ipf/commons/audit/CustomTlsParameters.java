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
package org.openehealth.ipf.commons.audit;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.HTTPS_CIPHERSUITES;
import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.JAVAX_NET_SSL_KEYSTORE;
import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.JAVAX_NET_SSL_KEYSTORE_PASSWORD;
import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.JAVAX_NET_SSL_KEYSTORE_TYPE;
import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.JAVAX_NET_SSL_TRUSTSTORE;
import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.JAVAX_NET_SSL_TRUSTSTORE_PASSWORD;
import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.JDK_TLS_CLIENT_PROTOCOLS;

/**
 * {@link TlsParameters} that can be set independently of the javax.net.ssl system
 * properties. Still, a newly instantiated instance of this class defaults to these
 * properties.
 */
public class CustomTlsParameters implements TlsParameters {

    private String provider = "SunJSSE";
    private String tlsProtocol = "TLSv1.2";
    private String certificateType = "SunX509";

    private String certAlias;

    protected String keyStoreType;
    protected String trustStoreType;
    protected String keyStoreFile;
    protected String keyStorePassword;
    protected String trustStoreFile;
    protected String trustStorePassword;

    protected String enabledCipherSuites;
    protected String enabledProtocols;

    private int sessionTimeout;
    private boolean performDomainValidation;
    private final List<String> sniHostnames = new ArrayList<>();

    static TlsParameters getDefault() {
        return new CustomTlsParameters();
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setTlsProtocol(String tlsProtocol) {
        this.tlsProtocol = tlsProtocol;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public void setCertAlias(String certAlias) {
        this.certAlias = certAlias;
    }

    public void setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public void setTrustStoreFile(String trustStoreFile) {
        this.trustStoreFile = trustStoreFile;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public void setEnabledCipherSuites(String enabledCipherSuites) {
        this.enabledCipherSuites = enabledCipherSuites;
    }

    public void setEnabledProtocols(String enabledProtocols) {
        this.enabledProtocols = enabledProtocols;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setPerformDomainValidation(boolean performDomainValidation) {
        this.performDomainValidation = performDomainValidation;
    }

    public List<String> getSniHostnames() {
        return sniHostnames;
    }

    public CustomTlsParameters() {
        keyStoreType = System.getProperty(JAVAX_NET_SSL_KEYSTORE_TYPE, KeyStore.getDefaultType());
        trustStoreType = keyStoreType;
        keyStoreFile = System.getProperty(JAVAX_NET_SSL_KEYSTORE);
        keyStorePassword = System.getProperty(JAVAX_NET_SSL_KEYSTORE_PASSWORD);
        trustStoreFile = System.getProperty(JAVAX_NET_SSL_TRUSTSTORE);
        trustStorePassword = System.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD);
        enabledCipherSuites = System.getProperty(HTTPS_CIPHERSUITES);
        enabledProtocols = System.getProperty(JDK_TLS_CLIENT_PROTOCOLS, "TLSv1.2");
    }

    private Function<SSLSocketFactory, SSLSocketFactory> sslSocketFactoryConfigurer() {
        return sslSocketFactory -> new SSLSocketFactoryDecorator(sslSocketFactory, sslSocketConfigurer());
    }

    private Function<SSLSocket, SSLSocket> sslSocketConfigurer() {
        return sslSocket -> {
            if (enabledCipherSuites != null) {
                sslSocket.setEnabledCipherSuites(split(enabledCipherSuites));
            }
            if (enabledProtocols != null) {
                sslSocket.setEnabledProtocols(split(enabledProtocols));
            }
            if (sniHostnames.isEmpty()) {
                var sslParameters = sslSocket.getSSLParameters();
                sslParameters.setServerNames(sniHostnames.stream()
                        .map(SNIHostName::new)
                        .collect(Collectors.toList()));
                sslSocket.setSSLParameters(sslParameters);
            }
            if (performDomainValidation) {
                var sslParameters = sslSocket.getSSLParameters();
                sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
                sslSocket.setSSLParameters(sslParameters);
            }
            return sslSocket;
        };
    }

    private Function<SSLEngine, SSLEngine> sslEngineConfigurer() {
        return sslEngine -> {
            if (enabledCipherSuites != null) {
                sslEngine.setEnabledCipherSuites(split(enabledCipherSuites));
            }
            if (enabledProtocols != null) {
                sslEngine.setEnabledProtocols(split(enabledProtocols));
            }
            return sslEngine;
        };
    }

    protected String[] split(String s) {
        return s.split("\\s*,\\s*");
    }

    @Override
    public SSLContext getSSLContext() {
        try {
            var keyStore = getKeyStore(keyStoreType, keyStoreFile, keyStorePassword);
            var keyManagerFactory = KeyManagerFactory.getInstance(certificateType, provider);
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
            var keyManagers = keyManagerFactory.getKeyManagers();

            if (keyManagers != null && certAlias != null) {
                for (var i = 0; i < keyManagers.length; i++) {
                    if (keyManagers[i] instanceof X509KeyManager) {
                        keyManagers[i] = new AliasX509ExtendedKeyManager((X509KeyManager) keyManagers[i], certAlias);
                    }
                }
            }

            var trustStore = getKeyStore(trustStoreType, trustStoreFile, trustStorePassword);
            var trustManagerFactory = TrustManagerFactory.getInstance(certificateType, provider);
            trustManagerFactory.init(trustStore);
            var trustManagers = trustManagerFactory.getTrustManagers();

            var secureRandom = new SecureRandom();
            var sslContext = SSLContext.getInstance(tlsProtocol, provider);
            sslContext.init(keyManagers, trustManagers, secureRandom);

            if (sessionTimeout > 0) {
                sslContext.getClientSessionContext().setSessionTimeout(sessionTimeout);
            }

            return new CustomSSLContext(new SSLContextSpiDecorator(
                    sslContext,
                    sslEngineConfigurer(),
                    sslSocketFactoryConfigurer()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private KeyStore getKeyStore(String type, String storePath, String password) throws Exception {
        var keyStore = KeyStore.getInstance(type);
        try (var in = Files.newInputStream(Paths.get(storePath))) {
            keyStore.load(in, password.toCharArray());
            return keyStore;
        }
    }


    private static final class CustomSSLContext extends SSLContext {

        CustomSSLContext(SSLContextSpiDecorator sslContextSpiDecorator) {
            super(sslContextSpiDecorator,
                    sslContextSpiDecorator.getDelegate().getProvider(),
                    sslContextSpiDecorator.getDelegate().getProtocol());
        }

    }

    private static final class SSLContextSpiDecorator extends SSLContextSpi {

        private final SSLContext sslContext;
        private final Function<SSLEngine, SSLEngine> sslEngineConfigurer;
        private final Function<SSLSocketFactory, SSLSocketFactory> sslSocketFactoryConfigurer;

        public SSLContextSpiDecorator(SSLContext sslContext,
                                      Function<SSLEngine, SSLEngine> sslEngineConfigurer,
                                      Function<SSLSocketFactory, SSLSocketFactory> sslSocketFactoryConfigurer) {
            this.sslContext = sslContext;
            this.sslEngineConfigurer = sslEngineConfigurer;
            this.sslSocketFactoryConfigurer = sslSocketFactoryConfigurer;
        }

        SSLContext getDelegate() {
            return sslContext;
        }

        @Override
        protected void engineInit(KeyManager[] keyManagers,
                                  TrustManager[] trustManagers,
                                  SecureRandom secureRandom) throws KeyManagementException {
            sslContext.init(keyManagers, trustManagers, secureRandom);
        }

        @Override
        protected SSLSocketFactory engineGetSocketFactory() {
            var factory = sslContext.getSocketFactory();
            return sslSocketFactoryConfigurer.apply(factory);
        }

        @Override
        protected SSLServerSocketFactory engineGetServerSocketFactory() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected SSLEngine engineCreateSSLEngine() {
            var engine = sslContext.createSSLEngine();
            return sslEngineConfigurer.apply(engine);
        }

        @Override
        protected SSLEngine engineCreateSSLEngine(String host, int port) {
            var engine = sslContext.createSSLEngine(host, port);
            return sslEngineConfigurer.apply(engine);
        }

        @Override
        protected SSLSessionContext engineGetServerSessionContext() {
            return sslContext.getServerSessionContext();
        }

        @Override
        protected SSLSessionContext engineGetClientSessionContext() {
            return sslContext.getClientSessionContext();
        }
    }


    private static final class SSLSocketFactoryDecorator extends SSLSocketFactory {

        private final SSLSocketFactory delegate;
        private final Function<SSLSocket, SSLSocket> sslSocketConfigurer;

        public SSLSocketFactoryDecorator(SSLSocketFactory delegate, Function<SSLSocket, SSLSocket> sslSocketConfigurer) {
            this.delegate = delegate;
            this.sslSocketConfigurer = sslSocketConfigurer;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket() throws IOException {
            return configureSocket(delegate.createSocket());
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return configureSocket(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException {
            return configureSocket(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            return configureSocket(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return configureSocket(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return configureSocket(delegate.createSocket(address, port, localAddress, localPort));
        }

        public SSLSocketFactory getDelegate() {
            return this.delegate;
        }

        private Socket configureSocket(Socket s) {
            var socket = (SSLSocket) s;
            return sslSocketConfigurer.apply(socket);
        }
    }


    private static final class AliasX509ExtendedKeyManager extends X509ExtendedKeyManager {
        private final String certAlias;
        private final X509KeyManager keyManager;

        public AliasX509ExtendedKeyManager(X509KeyManager keyManager, String certAlias) {
            this.keyManager = keyManager;
            this.certAlias = certAlias;
        }

        public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
            return certAlias != null ? certAlias : keyManager.chooseClientAlias(keyTypes, issuers, socket);
        }

        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            return certAlias != null ? certAlias : keyManager.chooseServerAlias(keyType, issuers, socket);
        }

        @Override
        public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
            return certAlias != null ? certAlias : super.chooseEngineServerAlias(keyType, issuers, engine);
        }

        @Override
        public String chooseEngineClientAlias(String[] keyTypes, Principal[] issuers, SSLEngine engine) {
            return certAlias != null ? certAlias : super.chooseEngineClientAlias(keyTypes, issuers, engine);
        }

        public String[] getClientAliases(String keyType, Principal[] issuers) {
            return keyManager.getClientAliases(keyType, issuers);
        }

        public String[] getServerAliases(String keyType, Principal[] issuers) {
            return keyManager.getServerAliases(keyType, issuers);
        }

        public X509Certificate[] getCertificateChain(String alias) {
            return keyManager.getCertificateChain(alias);
        }

        public PrivateKey getPrivateKey(String alias) {
            return keyManager.getPrivateKey(alias);
        }

    }

}
