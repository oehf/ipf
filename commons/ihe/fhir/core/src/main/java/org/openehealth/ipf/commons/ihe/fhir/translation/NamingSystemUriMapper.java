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

import org.openehealth.ipf.commons.ihe.fhir.NamingSystemService;

import static org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType.OID;
import static org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType.OTHER;
import static org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType.URI;

/**
 * URI Mapper that is backed by a {@link NamingSystemService}. The mapping is done
 * by search for a {@link org.hl7.fhir.instance.model.NamingSystem NamingSystem}
 * with a uniqueID of a certain type and returning a unique ID with a different type.
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class NamingSystemUriMapper extends AbstractUriMapper {

    private final NamingSystemService namingSystemService;

    public NamingSystemUriMapper(NamingSystemService namingSystemService) {
        this.namingSystemService = namingSystemService;
    }

    @Override
    protected String mapOidToUri(String oid) {
        return namingSystemService.findActiveNamingSystemByTypeAndValue(OID, oid)
                .map(NamingSystemService.getValueOfType(URI))
                .orElse(null);
    }

    @Override
    protected String mapUriToOid(String uri) {
        return namingSystemService.findActiveNamingSystemByTypeAndValue(URI, uri)
                .map(NamingSystemService.getValueOfType(OID))
                .orElse(null);
    }

    @Override
    protected String mapUriToNamespace(String uri) {
        return namingSystemService.findActiveNamingSystemByTypeAndValue(URI, uri)
                .map(NamingSystemService.getValueOfType(OTHER))
                .orElse(null);
    }
}
