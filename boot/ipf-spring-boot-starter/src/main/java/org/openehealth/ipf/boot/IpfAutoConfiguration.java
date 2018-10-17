package org.openehealth.ipf.boot;

import org.apache.camel.util.jsse.CipherSuitesParameters;
import org.apache.camel.util.jsse.ClientAuthentication;
import org.apache.camel.util.jsse.KeyManagersParameters;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextClientParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.SSLContextServerParameters;
import org.apache.camel.util.jsse.SecureSocketProtocolsParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.commons.spring.core.config.SpringConfigurationPostProcessor;
import org.openehealth.ipf.commons.spring.core.config.SpringRegistry;
import org.openehealth.ipf.commons.spring.map.SpringBidiMappingService;
import org.openehealth.ipf.commons.spring.map.config.CustomMappingsConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@EnableConfigurationProperties(IpfConfigurationProperties.class)
public class IpfAutoConfiguration {

    @Autowired
    private IpfConfigurationProperties ipfConfigurationProperties;

    @Bean
    @ConditionalOnMissingBean(Registry.class)
    Registry springRegistry() {
        return new SpringRegistry();
    }

    // Dynamically collect CustomMappings, if available

    @Bean
    @ConditionalOnMissingBean(SpringConfigurationPostProcessor.class)
    public SpringConfigurationPostProcessor postProcessor(CustomMappingsConfigurer customMappingsConfigurer) {
        SpringConfigurationPostProcessor processor = new SpringConfigurationPostProcessor();
        List<OrderedConfigurer> list = new ArrayList<>();
        if (customMappingsConfigurer != null) list.add(customMappingsConfigurer);
        processor.setSpringConfigurers(list);
        return processor;
    }

    @Bean
    @ConditionalOnMissingBean(CustomMappingsConfigurer.class)
    protected CustomMappingsConfigurer customMappingsConfigurer(SpringBidiMappingService mappingService) {
        CustomMappingsConfigurer configurer = new CustomMappingsConfigurer();
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
        Ssl sslConfig = serverProperties.getSsl();

        // Keystore
        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource(resourceName(sslConfig.getKeyStore()));
        ksp.setPassword(sslConfig.getKeyStorePassword());
        ksp.setProvider(sslConfig.getKeyStoreProvider());
        ksp.setType(sslConfig.getKeyStoreType());
        KeyManagersParameters kmp = new KeyManagersParameters();
        kmp.setKeyStore(ksp);
        kmp.setKeyPassword(sslConfig.getKeyPassword());

        // Truststore
        KeyStoreParameters tsp = new KeyStoreParameters();
        tsp.setResource(resourceName(sslConfig.getTrustStore()));
        tsp.setPassword(sslConfig.getTrustStorePassword());
        tsp.setProvider(sslConfig.getTrustStoreProvider());
        tsp.setType(sslConfig.getTrustStoreType());
        TrustManagersParameters tmp = new TrustManagersParameters();
        tmp.setKeyStore(tsp);

        // Server-side TLS parameters
        SSLContextServerParameters scsp = new SSLContextServerParameters();
        if (sslConfig.getClientAuth() == Ssl.ClientAuth.WANT) {
            scsp.setClientAuthentication(ClientAuthentication.WANT.name());
        } else if (sslConfig.getClientAuth() == Ssl.ClientAuth.NEED) {
            scsp.setClientAuthentication(ClientAuthentication.REQUIRE.name());
        }
        SecureSocketProtocolsParameters sspp = new SecureSocketProtocolsParameters();
        sspp.setSecureSocketProtocol(Arrays.asList(sslConfig.getEnabledProtocols()));
        scsp.setSecureSocketProtocols(sspp);
        CipherSuitesParameters csp = new CipherSuitesParameters();
        csp.setCipherSuite(Arrays.asList(sslConfig.getCiphers()));
        scsp.setCipherSuites(csp);

        // Client-side TLS parameters - assume the same configuration as server-side
        SSLContextClientParameters sccp = new SSLContextClientParameters();
        sccp.setSecureSocketProtocols(sspp);
        sccp.setCipherSuites(csp);

        SSLContextParameters scp = new SSLContextParameters();
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
        int i = resource.indexOf(":");
        return i >= 0 ? resource.substring(i + 1) : resource;
    }
}
