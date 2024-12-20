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


import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.NamingSystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract Implementation that holds naming systems in memory as a map of {@link NamingSystem} sets. Before an
 * instance can be used, one of the adder methods of implementations must be called to initialize the
 * bundle.
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class AbstractNamingSystemServiceImpl implements NamingSystemService {

    protected final transient Map<String, Set<NamingSystem>> namingSystems = new HashMap<>();

    public void addNamingSystems(Bundle bundle) {
        this.namingSystems.merge(bundle.getIdElement().getIdPart(), setOfNamingSystems(bundle), (set1, set2) -> {
            var result = new HashSet<>(set1);
            result.addAll(set2);
            return result;
        });
    }

    @Override
    public Stream<NamingSystem> findNamingSystems(String id, Predicate<? super NamingSystem> predicate) {
        if (!namingSystems.containsKey(id)) {
            throw new IllegalArgumentException("No NamingSystem known with ID " + id);
        }
        return namingSystems.get(id).stream().filter(predicate);
    }

    private Set<NamingSystem> setOfNamingSystems(Bundle bundle) {
        return bundle.getEntry().stream()
                .map(bec -> (NamingSystem) bec.getResource())
                .collect(Collectors.toSet());
    }

}
