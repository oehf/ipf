/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti66;

import org.openehealth.ipf.commons.ihe.fhir.FhirProvider;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionOptions;

import java.util.Arrays;
import java.util.List;

/**
 * @since 4.1
 */
public enum Iti66Options implements FhirTransactionOptions {

    STRICT(Iti66StrictResourceProvider.class),
    LENIENT(Iti66ResourceProvider.class);

    private final List<Class<? extends FhirProvider>> resourceProviders;

    @SafeVarargs
    Iti66Options(Class<? extends FhirProvider>... resourceProviders) {
        this.resourceProviders = Arrays.asList(resourceProviders);
    }

    @Override
    public List<Class<? extends FhirProvider>> getSupportedThings() {
        return resourceProviders;
    }
}
