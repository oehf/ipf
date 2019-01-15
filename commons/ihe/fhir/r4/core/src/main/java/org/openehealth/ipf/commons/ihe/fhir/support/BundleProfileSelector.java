/*
 * Copyright 2019 the original author or authors.
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

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.UriType;
import org.openehealth.ipf.commons.ihe.fhir.SharedFhirProvider;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * BundleProfileSelector can be used as selector for FHIR Consumers in batch/transaction
 * requests. These requests can be typically handled by a subclass of
 * {@link SharedFhirProvider AbstractBatchTransactionResourceProvider}
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class BundleProfileSelector implements Predicate<Object> {

    private final Set<UriType> profileUris;

    public BundleProfileSelector(String... profileUris) {
        this.profileUris = Arrays.stream(profileUris)
                .map(UriType::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean test(Object object) {
        Bundle bundle = (Bundle)object;
        return bundle.getMeta().getProfile().stream()
                .anyMatch(profileUris::contains);
    }
}
