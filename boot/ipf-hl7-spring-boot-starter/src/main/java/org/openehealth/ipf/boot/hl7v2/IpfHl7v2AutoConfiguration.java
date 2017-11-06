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

package org.openehealth.ipf.boot.hl7v2;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.conf.store.ClasspathProfileStore;
import ca.uhn.hl7v2.conf.store.ProfileStore;
import ca.uhn.hl7v2.parser.ParserConfiguration;
import ca.uhn.hl7v2.util.idgenerator.FileBasedGenerator;
import ca.uhn.hl7v2.util.idgenerator.IDGenerator;
import ca.uhn.hl7v2.util.idgenerator.IpfHiLoIdGenerator;
import ca.uhn.hl7v2.util.idgenerator.NanoTimeGenerator;
import ca.uhn.hl7v2.util.idgenerator.UUIDGenerator;
import ca.uhn.hl7v2.validation.ValidationContext;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.hl7.CustomHL7MLLPCodec;
import org.apache.camel.component.hl7.HL7MLLPCodec;
import org.openehealth.ipf.boot.atna.IpfAtnaAutoConfiguration;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomPixAuditor;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.InteractiveContinuationStorage;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.UnsolicitedFragmentationStorage;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.openehealth.ipf.modules.hl7.parser.DefaultEscaping;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;
import org.openhealthtools.ihe.atna.auditor.PAMSourceAuditor;
import org.openhealthtools.ihe.atna.auditor.PDQConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXManagerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXSourceAuditor;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@AutoConfigureAfter(IpfAtnaAutoConfiguration.class)
@EnableConfigurationProperties(IpfHl7v2ConfigurationProperties.class)
public class IpfHl7v2AutoConfiguration {

    private static final String IPF_HL7_DEFINITIONS_PREFIX = "org.openehealth.ipf.commons.ihe.hl7v2.definitions";

    @Autowired
    private IpfHl7v2ConfigurationProperties properties;

    @Bean
    @ConditionalOnMissingBean(HL7MLLPCodec.class)
    HL7MLLPCodec hl7codec(IpfHl7v2ConfigurationProperties config) {
        HL7MLLPCodec hl7MLLPCodec = new CustomHL7MLLPCodec();
        if (config.getCharset() != null) {
            hl7MLLPCodec.setCharset(config.getCharset());
        }
        hl7MLLPCodec.setConvertLFtoCR(config.isConvertLinefeed());
        return hl7MLLPCodec;
    }

    @Bean
    @ConditionalOnMissingBean(CustomModelClassFactory.class)
    public CustomModelClassFactory mllpModelClassFactory() {
        Map<String, String[]> eventMap = new HashMap<>();
        eventMap.put("2.3.1", new String[] {
                IPF_HL7_DEFINITIONS_PREFIX + "pix.v231"
        });
        eventMap.put("2.5", new String[] {
                IPF_HL7_DEFINITIONS_PREFIX + "pdq.v25",
                IPF_HL7_DEFINITIONS_PREFIX + "pix.v25"
        });
        CustomModelClassFactory modelClassFactory = new CustomModelClassFactory(eventMap);
        modelClassFactory.setEventMapDirectory("org/openehealth/ipf/commons/ihe/hl7v2/");
        return modelClassFactory;
    }

    @Bean
    @ConditionalOnMissingBean(ProfileStore.class)
    public ProfileStore profileStore() {
        return new ClasspathProfileStore("/org/openehealth/ipf/gazelle/validation/profile/v2");
    }

    @Bean
    @ConditionalOnMissingBean(ValidationContext.class)
    public ValidationContext validationContext() {
        return ValidationContextFactory.noValidation();
    }

    @Bean
    @ConditionalOnMissingBean(IDGenerator.class)
    @ConditionalOnProperty(prefix = "ipf.hl7v2", name = "generator", havingValue = "file", matchIfMissing = true)
    public IDGenerator fileGenerator() {
        return new IpfHiLoIdGenerator(properties.getIdGenerator());
    }

    @Bean
    @ConditionalOnMissingBean(IDGenerator.class)
    @ConditionalOnProperty(prefix = "ipf.hl7v2", name = "generator", havingValue = "uuid")
    public IDGenerator uuidGenerator() {
        return new UUIDGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(IDGenerator.class)
    @ConditionalOnProperty(prefix = "ipf.hl7v2", name = "generator", havingValue = "nano")
    public IDGenerator nanoGenerator() {
        return new NanoTimeGenerator();
    }

    @ConfigurationProperties(prefix = "ipf.hl7v2.parser")
    @Bean
    public ParserConfiguration parserConfiguration() {
        return new ParserConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(HapiContext.class)
    public HapiContext hapiContext(CustomModelClassFactory modelClassFactory, ProfileStore profileStore,
                                   ValidationContext validationContext, ParserConfiguration parserConfiguration,
                                   IDGenerator idGenerator) {
        HapiContext context = new DefaultHapiContext();
        context.setModelClassFactory(modelClassFactory);
        context.setValidationContext(validationContext);
        context.setProfileStore(profileStore);
        context.setParserConfiguration(parserConfiguration);
        context.getParserConfiguration().setIdGenerator(idGenerator);
        context.getParserConfiguration().setEscaping(DefaultEscaping.INSTANCE);
        return context;
    }

    // Provide bean for MLLP endpoint dispatching
    @Bean
    @ConditionalOnMissingBean(ConsumerDispatchingInterceptor.class)
    public ConsumerDispatchingInterceptor mllpDispatcher(CamelContext camelContext) {
        return new ConsumerDispatchingInterceptor(camelContext);
    }

    // Provide "interactiveContinuationStorage" for HL7v2 paging
    @Bean
    @ConditionalOnMissingBean(InteractiveContinuationStorage.class)
    @ConditionalOnSingleCandidate(CacheManager.class)
    @ConditionalOnProperty("ipf.hl7v2.caching")
    public InteractiveContinuationStorage interactiveContinuationStorage(CacheManager cacheManager) {
        return new CachingInteractiveHl7v2ContinuationStorage(cacheManager);
    }

    // Provide "unsolicitedFragmentationStorage" for HL7v2 fragmentation
    @Bean
    @ConditionalOnMissingBean(UnsolicitedFragmentationStorage.class)
    @ConditionalOnSingleCandidate(CacheManager.class)
    @ConditionalOnProperty("ipf.hl7v2.caching")
    public UnsolicitedFragmentationStorage unsolicitedFragmentationStorage(CacheManager cacheManager) {
        return new CachingUnsolicitedFragmentionStorage(cacheManager);
    }


    // Some ATNA auditors

    @Bean
    @ConditionalOnMissingBean(PIXManagerAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor.enabled")
    public PIXManagerAuditor pixManagerAuditor(AuditorModuleConfig config) {
        PIXManagerAuditor auditor = PIXManagerAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(PIXConsumerAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor.enabled")
    public PIXConsumerAuditor pixConsumerAuditor(AuditorModuleConfig config) {
        PIXConsumerAuditor auditor = PIXConsumerAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(PIXSourceAuditor.class)
    @ConditionalOnProperty("ipf.atna.auditor.enabled")
    public PIXSourceAuditor pixSourceAuditor(AuditorModuleConfig config) {
        PIXSourceAuditor auditor = PIXSourceAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(PDQConsumerAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor.enabled")
    public PDQConsumerAuditor pdqConsumerAuditor(AuditorModuleConfig config) {
        PDQConsumerAuditor auditor = PDQConsumerAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(PAMSourceAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor.enabled")
    public PAMSourceAuditor pamSourceAuditor(AuditorModuleConfig config) {
        PAMSourceAuditor auditor = PAMSourceAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(CustomPixAuditor.class)
    @ConditionalOnSingleCandidate(AuditorModuleConfig.class)
    @ConditionalOnProperty("ipf.atna.auditor.enabled")
    public CustomPixAuditor customPixAuditor(AuditorModuleConfig config) {
        CustomPixAuditor auditor = CustomPixAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }
}
