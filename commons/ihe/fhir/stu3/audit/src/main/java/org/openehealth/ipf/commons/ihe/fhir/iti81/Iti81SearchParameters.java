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

package org.openehealth.ipf.commons.ihe.fhir.iti81;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;

import java.util.List;
import java.util.Set;

/**
 *
 */
@Builder
public class Iti81SearchParameters implements FhirSearchParameters {

    @Getter @Setter private DateRangeParam interval;
    @Getter @Setter private StringAndListParam address;
    @Getter @Setter private TokenAndListParam patientId;
    @Getter @Setter private TokenAndListParam entityId;
    @Getter @Setter private TokenAndListParam entityType;
    @Getter @Setter private TokenAndListParam entityRole;
    @Getter @Setter private StringAndListParam source;
    @Getter @Setter private TokenAndListParam type;
    @Getter @Setter private StringAndListParam user;
    @Getter @Setter private TokenAndListParam subtype;
    @Getter @Setter private TokenAndListParam outcome;

    @Getter @Setter private SortSpec sortSpec;
    @Getter @Setter private Set<Include> includeSpec;

    @Getter
    private FhirContext fhirContext;

    @Override
    public List<TokenParam> getPatientIdParam() {
        throw new UnsupportedOperationException();
    }
}
