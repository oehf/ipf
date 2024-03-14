package org.openehealth.ipf.boot;


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
import org.openehealth.ipf.commons.spring.core.config.SpringConfigurationPostProcessor;
import org.openehealth.ipf.commons.spring.core.config.SpringRegistry;
import org.openehealth.ipf.commons.spring.map.SpringBidiMappingService;
import org.openehealth.ipf.commons.spring.map.config.CustomMappingsConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@EnableConfigurationProperties(IpfConfigurationProperties.class)
public class IpfAutoConfiguration {

    private final IpfConfigurationProperties ipfConfigurationProperties;

    public IpfAutoConfiguration(IpfConfigurationProperties ipfConfigurationProperties) {
        this.ipfConfigurationProperties = ipfConfigurationProperties;
    }

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


    // Set up sslContextParameters bean from the Spring Boot server config. Can be used for e.g.
    // serving MLLP endpoints or as producer configuration.

    @Bean(name = "bootSslContextParameters")
    @ConditionalOnProperty(prefix = "ipf.commons", name = "reuse-ssl-config")
    public SSLContextParameters sslContextParameters(ServerProperties serverProperties) {
        var sslConfig = serverProperties.getSsl();

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
}
