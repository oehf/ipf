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

import java.net.URISyntaxException;

/**
 * The FHIR Identifier type introduces a new mechanism for conveying the originating system of a particular identifier.
 * Whereas HL7 Version 2 and Version 3 messages identify an assigning 1480 organization as a HD or an OID in the “root”
 * attribute respectively, HL7 FHIR permits the use of a URI. This requires some configuration on the part of actors
 * to correctly map a URL to an OID or HD to maintain consistency with other actors which are not implementing the
 * FHIR specification.
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
     * @return the mapped OID or null if no OID could be identified
     * @throws URISyntaxException if the uri string is no valid URI
     */
    String uriToOid(String uri) throws URISyntaxException;

    /**
     * Translates an URI into a Namespace.
     *
     * @param uri the URI
     * @return the mapped namespace or null if no namespace could be identified
     * @throws URISyntaxException if the uri string is no valid URI
     */
    String uriToNamespace(String uri) throws URISyntaxException;

    /**
     * Translates an OID into an URI. Instead of a real mapping, an URN can be derived from the OID
     * (i.e. urn:oid:1.2.3.4), but in general the inverse mapping to {@link #uriToOid(String)} should
     * be applied.
     *
     * @param oid the OID
     * @return the mapped URI
     * @throws URISyntaxException
     */
    String oidToUri(String oid) throws URISyntaxException;

}
