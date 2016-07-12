package org.openehealth.ipf.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
