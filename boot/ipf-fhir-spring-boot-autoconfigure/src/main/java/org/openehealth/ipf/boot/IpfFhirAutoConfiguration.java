package org.openehealth.ipf.boot;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import org.hl7.fhir.instance.conf.ServerConformanceProvider;
import org.hl7.fhir.instance.model.Conformance;
import org.openehealth.ipf.commons.ihe.core.atna.custom.FhirAuditor;
import org.openehealth.ipf.commons.ihe.fhir.DefaultNamingSystemServiceImpl;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.NamingSystemService;
import org.openehealth.ipf.commons.ihe.fhir.translation.NamingSystemUriMapper;
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorFhirToHL7v2;
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper;
import org.openehealth.ipf.commons.map.config.CustomMappings;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Configuration
@EnableConfigurationProperties(IpfFhirConfigurationProperties.class)
public class IpfFhirAutoConfiguration {

    @Autowired
    private IpfFhirConfigurationProperties config;

    @Bean
    @ConditionalOnMissingBean(FhirAuditor.class)
    public FhirAuditor fhirAuditor(AuditorModuleConfig auditorModuleConfig) {
        FhirAuditor auditor = FhirAuditor.getAuditor();
        auditor.setConfig(auditorModuleConfig);
        return auditor;
    }

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forDstu2Hl7Org();
    }

    @Bean
    @ConditionalOnMissingBean(NamingSystemService.class)
    public NamingSystemService namingSystemService(FhirContext fhirContext) throws IOException {
        DefaultNamingSystemServiceImpl namingSystemService = new DefaultNamingSystemServiceImpl(fhirContext);
        namingSystemService.addNamingSystemsFromXml(new InputStreamReader(config.getIdentifierNamingSystems().getInputStream(), StandardCharsets.UTF_8));
        return namingSystemService;
    }

    @Bean
    @ConditionalOnMissingBean(UriMapper.class)
    public UriMapper uriMapper(NamingSystemService namingSystemService) {
        return new NamingSystemUriMapper(namingSystemService);
    }

    @Bean
    @ConditionalOnClass(TranslatorFhirToHL7v2.class)
    public CustomMappings translationFhirHl7v2Mappings() {
        CustomMappings mappings = new CustomMappings();
        mappings.setMappingResource(new ClassPathResource("META-INF/map/fhir-hl7v2-translation.map"));
        return mappings;
    }

    @Bean
    @ConditionalOnMissingBean(name = "fhirServletRegistration")
    @ConditionalOnWebApplication
    public ServletRegistrationBean fhirServletRegistration(IpfFhirServlet camelFhirServlet) {
        String urlMapping = config.getFhirMapping();
        ServletRegistrationBean registration = new ServletRegistrationBean(camelFhirServlet, urlMapping);
        IpfFhirConfigurationProperties.Servlet servletProperties = config.getServlet();
        registration.setLoadOnStartup(servletProperties.getLoadOnStartup());
        registration.setName(servletProperties.getName());
        for (Map.Entry<String, String> entry : servletProperties.getInit().entrySet()) {
            registration.addInitParameter(entry.getKey(), entry.getValue());
        }
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean(IServerConformanceProvider.class)
    public IServerConformanceProvider serverConformanceProvider() {
        return new ServerConformanceProvider();
    }

    @Bean
    @ConditionalOnMissingBean(IpfFhirServlet.class)
    @ConditionalOnWebApplication
    public IpfFhirServlet fhirServlet(IServerConformanceProvider serverConformanceProvider) {
        IpfFhirServlet fhirServlet = new IpfFhirServlet();
        IpfFhirConfigurationProperties.Servlet servletProperties = config.getServlet();
        fhirServlet.setDefaultPageSize(servletProperties.getDefaultPageSize());
        fhirServlet.setMaximumPageSize(servletProperties.getMaxPageSize());
        fhirServlet.setPagingProviderSize(servletProperties.getPagingProviderSize());
        fhirServlet.setLogging(servletProperties.isLogging());
        fhirServlet.setPrettyPrint(servletProperties.isPrettyPrint());
        fhirServlet.setResponseHighlighting(servletProperties.isResponseHighlighting());
        fhirServlet.setServerConformanceProvider(serverConformanceProvider);
        return fhirServlet;
    }

}
