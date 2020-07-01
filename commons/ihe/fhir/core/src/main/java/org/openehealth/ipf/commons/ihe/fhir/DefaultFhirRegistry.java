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

import ca.uhn.fhir.rest.server.RestfulServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link FhirRegistry}
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class DefaultFhirRegistry implements FhirRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultFhirRegistry.class);

    private static final Map<String, FhirRegistry> registries = new ConcurrentHashMap<>();

    private final Set<Object> resourceProviders;
    private final Set<RestfulServer> servlets;

    private DefaultFhirRegistry() {
        resourceProviders = new HashSet<>();
        servlets = new HashSet<>();
    }

    /**
     * Lookup or create a new FHIR registry if none exists with the given (servlet) name
     */
    public static FhirRegistry getFhirRegistry(String name) {
        return registries.computeIfAbsent(name, s -> new DefaultFhirRegistry());
    }

    /**
     * Removes the FHIR registry with the given (servlet) name
     */
    public static FhirRegistry removeFhirRegistry(String name) {
        return registries.remove(name);
    }

    @Override
    public void register(Object resourceProvider) {
        if (!(resourceProvider instanceof FhirProvider) || ((FhirProvider)resourceProvider).requiresRegistration()) {
            if (resourceProviders.add(resourceProvider)) {
                for (var servlet : servlets) {
                    servlet.registerProvider(resourceProvider);
                }
            } else {
                LOG.info("Resource Provider {} was already registered. Ignored registration.", resourceProvider);
            }
        }
    }

    @Override
    public void unregister(Object resourceProvider) {
        if (!(resourceProvider instanceof FhirProvider) || ((FhirProvider)resourceProvider).requiresDeregistration()) {
            if (resourceProviders.remove(resourceProvider)) {
                for (var provider : servlets) {
                    provider.unregisterProvider(resourceProvider);
                }
            } else {
                LOG.info("Resource Provider {} was not registered. Ignored deregistration.", resourceProvider);
            }
        }
    }

    @Override
    public void register(RestfulServer servlet) {
        LOG.debug("Registering FHIR servlet with name {}. Providers registered so far: {}",
                servlet.getServletName(), resourceProviders.size());
        servlets.add(servlet);
        servlet.registerProviders(resourceProviders);
    }

    @Override
    public void unregister(RestfulServer servlet) {
        LOG.debug("Unregistering FHIR Servlet with name {} and {} connected providers",
                servlet.getServletName(), resourceProviders.size());
        servlets.remove(servlet);
        servlet.unregisterProviders(resourceProviders);
    }

    @Override
    public boolean hasServlet(String servletName) {
        return servlets.stream().anyMatch(p -> p.getServletName().equals(servletName));
    }
}
