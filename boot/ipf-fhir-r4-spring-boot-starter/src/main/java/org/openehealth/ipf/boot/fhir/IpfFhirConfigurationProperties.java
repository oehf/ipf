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

import ca.uhn.fhir.context.FhirVersionEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.fhir.streaming.Base64SplittingFilter;
import org.openehealth.ipf.commons.ihe.fhir.streaming.StreamingUploadOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;
import java.util.*;

/**
 *
 */
@Validated
@ConfigurationProperties(prefix = "ipf.fhir")
public class IpfFhirConfigurationProperties {

    /**
     * Which FHIR version to use
     */
    @Getter @Setter
    private FhirVersionEnum fhirVersion = FhirVersionEnum.R4;

    @NestedConfigurationProperty
    @Getter
    private final Servlet servlet = new Servlet();

    @NestedConfigurationProperty
    @Getter
    private final CorsConfiguration cors;

    @NestedConfigurationProperty
    @Getter
    private final StreamingUpload streamingUpload = new StreamingUpload();

    /**
     * Path that serves as the base URI for the FHIR services.
     */
    @NotNull
    @Getter @Setter
    @Pattern(regexp = "/[^?#]*", message = "Path must start with /")
    private String path = "/fhir";

    /**
     * Resources containing NamingSystems used for mapping between namespaces
     */
    @Getter @Setter
    private List<Resource> namingSystems = new ArrayList<>();

    /**
     * Resources containing custom mapping files for FHIR/HL7v2 translation
     */
    @Getter @Setter
    private List<Resource> mappings = new ArrayList<>();

    /**
     * Whether to create a cached PagingProvider
     */
    @Getter @Setter
    private boolean caching;

    public IpfFhirConfigurationProperties() {
        this.cors = defaultCorsConfiguration();
    }

    String getFhirMapping() {
        var path = getPath();
        return path.endsWith("/") ? path + "*" : path + "/*";
    }

    public static class Servlet {

        /**
         * Servlet init parameters to pass to the FHIR Servlet.
         */
        @Getter @Setter
        private Map<String, String> init = new HashMap<>();

        /**
         * Load on startup priority of the FHIR servlet.
         */
        @Getter @Setter
        private int loadOnStartup = 1;

        /**
         * Name of the servlet
         */
        @Getter @Setter
        private String name = "FhirServlet";

        /**
         * Number of concurrent paging requests that can be handled
         */
        @Getter @Setter
        private int pagingRequests = 50;

        /**
         * Default number of result entries to be returned if no _count parameter is specified in a search
         */
        @Getter @Setter
        private int defaultPageSize = 50;

        /**
         * Maximum number of result entries to be returned even if the _count parameter of a search demands for more
         */
        @Getter @Setter
        private int maxPageSize = 100;

        /**
         * Whether the Paging Provider cache is expected to be distributed, so that serialization of result
         * bundles is necessary. In this case, FHIR endpoints must not use lazy-loading of results.
         */
        @Getter @Setter
        private boolean distributedPagingProvider = false;

        /**
         * Enable server-side request logging
         */
        @Getter @Setter
        private boolean logging = false;

        /**
         * Enable pretty-printing responses
         */
        @Getter @Setter
        private boolean prettyPrint = true;

        /**
         * Enable color-coding responses queried from a Web Browser
         */
        @Getter @Setter
        private boolean responseHighlighting = true;

        /**
         * Enable strict resource parsing
         */
        @Getter @Setter
        private boolean strict = false;
    }

    /**
     * Configuration of the streaming upload split, i.e. this starter's half of the bridge to
     * {@link Base64SplittingFilter}. The split itself knows nothing about Spring or about this starter;
     * everything it needs is handed to it as {@link StreamingUploadOptions}.
     *
     * @see IpfFhirStreamingUploadAutoConfiguration
     */
    public static class StreamingUpload {

        /**
         * Whether large document uploads shall be split before they reach the FHIR parser.
         */
        @Getter @Setter
        private boolean enabled;

        /**
         * Only request bodies larger than this are split; smaller ones are passed through byte for byte, so that
         * ordinary requests behave exactly as they do without this filter.
         * <p>
         * This is measured on the whole request body, i.e. metadata plus the base64 of all documents in it.
         */
        @Getter @Setter
        private int engageAboveBytes = StreamingUploadOptions.DEFAULT_ENGAGE_ABOVE_BYTES;

        /**
         * Extracted document content up to this size is kept in memory instead of being written to a temporary
         * file.
         * <p>
         * This is measured on a single decoded document and is therefore deliberately independent of
         * {@link #engageAboveBytes}; it is quite normal for it to be the larger of the two. Splitting the body is
         * what avoids the expensive part - the base64 text and the decoded array that the FHIR parser would
         * otherwise materialize - whereas a temporary file is only needed to also avoid a large contiguous heap
         * allocation. Small documents therefore stay in memory and cause no disk I/O at all.
         */
        @Getter @Setter
        private int memoryThresholdBytes = StreamingUploadOptions.DEFAULT_MEMORY_THRESHOLD_BYTES;

        /**
         * Maximum size of a single extracted document. Bounds how much temporary disk space one request can use.
         */
        @Getter @Setter
        private long maxDocumentSizeBytes = StreamingUploadOptions.DEFAULT_MAX_DOCUMENT_SIZE_BYTES;

        /**
         * Directory for temporary document files. Defaults to the JVM temporary directory.
         */
        @Getter @Setter
        private Path tempDirectory = StreamingUploadOptions.defaultTempDirectory();

        /**
         * Types of the resources whose create interaction carries a document inline. Defaults to what MHD needs,
         * i.e. the ITI-105 create of a DocumentReference; a POST to the FHIR base itself, such as the ITI-65
         * transaction bundle, is always considered regardless of this setting.
         */
        @Getter @Setter
        private Set<String> inlineDocumentResources = StreamingUploadOptions.MHD_INLINE_DOCUMENT_RESOURCES;

        /**
         * Order of the filter in the servlet filter chain. The default places it directly after the Spring
         * Security filter chain, whose own default order is -100: unauthenticated requests must never be spooled
         * to disk, and any request logging registered further down then sees the slimmed-down body rather than
         * buffering the full upload a second time. The value is spelled out rather than derived from Spring
         * Boot's {@code SecurityProperties.DEFAULT_FILTER_ORDER}, which this starter does not depend on.
         */
        @Getter @Setter
        private int filterOrder = DEFAULT_FILTER_ORDER;

        /**
         * Spring Security's default filter order plus a little, see {@link #filterOrder}.
         */
        public static final int DEFAULT_FILTER_ORDER = -100 + 10;

        /**
         * @return these settings as the split's own, framework-independent options
         */
        public StreamingUploadOptions toOptions() {
            return new StreamingUploadOptions(engageAboveBytes, memoryThresholdBytes, maxDocumentSizeBytes,
                    tempDirectory, inlineDocumentResources);
        }
    }

    public static class CorsConfiguration extends org.springframework.web.cors.CorsConfiguration {
    }

    private static CorsConfiguration defaultCorsConfiguration() {
        var cors = new CorsConfiguration();
        cors.addAllowedOrigin("*");
        cors.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        // A comma separated list of allowed headers when making a non simple CORS request.
        cors.setAllowedHeaders(Arrays.asList("Origin", "Accept", "Content-Type",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization",
                "Prefer", "If-Match", "If-None-Match", "If-Modified-Since", "If-None-Exist"));
        cors.setExposedHeaders(Arrays.asList("Location", "Content-Location", "ETag", "Last-Modified"));
        cors.setMaxAge(300L);
        return cors;
    }
}
