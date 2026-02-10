/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.boot;

import org.apache.camel.CamelContext;
import org.apache.camel.support.jsse.CipherSuitesParameters;
import org.apache.camel.support.jsse.ClientAuthentication;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextClientParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.SSLContextServerParameters;
import org.apache.camel.support.jsse.SecureSocketProtocolsParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.commons.core.ssl.CustomTlsParameters;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;
import org.openehealth.ipf.commons.spring.core.config.SpringConfigurationPostProcessor;
import org.openehealth.ipf.commons.spring.core.config.SpringRegistry;
import org.openehealth.ipf.commons.spring.map.SpringBidiMappingService;
import org.openehealth.ipf.commons.spring.map.config.CustomMappingsConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ssl.NoSuchSslBundleException;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@EnableConfigurationProperties(IpfConfigurationProperties.class)
public class IpfAutoConfiguration {

    public static final String SERVER_SSL_CONTEXT_PARAMETERS = "bootSslContextParameters";
    public static final String SERVER_TLS_PARAMETERS = "bootTlsParameters";

    private static final Logger log = LoggerFactory.getLogger(IpfAutoConfiguration.class);


    @Bean
    @ConditionalOnMissingBean(Registry.class)
    Registry springRegistry() {
        return new SpringRegistry();
    }

    // Dynamically collect CustomMappings, if available

    @Bean
    @ConditionalOnMissingBean(SpringConfigurationPostProcessor.class)
    public SpringConfigurationPostProcessor postProcessor(CustomMappingsConfigurer<SpringRegistry> customMappingsConfigurer) {
        var processor = new SpringConfigurationPostProcessor();
        var list = new ArrayList<OrderedConfigurer>();
        if (customMappingsConfigurer != null) list.add(customMappingsConfigurer);
        processor.setSpringConfigurers(list);
        return processor;
    }

    @Bean
    @ConditionalOnMissingBean(CustomMappingsConfigurer.class)
    protected CustomMappingsConfigurer<SpringRegistry> customMappingsConfigurer(SpringBidiMappingService mappingService) {
        var configurer = new CustomMappingsConfigurer<SpringRegistry>();
        configurer.setMappingService(mappingService);
        return configurer;
    }

    @Bean
    @ConditionalOnMissingBean(SpringBidiMappingService.class)
    public SpringBidiMappingService mappingService() {
        return new SpringBidiMappingService();
    }


    @Bean(name = SERVER_TLS_PARAMETERS)
    @ConditionalOnProperty(prefix = "ipf.commons", name = "reuse-ssl-config", havingValue = "true")
    public TlsParameters tlsParameters(SslBundles sslBundles, ServerProperties serverProperties) {
        var sslConfig = serverProperties.getSsl();
        if (sslConfig.getBundle() != null) {
            try {
                return new SslBundleTlsParameters(sslBundles, sslConfig.getBundle());
            } catch (NoSuchSslBundleException e) {
                log.info("No Spring Boot server bundle configured, using dedicated TLS attributes for bean {}",
                    SERVER_TLS_PARAMETERS);
            }
        }
        var tlsParameters = new CustomTlsParameters();
        tlsParameters.setKeyStoreFile(resolveResourcePath(sslConfig.getKeyStore()));
        tlsParameters.setKeyStorePassword(sslConfig.getKeyStorePassword());
        tlsParameters.setKeyStoreType(sslConfig.getKeyStoreType());
        tlsParameters.setTrustStoreFile(resolveResourcePath(sslConfig.getTrustStore()));
        tlsParameters.setTrustStorePassword(sslConfig.getTrustStorePassword());
        tlsParameters.setTrustStoreType(sslConfig.getTrustStoreType());
        tlsParameters.setEnabledProtocols(String.join(",", sslConfig.getEnabledProtocols()));
        tlsParameters.setClientAuthentication(sslConfig.getClientAuth() != null ? sslConfig.getClientAuth().name() : null);
        tlsParameters.setTlsProtocol(sslConfig.getProtocol());
        tlsParameters.setProvider(sslConfig.getKeyStoreProvider());
        return tlsParameters;
    }

    // Set up sslContextParameters bean from the Spring Boot server config. Can be used for e.g.
    // serving MLLP endpoints or as producer configuration.

    @Bean(name = SERVER_SSL_CONTEXT_PARAMETERS)
    @ConditionalOnProperty(prefix = "ipf.commons", name = "reuse-ssl-config", havingValue = "true")
    public SSLContextParameters sslContextParameters(SslBundles sslBundles, ServerProperties serverProperties) {
        var sslConfig = serverProperties.getSsl();
        if (sslConfig.getBundle() != null) {
            try {
                var sslBundle = sslBundles.getBundle(sslConfig.getBundle());
                return new SSLContextParameters() {
                    @Override
                    protected void configureSSLContext(SSLContext context) {
                        throw new UnsupportedOperationException("Static SSLContextParameters cannot be further configured");
                    }

                    @Override
                    public SSLContext createSSLContext(CamelContext camelContext) {
                        return sslBundle.createSslContext();
                    }
                };
            } catch (NoSuchSslBundleException e) {
                log.info("No Spring Boot server SslBundle configured, using dedicated TLS attributes for {} bean,",
                    SERVER_SSL_CONTEXT_PARAMETERS);
            }
        }
        // Keystore
        var ksp = new KeyStoreParameters();
        ksp.setResource(resourceName(sslConfig.getKeyStore()));
        ksp.setPassword(sslConfig.getKeyStorePassword());
        ksp.setProvider(sslConfig.getKeyStoreProvider());
        ksp.setType(sslConfig.getKeyStoreType());
        var kmp = new KeyManagersParameters();
        kmp.setKeyStore(ksp);
        kmp.setKeyPassword(sslConfig.getKeyPassword());

        // Truststore
        var tsp = new KeyStoreParameters();
        tsp.setResource(resourceName(sslConfig.getTrustStore()));
        tsp.setPassword(sslConfig.getTrustStorePassword());
        tsp.setProvider(sslConfig.getTrustStoreProvider());
        tsp.setType(sslConfig.getTrustStoreType());
        var tmp = new TrustManagersParameters();
        tmp.setKeyStore(tsp);

        // Server-side TLS parameters
        var scsp = new SSLContextServerParameters();
        if (sslConfig.getClientAuth() == Ssl.ClientAuth.WANT) {
            scsp.setClientAuthentication(ClientAuthentication.WANT.name());
        } else if (sslConfig.getClientAuth() == Ssl.ClientAuth.NEED) {
            scsp.setClientAuthentication(ClientAuthentication.REQUIRE.name());
        }
        var sspp = new SecureSocketProtocolsParameters();
        sspp.setSecureSocketProtocol(Arrays.asList(sslConfig.getEnabledProtocols()));
        scsp.setSecureSocketProtocols(sspp);
        var csp = new CipherSuitesParameters();
        csp.setCipherSuite(Arrays.asList(sslConfig.getCiphers()));
        scsp.setCipherSuites(csp);

        // Client-side TLS parameters - assume the same configuration as server-side
        var sccp = new SSLContextClientParameters();
        sccp.setSecureSocketProtocols(sspp);
        sccp.setCipherSuites(csp);

        var scp = new SSLContextParameters();
        scp.setCertAlias(sslConfig.getKeyAlias());
        scp.setKeyManagers(kmp);
        scp.setTrustManagers(tmp);
        scp.setServerParameters(scsp);
        scp.setClientParameters(sccp);

        return scp;
    }

    // Spring Boot Keystore names are prepended with file: or classpath:, while
    // Camel takes the plain resource name and tries all possibilities
    private String resourceName(String resource) {
        var i = resource.indexOf(":");
        return i >= 0 ? resource.substring(i + 1) : resource;
    }

    private String resolveResourcePath(String resource) {
        if (resource == null) {
            return null;
        }

        if (resource.startsWith("classpath:")) {
            var resourcePath = resource.substring("classpath:".length());
            try {
                var url = getClass().getClassLoader().getResource(resourcePath);
                if (url != null) {
                    return Paths.get(url.toURI()).toAbsolutePath().toString();
                }
                throw new IllegalArgumentException("Classpath resource not found: " + resourcePath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to resolve classpath resource: " + resource, e);
            }
        } else if (resource.startsWith("file:")) {
            return resource.substring("file:".length());
        }

        // Assume it's already a plain file path
        return resource;
    }
}
