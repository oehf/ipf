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

import java.net.URISyntaxException;

/**
 * Default URI Mapper implementation that requires a {@link MappingService}
 * for URI-to-OID translation
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class DefaultUriMapper extends AbstractUriMapper {

    private final MappingService mappingService;
    private String uriToOidMappingKey;
    private String uriToNamespaceMappingKey;

    public DefaultUriMapper(MappingService mappingService) {
        this.mappingService = mappingService;
    }

    public DefaultUriMapper(MappingService mappingService, String uriToOidMappingKey, String uriToNamespaceMappingKey) {
        this.mappingService = mappingService;
        this.uriToOidMappingKey = uriToOidMappingKey;
        this.uriToNamespaceMappingKey = uriToNamespaceMappingKey;
    }

    /**
     * Forwards to {@link #uriToOid(String, String)}using {@link #uriToOidMappingKey} as mapping identifiers
     *
     * @param uri URI
     * @return OID
     * @throws URISyntaxException
     */
    public String uriToOid(String uri) throws URISyntaxException {
        return uriToOid(uriToOidMappingKey, uri);
    }

    /**
     * Forwards to {@link #namespaceToUri(String, String)}using {@link #uriToNamespaceMappingKey} as mapping identifiers
     *
     * @param namespace namespace
     * @return URI
     * @throws URISyntaxException
     */
    public String namespaceToUri(String namespace) throws URISyntaxException {
        return namespaceToUri(uriToNamespaceMappingKey, namespace);
    }

    /**
     * Forwards to {@link #oidToUri(String, String)}using {@link #uriToOidMappingKey} as mapping identifiers
     *
     * @param oid OID
     * @return URI
     * @throws URISyntaxException
     */
    public String oidToUri(String oid) throws URISyntaxException {
        return super.oidToUri(uriToOidMappingKey, oid);
    }

    /**
     * Forwards to {@link #uriToNamespace(String, String)}using {@link #uriToNamespaceMappingKey} as mapping identifiers
     *
     * @param uri URI
     * @return namespace
     * @throws URISyntaxException
     */
    public String uriToNamespace(String uri) throws URISyntaxException {
        return super.uriToNamespace(uriToNamespaceMappingKey, uri);
    }

    @Override
    protected String mapUriToOid(String id, String uri) {
        return (String) mappingService.get(id, uri);
    }

    @Override
    protected String mapOidToUri(String id, String oid) {
        return (String) mappingService.getKey(id, oid);
    }

    @Override
    protected String mapUriToNamespace(String id, String uri) {
        return (String) mappingService.get(id, uri);
    }

    @Override
    protected String mapNamespaceToUri(String id, String namespace) {
        return (String) mappingService.getKey(id, namespace);
    }
}
