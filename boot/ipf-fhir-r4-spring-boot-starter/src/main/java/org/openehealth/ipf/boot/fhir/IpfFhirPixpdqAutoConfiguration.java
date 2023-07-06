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

package org.openehealth.ipf.boot.fhir;

import org.openehealth.ipf.boot.atna.IpfAtnaAutoConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PDQM;
import org.openehealth.ipf.commons.spring.map.config.CustomMappings;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@ConditionalOnClass({PDQM.class})
@Configuration
@AutoConfigureAfter(IpfAtnaAutoConfiguration.class)
@EnableConfigurationProperties(IpfFhirConfigurationProperties.class)
public class IpfFhirPixpdqAutoConfiguration {

    private final IpfFhirConfigurationProperties config;

    public IpfFhirPixpdqAutoConfiguration(IpfFhirConfigurationProperties config) {
        this.config = config;
    }

    @Bean
    public CustomMappings translationFhirHl7v2Mappings(FhirMappingCustomizer fhirMappingCustomizer) {
        var mappings = new CustomMappings();
        mappings.addMappingResource(new ClassPathResource("META-INF/map/fhir-hl7v2-translation.map"));
        config.getMappings().forEach(mappings::addMappingResource);
        fhirMappingCustomizer.customizeFhirMapping(mappings);
        return mappings;
    }

    @Bean
    @ConditionalOnMissingBean(FhirMappingCustomizer.class)
    public FhirMappingCustomizer fhirMappingCustomizer() {
        return FhirMappingCustomizer.NOOP;
    }

}
