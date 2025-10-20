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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditMessagePostProcessor;
import org.openehealth.ipf.commons.audit.AuditMetadataProvider;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.DefaultAuditMetadataProvider;
import org.openehealth.ipf.commons.audit.DefaultBalpAuditContext;
import org.openehealth.ipf.commons.audit.FhirAuditDatasetEnricher;
import org.openehealth.ipf.commons.audit.WsAuditDatasetEnricher;
import org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler;
import org.openehealth.ipf.commons.audit.handler.LoggingAuditExceptionHandler;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionChannel;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.springframework.beans.factory.ObjectProvider;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;
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

@Configuration
@EnableConfigurationProperties(IpfAtnaConfigurationProperties.class)
public class IpfAtnaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuditContext auditContext(IpfAtnaConfigurationProperties config,
                                     ObjectProvider<AuditContextCustomizer> auditContextCustomizer,
                                     AuditTransmissionProtocol auditTransmissionProtocol,
                                     AuditMessageQueue auditMessageQueue,
                                     TlsParameters tlsParameters,
                                     AuditMetadataProvider auditMetadataProvider,
                                     AuditExceptionHandler auditExceptionHandler,
                                     AuditMessagePostProcessor auditMessagePostProcessor,
                                     WsAuditDatasetEnricher wsAuditDatasetEnricher,
                                     FhirAuditDatasetEnricher fhirAuditDatasetEnricher,
                                     @Value("${spring.application.name}") String appName) {
        DefaultAuditContext auditContext;
        if (config.getBalp() != null) {
            auditContext = new DefaultBalpAuditContext();
            configureBalpAuditContext((DefaultBalpAuditContext) auditContext, config);
        } else {
            auditContext = new DefaultAuditContext();
        }
        configureDefaultAuditContext(auditContext, config, auditTransmissionProtocol,
            auditMessageQueue, tlsParameters, auditMetadataProvider, auditExceptionHandler,
            auditMessagePostProcessor, wsAuditDatasetEnricher, fhirAuditDatasetEnricher, appName);
        auditContextCustomizer.forEach(consumer ->
            consumer.customizeAuditContext(auditContext));
        return auditContext;
    }

    private void configureDefaultAuditContext(DefaultAuditContext auditContext,
                                              IpfAtnaConfigurationProperties config,
                                              AuditTransmissionProtocol auditTransmissionProtocol,
                                              AuditMessageQueue auditMessageQueue,
                                              TlsParameters tlsParameters,
                                              AuditMetadataProvider auditMetadataProvider,
                                              AuditExceptionHandler auditExceptionHandler,
                                              AuditMessagePostProcessor auditMessagePostProcessor,
                                              WsAuditDatasetEnricher wsAuditDatasetEnricher,
                                              FhirAuditDatasetEnricher fhirAuditDatasetEnricher,
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
        if (fhirAuditDatasetEnricher != FhirAuditDatasetEnricher.NONE) {
            auditContext.setFhirAuditDatasetEnricher(fhirAuditDatasetEnricher);
        }
    }

    private void configureBalpAuditContext(DefaultBalpAuditContext auditContext, IpfAtnaConfigurationProperties config) {
        auditContext.setAuditRepositoryContextPath(config.getBalp().getAuditRepositoryContextPath());

        // FHIR Serialization Strategy is applied via AuditContextCustomizer
        // in IpfFhirAutoConfiguration
        var oAuth = config.getBalp().getOauth();
        var props = auditContext.getBalpJwtExtractorProperties();

        if (oAuth != null) {
            if (oAuth.getIdPath() != null) {
                props.setIdPath(oAuth.getIdPath());
            }
            if (oAuth.getClientIdPath() != null) {
                props.setClientIdPath(oAuth.getClientIdPath());
            }
            if (oAuth.getIssuerPath() != null) {
                props.setIssuerPath(oAuth.getIssuerPath());
            }
            if (oAuth.getSubjectPath() != null) {
                props.setSubjectPath(oAuth.getSubjectPath());
            }
            if (oAuth.getSubjectNamePath() != null) {
                props.setSubjectNamePath(oAuth.getSubjectNamePath());
            }
            if (oAuth.getSubjectRolePath() != null) {
                props.setSubjectRolePath(oAuth.getSubjectRolePath());
            }
            if (oAuth.getSubjectOrganizationIdPath() != null) {
                props.setSubjectOrganizationIdPath(oAuth.getSubjectOrganizationIdPath());
            }
            if (oAuth.getPurposeOfUsePath() != null) {
                props.setPurposeOfUsePath(oAuth.getPurposeOfUsePath());
            }
            if (oAuth.getHomeCommunityIdPath() != null) {
                props.setHomeCommunityIdPath(oAuth.getHomeCommunityIdPath());
            }
            if (oAuth.getNationalProviderIdPath() != null) {
                props.setNationalProviderIdPath(oAuth.getNationalProviderIdPath());
            }
            if (oAuth.getDocIdPath() != null) {
                props.setDocIdPath(oAuth.getDocIdPath());
            }
            if (oAuth.getPatientIdPath() != null) {
                props.setPatientIdPath(oAuth.getPatientIdPath());
            }
            if (oAuth.getPersonIdPath() != null) {
                props.setPersonIdPath(oAuth.getPersonIdPath());
            }
            if (oAuth.getAcpPath() != null) {
                props.setAcpPath(oAuth.getAcpPath());
            }
        }
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

    @Bean
    @ConditionalOnMissingBean
    public FhirAuditDatasetEnricher fhirAuditDatasetEnricher(IpfAtnaConfigurationProperties config) throws Exception {
        if (config.getFhirAuditDatasetEnricherClass() != null) {
            return config.getFhirAuditDatasetEnricherClass().getConstructor().newInstance();
        }
        return FhirAuditDatasetEnricher.NONE;
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
