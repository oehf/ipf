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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.UriType;
import org.openehealth.ipf.commons.ihe.fhir.SharedFhirProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * BundleProfileSelector can be used as selector for FHIR Consumers in batch/transaction
 * requests. These requests can be typically handled by a subclass of
 * {@link SharedFhirProvider SharedFhirProvider} like BatchTransactionResourceProvider.
 *
 * @author Christian Ohr
 * @since 3.6
 * @deprecated use {@link org.openehealth.ipf.commons.ihe.fhir.BundleProfileSelector instead}
 */
public class BundleProfileSelector implements Predicate<RequestDetails> {

    private final Set<String> profileUris;

    /**
     * @param profileUris Profile URIs expected in the Bundle's meta element
     */
    public BundleProfileSelector(String... profileUris) {
        this.profileUris = new HashSet<>(Arrays.asList(profileUris));
    }

    /**
     * @param requestDetails request
     * @return true if one of the {@link #profileUris} are present in the Bundle's meta.profile
     */
    @Override
    public boolean test(RequestDetails requestDetails) {
        var bundle = (Bundle) requestDetails.getResource();
        return bundle.getMeta().getProfile().stream()
                .map(UriType::getValueAsString)
                .anyMatch(profileUris::contains);
    }
}
