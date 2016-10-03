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

package org.openehealth.ipf.boot.atna;

import org.openhealthtools.ihe.atna.auditor.AuditorTLSConfig;
import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.security.AbstractAuthenticationAuditListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
@EnableConfigurationProperties(IpfAtnaConfigurationProperties.class)
public class IpfAtnaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuditorModuleContext atnaAuditorModuleContext(IpfAtnaConfigurationProperties config)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        AuditorModuleContext auditorModuleContext = AuditorModuleContext.getContext();
        auditorModuleContext.setQueue(config.getAuditQueueClass().newInstance());
        return auditorModuleContext;
    }

    @Bean
    @ConfigurationProperties(prefix = "ipf.atna")
    @ConditionalOnMissingBean
    public AuditorModuleConfig atnaAuditorModuleConfig(AuditorModuleContext auditorModuleContext,
                                                       @Value("${spring.application.name}") String appName) {
        AuditorModuleConfig auditorModuleConfig = auditorModuleContext.getConfig();
        // Use app name as auditor source if not configured otherwise
        if (auditorModuleConfig.getAuditSourceId() == null) {
            auditorModuleConfig.setAuditSourceId(appName);
        }
        return auditorModuleConfig;
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.auditor.enabled")
    @ConditionalOnMissingBean
    AuditorTLSConfig atnaAuditorTLSConfig(AuditorModuleConfig auditorModuleConfig, IpfAtnaConfigurationProperties config) {
        AuditorTLSConfig auditorTLSConfig = new AuditorTLSConfig(auditorModuleConfig);
        auditorTLSConfig.setSecurityDomainName(config.getSecurityDomainName());
        return auditorTLSConfig;
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.auditor.enabled")
    public IHEAuditor basicAuditor(AuditorModuleConfig config) {
        IHEAuditor auditor = IHEAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.auditor.enabled")
    @ConditionalOnMissingBean
    ApplicationStartEventListener applicationStartEventListener(@Qualifier("basicAuditor") IHEAuditor auditor) {
        return new ApplicationStartEventListener(auditor);
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.auditor.enabled")
    @ConditionalOnMissingBean
    ApplicationStopEventListener applicationStopEventListener(@Qualifier("basicAuditor") IHEAuditor auditor) {
        return new ApplicationStopEventListener(auditor);
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.auditor.enabled")
    @ConditionalOnClass(name = "org.springframework.security.authentication.event.AbstractAuthenticationEvent")
    @ConditionalOnMissingBean(AbstractAuthenticationAuditListener.class)
    AuthenticationListener loginListener(@Qualifier("basicAuditor") IHEAuditor auditor) {
        return new AuthenticationListener(auditor);
    }


}
