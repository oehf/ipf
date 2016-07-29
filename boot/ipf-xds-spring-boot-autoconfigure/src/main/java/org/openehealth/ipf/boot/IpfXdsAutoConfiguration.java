package org.openehealth.ipf.boot;

import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomXdsAuditor;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openhealthtools.ihe.atna.auditor.XCAInitiatingGatewayAuditor;
import org.openhealthtools.ihe.atna.auditor.XCARespondingGatewayAuditor;
import org.openhealthtools.ihe.atna.auditor.XDMAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSRegistryAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSRepositoryAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSSourceAuditor;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@EnableConfigurationProperties(IpfXdsConfigurationProperties.class)
public class IpfXdsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(XDSSourceAuditor.class)
    public XDSSourceAuditor xdsSourceAuditor(AuditorModuleConfig config) {
        XDSSourceAuditor auditor = XDSSourceAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XDSConsumerAuditor.class)
    public XDSConsumerAuditor xdsConsumerAuditor(AuditorModuleConfig config) {
        XDSConsumerAuditor auditor = XDSConsumerAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XDSConsumerAuditor.class)
    public XDSRepositoryAuditor xdsRepositoryAuditor(AuditorModuleConfig config) {
        XDSRepositoryAuditor auditor = XDSRepositoryAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XDSConsumerAuditor.class)
    public XDSRegistryAuditor xdsRegistryAuditor(AuditorModuleConfig config) {
        XDSRegistryAuditor auditor = XDSRegistryAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(CustomXdsAuditor.class)
    public CustomXdsAuditor customXdsAuditor(AuditorModuleConfig config) {
        CustomXdsAuditor auditor = CustomXdsAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XCAInitiatingGatewayAuditor.class)
    public XCAInitiatingGatewayAuditor xcaInitiatingGatewayAuditor(AuditorModuleConfig config) {
        XCAInitiatingGatewayAuditor auditor = XCAInitiatingGatewayAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XCARespondingGatewayAuditor.class)
    public XCARespondingGatewayAuditor xcaRespondingGatewayAuditor(AuditorModuleConfig config) {
        XCARespondingGatewayAuditor auditor = XCARespondingGatewayAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XDMAuditor.class)
    public XDMAuditor xdmAuditor(AuditorModuleConfig config) {
        XDMAuditor auditor = XDMAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }


    @Bean
    @ConditionalOnMissingBean(AsynchronyCorrelator.class)
    public AsynchronyCorrelator cachingAsynchronyCorrelator(CacheManager cacheManager) {
        return new CachingAsynchronyCorrelator(cacheManager);
    }

}
