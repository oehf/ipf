package org.openehealth.ipf.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@ConfigurationProperties(prefix = "ipf.commons")
public class IpfConfigurationProperties {

    /**
     * Determines if an existing Spring Boot SSL configuration (server.ssl.*) shall be reused for constructing
     * a Camel SSLContextParameter bean
     */
    boolean reuseSslConfig = true;
}
