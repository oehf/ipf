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
package org.openehealth.ipf.commons.map;

/**
 * The answer to a lookup together with what a caller needs to build a coded value from it: the
 * code system it belongs to and its display, if the mapping declares one.
 * <p>
 * The system is that of the mapping which actually answered. For a single mapping that is its
 * value system (key system in reverse); for a composite mapping - several mappings asked in turn,
 * such as the groups of a FHIR ConceptMap targeting different code systems - it is the system of
 * the part that answered, so {@code UNK} from a null-flavor group comes back with the null-flavor
 * system rather than that of the group next to it.
 *
 * @param code    the mapped code, never {@code null}
 * @param system  formal identifier of the code system the code belongs to, {@code null} if the
 *                answering mapping declares none
 * @param display human-readable name of the code as the mapping declares it, may be {@code null}
 * @see Mappings#translate(String, String)
 * @since 6.0
 */
public record Translation(String code, String system, String display) {

    public Translation {
        if (code == null) {
            throw new IllegalArgumentException("A translation requires a code");
        }
    }
}
