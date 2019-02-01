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

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * MedicationRequest Resource Provider for QEDm (PCC-44)
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class MedicationRequestResourceProvider extends AbstractPcc44ResourceProvider<MedicationRequest> {

    public MedicationRequestResourceProvider() {
        super(MedicationRequest.class);
    }

    @SuppressWarnings("unused")
    @Search
    public IBundleProvider search(
            @RequiredParam(name = MedicationRequest.SP_PATIENT) ReferenceParam patient,
            // Extension
            @OptionalParam(name = IAnyResource.SP_RES_ID) TokenParam resourceId,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            @IncludeParam(reverse = true) Set<Include> revIncludeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {


        MedicationRequestSearchParameters parameters = MedicationRequestSearchParameters.builder()
                .patientReference(patient)
                ._id(resourceId)
                .sortSpec(sortSpec)
                .includeSpec(includeSpec)
                .revIncludeSpec(revIncludeSpec)
                .fhirContext(getFhirContext())
                .build();

        // Run down the route
        return requestBundleProvider(null, parameters, ResourceType.MedicationRequest.name(),
                httpServletRequest, httpServletResponse, requestDetails);
    }

}
