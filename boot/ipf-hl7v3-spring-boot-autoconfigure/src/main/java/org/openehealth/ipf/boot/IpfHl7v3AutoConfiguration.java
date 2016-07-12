package org.openehealth.ipf.boot;

import org.openehealth.ipf.commons.ihe.core.atna.custom.Hl7v3Auditor;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@EnableConfigurationProperties(IpfHl7v3ConfigurationProperties.class)
public class IpfHl7v3AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Hl7v3Auditor.class)
    public Hl7v3Auditor pdqConsumerAuditor(AuditorModuleConfig config) {
        Hl7v3Auditor auditor = Hl7v3Auditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

}
