/*
 * Copyright 2022 the original author or authors.
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

import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Extension of FhirSearchParameters that add sorting capabilities. Implementing classes must overwrite
 * {@link #comparatorFor(String)} to provide explicit sorting before a search response bundle is returned.
 *
 * @param <T>
 */
public abstract class FhirSearchAndSortParameters<T extends IBaseResource> implements FhirSearchParameters {

    @SuppressWarnings("unused")
    public FhirSearchAndSortParameters() {
    }

    /**
     * @return the sort specification as requested by the client
     */
    public abstract SortSpec getSortSpec();

    private final Map<String, Comparator<T>> comparators = new HashMap<>();

    /**
     * Can be called to sort a result list according to its {@link SortSpec} as indicated in the query
     *
     * @param resultList (unsorted) result list
     */
    public void sort(List<T> resultList) {
        if (!resultList.isEmpty()) {
            resultList.sort(comparatorFor(getSortSpec()));
        }
    }

    /**
     * Returns a {@link Comparator} to sort resources of type T by given sort parameter name. This default implementation
     * keeps the current order, i.e. to implement actual sorting you MUST overwrite this method.
     *
     * @param paramName sort parameter
     * @return comparator
     */
    protected Optional<Comparator<T>> comparatorFor(String paramName) {
        return Optional.of(noop());
    }

    /**
     * Returns the {@link Comparator} needed for {@link #sort(List)}.
     * Always overwrite {@link #reversibleComparator(String, boolean)} to implement a Comparator for a dedicated
     * resource type.
     *
     * @param sortSpec sort specification
     * @return Comparator matching the sortSpec
     */
    private Comparator<T> comparatorFor(SortSpec sortSpec) {
        if (sortSpec == null || sortSpec.getParamName() == null) {
            return Comparator.comparingInt(value -> 0);  // dummy comparator, keep order
        }
        return reversibleComparator(sortSpec.getParamName(), sortSpec.getOrder() == SortOrderEnum.DESC)
                .thenComparing(comparatorFor(sortSpec.getChain()));
    }

    private Comparator<T> reversibleComparator(String paramName, boolean reversed) {
        var comparator =  comparators.computeIfAbsent(paramName, p -> comparatorFor(p).orElse(noop()));
        return reversed ? comparator.reversed() : comparator;
    }

    @SuppressWarnings("rawtypes")
    private static final Comparator NOOP = Comparator.comparingInt(value -> 0);

    @SuppressWarnings("unchecked")
    public static <K> Comparator<K> noop() {
        return (Comparator<K>) NOOP;
    }
}
