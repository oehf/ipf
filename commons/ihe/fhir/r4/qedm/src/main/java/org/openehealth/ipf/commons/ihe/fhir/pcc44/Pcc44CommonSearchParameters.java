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

package org.openehealth.ipf.commons.ihe.fhir.pcc44;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hl7.fhir.r4.model.DomainResource;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchAndSortParameters;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Christian Ohr
 * @since 3.6
 */
@AllArgsConstructor
abstract class Pcc44CommonSearchParameters<T extends DomainResource> extends FhirSearchAndSortParameters<T> {

    @Getter private final ReferenceParam patientReference;
    @Getter private final TokenParam _id;

    @Getter private final SortSpec sortSpec;
    @Getter private final Set<Include> includeSpec;
    @Getter private final Set<Include> revIncludeSpec;

    @Getter
    private final FhirContext fhirContext;

    @Override
    public List<TokenParam> getPatientIdParam() {
        if (_id != null)
            return Collections.singletonList(_id);
        return Collections.singletonList(patientReference.toTokenParam(fhirContext));
    }
}
