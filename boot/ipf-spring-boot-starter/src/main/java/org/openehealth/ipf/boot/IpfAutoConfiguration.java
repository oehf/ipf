package org.openehealth.ipf.boot;

import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.commons.core.config.SpringConfigurationPostProcessor;
import org.openehealth.ipf.commons.core.config.SpringRegistry;
import org.openehealth.ipf.commons.map.SpringBidiMappingService;
import org.openehealth.ipf.commons.map.config.CustomMappingsConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@EnableConfigurationProperties(IpfConfigurationProperties.class)
public class IpfAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Registry.class)
    Registry springRegistry() {
        return new SpringRegistry();
    }

    // Dynamically collect CustomMappings, if available

    @Bean
    @ConditionalOnMissingBean(SpringConfigurationPostProcessor.class)
    public SpringConfigurationPostProcessor postProcessor(CustomMappingsConfigurer customMappingsConfigurer) {
        SpringConfigurationPostProcessor processor = new SpringConfigurationPostProcessor();
        List<OrderedConfigurer> list = new ArrayList<>();
        if (customMappingsConfigurer != null) list.add(customMappingsConfigurer);
        processor.setSpringConfigurers(list);
        return processor;
    }

    @Bean
    @ConditionalOnMissingBean(CustomMappingsConfigurer.class)
    protected CustomMappingsConfigurer customMappingsConfigurer(SpringBidiMappingService mappingService) {
        CustomMappingsConfigurer configurer = new CustomMappingsConfigurer();
        configurer.setMappingService(mappingService);
        return configurer;
    }

    @Bean
    @ConditionalOnMissingBean(SpringBidiMappingService.class)
    public SpringBidiMappingService mappingService() {
        return new SpringBidiMappingService();
    }

}
