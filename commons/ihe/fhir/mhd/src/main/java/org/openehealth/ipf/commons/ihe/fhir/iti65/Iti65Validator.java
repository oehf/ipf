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
import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.CustomValidationSupport;
import org.openehealth.ipf.commons.ihe.fhir.FhirUtils;
import org.openehealth.ipf.commons.ihe.fhir.FhirValidator;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;

import java.util.*;

/**
 * Validator for ITI-65 transactions.
 *
 *  @author Christian Ohr
 *  @since 3.2
 */
public class Iti65Validator implements FhirValidator {

    private static final IValidationSupport VALIDATION_SUPPORT = new CustomValidationSupport("profiles/MHD");

    // Prepare the required validator instances so that the structure defintions are not reloaded each time
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

    @Override
    public void validateResponse(FhirContext context, Object payload, Map<String, Object> parameters) {

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
        if (profiles.isEmpty() || !Iti65Constants.ITI65_TAG.getCode().equals(profiles.get(0).getValue())) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "Request bundle must have profile",
                    Iti65Constants.ITI65_TAG.getCode());
        }

    }

    /**
     * Verifies that bundle has expected content and consistent patient references
     *
     * @param bundle transaction bundle
     */
    protected void validateBundleConsistency(Bundle bundle) {

        Map<String, List<Bundle.BundleEntryComponent>> entries = FhirUtils.getBundleEntries(bundle);

        // Verify that the bundle has all required resources
        if (entries.getOrDefault(DocumentManifest.class.getSimpleName(), Collections.emptyList()).size() != 1) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "Request bundle must have exactly one DocumentManifest"
            );
        }
        if (entries.getOrDefault(DocumentManifest.class.getSimpleName(), Collections.emptyList()).isEmpty()) {
            throw FhirUtils.unprocessableEntity(
                    OperationOutcome.IssueSeverity.ERROR,
                    OperationOutcome.IssueType.INVALID,
                    null, null,
                    "Request bundle must have at least one DocumentReference"
            );
        }

        Set<String> references = new HashSet<>();
        entries.values().stream()
                .flatMap(Collection::stream)
                .map(Bundle.BundleEntryComponent::getResource)
                .forEach(resource -> {
                    Reference subject = null;
                    if (resource instanceof DocumentManifest) {
                        subject = ((DocumentManifest) resource).getSubject();
                    } else if (resource instanceof DocumentReference) {
                        subject = ((DocumentReference) resource).getSubject();
                    } else if (resource instanceof List_) {
                        subject = ((List_) resource).getSubject();
                    } else if (!(resource instanceof Binary)) {
                        throw FhirUtils.unprocessableEntity(
                                OperationOutcome.IssueSeverity.ERROR,
                                OperationOutcome.IssueType.INVALID,
                                null, null,
                                "Unexpected bundle component %s",
                                resource.getClass().getSimpleName()
                        );
                    }
                    if (subject != null) {
                        references.add(subject.getReference());
                    } else {
                        throw FhirUtils.unprocessableEntity(
                                OperationOutcome.IssueSeverity.ERROR,
                                OperationOutcome.IssueType.INVALID,
                                ErrorCode.UNKNOWN_PATIENT_ID.getOpcode(),
                                null,
                                "Empty Patient reference in resource %s",
                                resource
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
    }



}
