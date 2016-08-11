package org.openehealth.ipf.boot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@ConfigurationProperties(prefix = "ipf.xds")
public class IpfXdsConfigurationProperties {

    @Getter
    @Setter
    private boolean caching;
}
