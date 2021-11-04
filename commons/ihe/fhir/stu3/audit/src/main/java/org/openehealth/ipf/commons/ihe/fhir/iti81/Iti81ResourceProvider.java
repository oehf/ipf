/*
 * Copyright 2015 the original author or authors.
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

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * According to the Restful ATNA specification, this resource provider must handle requests in the form
 * GET [base]/AuditEvent?date=>start-time&date=<stoptime>&<query>{&_format=[mime-type]}
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class Iti81ResourceProvider extends AbstractPlainProvider {

    @SuppressWarnings("unused")
    @Search(type = AuditEvent.class)
    public IBundleProvider auditSearch(
            @RequiredParam(name = AuditEvent.SP_DATE) DateRangeParam interval,
            @OptionalParam(name = AuditEvent.SP_ADDRESS) StringAndListParam address,
            @OptionalParam(name = AuditEvent.SP_PATIENT + ".identifier") TokenAndListParam patientId,
            @OptionalParam(name = AuditEvent.SP_ENTITY_ID) TokenAndListParam entityId,
            @OptionalParam(name = AuditEvent.SP_ENTITY_TYPE) TokenAndListParam entityType,
            @OptionalParam(name = AuditEvent.SP_ENTITY_ROLE) TokenAndListParam entityRole,
            @OptionalParam(name = AuditEvent.SP_SOURCE) StringAndListParam source,
            @OptionalParam(name = AuditEvent.SP_TYPE) TokenAndListParam type,
            @OptionalParam(name = AuditEvent.SP_USER) StringAndListParam user,
            @OptionalParam(name = AuditEvent.SP_SUBTYPE) TokenAndListParam subtype,
            @OptionalParam(name = AuditEvent.SP_OUTCOME) TokenAndListParam outcome,
            @OptionalParam(name = AuditEvent.SP_ALTID) TokenAndListParam altid,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        var searchParameters = Iti81SearchParameters.builder()
                .interval(interval)
                .address(address)
                .patientId(patientId)
                .entityId(entityId)
                .entityType(entityType)
                .entityRole(entityRole)
                .source(source)
                .type(type)
                .user(user)
                .subtype(subtype)
                .outcome(outcome)
                .altid(altid)
                .sortSpec(sortSpec)
                .includeSpec(includeSpec)
                .fhirContext(getFhirContext())
                .build();

        // Run down the route
        return requestBundleProvider(null, searchParameters, ResourceType.AuditEvent.name(),
                httpServletRequest, httpServletResponse, requestDetails);

    }

}
