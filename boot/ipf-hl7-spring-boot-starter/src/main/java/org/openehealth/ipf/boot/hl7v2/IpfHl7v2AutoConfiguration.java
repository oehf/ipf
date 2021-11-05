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
import ca.uhn.hl7v2.util.idgenerator.IDGenerator;
import ca.uhn.hl7v2.util.idgenerator.IpfHiLoIdGenerator;
import ca.uhn.hl7v2.util.idgenerator.NanoTimeGenerator;
import ca.uhn.hl7v2.util.idgenerator.UUIDGenerator;
import ca.uhn.hl7v2.validation.ValidationContext;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.hl7.HL7MLLPNettyDecoderFactory;
import org.apache.camel.component.hl7.HL7MLLPNettyEncoderFactory;
import org.openehealth.ipf.boot.atna.IpfAtnaAutoConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.InteractiveContinuationStorage;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.SpringCacheInteractiveContinuationStorage;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.SpringCacheUnsolicitedFragmentationStorage;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.UnsolicitedFragmentationStorage;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.openehealth.ipf.modules.hl7.parser.DefaultEscaping;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@AutoConfigureAfter(IpfAtnaAutoConfiguration.class)
@EnableConfigurationProperties(IpfHl7v2ConfigurationProperties.class)
public class IpfHl7v2AutoConfiguration {

    private static final String IPF_HL7_DEFINITIONS_PREFIX = "org.openehealth.ipf.commons.ihe.hl7v2.definitions";

    private final IpfHl7v2ConfigurationProperties properties;

    public IpfHl7v2AutoConfiguration(IpfHl7v2ConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(HL7MLLPNettyDecoderFactory.class)
    HL7MLLPNettyDecoderFactory hl7decoder(IpfHl7v2ConfigurationProperties config) {
        var decoder = new HL7MLLPNettyDecoderFactory();
        if (config.getCharset() != null) {
            decoder.setCharset(config.getCharset());
        }
        decoder.setConvertLFtoCR(config.isConvertLinefeed());
        return decoder;
    }

    @Bean
    @ConditionalOnMissingBean(HL7MLLPNettyEncoderFactory.class)
    HL7MLLPNettyEncoderFactory hl7encoder(IpfHl7v2ConfigurationProperties config) {
        var encoder = new HL7MLLPNettyEncoderFactory();
        if (config.getCharset() != null) {
            encoder.setCharset(config.getCharset());
        }
        encoder.setConvertLFtoCR(config.isConvertLinefeed());
        return encoder;
    }

    @Bean
    @ConditionalOnMissingBean(CustomModelClassFactory.class)
    public CustomModelClassFactory mllpModelClassFactory() {
        var eventMap = new HashMap<String, String[]>();
        eventMap.put("2.3.1", new String[] {
                IPF_HL7_DEFINITIONS_PREFIX + "pix.v231"
        });
        eventMap.put("2.5", new String[] {
                IPF_HL7_DEFINITIONS_PREFIX + "pdq.v25",
                IPF_HL7_DEFINITIONS_PREFIX + "pix.v25"
        });
        var modelClassFactory = new CustomModelClassFactory(eventMap);
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
                                   ObjectProvider<IDGenerator> idGenerator) {
        HapiContext context = new DefaultHapiContext();
        context.setModelClassFactory(modelClassFactory);
        context.setValidationContext(validationContext);
        context.setProfileStore(profileStore);
        context.setParserConfiguration(parserConfiguration);
        idGenerator.ifAvailable(parserConfiguration::setIdGenerator);
        context.getParserConfiguration().setEscaping(DefaultEscaping.INSTANCE);
        return context;
    }

    @Bean
    @ConditionalOnMissingBean(HapiContextCustomizer.class)
    public HapiContextCustomizer hapiContextCustomizer() {
        return HapiContextCustomizer.NOOP;
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
        return new SpringCacheInteractiveContinuationStorage(cacheManager);
    }

    // Provide "unsolicitedFragmentationStorage" for HL7v2 fragmentation
    @Bean
    @ConditionalOnMissingBean(UnsolicitedFragmentationStorage.class)
    @ConditionalOnSingleCandidate(CacheManager.class)
    @ConditionalOnProperty("ipf.hl7v2.caching")
    public UnsolicitedFragmentationStorage unsolicitedFragmentationStorage(CacheManager cacheManager) {
        return new SpringCacheUnsolicitedFragmentationStorage(cacheManager);
    }

}
