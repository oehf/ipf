/*
 * Copyright 2015 the original author or authors.
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

/**
 * Default URI Mapper implementation that requires a {@link MappingService}
 * for URI-to-OID translation
 *
 * @since 3.1
 */
public class DefaultUriMapper extends AbstractUriMapper {

    private final MappingService mappingService;
    private final String uriToOidMappingKey;

    public DefaultUriMapper(MappingService mappingService, String uriToOidMappingKey) {
        this.mappingService = mappingService;
        this.uriToOidMappingKey = uriToOidMappingKey;
    }

    @Override
    protected String mapUriToOid(String uri) {
        return mappingService.get(uriToOidMappingKey, uri).toString();
    }

    @Override
    protected String mapOidToUri(String oid) {
        return mappingService.getKey(uriToOidMappingKey, oid).toString();
    }
}
