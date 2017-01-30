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

package org.openehealth.ipf.commons.ihe.fhir.iti65;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.instance.hapi.validation.IValidationSupport;
import org.hl7.fhir.instance.model.Binary;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.DocumentManifest;
import org.hl7.fhir.instance.model.DocumentReference;
import org.hl7.fhir.instance.model.List_;
import org.hl7.fhir.instance.model.OperationOutcome;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.ResourceType;
import org.hl7.fhir.instance.model.UriType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.CustomValidationSupport;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.FhirUtils;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Validator for ITI-65 transactions.
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class Iti65Validator extends FhirTransactionValidator.Support {

    private static final IValidationSupport VALIDATION_SUPPORT = new CustomValidationSupport("profiles/MHD");

    // Prepare the required validator instances so that the structure definitions are not reloaded each time
    private static Map<Class<?>, FhirInstanceValidator> VALIDATORS = new HashMap<>();

    static {
        VALIDATORS.put(DocumentManifest.class, new FhirInstanceValidator(VALIDATION_SUPPORT));
        VALIDATORS.put(DocumentReference.class, new FhirInstanceValidator(VALIDATION_SUPPORT));
        VALIDATORS.put(List_.class, new FhirInstanceValidator(VALIDATION_SUPPORT));
    }

    @Override
    public void validateRequest(FhirContext context, Object payload, Map<String, Object> parameters) {
        Bundle transactionBundle = (Bundle) payload;
        validateTransactionBundle(transactionBundle);
        validateBundleConsistency(transactionBundle);

        for (Bundle.BundleEntryComponent entry : transactionBundle.getEntry()) {
            Class<? extends IBaseResource> clazz = entry.getResource().getClass();
            if (VALIDATORS.containsKey(clazz)) {
                ca.uhn.fhir.validation.FhirValidator validator = context.newValidator();
                validator.registerValidatorModule(VALIDATORS.get(clazz));
                ValidationResult validationResult = validator.validateWithResult(entry.getResource());
                if (!validationResult.isSuccessful()) {
                    IBaseOperationOutcome operationOutcome = validationResult.toOperationOutcome();
                    throw FhirUtils.exception(UnprocessableEntityException::new, operationOutcome, "Validation Failed");
                }
            }
        }
    }


    /**
     * Validates bundle type, meta data and consistency of contained resources
     *
     * @param bundle transaction bundle
     */
    protected void validateTransactionBundle(Bundle bundle) {
        if (!Bundle.BundleType.TRANSACTION.equals(bundle.getType())) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "Bundle type must be %s, but was %s",
                    Bundle.BundleType.TRANSACTION.toCode(), bundle.getType().toCode());
        }
        List<UriType> profiles = bundle.getMeta().getProfile();
        if (profiles.isEmpty() || !Iti65Constants.ITI65_PROFILE.equals(profiles.get(0).getValue())) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "Request bundle must have profile",
                    Iti65Constants.ITI65_PROFILE);
        }

    }

    /**
     * Verifies that bundle has expected content and consistent patient references
     *
     * @param bundle transaction bundle
     */
    protected void validateBundleConsistency(Bundle bundle) {

        Map<ResourceType, List<Bundle.BundleEntryComponent>> entries = FhirUtils.getBundleEntries(bundle);

        // Verify that the bundle has all required resources
        if (entries.getOrDefault(ResourceType.DocumentManifest, Collections.emptyList()).size() != 1) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "Request bundle must have exactly one DocumentManifest"
            );
        }
        if (entries.getOrDefault(ResourceType.DocumentReference, Collections.emptyList()).isEmpty()) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "Request bundle must have at least one DocumentReference"
            );
        }

        Set<String> references = new HashSet<>();
        Set<String> expectedBinaryFullUrls = new HashSet<>();
        Set<String> expectedReferenceFullUrls = new HashSet<>();
        entries.values().stream()
                .flatMap(Collection::stream)
                .map(Bundle.BundleEntryComponent::getResource)
                .forEach(resource -> {
                    if (resource instanceof DocumentManifest) {
                        DocumentManifest dm = (DocumentManifest) resource;
                        for (DocumentManifest.DocumentManifestContentComponent content : dm.getContent()) {
                            try {
                                expectedReferenceFullUrls.add(content.getPReference().getReference());
                            } catch (Exception ignored) {
                            }
                        }
                        references.add(getSubjectReference(resource, r -> dm.getSubject()));
                    } else if (resource instanceof DocumentReference) {
                        DocumentReference dr = (DocumentReference) resource;
                        for (DocumentReference.DocumentReferenceContentComponent content : dr.getContent()) {
                            expectedBinaryFullUrls.add(content.getAttachment().getUrl());
                        }
                        references.add(getSubjectReference(resource, r -> ((DocumentReference) r).getSubject()));
                    } else if (resource instanceof List_) {
                        references.add(getSubjectReference(resource, r -> ((List_) r).getSubject()));
                    } else if (!(resource instanceof Binary)) {
                        throw FhirUtils.unprocessableEntity(
                                OperationOutcome.IssueSeverity.ERROR,
                                OperationOutcome.IssueType.INVALID,
                                null, null,
                                "Unexpected bundle component %s",
                                resource.getClass().getSimpleName()
                        );
                    }
                });

        if (references.size() != 1) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    ErrorCode.PATIENT_ID_DOES_NOT_MATCH.getOpcode(),
                    null,
                    "Inconsistent patient references %s",
                    references
            );
        }

        entries.values().stream()
                .flatMap(Collection::stream)
                .forEach(entry -> {
                    if (ResourceType.DocumentReference == entry.getResource().getResourceType()) {
                        if (!expectedReferenceFullUrls.remove(entry.getFullUrl())) {
                            throw FhirUtils.unprocessableEntity(
                                    OperationOutcome.IssueSeverity.ERROR,
                                    OperationOutcome.IssueType.INVALID,
                                    null, null,
                                    "DocumentReference with URL %s is not referenced by any DocumentManifest",
                                    entry.getFullUrl()
                            );
                        }
                    } else if (ResourceType.Binary == entry.getResource().getResourceType()) {
                        if (!expectedBinaryFullUrls.remove(entry.getFullUrl())) {
                            throw FhirUtils.unprocessableEntity(
                                    OperationOutcome.IssueSeverity.ERROR,
                                    OperationOutcome.IssueType.INVALID,
                                    null, null,
                                    "Binary with URL %s is not referenced by any DocumentReference",
                                    entry.getFullUrl()
                            );
                        }
                    }
                });

        if (!expectedBinaryFullUrls.isEmpty()) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "Binary with URLs %s referenced, but not present in this bundle",
                    expectedBinaryFullUrls
            );
        }

        if (!expectedReferenceFullUrls.isEmpty()) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "DocumentReference with URLs %s referenced, but not present in this bundle",
                    expectedBinaryFullUrls
            );
        }

    }


    private String getSubjectReference(Resource resource, Function<Resource, Reference> f) {
        Reference reference = f.apply(resource);
        if (reference == null) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    ErrorCode.UNKNOWN_PATIENT_ID.getOpcode(),
                    null,
                    "Empty Patient reference in resource %s",
                    resource
            );
        }
        // Could be contained resources
        if (reference.getResource() != null) {
            Patient patient = (Patient) reference.getResource();
            return patient.getIdentifier().get(0).getValue();
        }
        return reference.getReference();
    }


}
