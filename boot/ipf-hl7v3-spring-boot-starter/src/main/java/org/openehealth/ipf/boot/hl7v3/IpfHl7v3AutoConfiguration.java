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

package org.openehealth.ipf.boot.hl7v3;

import org.openehealth.ipf.commons.ihe.core.atna.custom.Hl7v3Auditor;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3ContinuationStorage;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
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
    @ConditionalOnProperty("ipf.atna.auditor.enabled")
    public Hl7v3Auditor pdqConsumerAuditor(AuditorModuleConfig config) {
        Hl7v3Auditor auditor = Hl7v3Auditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    // Provide "interactiveContinuationStorage" for HL7v2 paging
    @Bean
    @ConditionalOnMissingBean(Hl7v3ContinuationStorage.class)
    public Hl7v3ContinuationStorage hl7v3ContinuationStorage(CacheManager cacheManager) {
        return new CachingHl7v3ContinuationStorage(cacheManager);
    }

}