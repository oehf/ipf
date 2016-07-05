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

import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import org.hl7.fhir.instance.model.AuditEvent;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            @RequiredParam(name = AuditEvent.SP_DATE) DateAndListParam interval,
            @OptionalParam(name = AuditEvent.SP_ADDRESS) StringAndListParam address,
            @OptionalParam(name = Iti81Constants.SP_PATIENTID) TokenAndListParam patientId,
            @OptionalParam(name = AuditEvent.SP_IDENTITY) TokenAndListParam identity,
            @OptionalParam(name = AuditEvent.SP_OBJECTTYPE) TokenAndListParam objectType,
            @OptionalParam(name = Iti81Constants.SP_ROLE) TokenAndListParam role,
            @OptionalParam(name = AuditEvent.SP_SOURCE) StringAndListParam source,
            @OptionalParam(name = AuditEvent.SP_TYPE) TokenAndListParam type,
            @OptionalParam(name = AuditEvent.SP_PARTICIPANT) StringAndListParam participant,
            @OptionalParam(name = AuditEvent.SP_SUBTYPE) TokenAndListParam subtype,
            @OptionalParam(name = Iti81Constants.SP_OUTCOME) TokenAndListParam outcome,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        Iti81SearchParameters searchParameters = Iti81SearchParameters.builder()
                .interval(interval)
                .address(address)
                .patientId(patientId)
                .identity(identity)
                .objectType(objectType)
                .role(role)
                .source(source)
                .type(type)
                .participant(participant)
                .subtype(subtype)
                .outcome(outcome).build();

        // Run down the route
        return requestBundleProvider(null, searchParameters, httpServletRequest, httpServletResponse);

    }

}
