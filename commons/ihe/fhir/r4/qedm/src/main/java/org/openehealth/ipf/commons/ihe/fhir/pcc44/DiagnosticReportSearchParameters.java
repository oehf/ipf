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
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DiagnosticReport;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

/**
 * @author Christian Ohr
 * @since 3.6
 */

@ToString
public class DiagnosticReportSearchParameters extends Pcc44CommonSearchParameters<DiagnosticReport> {

    @Getter @Setter
    private TokenOrListParam category;
    @Getter @Setter
    private TokenOrListParam code;
    @Getter @Setter
    private DateRangeParam date;

    @Builder
    DiagnosticReportSearchParameters(TokenOrListParam category,
                                            TokenOrListParam code,
                                            DateRangeParam date, ReferenceParam patientReference,
                                            TokenParam _id,
                                            SortSpec sortSpec,
                                            Set<Include> includeSpec,
                                            Set<Include> revIncludeSpec,
                                            FhirContext fhirContext) {
        super(patientReference, _id, sortSpec, includeSpec, revIncludeSpec, fhirContext);
        this.category = category;
        this.code = code;
        this.date = date;
    }

    @Override
    protected Optional<Comparator<DiagnosticReport>> comparatorFor(String paramName) {
        if (DiagnosticReport.SP_DATE.equals(paramName)) {
            return Optional.of(nullsLast(CP_EFFECTIVE));
        }
        return Optional.empty();
    }

    private static final Comparator<DiagnosticReport> CP_EFFECTIVE = nullsLast(comparing(diagnosticReport -> {
        if (!diagnosticReport.hasEffective()) return null;
        var effective = diagnosticReport.getEffective();
        if (effective instanceof DateTimeType) {
            return diagnosticReport.getEffectiveDateTimeType().getValueAsString();
        } else {
            return diagnosticReport.getEffectivePeriod().getStartElement().getValueAsString();
        }
    }));
}
