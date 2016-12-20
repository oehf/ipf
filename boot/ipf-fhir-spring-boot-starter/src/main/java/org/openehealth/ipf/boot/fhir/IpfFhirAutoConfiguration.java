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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.ApacheProxyAddressStrategy;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import org.hl7.fhir.instance.conf.ServerConformanceProvider;
import org.openehealth.ipf.boot.atna.IpfAtnaAutoConfiguration;
import org.openehealth.ipf.commons.ihe.core.atna.custom.FhirAuditor;
import org.openehealth.ipf.commons.ihe.fhir.DefaultNamingSystemServiceImpl;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.NamingSystemService;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Configuration
@AutoConfigureAfter(IpfAtnaAutoConfiguration.class)
@EnableConfigurationProperties(IpfFhirConfigurationProperties.class)
public class IpfFhirAutoConfiguration {

    @Autowired
    private IpfFhirConfigurationProperties config;

    @Bean
    @ConditionalOnMissingBean(FhirAuditor.class)
    @ConditionalOnProperty("ipf.atna.auditor.enabled")
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
        for (Resource resource : config.getNamingSystems()) {
            namingSystemService.addNamingSystemsFromXml(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        }
        return namingSystemService;
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
    @ConditionalOnMissingBean(IServerAddressStrategy.class)
    public IServerAddressStrategy serverAddressStrategy() {
        return ApacheProxyAddressStrategy.forHttp();
    }

    @Bean
    @ConditionalOnMissingBean(INarrativeGenerator.class)
    public INarrativeGenerator narrativeGenerator() {
        return null;
    }

    @Bean
    @ConditionalOnMissingBean(IPagingProvider.class)
    @ConditionalOnProperty("ipf.fhir.caching")
    public IPagingProvider pagingProvider(CacheManager cacheManager, FhirContext fhirContext) {
        IpfFhirConfigurationProperties.Servlet servletProperties = config.getServlet();
        CachingPagingProvider pagingProvider = new CachingPagingProvider(cacheManager, fhirContext);
        pagingProvider.setDefaultPageSize(servletProperties.getDefaultPageSize());
        pagingProvider.setMaximumPageSize(servletProperties.getMaxPageSize());
        pagingProvider.setDistributed(servletProperties.isDistributedPagingProvider());
        return pagingProvider;
    }

    @Bean
    @ConditionalOnMissingBean(IpfFhirServlet.class)
    @ConditionalOnWebApplication
    public IpfFhirServlet fhirServlet(
            IServerConformanceProvider serverConformanceProvider,
            IPagingProvider pagingProvider,
            IServerAddressStrategy serverAddressStrategy,
            INarrativeGenerator narrativeGenerator) {
        IpfFhirServlet fhirServlet = new IpfFhirServlet();
        IpfFhirConfigurationProperties.Servlet servletProperties = config.getServlet();
        fhirServlet.setPagingProvider(pagingProvider);
        fhirServlet.setLogging(servletProperties.isLogging());
        fhirServlet.setPrettyPrint(servletProperties.isPrettyPrint());
        fhirServlet.setResponseHighlighting(servletProperties.isResponseHighlighting());
        fhirServlet.setServerConformanceProvider(serverConformanceProvider);
        fhirServlet.setServerAddressStrategy(serverAddressStrategy);
        fhirServlet.setNarrativeGenerator(narrativeGenerator);
        return fhirServlet;
    }

}
