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

package org.openehealth.ipf.commons.ihe.fhir.iti67;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Resource Provider for MHD (ITI-67).
 * <p>
 * Note (CP by Rick Riemer):
 * When searching for XDS documents with specific referenceIdList values, MHD specifies to use the related-id query parameter.
 * This parameter is of type token (https://www.hl7.org/fhir/search.html#token). The token type does not allow searching for
 * the Identifier.type attribute, which would be a primary use case.
 * <p>
 * IHE should provide a mechanism to search for referenceIdList values by type, in addition to system and value.
 * Suggestion: don’t use token, but use composite, (https://www.hl7.org/fhir/search.html#composite) and define how
 * to use it for searching against referenceIdList values. A composite parameter could look like:
 * related id=urn:oid:1.2.3.4.5.6|2013001$urn:ihe:iti:xds:2013:accession.
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti67ResourceProvider extends AbstractPlainProvider {

    @SuppressWarnings("unused")
    @Search(type = DocumentReference.class)
    public IBundleProvider documentReferenceSearch(
            @RequiredParam(name = DocumentReference.SP_PATIENT, chainWhitelist = {"", Patient.SP_IDENTIFIER}) ReferenceParam patient,
            @OptionalParam(name = DocumentReference.SP_STATUS) TokenOrListParam status,
            @OptionalParam(name = DocumentReference.SP_INDEXED) DateRangeParam indexed,
            @OptionalParam(name = DocumentReference.SP_AUTHOR, chainWhitelist = { Practitioner.SP_FAMILY, Practitioner.SP_GIVEN }) ReferenceAndListParam author,
            @OptionalParam(name = DocumentReference.SP_CLASS) TokenOrListParam class_,
            @OptionalParam(name = DocumentReference.SP_TYPE) TokenOrListParam type,
            @OptionalParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OptionalParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OptionalParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OptionalParam(name = DocumentReference.SP_EVENT) TokenOrListParam event,
            @OptionalParam(name = DocumentReference.SP_SECURITYLABEL) TokenOrListParam securityLabel,
            @OptionalParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @OptionalParam(name = DocumentReference.SP_RELATED_ID) TokenOrListParam relatedId,
            // Extension to ITI-66
            @OptionalParam(name = IAnyResource.SP_RES_ID) TokenParam resourceId,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        var searchParameters = Iti67SearchParameters.builder()
                .status(status)
                .indexed(indexed)
                .class_(class_)
                .type(type)
                .setting(setting)
                .period(period)
                .facility(facility)
                .event(event)
                .securityLabel(securityLabel)
                .format(format)
                .relatedId(relatedId)
                ._id(resourceId)
                .sortSpec(sortSpec)
                .includeSpec(includeSpec)
                .fhirContext(getFhirContext())
                .build();

        searchParameters.setAuthor(author);

        var patientChain = patient.getChain();
        if (Patient.SP_IDENTIFIER.equals(patientChain)) {
            searchParameters.setPatientIdentifier(patient.toTokenParam(getFhirContext()));
        } else if (patientChain == null || patientChain.isEmpty()) {
            searchParameters.setPatientReference(patient);
        }

        // Run down the route
        return requestBundleProvider(null, searchParameters, ResourceType.DocumentReference.name(),
                httpServletRequest, httpServletResponse, requestDetails);
    }

    /**
     * Handles DocumentReference Retrieve. This is not an actual part of the ITI-67 specification, but in the
     * context of restful FHIR IHE transaction it makes sense to be able to retrieve a DocumentReference by
     * its resource ID.
     *
     * @param id                  resource ID
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param requestDetails      request details
     * @return {@link DocumentReference} resource
     */
    @SuppressWarnings("unused")
    @Read(version = true, type = DocumentReference.class)
    public DocumentReference documentReferenceRetrieve(
            @IdParam IdType id,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        if (id == null) throw new InvalidRequestException("Must provide ID with READ request");
        // Run down the route
        return requestResource(id, null, DocumentReference.class,
                httpServletRequest, httpServletResponse, requestDetails);
    }
}
