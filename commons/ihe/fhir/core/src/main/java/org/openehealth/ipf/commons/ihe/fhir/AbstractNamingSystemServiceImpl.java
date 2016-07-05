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

package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.NamingSystem;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Abstract Implementation that holds naming systems in memory as a FHIR bundle. Before an
 * instance can be used, one of the setter methods of implementations must be called to initialize the
 * bundle.
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class AbstractNamingSystemServiceImpl implements NamingSystemService {

    protected transient Bundle namingSystems;

    public void setNamingSystems(Bundle namingSystems) {
        this.namingSystems = namingSystems;
    }

    @Override
    public Stream<NamingSystem> findNamingSystems(Predicate<? super NamingSystem> predicate) {
        if (namingSystems == null) {
            throw new IllegalStateException("No naming systems loaded");
        }
        return namingSystems.getEntry().stream()
                .map(bec -> (NamingSystem) bec.getResource())
                .filter(predicate);
    }


}
