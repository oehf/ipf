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
package org.openehealth.ipf.commons.ihe.hl7v3.core.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Simplified model of a PDQv3 query (ITI-47, PRPA_IN201305UV02).
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PdqV3QueryRequest extends PatientDemographicsQueryRequest {

    /**
     * Query parameter {@code OtherIDs.scopingOrganization.id}: OIDs of the assigning authorities for which
     * patient IDs shall be returned. The set is never {@code null}.
     */
    private final Set<String> otherIDsScopingOrganizationOids = new LinkedHashSet<>();

    /**
     * The number of matches to return in the first response (Continuation option). If {@code null}, all matches
     * are requested.
     */
    private Integer initialQuantity;

    @Override
    protected void copyQueryParameters(final PatientDemographicsQueryRequest query) {
        super.copyQueryParameters(query);
        if (query instanceof PdqV3QueryRequest pdqQuery) {
            this.otherIDsScopingOrganizationOids.addAll(pdqQuery.getOtherIDsScopingOrganizationOids());
            this.initialQuantity = pdqQuery.getInitialQuantity();
        }
    }
}
