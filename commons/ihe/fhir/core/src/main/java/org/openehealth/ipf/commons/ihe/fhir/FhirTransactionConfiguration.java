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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.PerformanceOptionsEnum;

/**
 * Static configuration for FHIR transaction components
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class FhirTransactionConfiguration {

    private final FhirVersionEnum fhirVersion;
    private final AbstractPlainProvider staticResourceProvider;
    private final ClientRequestFactory<?> staticClientRequestFactory;
    private final FhirValidator fhirValidator;
    private boolean supportsLazyLoading;
    private boolean deferModelScanning;

    public FhirTransactionConfiguration(
            AbstractPlainProvider resourceProvider,
            ClientRequestFactory<?> clientRequestFactory) {
        this(resourceProvider, clientRequestFactory, FhirValidator.NO_VALIDATION);
    }

    public FhirTransactionConfiguration(
            AbstractPlainProvider resourceProvider,
            ClientRequestFactory<?> clientRequestFactory,
            FhirValidator fhirValidator) {
        this(FhirVersionEnum.DSTU2_HL7ORG, resourceProvider, clientRequestFactory, fhirValidator);
    }

    public FhirTransactionConfiguration(
            FhirVersionEnum fhirVersion,
            AbstractPlainProvider resourceProvider,
            ClientRequestFactory<?> clientRequestFactory,
            FhirValidator fhirValidator) {
        this.fhirVersion = fhirVersion;
        this.staticResourceProvider = resourceProvider;
        this.staticClientRequestFactory = clientRequestFactory;
        this.fhirValidator = fhirValidator;
    }

    public AbstractPlainProvider getStaticResourceProvider() {
        return staticResourceProvider;
    }

    public ClientRequestFactory<?> getStaticClientRequestFactory() {
        return staticClientRequestFactory;
    }

    public FhirContext createFhirContext() {
        FhirContext context = new FhirContext(fhirVersion);
        context.setRestfulClientFactory(new SslAwareApacheRestfulClientFactory(context));
        if (deferModelScanning) {
            context.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
        }
        return context;
    }

    public FhirValidator getFhirValidator() {
        return fhirValidator;
    }

    /**
     * Determines if the component and backend implementation does support lazy-loading of search result sets.
     * Even if true, the endpoint URI, however, must be explicitly configured to use lazy-loading.
     *
     * @param supportsLazyLoading true if this component support lazy-loading
     */
    public void setSupportsLazyLoading(boolean supportsLazyLoading) {
        this.supportsLazyLoading = supportsLazyLoading;
    }

    public boolean supportsLazyLoading() {
        return supportsLazyLoading;
    }

    public boolean isDeferModelScanning() {
        return deferModelScanning;
    }

    /**
     * By default, HAPI will scan each model type it encounters as soon as it encounters it. This scan includes a check
     * for all fields within the type, and makes use of reflection to do this. While this process is not particularly significant
     * on reasonably performant machines, on some devices it may be desirable to defer this scan. When the scan is deferred,
     * objects will only be scanned when they are actually accessed, meaning that only types that are actually used in an
     * application get scanned.
     */
    public void setDeferModelScanning(boolean deferModelScanning) {
        this.deferModelScanning = deferModelScanning;
    }
}
