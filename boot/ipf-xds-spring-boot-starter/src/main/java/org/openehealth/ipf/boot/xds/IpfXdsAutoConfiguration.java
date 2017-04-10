/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.boot.xds;

import org.openehealth.ipf.boot.atna.IpfAtnaAutoConfiguration;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@AutoConfigureAfter(IpfAtnaAutoConfiguration.class)
@EnableConfigurationProperties(IpfXdsConfigurationProperties.class)
public class IpfXdsAutoConfiguration {

    @Autowired
    private IpfXdsConfigurationProperties xdsConfig;

    @Bean
    @ConditionalOnMissingBean(XDSSourceAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor-enabled")
    public XDSSourceAuditor xdsSourceAuditor(AuditorModuleConfig config) {
        XDSSourceAuditor auditor = XDSSourceAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XDSConsumerAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor-enabled")
    public XDSConsumerAuditor xdsConsumerAuditor(AuditorModuleConfig config) {
        XDSConsumerAuditor auditor = XDSConsumerAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XDSConsumerAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor-enabled")
    public XDSRepositoryAuditor xdsRepositoryAuditor(AuditorModuleConfig config) {
        XDSRepositoryAuditor auditor = XDSRepositoryAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XDSConsumerAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor-enabled")
    public XDSRegistryAuditor xdsRegistryAuditor(AuditorModuleConfig config) {
        XDSRegistryAuditor auditor = XDSRegistryAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(CustomXdsAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor-enabled")
    public CustomXdsAuditor customXdsAuditor(AuditorModuleConfig config) {
        CustomXdsAuditor auditor = CustomXdsAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XCAInitiatingGatewayAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor-enabled")
    public XCAInitiatingGatewayAuditor xcaInitiatingGatewayAuditor(AuditorModuleConfig config) {
        XCAInitiatingGatewayAuditor auditor = XCAInitiatingGatewayAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XCARespondingGatewayAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor-enabled")
    public XCARespondingGatewayAuditor xcaRespondingGatewayAuditor(AuditorModuleConfig config) {
        XCARespondingGatewayAuditor auditor = XCARespondingGatewayAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(XDMAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor-enabled")
    public XDMAuditor xdmAuditor(AuditorModuleConfig config) {
        XDMAuditor auditor = XDMAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }


    @Bean
    @ConditionalOnMissingBean(AsynchronyCorrelator.class)
    @ConditionalOnSingleCandidate(CacheManager.class)
    @ConditionalOnProperty("ipf.xds.caching")
    public AsynchronyCorrelator cachingAsynchronyCorrelator(CacheManager cacheManager) {
        return new CachingAsynchronyCorrelator(cacheManager);
    }

}
