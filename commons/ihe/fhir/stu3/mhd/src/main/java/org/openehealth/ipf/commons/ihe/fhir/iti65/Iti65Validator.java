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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.DocumentManifest;
import org.hl7.fhir.dstu3.model.DocumentReference;
import org.hl7.fhir.dstu3.model.ListResource;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.CustomValidationSupport;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Validator for ITI-65 transactions.
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti65Validator extends FhirTransactionValidator.Support {

    private final FhirContext fhirContext;
    private final Map<Class<?>, FhirInstanceValidator> validators;


    public Iti65Validator(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        IValidationSupport validationSupport = new CustomValidationSupport(fhirContext, "META-INF/profiles/MHD-");
        validators = new HashMap<>();
        validators.put(DocumentManifest.class, fhirInstanceValidator(validationSupport));
        validators.put(DocumentReference.class, fhirInstanceValidator(validationSupport));
        validators.put(ListResource.class, fhirInstanceValidator(validationSupport));
    }

    private FhirInstanceValidator fhirInstanceValidator(IValidationSupport validationSupport) {
        FhirInstanceValidator fhirInstanceValidator = new FhirInstanceValidator(validationSupport);
        fhirInstanceValidator.setNoTerminologyChecks(true);
        fhirInstanceValidator.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Hint);
        fhirInstanceValidator.setErrorForUnknownProfiles(true);
        return fhirInstanceValidator;
    }

    @Override
    public void validateRequest(Object payload, Map<String, Object> parameters) {
        var transactionBundle = (Bundle) payload;
        validateTransactionBundle(transactionBundle);
        validateBundleConsistency(transactionBundle);

        for (var entry : transactionBundle.getEntry()) {
            Class<? extends IBaseResource> clazz = entry.getResource().getClass();
            if (validators.containsKey(clazz)) {
                var validator = fhirContext.newValidator();
                validator.registerValidatorModule(validators.get(clazz));
                var validationResult = validator.validateWithResult(entry.getResource());
                if (!validationResult.isSuccessful()) {
                    var operationOutcome = validationResult.toOperationOutcome();
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
        var profiles = bundle.getMeta().getProfile();
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

        var entries = FhirUtils.getBundleEntries(bundle);

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

        Set<String> patientReferences = new HashSet<>();
        Set<String> expectedBinaryFullUrls = new HashSet<>();
        Set<String> expectedReferenceFullUrls = new HashSet<>();
        entries.values().stream()
                .flatMap(Collection::stream)
                .map(Bundle.BundleEntryComponent::getResource)
                .forEach(resource -> {
                    if (resource instanceof DocumentManifest) {
                        var dm = (DocumentManifest) resource;
                        for (var content : dm.getContent()) {
                            try {
                                expectedReferenceFullUrls.add(content.getPReference().getReference());
                            } catch (Exception ignored) {
                            }
                        }
                        patientReferences.add(getSubjectReference(resource, r -> dm.getSubject()));
                    } else if (resource instanceof DocumentReference) {
                        var dr = (DocumentReference) resource;
                        for (var content : dr.getContent()) {
                            var url = content.getAttachment().getUrl();
                            if (!url.startsWith("http")) {
                                expectedBinaryFullUrls.add(url);
                            }
                        }
                        patientReferences.add(getSubjectReference(resource, r -> ((DocumentReference) r).getSubject()));
                    } else if (resource instanceof ListResource) {
                        patientReferences.add(getSubjectReference(resource, r -> ((ListResource) r).getSubject()));
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

        if (patientReferences.size() != 1) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    ErrorCode.PATIENT_ID_DOES_NOT_MATCH.getOpcode(),
                    null,
                    "Inconsistent patient references %s",
                    patientReferences
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
                    expectedReferenceFullUrls
            );
        }

    }


    private String getSubjectReference(Resource resource, Function<Resource, Reference> f) {
        var reference = f.apply(resource);
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
            var patient = (Patient) reference.getResource();
            return patient.getIdentifier().get(0).getValue();
        }
        return reference.getReference();
    }


}
