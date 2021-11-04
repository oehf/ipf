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

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <p>
 * The FHIR Identifier type introduces a new mechanism for conveying the originating system of a particular identifier.
 * Whereas HL7-based messages identify an assigning organization as a HD or an OID in the 'root'
 * attribute respectively, HL7 FHIR permits the use of a URI. This requires some configuration on the part of actors
 * to correctly map a URI to an OID or HD to maintain consistency with other actors which are not implementing the
 * FHIR specification.
 * </p>
 * <p>
 * The same is basically true for code system identifications. HL7 FHIR permits the use of a URI, whereas
 * HL7-based messages and requests use HD or OIDs.
 * </p>
 *
 * @author Christian Ohr
 * @since 3.1
 */
public interface UriMapper {

    /**
     * Translates an URI into an OID. If the URI is an (OID) URN, the namespace-specific part should,
     * be used as the OID.
     *
     * @param uri the URI
     * @return the mapped OID
     * @throws InvalidUriSyntaxException if the uri string is no valid URI
     */
    Optional<String> uriToOid(String uri);

    /**
     * Translates an URI into a Namespace.
     *
     * @param uri the URI
     * @return the mapped namespace
     * @throws InvalidUriSyntaxException if the uri string is no valid URI
     */
    Optional<String> uriToNamespace(String uri);

    /**
     * Translates an OID into an URI. Instead of a real mapping, an URN can be derived from the OID
     * (i.e. urn:oid:1.2.3.4), but in general the inverse mapping to {@link #uriToOid(String)} should
     * be applied.
     *
     * @param oid the OID
     * @return the mapped URI
     */
    String oidToUri(String oid);

    /**
     * Translates an Namespace into an URI. Instead of a real mapping, an URN can be derived from the namespace
     * (i.e. urn:pin:namespace), but in general the inverse mapping to {@link #uriToNamespace(String)} (String)} should
     * be applied.
     *
     * @param namespace the namespace
     * @return the mapped URI
     */
    String namespaceToUri(String namespace);


    @SafeVarargs
    static <T> Optional<T> findFirst(Supplier<Optional<T>>... suppliers) {
        return Stream.of(suppliers)
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

}
