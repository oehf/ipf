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
import ca.uhn.fhir.rest.param.*;
import lombok.*;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Christian Ohr
 * @since 3.5
 */
@AllArgsConstructor
abstract class Pcc44CommonSearchParameters implements FhirSearchParameters {

    @Getter private ReferenceParam patientReference;
    @Getter private TokenParam _id;

    @Getter private SortSpec sortSpec;
    @Getter private Set<Include> includeSpec;
    @Getter private Set<Include> revIncludeSpec;

    @Getter
    private FhirContext fhirContext;

    @Override
    public List<TokenParam> getPatientIdParam() {
        if (_id != null)
            return Collections.singletonList(_id);
        return Collections.singletonList(patientReference.toTokenParam(fhirContext));
    }
}
