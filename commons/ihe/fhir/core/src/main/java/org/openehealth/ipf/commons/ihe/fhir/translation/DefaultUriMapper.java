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

package org.openehealth.ipf.commons.ihe.fhir.translation;

import org.openehealth.ipf.commons.map.MappingService;
import org.openehealth.ipf.commons.map.Mappings;

import java.util.Optional;

/**
 * Default URI Mapper implementation that requires {@link Mappings}
 * for URI-to-OID translation
 *
 * @author Christian Ohr
 * @since 3.1
 */
@SuppressWarnings("removal")
public class DefaultUriMapper extends AbstractUriMapper {

    // exactly one of these is set; the untyped one only for the deprecated constructors
    private final Mappings mappings;
    private final MappingService mappingService;

    private String uriToOidMappingKey;
    private String uriToNamespaceMappingKey;

    public DefaultUriMapper(Mappings mappings) {
        this.mappings = mappings;
        this.mappingService = null;
    }

    public DefaultUriMapper(Mappings mappings, String uriToOidMappingKey, String uriToNamespaceMappingKey) {
        this(mappings);
        this.uriToOidMappingKey = uriToOidMappingKey;
        this.uriToNamespaceMappingKey = uriToNamespaceMappingKey;
    }

    /**
     * @deprecated as of 6.0, pass {@link Mappings} instead
     */
    @Deprecated(since = "6.0", forRemoval = true)
    public DefaultUriMapper(MappingService mappingService) {
        this.mappings = null;
        this.mappingService = mappingService;
    }

    /**
     * @deprecated as of 6.0, pass {@link Mappings} instead
     */
    @Deprecated(since = "6.0", forRemoval = true)
    public DefaultUriMapper(MappingService mappingService, String uriToOidMappingKey, String uriToNamespaceMappingKey) {
        this(mappingService);
        this.uriToOidMappingKey = uriToOidMappingKey;
        this.uriToNamespaceMappingKey = uriToNamespaceMappingKey;
    }

    @Override
    protected Optional<String> mapUriToOid(String uri) {
        return map(uriToOidMappingKey, uri);
    }

    @Override
    protected Optional<String> mapOidToUri(String oid) {
        return mapReverse(uriToOidMappingKey, oid);
    }

    @Override
    protected Optional<String> mapUriToNamespace(String uri) {
        return map(uriToNamespaceMappingKey, uri);
    }

    @Override
    protected Optional<String> mapNamespaceToUri(String namespace) {
        return mapReverse(uriToNamespaceMappingKey, namespace);
    }

    private Optional<String> map(String mapping, String key) {
        return mappings != null
                ? mappings.map(mapping, key)
                : Optional.ofNullable((String) mappingService.get(mapping, key));
    }

    private Optional<String> mapReverse(String mapping, String value) {
        return mappings != null
                ? mappings.mapReverse(mapping, value)
                : Optional.ofNullable((String) mappingService.getKey(mapping, value));
    }
}
