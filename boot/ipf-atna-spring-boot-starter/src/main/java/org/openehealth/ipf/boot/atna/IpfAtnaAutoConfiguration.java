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

import org.openehealth.ipf.commons.audit.*;
import org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler;
import org.openehealth.ipf.commons.audit.handler.LoggingAuditExceptionHandler;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionChannel;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.security.AbstractAuthenticationAuditListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    public AuditContext auditContext(IpfAtnaConfigurationProperties config,
                                     AuditTransmissionProtocol auditTransmissionProtocol,
                                     AuditMessageQueue auditMessageQueue,
                                     TlsParameters tlsParameters,
                                     AuditMetadataProvider auditMetadataProvider,
                                     AuditExceptionHandler auditExceptionHandler,
                                     AuditMessagePostProcessor auditMessagePostProcessor,
                                     @Value("${spring.application.name}") String appName) {
        DefaultAuditContext auditContext = new DefaultAuditContext();
        auditContext.setAuditEnabled(config.isAuditEnabled());

        // Simple properties
        auditContext.setAuditSourceId(config.getAuditSourceId() != null ? config.getAuditSourceId() : appName);
        auditContext.setAuditEnterpriseSiteId(config.getAuditEnterpriseSiteId());
        auditContext.setAuditRepositoryHost(config.getAuditRepositoryHost());
        auditContext.setAuditRepositoryPort(config.getAuditRepositoryPort());
        auditContext.setAuditSource(config.getAuditSourceType());
        auditContext.setIncludeParticipantsFromResponse(config.isIncludeParticipantsFromResponse());
        auditContext.setAuditValueIfMissing(config.getAuditValueIfMissing());

        // Strategies and complex parameters; overrideable
        auditContext.setTlsParameters(tlsParameters);
        auditContext.setAuditMetadataProvider(auditMetadataProvider);
        auditContext.setAuditTransmissionProtocol(auditTransmissionProtocol);
        auditContext.setAuditMessageQueue(auditMessageQueue);
        auditContext.setAuditExceptionHandler(auditExceptionHandler);
        auditContext.setAuditMessagePostProcessor(auditMessagePostProcessor);

        return auditContext;
    }

    // The following beans configure aud strategies (formats, queues, exception handlers) and
    // can all be overwritten

    @Bean
    @ConditionalOnMissingBean
    public AuditMessageQueue auditMessageQueue(IpfAtnaConfigurationProperties config) throws Exception {
        return config.getAuditQueueClass().getConstructor().newInstance();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditMessagePostProcessor auditMessagePostProcessor(IpfAtnaConfigurationProperties config) throws Exception {
        if (config.getAuditMessagePostProcessorClass() != null) {
            return config.getAuditMessagePostProcessorClass().getConstructor().newInstance();
        }
        return AuditMessagePostProcessor.noOp();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditTransmissionProtocol auditTransmissionProtocol(IpfAtnaConfigurationProperties config,
                                                               TlsParameters tlsParameters) throws Exception {
        if (config.getAuditSenderClass() != null) {
            return config.getAuditSenderClass().getConstructor(TlsParameters.class)
                    .newInstance(tlsParameters);
        }
        return AuditTransmissionChannel.fromProtocolName(config.getAuditRepositoryTransport())
                .makeInstance(tlsParameters);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditMetadataProvider auditMetadataProvider(IpfAtnaConfigurationProperties config,
                                                       @Value("${spring.application.name}") String appName) {
        DefaultAuditMetadataProvider auditMetadataProvider = new DefaultAuditMetadataProvider();
        auditMetadataProvider.setSendingApplication(config.getAuditSendingApplication() != null ?
                config.getAuditSendingApplication() :
                appName);
        return auditMetadataProvider;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditExceptionHandler auditExceptionHandler(IpfAtnaConfigurationProperties config) throws Exception {
        if (config.getAuditExceptionHandlerClass() != null) {
            return config.getAuditExceptionHandlerClass().getConstructor().newInstance();
        }
        return new LoggingAuditExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TlsParameters tlsParameters() {
        return TlsParameters.getDefault();
    }

    // Some audit event listeners

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.audit-enabled")
    @ConditionalOnMissingBean
    ApplicationStartEventListener applicationStartEventListener(AuditContext auditContext) {
        return new ApplicationStartEventListener(auditContext);
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.audit-enabled")
    @ConditionalOnMissingBean
    ApplicationStopEventListener applicationStopEventListener(AuditContext auditContext) {
        return new ApplicationStopEventListener(auditContext);
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.audit-enabled")
    @ConditionalOnClass(name = "org.springframework.security.authentication.event.AbstractAuthenticationEvent")
    @ConditionalOnMissingBean(AbstractAuthenticationAuditListener.class)
    AuthenticationListener loginListener(AuditContext auditContext) {
        return new AuthenticationListener(auditContext);
    }

}
