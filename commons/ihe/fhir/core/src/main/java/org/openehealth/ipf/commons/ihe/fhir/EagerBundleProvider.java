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

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Map;

/**
 * Simple Bundle provider that fetches ALL results and caches them as soon as one
 * of {@link #size()} or {@link #getResources(int, int)} is called.
 * <p>
 * Note: instances of this class is neither thread-safe nor can they be reused across requests
 * </p>
 */
public class EagerBundleProvider extends AbstractBundleProvider {

    private transient List<IBaseResource> resources;

    public EagerBundleProvider(RequestConsumer consumer, Object payload, Map<String, Object> headers) {
        super(consumer, payload, headers);
    }

    @Override
    public List<IBaseResource> getResources(int fromIndex, int toIndex) {
        var resources = fetchResources();
        return resources.subList(fromIndex, Math.min(toIndex, resources.size()));
    }

    @Override
    public Integer size() {
        return fetchResources().size();
    }

    /**
     * @return all matching resources, regardless of from/toIndex
     */
    private List<IBaseResource> fetchResources() {
        if (resources == null) {
            resources = obtainResources(getPayload(), getHeaders());
        }
        return resources;
    }
}
