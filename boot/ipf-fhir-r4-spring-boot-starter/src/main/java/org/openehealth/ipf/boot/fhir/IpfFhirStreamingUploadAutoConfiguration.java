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
import org.openehealth.ipf.commons.ihe.fhir.streaming.Base64SplittingFilter;
import org.openehealth.ipf.commons.ihe.fhir.streaming.StreamedDocumentListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.UnaryOperator;

/**
 * Registers the {@link Base64SplittingFilter}, which streams the document content of a large FHIR upload past
 * the FHIR parser instead of letting it materialize on the heap.
 * <p>
 * This is the runtime half of the bridge to the split: the split itself knows nothing about Spring, about
 * configuration properties or about metrics, so everything it needs is assembled here. Two pieces of that are
 * deployment specific and can be contributed as beans:
 * <ul>
 *     <li>a {@link StreamedDocumentListener}, to meter the split - the filter answers to it without having to
 *     know about any particular metrics library</li>
 *     <li>a {@code UnaryOperator<String>} named {@link #STREAMING_UPLOAD_DIAGNOSTICS_POLICY}, applied to the
 *     diagnostics of a rejection before it is sent. The filter answers a rejected upload itself and therefore
 *     bypasses whatever the FHIR servlet's interceptors would have applied, so an application that does not
 *     disclose error details to its clients has to redact them here as well.</li>
 * </ul>
 * The filter is off unless {@code ipf.fhir.streaming-upload.enabled} is set; when off, it is not registered at
 * all.
 *
 * @author Christian Ohr
 * @see IpfFhirConfigurationProperties.StreamingUpload
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "ipf.fhir.streaming-upload", name = "enabled")
@AutoConfigureAfter(IpfFhirAutoConfiguration.class)
@EnableConfigurationProperties(IpfFhirConfigurationProperties.class)
public class IpfFhirStreamingUploadAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(IpfFhirStreamingUploadAutoConfiguration.class);

    /**
     * Bean name of the {@code UnaryOperator<String>} that the filter applies to the diagnostics of a rejection.
     */
    public static final String STREAMING_UPLOAD_DIAGNOSTICS_POLICY = "streamingUploadDiagnosticsPolicy";

    /**
     * Name under which the filter is registered.
     */
    public static final String FILTER_NAME = "base64SplittingFilter";

    @Bean
    @ConditionalOnMissingBean(name = FILTER_NAME + "Registration")
    public FilterRegistrationBean<Base64SplittingFilter> base64SplittingFilterRegistration(
            IpfFhirConfigurationProperties config,
            FhirContext fhirContext,
            ObjectProvider<StreamedDocumentListener> listener,
            @Qualifier(STREAMING_UPLOAD_DIAGNOSTICS_POLICY) ObjectProvider<UnaryOperator<String>> diagnosticsPolicy) {

        var properties = config.getStreamingUpload();
        var filter = new Base64SplittingFilter(
                properties.toOptions(),
                fhirContext,
                config.getPath(),
                diagnosticsPolicy.getIfAvailable(UnaryOperator::identity),
                listener.getIfAvailable(() -> StreamedDocumentListener.NOOP));

        var registration = new FilterRegistrationBean<>(filter);
        registration.setName(FILTER_NAME);
        registration.setOrder(properties.getFilterOrder());
        registration.addUrlPatterns("/*");

        log.info("Document content of uploads larger than {} bytes will be streamed past the FHIR parser " +
                        "(in-memory up to {} bytes, temporary files in {}, at most {} bytes per document)",
                properties.getEngageAboveBytes(),
                properties.getMemoryThresholdBytes(),
                properties.getTempDirectory(),
                properties.getMaxDocumentSizeBytes());
        return registration;
    }
}
