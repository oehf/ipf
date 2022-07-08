/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.audit;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Optional;

/**
 * Interface used to extract patient references from FHIR resources or search parameters.
 * This is useful for obtaining patient context for auditing and other purposes.
 */
public interface PatientIdExtractor {

    /**
     * Returns the patient reference from a resource or {@link Optional#empty()} if the resource has none.
     *
     * @param resource FHIR resource
     * @return patient reference from the resource
     */
    Optional<? extends IBaseReference> patientReferenceFromResource(IBaseResource resource);

    /**
     * Returns the name of the search parameter that is used to restrict a search to a certain patient
     *
     * @param resourceName resource Name
     * @return the name of the patient search parameter
     */
    Optional<String> patientReferenceParameterName(String resourceName);

    Optional<String> patientIdentifierParameterName(RequestDetails requestDetails);

    /**
     * Returns the patient ID from a search parameter referencing a Patient resource or {@link Optional#empty()}
     * if the search has no such parameter or if it is empty. When a Patient resource is searched, the _id
     * parameter value will be returned.
     *
     * @param requestDetails HAPI FHIR request details
     * @return patient ID from the search parameters
     */
    Optional<String> patientReferenceFromSearchParameter(RequestDetails requestDetails);

    /**
     * Returns the patient domain identifier from  a search parameter or {@link Optional#empty()}
     * if the search has no such parameter or if it is empty. This is usually a chained token parameter
     * (i.e. subject.identifier=system|value), where this function will return the value.
     * When a Patient resource is searched, the identifier parameter value will be returned.
     *
     * @param requestDetails HAPI FHIR request details
     * @return patient identifier extension from the search parameters
     */
    Optional<String> patientIdentifierFromSearchParameter(RequestDetails requestDetails);
}