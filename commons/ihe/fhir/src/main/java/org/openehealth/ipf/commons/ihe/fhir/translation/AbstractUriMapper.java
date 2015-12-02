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

import org.openehealth.ipf.commons.core.URN;

import java.net.URISyntaxException;
import java.util.Objects;

/**
 * URI mapper base implementation that recognizes and creates OID URNs
 *
 * @since 3.1
 */
public abstract class AbstractUriMapper implements UriMapper {

    @Override
    public String uriToOid(String uri) throws URISyntaxException {
        if (URN.isURN(uri)) {
            URN urn = URN.create(uri);
            if (Objects.equals(urn.getNamespaceId(), URN.OID)) {
                return urn.getNamespaceSpecificString();
            } else {
                throw new FhirTranslationException("Unknown URN scheme ${urn.namespaceId}");
            }
        }
        return mapUriToOid(uri);
    }

    @Override
    public String uriToNamespace(String uri) throws URISyntaxException {
        return mapUriToNamespace(uri);
    }

    @Override
    public String oidToUri(String oid) throws URISyntaxException {
        try {
            String uri = mapOidToUri(oid);
            if (uri != null && !oid.equals(uri)) return uri;
        } catch (Exception e) {
            // handled below
        }
        return new URN(URN.OID, oid).toString();
    }

    /**
     * Translate a non-URN URI (e.g. a URL) into an OID
     *
     * @param uri URI
     * @return OID string
     */
    protected abstract String mapUriToOid(String uri);

    /**
     * Translate a non-URN URI (e.g. a URL) into an OID
     *
     * @param uri URI
     * @return OID string
     */
    protected abstract String mapUriToNamespace(String uri);

    /**
     * Translate a OID into an URI. Can either return null or throw and
     * exception if no URL was found.
     *
     * @param oid OID
     * @return valid URI or null if no URI was found
     * @throws Exception if no URI was found
     */
    protected abstract String mapOidToUri(String oid);
}
