/*
 * Copyright 2026 the original author or authors.
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
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.streaming.Base64SplittingFilter;
import org.openehealth.ipf.commons.ihe.fhir.streaming.StreamedDocumentListener;
import org.openehealth.ipf.commons.ihe.fhir.streaming.StreamingUploadOptions;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The streaming upload split is off unless asked for, and when it is asked for, the deployment stays in control
 * of the two things that are deployment specific: how a rejection is worded and how the split is metered.
 *
 * @author Christian Ohr
 */
public class IpfFhirStreamingUploadAutoConfigurationTest {

    @Test
    public void filterIsNotRegisteredUnlessEnabled() {
        try (var context = context()) {
            assertTrue(context.getBeansOfType(FilterRegistrationBean.class).isEmpty(),
                    "The filter must not be registered while the split is off");
        }
        try (var context = context("ipf.fhir.streaming-upload.enabled=false")) {
            assertTrue(context.getBeansOfType(FilterRegistrationBean.class).isEmpty());
        }
    }

    @Test
    public void filterIsRegisteredWithTheConfiguredSettings() {
        try (var context = context(
                "ipf.fhir.streaming-upload.enabled=true",
                "ipf.fhir.streaming-upload.engage-above-bytes=1024",
                "ipf.fhir.streaming-upload.memory-threshold-bytes=2048",
                "ipf.fhir.streaming-upload.max-document-size-bytes=4096",
                "ipf.fhir.streaming-upload.temp-directory=/var/tmp",
                "ipf.fhir.streaming-upload.inline-document-resources=DocumentReference,Patient",
                "ipf.fhir.streaming-upload.filter-order=42")) {

            var registration = context.getBean(FilterRegistrationBean.class);
            assertEquals(IpfFhirStreamingUploadAutoConfiguration.FILTER_NAME, registration.getFilterName());
            assertEquals(42, registration.getOrder());
            assertInstanceOf(Base64SplittingFilter.class, registration.getFilter());

            var options = streamingUploadOptions(context);
            assertEquals(1024, options.engageAboveBytes());
            assertEquals(2048, options.memoryThresholdBytes());
            assertEquals(4096L, options.maxDocumentSizeBytes());
            assertEquals(Path.of("/var/tmp"), options.tempDirectory());
            assertEquals(Set.of("DocumentReference", "Patient"), options.inlineDocumentResources());
        }
    }

    /**
     * Out of the box the split covers MHD and runs right after the Spring Security filter chain.
     */
    @Test
    public void defaultsCoverMhdAndRunAfterSpringSecurity() {
        try (var context = context("ipf.fhir.streaming-upload.enabled=true")) {
            assertEquals(IpfFhirConfigurationProperties.StreamingUpload.DEFAULT_FILTER_ORDER,
                    context.getBean(FilterRegistrationBean.class).getOrder());

            var options = streamingUploadOptions(context);
            assertEquals(Set.of("DocumentReference"), options.inlineDocumentResources());
            assertEquals(StreamingUploadOptions.DEFAULT_ENGAGE_ABOVE_BYTES, options.engageAboveBytes());
        }
    }

    /**
     * The listener and the diagnostics policy are the deployment's contribution: the split itself must not have
     * to know about a metrics library or about an application's policy on disclosing error details.
     */
    @Test
    public void listenerAndDiagnosticsPolicyAreTakenFromTheContext() {
        try (var context = context(DeploymentContribution.class, "ipf.fhir.streaming-upload.enabled=true")) {
            assertTrue(context.containsBean(
                    IpfFhirStreamingUploadAutoConfiguration.STREAMING_UPLOAD_DIAGNOSTICS_POLICY));
            assertSame(context.getBean(DeploymentContribution.class).listener,
                    context.getBean(StreamedDocumentListener.class));
            // Both are consumed by the filter, which does not expose them again, so what is asserted here is
            // that they are resolvable at all and that the filter was built from them without failing
            assertInstanceOf(Base64SplittingFilter.class,
                    context.getBean(FilterRegistrationBean.class).getFilter());
        }
    }

    private static StreamingUploadOptions streamingUploadOptions(AnnotationConfigWebApplicationContext context) {
        return context.getBean(IpfFhirConfigurationProperties.class).getStreamingUpload().toOptions();
    }

    private AnnotationConfigWebApplicationContext context(String... properties) {
        return context(null, properties);
    }

    private AnnotationConfigWebApplicationContext context(Class<?> userConfiguration, String... properties) {
        var context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        TestPropertyValues.of(properties).applyTo(context);
        context.register(FhirContextConfiguration.class, IpfFhirStreamingUploadAutoConfiguration.class);
        if (userConfiguration != null) {
            context.register(userConfiguration);
        }
        context.refresh();
        return context;
    }

    /**
     * The FHIR context the filter serializes a rejection with. Supplied here rather than by starting the whole
     * FHIR auto configuration, which is not what this test is about.
     */
    @Configuration
    static class FhirContextConfiguration {

        @Bean
        public FhirContext fhirContext() {
            return FhirContext.forR4Cached();
        }
    }

    @Configuration
    static class DeploymentContribution {

        final List<Long> streamedSizes = new ArrayList<>();
        final StreamedDocumentListener listener = streamedSizes::add;

        @Bean
        public StreamedDocumentListener streamedDocumentListener() {
            return listener;
        }

        @Bean(IpfFhirStreamingUploadAutoConfiguration.STREAMING_UPLOAD_DIAGNOSTICS_POLICY)
        public UnaryOperator<String> streamingUploadDiagnosticsPolicy() {
            return diagnostics -> "Error diagnostics removed";
        }
    }
}
