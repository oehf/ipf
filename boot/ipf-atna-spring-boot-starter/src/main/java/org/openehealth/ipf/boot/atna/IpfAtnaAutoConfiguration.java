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
import org.openehealth.ipf.commons.ihe.fhir.support.audit.marshal.BalpJsonSerializationStrategy;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.marshal.BalpXmlSerializationStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.security.AbstractAuthenticationAuditListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
                                     WsAuditDatasetEnricher wsAuditDatasetEnricher,
                                     @Value("${spring.application.name}") String appName) {
        if (config.getBalp() != null) {
            return balpConfiguration(defaultContextConfiguration(new DefaultBalpAuditContext(), config,
                auditTransmissionProtocol, auditMessageQueue, tlsParameters, auditMetadataProvider,
                auditExceptionHandler, auditMessagePostProcessor, wsAuditDatasetEnricher, appName), config);
        } else {
            return defaultContextConfiguration(new DefaultAuditContext(), config, auditTransmissionProtocol,
                auditMessageQueue, tlsParameters, auditMetadataProvider, auditExceptionHandler,
                auditMessagePostProcessor, wsAuditDatasetEnricher, appName);
        }
    }

    private <T extends DefaultAuditContext> T defaultContextConfiguration(T auditContext,
                                             IpfAtnaConfigurationProperties config,
                                             AuditTransmissionProtocol auditTransmissionProtocol,
                                             AuditMessageQueue auditMessageQueue,
                                             TlsParameters tlsParameters,
                                             AuditMetadataProvider auditMetadataProvider,
                                             AuditExceptionHandler auditExceptionHandler,
                                             AuditMessagePostProcessor auditMessagePostProcessor,
                                             WsAuditDatasetEnricher wsAuditDatasetEnricher,
                                             @Value("${spring.application.name}") String appName) {

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

        if (wsAuditDatasetEnricher != WsAuditDatasetEnricher.NONE) {
            auditContext.setWsAuditDatasetEnricher(wsAuditDatasetEnricher);
        }

        return auditContext;
    }

    private DefaultBalpAuditContext balpConfiguration(DefaultBalpAuditContext auditContext, IpfAtnaConfigurationProperties config) {
        if (config.getBalp() != null) {
            auditContext.setAuditRepositoryContextPath(config.getBalp().getAuditRepositoryContextPath());

            if (isNotBlank(config.getBalp().getAuditEventSerializationType())) {
                auditContext.setSerializationStrategy(
                    config.getBalp().getAuditEventSerializationType().equalsIgnoreCase("json") ?
                        new BalpJsonSerializationStrategy() : new BalpXmlSerializationStrategy());
            }
            if (config.getBalp().getOauth() != null) {
                if (config.getBalp().getOauth().getIdPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setIdPath(config.getBalp().getOauth().getIdPath());
                }
                if (config.getBalp().getOauth().getClientIdPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setClientIdPath(config.getBalp().getOauth().getClientIdPath());
                }
                if (config.getBalp().getOauth().getIssuerPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setIssuerPath(config.getBalp().getOauth().getIssuerPath());
                }
                if (config.getBalp().getOauth().getSubjectPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setSubjectPath(config.getBalp().getOauth().getSubjectPath());
                }
                if (config.getBalp().getOauth().getSubjectNamePath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setSubjectNamePath(config.getBalp().getOauth().getSubjectNamePath());
                }
                if (config.getBalp().getOauth().getSubjectRolePath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setSubjectRolePath(config.getBalp().getOauth().getSubjectRolePath());
                }
                if (config.getBalp().getOauth().getSubjectOrganizationIdPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setSubjectOrganizationIdPath(config.getBalp().getOauth().getSubjectOrganizationIdPath());
                }
                if (config.getBalp().getOauth().getPurposeOfUsePath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setPurposeOfUsePath(config.getBalp().getOauth().getPurposeOfUsePath());
                }
                if (config.getBalp().getOauth().getHomeCommunityIdPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setHomeCommunityIdPath(config.getBalp().getOauth().getHomeCommunityIdPath());
                }
                if (config.getBalp().getOauth().getNationalProviderIdPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setNationalProviderIdPath(config.getBalp().getOauth().getNationalProviderIdPath());
                }
                if (config.getBalp().getOauth().getDocIdPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setDocIdPath(config.getBalp().getOauth().getDocIdPath());
                }
                if (config.getBalp().getOauth().getPatientIdPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setPatientIdPath(config.getBalp().getOauth().getPatientIdPath());
                }
                if (config.getBalp().getOauth().getPersonIdPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setPersonIdPath(config.getBalp().getOauth().getPersonIdPath());
                }
                if (config.getBalp().getOauth().getAcpPath() != null) {
                    auditContext.getBalpJwtExtractorProperties().setAcpPath(config.getBalp().getOauth().getAcpPath());
                }
            }
        }
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
        var auditMetadataProvider = new DefaultAuditMetadataProvider();
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

    @Bean
    @ConditionalOnMissingBean
    public WsAuditDatasetEnricher wsAuditDatasetEnricher(IpfAtnaConfigurationProperties config) throws Exception {
        if (config.getWsAuditDatasetEnricherClass() != null) {
            return config.getWsAuditDatasetEnricherClass().getConstructor().newInstance();
        }
        return WsAuditDatasetEnricher.NONE;
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
