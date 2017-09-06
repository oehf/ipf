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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


@Configuration
@EnableConfigurationProperties(IpfFhirConfigurationProperties.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "ipf.fhir.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CorsAutoConfiguration {

    @Autowired
    private IpfFhirConfigurationProperties config;

    @Bean
    @ConditionalOnMissingBean(CorsConfiguration.class)
    @ConfigurationProperties(prefix = "ipf.fhir.cors")
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();

        // A comma separated list of allowed origins. Note: An '*' cannot be used for an allowed origin when using credentials.
        config.addAllowedOrigin("*");
        config.setAllowedMethods(Arrays.asList(new String[] {
                "GET","POST","PUT","DELETE","OPTIONS"
        }));
        // A comma separated list of allowed headers when making a non simple CORS request.
        config.setAllowedHeaders(Arrays.asList(new String[]{
                "X-FHIR-Starter", "Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"
        }));
        config.setExposedHeaders(Arrays.asList(new String[]{
                "Location", "Content-Location"
        }));
        config.setMaxAge(300L);
        return config;
    }

    @Bean
    @ConditionalOnMissingBean(name = "corsFilterRegistration")
    public FilterRegistrationBean corsFilterRegistration(CorsConfiguration corsConfiguration) {
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.addUrlPatterns(config.getFhirMapping());
        frb.setFilter(new CorsFilter(request -> corsConfiguration));
        return frb;
    }

}
