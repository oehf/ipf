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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.core.TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Static configuration for FHIR transaction components
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class FhirTransactionConfiguration extends TransactionConfiguration {

    @Getter
    private final FhirVersionEnum fhirVersion;
    private final FhirContextProvider fhirContextProvider;
    private final Supplier<? extends List<? extends FhirProvider>> resourceProviderFactory;
    @Getter
    private final ClientRequestFactory staticClientRequestFactory;
    private final Supplier<FhirTransactionValidator> fhirValidatorSupplier;

    private transient FhirTransactionValidator fhirValidator;

    @Getter @Setter
    private boolean supportsLazyLoading;
    @Getter @Setter
    private Predicate<RequestDetails> staticConsumerSelector = o -> true;
    @Getter @Setter
    private Set<String> requestValidationProfiles = Set.of();
    @Getter @Setter
    private Set<String> responseValidationProfiles = Set.of();

    /**
     * @deprecated use the constructor taking a resource provider factory. Resource provider instances
     * passed here are shared by all FHIR servlets in the JVM.
     */
    @Deprecated(since = "6.1")
    public FhirTransactionConfiguration(
            String name,
            String description,
            boolean isQuery,
            AuditStrategy<? extends FhirAuditDataset> clientAuditStrategy,
            AuditStrategy<? extends FhirAuditDataset> serverAuditStrategy,
            FhirContext defaultFhirContext,
            FhirProvider resourceProvider,
            ClientRequestFactory clientRequestFactory,
            Function<FhirContext, FhirTransactionValidator> fhirValidator) {
        this(name, description, isQuery, clientAuditStrategy, serverAuditStrategy, defaultFhirContext,
                Collections.singletonList(resourceProvider), clientRequestFactory, fhirValidator);
    }

    /**
     * @deprecated use the constructor taking a resource provider factory. Resource provider instances
     * passed here are shared by all FHIR servlets in the JVM.
     */
    @Deprecated(since = "6.1")
    public FhirTransactionConfiguration(
            String name,
            String description,
            boolean isQuery,
            AuditStrategy<? extends FhirAuditDataset> clientAuditStrategy,
            AuditStrategy<? extends FhirAuditDataset> serverAuditStrategy,
            FhirContext fhirContext,
            List<? extends FhirProvider> resourceProviders,
            ClientRequestFactory clientRequestFactory,
            Function<FhirContext, FhirTransactionValidator> fhirValidator) {
        super(name, description, isQuery, clientAuditStrategy, serverAuditStrategy);
        this.fhirVersion = fhirContext.getVersion().getVersion();
        this.fhirContextProvider = (fhirVersion) -> fhirContext;
        this.resourceProviderFactory = () -> resourceProviders;
        this.staticClientRequestFactory = clientRequestFactory;
        this.fhirValidatorSupplier = fhirValidator != null ? () -> fhirValidator.apply(initializeFhirContext()) : null;
    }

    /**
     * @deprecated use the constructor taking a resource provider factory. Resource provider instances
     * passed here are shared by all FHIR servlets in the JVM.
     */
    @Deprecated(since = "6.1")
    public FhirTransactionConfiguration(
            String name,
            String description,
            boolean isQuery,
            AuditStrategy<? extends FhirAuditDataset> clientAuditStrategy,
            AuditStrategy<? extends FhirAuditDataset> serverAuditStrategy,
            FhirVersionEnum fhirVersion,
            FhirProvider resourceProvider,
            ClientRequestFactory clientRequestFactory,
            Function<FhirContext, FhirTransactionValidator> fhirValidator) {
        this(name, description, isQuery, clientAuditStrategy, serverAuditStrategy, fhirVersion,
                Collections.singletonList(resourceProvider), clientRequestFactory, fhirValidator);
    }

    /**
     * @deprecated use the constructor taking a resource provider factory. Resource provider instances
     * passed here are shared by all FHIR servlets in the JVM.
     */
    @Deprecated(since = "6.1")
    public FhirTransactionConfiguration(
            String name,
            String description,
            boolean isQuery,
            AuditStrategy<? extends FhirAuditDataset> clientAuditStrategy,
            AuditStrategy<? extends FhirAuditDataset> serverAuditStrategy,
            FhirVersionEnum fhirVersion,
            List<? extends FhirProvider> resourceProviders,
            ClientRequestFactory clientRequestFactory,
            Function<FhirContext, FhirTransactionValidator> fhirValidator) {
        super(name, description, isQuery, clientAuditStrategy, serverAuditStrategy);
        this.fhirVersion = fhirVersion;
        this.fhirContextProvider = fhirContextProvider();
        this.resourceProviderFactory = () -> resourceProviders;
        this.staticClientRequestFactory = clientRequestFactory;
        this.fhirValidatorSupplier = fhirValidator != null ? () -> fhirValidator.apply(initializeFhirContext()) : null;
    }

    /**
     * @param resourceProviderFactory creates new instances of the resource providers that are used on the
     *                                consumer side. Each FHIR servlet (more precisely, each {@link FhirRegistry})
     *                                uses one instance per resource provider class, so resource providers are not
     *                                shared between servlets. The factory may be called more often than that.
     * @since 6.1
     */
    public FhirTransactionConfiguration(
            String name,
            String description,
            boolean isQuery,
            AuditStrategy<? extends FhirAuditDataset> clientAuditStrategy,
            AuditStrategy<? extends FhirAuditDataset> serverAuditStrategy,
            FhirContext fhirContext,
            Supplier<? extends List<? extends FhirProvider>> resourceProviderFactory,
            ClientRequestFactory clientRequestFactory,
            Function<FhirContext, FhirTransactionValidator> fhirValidator) {
        super(name, description, isQuery, clientAuditStrategy, serverAuditStrategy);
        this.fhirVersion = fhirContext.getVersion().getVersion();
        this.fhirContextProvider = (fhirVersion) -> fhirContext;
        this.resourceProviderFactory = resourceProviderFactory;
        this.staticClientRequestFactory = clientRequestFactory;
        this.fhirValidatorSupplier = fhirValidator != null ? () -> fhirValidator.apply(initializeFhirContext()) : null;
    }

    /**
     * @param resourceProviderFactory creates new instances of the resource providers that are used on the
     *                                consumer side. Each FHIR servlet (more precisely, each {@link FhirRegistry})
     *                                uses one instance per resource provider class, so resource providers are not
     *                                shared between servlets. The factory may be called more often than that.
     * @since 6.1
     */
    public FhirTransactionConfiguration(
            String name,
            String description,
            boolean isQuery,
            AuditStrategy<? extends FhirAuditDataset> clientAuditStrategy,
            AuditStrategy<? extends FhirAuditDataset> serverAuditStrategy,
            FhirVersionEnum fhirVersion,
            Supplier<? extends List<? extends FhirProvider>> resourceProviderFactory,
            ClientRequestFactory clientRequestFactory,
            Function<FhirContext, FhirTransactionValidator> fhirValidator) {
        super(name, description, isQuery, clientAuditStrategy, serverAuditStrategy);
        this.fhirVersion = fhirVersion;
        this.fhirContextProvider = fhirContextProvider();
        this.resourceProviderFactory = resourceProviderFactory;
        this.staticClientRequestFactory = clientRequestFactory;
        this.fhirValidatorSupplier = fhirValidator != null ? () -> fhirValidator.apply(initializeFhirContext()) : null;
    }

    /**
     * @return new instances of the resource providers for the consumer side of this transaction
     * (unless the deprecated constructors taking provider instances were used).
     * @since 6.1
     */
    public List<? extends FhirProvider> createResourceProviders() {
        var providers = resourceProviderFactory != null ? resourceProviderFactory.get() : null;
        return providers != null ? providers : List.of();
    }

    /**
     * @return same as {@link #createResourceProviders()}, i.e. usually new instances, not the instances that
     * consumers are connected to
     * @deprecated use {@link #createResourceProviders()}
     */
    @Deprecated(since = "6.1")
    public List<? extends FhirProvider> getStaticResourceProvider() {
        return createResourceProviders();
    }

    /**
     * Initializes the FHIR context. Note that this method
     * is only called when the endpoint does not configure its custom (pre-initialized) FhirContext
     *
     * @return the initialized FhirContext
     */
    public FhirContext initializeFhirContext() {
        return fhirContextProvider.apply(fhirVersion);
    }

    /**
     * Returns a default provider of a FhirContext with the configured FHIR version. Do NOT
     * use the "cached" static initializers (e.g. @link{{@link FhirContext#forCached(FhirVersionEnum)}}
     * as we may want to customize the FhirContext afterwards.
     *
     * @return the initialized FhirContext
     */
    private FhirContextProvider fhirContextProvider() {
        return (fhirVersion) -> {
            var fhirContext = new FhirContext(fhirVersion);
            try {
                Class.forName("org.apache.hc.client5.http.classic.HttpClient");
                fhirContext.setRestfulClientFactory(new SslAwareApacheRestfulClient5Factory(fhirContext));
            } catch (ClassNotFoundException e) {
                fhirContext.setRestfulClientFactory(new SslAwareApacheRestfulClientFactory(fhirContext));
            }

            return fhirContext;
        };
    }

    public synchronized FhirTransactionValidator getFhirValidator() {
        if (fhirValidator == null) {
            if (fhirValidatorSupplier != null) {
                fhirValidator = fhirValidatorSupplier.get();
            }
        }
        return fhirValidator;
    }

}
