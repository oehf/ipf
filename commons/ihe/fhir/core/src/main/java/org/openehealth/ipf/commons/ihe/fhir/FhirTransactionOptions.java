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

package org.openehealth.ipf.commons.ihe.fhir;

import org.openehealth.ipf.commons.ihe.core.TransactionOptions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christian Ohr
 * @since 3.5
 */
public interface FhirTransactionOptions extends TransactionOptions<Class<? extends FhirProvider>> {

    static List<? extends FhirProvider> concatProviders(List<? extends FhirTransactionOptions> options) {
        return options.stream()
                .flatMap(o -> o.getSupportedThings().stream())
                .map(FhirTransactionOptions::createResourceProvider)
                .collect(Collectors.toList());
    }

    static FhirProvider createResourceProvider(Class<? extends FhirProvider> c) {
        try {
            return c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
