/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Paging provider that uses a Spring cache abstraction to store {@link IBundleProvider} instances.
 * The PagingProvider is used whenever there are more results than the client has requested.
 * <p>
 * Note that {@link IBundleProvider} is not serializable, so if {@link #isDistributed()} returns true,
 * only the bundles are stored. When the result list is {@link #retrieveResultList(RequestDetails, String) retrieved}
 * a new instance of {@link SimpleBundleProvider} is created and returned instead. This does
 * not work with {@link org.openehealth.ipf.commons.ihe.fhir.LazyBundleProvider}
 * as this class relies on a completely initialized result list.
 * </p>
 * <p>
 * Eviction of paging results is done by appropriately configuring the fhirPagingCache cache
 * </p>
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class SpringCachePagingProvider implements IPagingProvider {

    private static final String PAGING_CACHE = "fhirPagingCache";
    private Cache cache;

    @Getter
    @Setter
    private int defaultPageSize = 50;

    @Getter
    @Setter
    private int maximumPageSize = 100;

    @Getter
    @Setter
    private boolean distributed;


    private FhirContext fhirContext;

    public SpringCachePagingProvider(CacheManager cacheManager, FhirContext fhirContext) {
        this.cache = cacheManager.getCache(PAGING_CACHE);
        this.fhirContext = fhirContext;
    }

    @Override
    public String storeResultList(RequestDetails requestDetails, IBundleProvider bundleProvider) {
        var key = UUID.randomUUID().toString();
        cache.put(key, distributed ?
                serialize(bundleProvider) :
                bundleProvider);
        return key;
    }

    @Override
    public IBundleProvider retrieveResultList(RequestDetails requestDetails, String id) {
        return distributed ?
                deserialize(cache.get(id, List.class)) :
                cache.get(id, IBundleProvider.class);
    }


    private List<String> serialize(IBundleProvider bundleProvider) {
        var parser = fhirContext.newJsonParser();
        return bundleProvider.getResources(0, Integer.MAX_VALUE).stream()
                .map(parser::encodeResourceToString)
                .collect(Collectors.toList());
    }

    private IBundleProvider deserialize(List<String> list) {
        if (list == null) return null;
        var parser = fhirContext.newJsonParser();
        return new SimpleBundleProvider(list.stream()
                .map(parser::parseResource)
                .collect(Collectors.toList()));
    }

}
