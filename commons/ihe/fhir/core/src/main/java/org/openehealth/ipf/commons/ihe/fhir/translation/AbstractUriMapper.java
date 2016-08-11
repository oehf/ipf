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

import org.openehealth.ipf.commons.core.URN;

import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

/**
 * URI mapper base implementation that recognizes and creates OID URNs
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class AbstractUriMapper implements UriMapper {

    @Override
    public Optional<String> uriToOid(String uri) {
        return UriMapper.findFirst(
                () -> translateURN(uri, URN.OID),
                () -> mapUriToOid(uri));
    }

    @Override
    public Optional<String> uriToNamespace(String uri) {
        return UriMapper.findFirst(
                () -> translateURN(uri, URN.PIN),
                () -> mapUriToNamespace(uri));
    }

    @Override
    public String oidToUri(String oid) {
        return mapOidToUri(oid)
                .filter(uri -> !oid.equals(uri))
                .orElse(urn(URN.OID, oid).toString());
    }

    @Override
    public String namespaceToUri(String namespace) {
        return mapNamespaceToUri(namespace)
                .filter(uri -> !namespace.equals(uri))
                .orElse(urn(URN.PIN, namespace.replaceAll("\\s+", "_")).toString());
    }

    /**
     * Translate a non-URN URI (e.g. a URL) into an OID
     *
     * @param uri URI
     * @return OID string
     */
    protected abstract Optional<String> mapUriToOid(String uri);

    /**
     * Translate a non-URN URI (e.g. a URL) into an OID
     *
     * @param uri URI
     * @return OID string
     */
    protected abstract Optional<String> mapUriToNamespace(String uri);

    /**
     * Translate a OID into an URI. Can either return null or throw an
     * exception if no URL was found.
     *
     * @param oid OID
     * @return valid URI or null if no URI was found
     * @throws Exception if no URI was found
     */
    protected abstract Optional<String> mapOidToUri(String oid);

    /**
     * Translate a namespace into an URI. Can either return null or throw an
     * exception if no URL was found.
     *
     * @param namespace namespace
     * @return valid URI or null if no URI was found
     * @throws Exception if no URI was found
     */
    protected abstract Optional<String> mapNamespaceToUri(String namespace);

    /**
     * Returns the NSS of an URI if it is an URN with the given NID, e.g. urn:oid:1.2.3.4
     * will return 1.2.3.4 if nid = 'oid'
     *
     * @param uri URI
     * @param nid namespace ID
     * @return namespace-specific string
     */
    private Optional<String> translateURN(String uri, String nid) {
        if (URN.isURN(uri)) {
            URN urn = urn(uri);
            if (Objects.equals(urn.getNamespaceId(), nid)) {
                return Optional.of(urn.getNamespaceSpecificString());
            }
        }
        return Optional.empty();
    }

    private URN urn(String uri) {
        try {
            return URN.create(uri);
        } catch (URISyntaxException e) {
            throw new InvalidUriSyntaxException(uri, e);
        }
    }

    private URN urn(String nid, String nss) {
        try {
            return new URN(nid, nss);
        } catch (URISyntaxException e) {
            throw new InvalidUriSyntaxException("uri:urn:" + nss, e);
        }
    }

}
