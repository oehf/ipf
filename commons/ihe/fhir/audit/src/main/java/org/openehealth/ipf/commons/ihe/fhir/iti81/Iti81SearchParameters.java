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

import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;

/**
 *
 */
@Builder
public class Iti81SearchParameters implements FhirSearchParameters {

    @Getter @Setter private DateAndListParam interval;
    @Getter @Setter private StringAndListParam address;
    @Getter @Setter private TokenAndListParam patientId;
    @Getter @Setter private TokenAndListParam identity;
    @Getter @Setter private TokenAndListParam objectType;
    @Getter @Setter private TokenAndListParam role;
    @Getter @Setter private StringAndListParam source;
    @Getter @Setter private TokenAndListParam type;
    @Getter @Setter private StringAndListParam participant;
    @Getter @Setter private TokenAndListParam subtype;
    @Getter @Setter private TokenAndListParam outcome;

}
