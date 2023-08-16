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

package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq3;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.IdType;
import org.openehealth.ipf.commons.ihe.fhir.AbstractResourceProvider;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChPpq3ResourceProvider extends AbstractResourceProvider {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Consent.class;
    }

    @Create
    public MethodOutcome createConsent(
            @ResourceParam Consent consent,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
    {
        return requestAction(consent, null, httpServletRequest, httpServletResponse, requestDetails);
    }

    // Conditionally Update / Add
    @Update
    public MethodOutcome updateConsent(
            @ResourceParam Consent consent,
            @IdParam IdType resourceId,
            @ConditionalUrlParam String condition,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
    {
        return requestAction(consent, null, httpServletRequest, httpServletResponse, requestDetails);
    }

    @Delete
    public MethodOutcome deleteConsent(
            @IdParam IdType id,
            @IdParam IdType resourceId,
            @ConditionalUrlParam String condition,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
    {
        String consentId = ChPpqmUtils.extractConsentIdFromUrl(condition);
        return requestAction(consentId, null, httpServletRequest, httpServletResponse, requestDetails);
    }

}
