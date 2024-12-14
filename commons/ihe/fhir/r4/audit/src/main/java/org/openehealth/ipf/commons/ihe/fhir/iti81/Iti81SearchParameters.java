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
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hl7.fhir.r4.model.AuditEvent;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchAndSortParameters;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.*;

/**
 * @since 3.6
 */
@Builder
@ToString
@AllArgsConstructor
public class Iti81SearchParameters extends FhirSearchAndSortParameters<AuditEvent> {

    @Getter @Setter private DateRangeParam interval;
    @Getter @Setter private StringAndListParam address;
    @Getter @Setter private ReferenceParam agent;
    @Getter @Setter private TokenAndListParam patientId;
    @Getter @Setter private ReferenceParam entity;
    @Getter @Setter private TokenAndListParam entityType;
    @Getter @Setter private TokenAndListParam entityRole;
    @Getter @Setter private ReferenceParam source;
    @Getter @Setter private TokenAndListParam type;
    @Getter @Setter private TokenAndListParam subtype;
    @Getter @Setter private TokenAndListParam outcome;
    @Getter @Setter private TokenAndListParam altid;

    @Getter @Setter private SortSpec sortSpec;
    @Getter @Setter private Set<Include> includeSpec;

    @Getter
    private final FhirContext fhirContext;

    @Override
    public List<TokenParam> getPatientIdParam() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Comparator<AuditEvent>> comparatorFor(String paramName) {
        if (AuditEvent.SP_DATE.equals(paramName)) {
            return Optional.of(CP_DATE);
        } else if (AuditEvent.SP_ADDRESS.equals(paramName)) {
            return Optional.of(CP_ADDRESS);
        }
        return Optional.empty();
    }

    private static final Comparator<AuditEvent> CP_DATE = nullsLast(comparing(AuditEvent::getRecorded));
    private static final Comparator<AuditEvent> CP_ADDRESS = comparing(
        Iti81SearchParameters::getNetworkAddress, nullsLast(naturalOrder()));

    private static String getNetworkAddress(AuditEvent auditEvent) {
        if (auditEvent.hasAgent()) {
            var agent = auditEvent.getAgentFirstRep();
            if (agent.hasNetwork()) {
                return agent.getNetwork().getAddressElement().getValueNotNull();
            }
        }
        return null;
    }

}
