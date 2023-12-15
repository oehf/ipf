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

import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.r4.model.Consent;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ChPpq5ResourceProvider extends AbstractPlainProvider {

    @Search(type = Consent.class)
    public IBundleProvider searchConsents(
            @OptionalParam(name = Consent.SP_PATIENT) TokenParam patientId,
            @OptionalParam(name = Consent.SP_IDENTIFIER) TokenParam consentId,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
    {
        ChPpq5SearchParameters searchParameters = new ChPpq5SearchParameters(getFhirContext(), patientId, consentId);
        return requestBundleProvider(null, searchParameters, ResourceType.Consent.name(),
                httpServletRequest, httpServletResponse, requestDetails);
    }

}
