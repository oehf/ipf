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

package org.openehealth.ipf.commons.ihe.fhir.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;

import java.util.Comparator;

/**
 * Validator which uses Implementation Guides to validate FHIR resources.
 *
 * @author Dmytro Rud
 */
abstract public class IgBasedInstanceValidator extends FhirTransactionValidator.Support {

    private static final String STANDARD_PREFIX = "http://hl7.org/fhir/StructureDefinition/";
    private final FhirContext fhirContext;

    protected IgBasedInstanceValidator(FhirContext fhirContext) {
        this.fhirContext = Validate.notNull(fhirContext, "FHIR context must be provided");
    }

    /**
     * @param resource FHIR resource to validate.
     * @return {@link OperationOutcome} containing or not containing validation errors (never <code>null</code>).
     */
    protected OperationOutcome validateProfileConformance(Resource resource, String profileUri) {

        if (profileUri.startsWith(STANDARD_PREFIX)) {
            String expectedResourceType = profileUri.substring(STANDARD_PREFIX.length());
            if (resource.fhirType().equals(expectedResourceType)) {
                return doValidate(resource);
            } else {
                return new OperationOutcome()
                        .addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                                .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                                .setCode(OperationOutcome.IssueType.INVALID)
                                .setDiagnostics("Resource shall be of type " + expectedResourceType));
            }
        } else {
            for (CanonicalType profile : resource.getMeta().getProfile()) {
                if (profile.equals(profileUri)) {
                    return doValidate(resource);
                }
            }
        }

        return new OperationOutcome()
                .addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                        .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                        .setCode(OperationOutcome.IssueType.REQUIRED)
                        .setDiagnostics("Resource shall declare profile " + profileUri));
    }

    private OperationOutcome doValidate(Resource resource) {
        FhirValidator validator = fhirContext.newValidator();
        ValidationResult validationResult = validator.validateWithResult(resource);
        return validationResult.isSuccessful()
                ? new OperationOutcome()
                : (OperationOutcome) validationResult.toOperationOutcome();
    }

    protected void handleOperationOutcome(OperationOutcome outcome) {
        if (outcome == null) {
            return;
        }
        outcome.getIssue().sort(Comparator.comparing(OperationOutcome.OperationOutcomeIssueComponent::getSeverity));
        for (OperationOutcome.OperationOutcomeIssueComponent issue : outcome.getIssue()) {
            if ((issue.getSeverity() == OperationOutcome.IssueSeverity.FATAL) || (issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR)) {
                throw FhirUtils.exception(UnprocessableEntityException::new, outcome, "Validation Failed");
            }
        }
    }

}
