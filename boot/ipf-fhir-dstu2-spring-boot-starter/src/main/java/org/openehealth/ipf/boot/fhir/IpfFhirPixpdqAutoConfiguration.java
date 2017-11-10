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
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator;
import org.openehealth.ipf.commons.map.config.CustomMappings;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@ConditionalOnClass({FhirTranslator.class})
@Configuration
@AutoConfigureAfter(IpfAtnaAutoConfiguration.class)
@EnableConfigurationProperties(IpfFhirConfigurationProperties.class)
public class IpfFhirPixpdqAutoConfiguration {

    @Bean
    public CustomMappings translationFhirHl7v2Mappings() {
        CustomMappings mappings = new CustomMappings();
        mappings.setMappingResource(new ClassPathResource("META-INF/map/fhir-hl7v2-translation.map"));
        return mappings;
    }

}
