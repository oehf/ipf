/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@ToString(exclude = {"fhirContext"})
@AllArgsConstructor
public class ChPpq5SearchParameters implements FhirSearchParameters {

    @Getter
    private FhirContext fhirContext;

    @Getter @Setter
    private TokenParam patientId;

    @Getter @Setter
    private TokenParam consentId;

    @Override
    public List<TokenParam> getPatientIdParam() {
        return (patientId != null) ? Collections.singletonList(patientId) : Collections.emptyList();
    }

    @Override
    public Set<Include> getIncludeSpec() {
        return null;
    }
}
