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

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Optional;

/**
 * Generic extractor of patient references, independent of the FHIR resource and version
 */
public class GenericPatientIdExtractor implements PatientIdExtractor {

    private static final String PATIENT = "Patient";
    private final FhirContext fhirContext;
    private final Class<? extends IBaseReference> referenceClass;

    @SuppressWarnings("unchecked")
    public GenericPatientIdExtractor(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        this.referenceClass = fhirContext.getElementDefinitions().stream()
                .map(BaseRuntimeElementDefinition::getImplementingClass)
                .filter(IBaseReference.class::isAssignableFrom)
                .map(c -> (Class<? extends IBaseReference>) c)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Should never happen: no IBaseReference implementation found"));
    }

    @Override
    public Optional<? extends IBaseReference> patientReferenceFromResource(IBaseResource resource) {
        if (resource != null) {
            var resourceDefinition = fhirContext.getResourceDefinition(resource);
            if (PATIENT.equals(resourceDefinition.getName())) {
                String id = resource.getIdElement().getIdPart();
                return id != null ? Optional.of(patientReference(id)) : Optional.empty();
            } else {
                try {
                    return SearchParameterUtil.getOnlyPatientSearchParamForResourceType(fhirContext, resourceDefinition.getName())
                            .map(RuntimeSearchParam::getPath)
                            .flatMap(path -> fhirContext.newFhirPath().evaluateFirst(resource, simplifyPath(path), IBaseReference.class));
                } catch (Exception e) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

    // There is probably a bug in HAPI FHIR for expressions like DocumentReference.subject.where(resolve() is Patient)
    // resolve() always returns null instead of the actual reference value, see FhirPathEngine#funcResolve()
    private String simplifyPath(String path) {
        int idx = path.indexOf(".where");
        return idx < 0 ? path : path.substring(0, idx);
    }

    @Override
    public Optional<String> patientReferenceParameterName(String resourceName) {
        if (!PATIENT.equals(resourceName)) {
            try {
                return SearchParameterUtil.getOnlyPatientSearchParamForResourceType(fhirContext, resourceName)
                        .map(RuntimeSearchParam::getName);
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.of("_id");
    }

    @Override
    public Optional<String> patientReferenceFromSearchParameter(RequestDetails requestDetails) {
        return patientReferenceParameterName(requestDetails.getResourceName())
                .map(name -> requestDetails.getParameters().get(name))
                .filter(s -> s.length > 0)
                .map(s -> s[0].startsWith(PATIENT) ? s[0].substring(PATIENT.length() + 1) : s[0]);
    }

    @Override
    public Optional<String> patientIdentifierParameterName(RequestDetails requestDetails) {
        if (!PATIENT.equals(requestDetails.getResourceName())) {
            return patientReferenceParameterName(requestDetails.getResourceName())
                    .map(s -> s + ".identifier");
        }
        return Optional.of("identifier");
    }

    @Override
    public Optional<String> patientIdentifierFromSearchParameter(RequestDetails requestDetails) {
        return patientIdentifierParameterName(requestDetails)
                .map(name -> requestDetails.getParameters().get(name))
                .filter(s -> s.length > 0)
                .map(s -> s[0]);
        //.map(s -> s[0].substring(s[0].indexOf("|") + 1));
    }

    private IBaseReference patientReference(String idPart) {
        try {
            return referenceClass.getConstructor(String.class).newInstance(PATIENT + "/" + idPart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


