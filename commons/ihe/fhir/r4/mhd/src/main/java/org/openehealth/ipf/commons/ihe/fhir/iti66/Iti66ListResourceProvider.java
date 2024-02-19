/*
 * Copyright 2023 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti66;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.*;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import java.util.Set;

/**
 * Resource Provider for MHD (ITI-66 List)
 *
 * @author Christian Ohr
 * @since 4.8
 */
public class Iti66ListResourceProvider extends AbstractPlainProvider {

    // Supported as of MHD 4.2.1

    private static final String SP_DESIGNATION_TYPE = "designationType";
    private static final String SP_SOURCE_ID = "sourceId";

    @SuppressWarnings("unused")
    @Search(type = ListResource.class)
    public IBundleProvider listSearch(
            @RequiredParam(name = ListResource.SP_PATIENT, chainWhitelist = {"", Patient.SP_IDENTIFIER}) ReferenceParam patient,
            @OptionalParam(name = ListResource.SP_DATE) DateRangeParam date,
            @OptionalParam(name = ListResource.SP_SOURCE, chainWhitelist = { Practitioner.SP_FAMILY, Practitioner.SP_GIVEN }) ReferenceAndListParam author,
            @OptionalParam(name = ListResource.SP_IDENTIFIER) TokenParam identifier,
            @OptionalParam(name = ListResource.SP_CODE) TokenOrListParam code,
            @OptionalParam(name = ListResource.SP_STATUS) TokenOrListParam status,
            @OptionalParam(name = SP_SOURCE_ID) TokenOrListParam sourceId,
            @OptionalParam(name = SP_DESIGNATION_TYPE) TokenOrListParam designationType,
            // Extension to ITI-66
            @OptionalParam(name = IAnyResource.SP_RES_ID) TokenParam resourceId,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {


        var searchParameters = Iti66ListSearchParameters.builder()
                .date(date)
                .code(code)
                .designationType(designationType)
                .sourceId(sourceId)
                .status(status)
                .identifier(identifier)
                ._id(resourceId)
                .sortSpec(sortSpec)
                .includeSpec(includeSpec)
                .fhirContext(getFhirContext())
                .build();

        searchParameters.setAuthor(author);

        var chain = patient.getChain();
        if (Patient.SP_IDENTIFIER.equals(chain)) {
            searchParameters.setPatientIdentifier(patient.toTokenParam(getFhirContext()));
        } else if (chain == null || chain.isEmpty()) {
            searchParameters.setPatientReference(patient);
        }

        // Run down the route
        return requestBundleProvider(null, searchParameters, ResourceType.List.name(),
                httpServletRequest, httpServletResponse, requestDetails);
    }

    /**
     * Handles List Retrieve. This is not an actual part of the ITI-66 specification, but in the
     * context of restful FHIR IHE transaction it makes sense to be able to retrieve a ListResource by
     * its resource ID.
     *
     * @param id resource ID
     * @param httpServletRequest servlet request
     * @param httpServletResponse servlet response
     * @param requestDetails      request details
     * @return {@link ListResource} resource
     */
    @SuppressWarnings("unused")
    @Read(version = true, type = ListResource.class)
    public ListResource listRetrieve(
            @IdParam IdType id,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        if (id == null) throw new InvalidRequestException("Must provide ID with READ request");
        // Run down the route
        return requestResource(id, null, ListResource.class, httpServletRequest, httpServletResponse, requestDetails);
    }

}
