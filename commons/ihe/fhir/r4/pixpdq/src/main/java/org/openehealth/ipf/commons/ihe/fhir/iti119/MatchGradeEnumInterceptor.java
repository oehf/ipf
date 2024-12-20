/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti119;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;

import java.util.Optional;

import static org.openehealth.ipf.commons.ihe.fhir.iti119.AdditionalResourceMetadataKeyEnum.ENTRY_MATCH_GRADE;

/**
 * There is no universal mechanism to attach any kind of search attribute to a response bundle. As ITI-119 asks
 * for a custom attribute that cannot been easily be handled within the BundleFactory, an interceptor is required.
 * An instance of this interceptor must be registered with the {@link ca.uhn.fhir.rest.server.RestfulServer RestfulServer} or
 * the {@link org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet IpfFhirServlet} subclass. The Spring Boot starter does
 * this automatically.
 */
@Interceptor
public class MatchGradeEnumInterceptor {

    public static final String MATCH_GRADE_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/match-grade";

    @Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
    @SuppressWarnings("unused")
    public void insertMatchGrades(RequestDetails requestDetails, IBaseResource resource) {
        if (resource instanceof Bundle bundle) {
            bundle.getEntry().stream()
                .filter(Bundle.BundleEntryComponent::hasResource)
                .filter(Bundle.BundleEntryComponent::hasSearch)
                .filter(bc -> Bundle.SearchEntryMode.MATCH.equals(bc.getSearch().getMode()))
                .forEach(entry ->
                    Optional.ofNullable(ENTRY_MATCH_GRADE.get(entry.getResource())).ifPresent(matchGrade ->
                        entry.getSearch().addExtension(
                            MATCH_GRADE_EXTENSION_URL,
                            new Coding()
                                .setSystem(matchGrade.getSystem())
                                .setCode(matchGrade.toCode()))
                    )
                );
        }
    }
}
