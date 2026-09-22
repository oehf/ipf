/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.atna.translation;

import org.openehealth.ipf.commons.map.MappingFunctionProvider;
import org.openehealth.ipf.commons.map.MappingFunctionRegistry;

/**
 * The mapping functions that {@code META-INF/map/atna2fhir.mapping.xml} refers to. Registered
 * through the {@link MappingFunctionProvider} service so that the mapping file works wherever it
 * is loaded from.
 *
 * @since 6.0
 */
public class AtnaMappingFunctions implements MappingFunctionProvider {

    /**
     * Name under which {@link #oidUri(String)} is registered.
     */
    public static final String OID_URI = "oidUri";

    @Override
    public void register(MappingFunctionRegistry registry) {
        registry.register(OID_URI, AtnaMappingFunctions::oidUri);
    }

    /**
     * Turns a bare OID into the URI form FHIR expects, and leaves anything that is not an OID
     * alone. ATNA code system names are either a known symbolic name - those have an entry in the
     * mapping - or an OID written without its {@code urn:oid:} prefix.
     *
     * @param codeSystemName an ATNA code system name
     * @return {@code urn:oid:<name>} if the name starts with a digit, the name itself otherwise
     */
    public static String oidUri(String codeSystemName) {
        return codeSystemName != null && !codeSystemName.isEmpty()
                && Character.isDigit(codeSystemName.charAt(0))
                ? "urn:oid:" + codeSystemName
                : codeSystemName;
    }
}
