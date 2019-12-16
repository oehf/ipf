/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.support;


import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.NamingSystem;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Service for finding FHIR {@link NamingSystem} instances. Implementations should consider
 * caching all known naming systems and select specific ones.
 *
 * @author Christian Ohr
 * @since 3.6
 */
public interface NamingSystemService {

    /**
     * Finds all {@link NamingSystem} instances that match the provided {@link Predicate} and returns
     * a stream of these matches.
     *
     * @param id ID of a NamingSystem bundle
     * @param predicate predicate selecting a naming system
     * @return a stream of {@link NamingSystem} instances that match the provided {@link Predicate}
     */
    Stream<? extends NamingSystem> findNamingSystems(String id, Predicate<? super NamingSystem> predicate);

    /**
     * Returns the first {@link NamingSystem} instances that match the provided {@link Predicate}
     *
     * @param id ID of a NamingSystem bundle
     * @param predicate predicate selecting a naming system
     * @return {@link NamingSystem} instance that match the provided {@link Predicate}
     */
    default Optional<? extends NamingSystem> findFirstNamingSystem(String id, Predicate<? super NamingSystem> predicate) {
        return findNamingSystems(id, predicate).findFirst();
    }

    /**
     * Returns the first active {@link NamingSystem} instances that match the provided type and value
     *
     * @param id ID of a NamingSystem bundle
     * @param type  NamingSystem identifier type (oid, uuid, ...)
     * @param value value
     * @return {@link NamingSystem} instance that match the provided type and value
     */
    default Optional<? extends NamingSystem> findActiveNamingSystemByTypeAndValue(String id, NamingSystem.NamingSystemIdentifierType type, String value) {
        return findFirstNamingSystem(id, allOf(
                byTypeAndValue(type, value),
                byStatus(Enumerations.PublicationStatus.ACTIVE)));
    }

    // Predicates

    static Predicate<NamingSystem> allOf(Predicate<NamingSystem>... predicates) {
        return combine(Predicate::and, predicates);
    }

    static Predicate<NamingSystem> anyOf(Predicate<NamingSystem>... predicates) {
        return combine(Predicate::or, predicates);
    }

    static Predicate<NamingSystem> combine(BinaryOperator<Predicate<NamingSystem>> op, Predicate<NamingSystem>... predicates) {
        return predicates != null ?
                Stream.of(predicates).reduce(op).orElse(namingSystem -> false) :
                namingSystem -> false;
    }

    static Predicate<NamingSystem> byTypeAndValue(NamingSystem.NamingSystemIdentifierType type, String value) {
        return namingSystem -> namingSystem.getUniqueId().stream()
                .anyMatch(uniqueId -> uniqueId.getType() == type
                        && value.equals(uniqueId.getValue()));
    }

    static Predicate<NamingSystem> byId(String id) {
        return namingSystem -> Objects.equals(id, namingSystem.getId());
    }

    static Predicate<NamingSystem> byName(String name) {
        return namingSystem -> Objects.equals(name, namingSystem.getName());
    }

    static Predicate<NamingSystem> byKind(NamingSystem.NamingSystemType kind) {
        return namingSystem -> Objects.equals(kind, namingSystem.getKind());
    }

    static Predicate<NamingSystem> byStatus(Enumerations.PublicationStatus status) {
        return namingSystem -> Objects.equals(status, namingSystem.getStatus());
    }

    // Functions

    static Function<NamingSystem, String> getValueOfType(NamingSystem.NamingSystemIdentifierType type) {
        return namingSystem -> namingSystem.getUniqueId().stream()
                .filter(uniqueId -> type == uniqueId.getType())
                .findFirst()
                .map(NamingSystem.NamingSystemUniqueIdComponent::getValue)
                .orElse(null);
    }
}
