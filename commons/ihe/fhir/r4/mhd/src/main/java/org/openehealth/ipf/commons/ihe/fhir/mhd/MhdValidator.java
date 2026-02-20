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

package org.openehealth.ipf.commons.ihe.fhir.mhd;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.INTERACTION_REQUEST_VALIDATION_PROFILES;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.INTERACTION_RESPONSE_VALIDATION_PROFILES;

/**
 * Validator for MHD transactions.
 *
 * @author Christian Ohr
 * @since 4.8
 * <p>
 * TODO merge IgBasedInstanceValidator and MhdValidator
 */
public class MhdValidator extends FhirTransactionValidator.Support {

    private static final Logger log = LoggerFactory.getLogger(MhdValidator.class);
    private static final String STANDARD_PREFIX = "http://hl7.org/fhir/StructureDefinition/";
    private static final String MHD_PACKAGE_PATH = "classpath:META-INF/profiles/v423/ihe.iti.mhd.tgz";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation Failed";
    private static final String NO_VALIDATION_MESSAGE = "No validation was performed because no profile was configured for this transaction";

    private static final Set<OperationOutcome.IssueSeverity> CRITICAL_SEVERITIES = EnumSet.of(
        OperationOutcome.IssueSeverity.FATAL,
        OperationOutcome.IssueSeverity.ERROR
    );

    private final FhirValidator validator;

    public MhdValidator(FhirContext fhirContext) {
        log.debug("Initializing Validator for MHD");
        this.validator = createValidator(fhirContext);
        log.debug("Initialized Validator for MHD");
    }

    /**
     * Validates a FHIR request resource against configured validation profiles.
     * <p>
     * This method extracts the request validation profiles from the parameters and validates
     * that the payload resource conforms to one of the allowed profiles. If no profiles are
     * configured, an informational outcome is returned. If validation fails, an
     * {@link UnprocessableEntityException} is thrown.
     *
     * @param payload    the FHIR resource to validate (must not be {@code null})
     * @param parameters a map containing validation parameters, including the allowed request validation
     *                   profiles under the key {@link org.openehealth.ipf.commons.ihe.fhir.Constants#INTERACTION_REQUEST_VALIDATION_PROFILES}
     * @return an {@link OperationOutcome} containing the validation results, including any issues
     *         found during validation (never {@code null})
     * @throws UnprocessableEntityException if the resource fails validation against the configured profiles
     */
    @Override
    public OperationOutcome validateRequest(IBaseResource payload, Map<String, Object> parameters) {
        var requestValidationProfiles = extractValidationProfiles(parameters, INTERACTION_REQUEST_VALIDATION_PROFILES);
        return validateProfileConformance(payload, requestValidationProfiles);
    }

    /**
     * Validates a FHIR response resource against configured validation profiles.
     * <p>
     * This method extracts the response validation profiles from the parameters and validates
     * that the payload resource conforms to one of the allowed profiles. If no profiles are
     * configured, an informational outcome is returned. If validation fails, an
     * {@link UnprocessableEntityException} is thrown.
     *
     * @param payload    the FHIR resource to validate (must not be {@code null})
     * @param parameters a map containing validation parameters, including the allowed response validation
     *                   profiles under the key {@link org.openehealth.ipf.commons.ihe.fhir.Constants#INTERACTION_RESPONSE_VALIDATION_PROFILES}
     * @return an {@link OperationOutcome} containing the validation results, including any issues
     *         found during validation (never {@code null})
     * @throws UnprocessableEntityException if the resource fails validation against the configured profiles
     */
    @Override
    public OperationOutcome validateResponse(IBaseResource payload, Map<String, Object> parameters) {
        var responseValidationProfiles = extractValidationProfiles(parameters, INTERACTION_RESPONSE_VALIDATION_PROFILES);
        return validateProfileConformance(payload, responseValidationProfiles);
    }

    /**
     * Extracts validation profiles from parameters map.
     *
     * @param parameters the parameters map
     * @param key        the key to extract
     * @return set of validation profile URIs
     */
    @SuppressWarnings("unchecked")
    private Set<String> extractValidationProfiles(Map<String, Object> parameters, String key) {
        return (Set<String>) parameters.get(key);
    }

    /**
     * Creates and configures the FHIR validator with all necessary validation support.
     *
     * @param fhirContext the FHIR context
     * @return configured FHIR validator
     */
    private FhirValidator createValidator(FhirContext fhirContext) {
        var supportChain = createValidationSupportChain(fhirContext);
        var instanceValidator = createInstanceValidator(supportChain);

        return fhirContext.newValidator()
            .setValidateAgainstStandardSchema(false)
            .setValidateAgainstStandardSchematron(false)
            .setConcurrentBundleValidation(true)
            .registerValidatorModule(instanceValidator);
    }

    /**
     * Creates the validation support chain with all required validation supports.
     *
     * @param fhirContext the FHIR context
     * @return configured validation support chain
     */
    private ValidationSupportChain createValidationSupportChain(FhirContext fhirContext) {
        var supportChain = new ValidationSupportChain();
        supportChain.addValidationSupport(new DefaultProfileValidationSupport(fhirContext));
        supportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(fhirContext));
        supportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(fhirContext));
        supportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(fhirContext));
        addNpmPackageSupport(fhirContext, supportChain);
        return supportChain;
    }

    /**
     * Adds NPM package validation support to the support chain.
     *
     * @param fhirContext  the FHIR context
     * @param supportChain the validation support chain
     */
    private void addNpmPackageSupport(FhirContext fhirContext, ValidationSupportChain supportChain) {
        try {
            var npmPackageValidationSupport = new NpmPackageValidationSupport(fhirContext);
            npmPackageValidationSupport.loadPackageFromClasspath(MHD_PACKAGE_PATH);
            supportChain.addValidationSupport(npmPackageValidationSupport);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load MHD validation package from: " + MHD_PACKAGE_PATH, e);
        }
    }

    /**
     * Creates and configures the FHIR instance validator.
     *
     * @param supportChain the validation support chain
     * @return configured FHIR instance validator
     */
    private FhirInstanceValidator createInstanceValidator(ValidationSupportChain supportChain) {
        var instanceValidator = new FhirInstanceValidator(supportChain);
        instanceValidator.setNoTerminologyChecks(false);
        instanceValidator.setErrorForUnknownProfiles(true);
        instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Hint);
        instanceValidator.setAnyExtensionsAllowed(true);
        return instanceValidator;
    }

    /**
     * Validates that a FHIR resource conforms to one of the allowed profiles.
     *
     * @param resource           the FHIR resource to validate
     * @param allowedProfileUris set of allowed profile URIs
     * @return {@link OperationOutcome} containing validation results (never {@code null})
     */
    private OperationOutcome validateProfileConformance(IBaseResource resource, Set<String> allowedProfileUris) {
        if (allowedProfileUris == null || allowedProfileUris.isEmpty()) {
            return createNoValidationOutcome();
        }

        var profileUri = allowedProfileUris.iterator().next();
        return isStandardProfile(profileUri)
            ? validateStandardProfile(resource, profileUri)
            : validateCustomProfile(resource, allowedProfileUris);
    }

    /**
     * Checks if a profile URI is a standard FHIR profile.
     *
     * @param profileUri the profile URI to check
     * @return {@code true} if it's a standard profile, {@code false} otherwise
     */
    private boolean isStandardProfile(String profileUri) {
        return profileUri.startsWith(STANDARD_PREFIX);
    }

    /**
     * Validates a resource against a standard FHIR profile.
     *
     * @param resource   the FHIR resource to validate
     * @param profileUri the standard profile URI
     * @return {@link OperationOutcome} containing validation results
     */
    private OperationOutcome validateStandardProfile(IBaseResource resource, String profileUri) {
        var expectedResourceType = profileUri.substring(STANDARD_PREFIX.length());

        if (resource.fhirType().equals(expectedResourceType)) {
            return validatePayload(resource);
        }

        return handleOperationOutcome(createErrorOutcome(
            OperationOutcome.IssueType.INVALID,
            String.format("Resource shall be of type %s but was %s", expectedResourceType, resource.fhirType())
        ));
    }

    /**
     * Validates a resource against custom profiles.
     *
     * @param resource           the FHIR resource to validate
     * @param allowedProfileUris set of allowed profile URIs
     * @return {@link OperationOutcome} containing validation results
     */
    private OperationOutcome validateCustomProfile(IBaseResource resource, Set<String> allowedProfileUris) {
        var hasMatchingProfile = resource.getMeta().getProfile().stream()
            .map(IPrimitiveType::getValueAsString)
            .anyMatch(allowedProfileUris::contains);

        return hasMatchingProfile ?
            validatePayload(resource) :
            handleOperationOutcome(createErrorOutcome(
                OperationOutcome.IssueType.REQUIRED,
                "Resource shall declare one of the profiles: " + String.join(", ", allowedProfileUris)
            ));

    }

    /**
     * Performs the actual validation of the FHIR resource payload.
     *
     * @param resource the FHIR resource to validate
     * @return {@link OperationOutcome} containing validation results
     * @throws UnprocessableEntityException if validation fails
     */
    private OperationOutcome validatePayload(IBaseResource resource) {
        var validationResult = validator.validateWithResult(resource);

        if (!validationResult.isSuccessful()) {
            throw FhirUtils.exception(
                UnprocessableEntityException::new,
                validationResult.toOperationOutcome(),
                VALIDATION_FAILED_MESSAGE
            );
        }

        return handleOperationOutcome((OperationOutcome) validationResult.toOperationOutcome());
    }

    /**
     * Creates an OperationOutcome with an error issue.
     *
     * @param issueType   the type of issue
     * @param diagnostics the diagnostic message
     * @return {@link OperationOutcome} with error issue
     */
    private OperationOutcome createErrorOutcome(OperationOutcome.IssueType issueType, String diagnostics) {
        return new OperationOutcome()
            .addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                .setCode(issueType)
                .setDiagnostics(diagnostics));
    }

    /**
     * Creates an OperationOutcome indicating no validation was performed.
     *
     * @return {@link OperationOutcome} with informational issue
     */
    private OperationOutcome createNoValidationOutcome() {
        return new OperationOutcome()
            .addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                .setSeverity(OperationOutcome.IssueSeverity.INFORMATION)
                .setCode(OperationOutcome.IssueType.INFORMATIONAL)
                .setDiagnostics(NO_VALIDATION_MESSAGE));
    }

    /**
     * Handles the operation outcome by checking for critical issues and throwing exception if found.
     *
     * @param outcome the operation outcome to handle
     * @return the same operation outcome if no critical issues found
     * @throws UnprocessableEntityException if critical issues are found
     */
    private OperationOutcome handleOperationOutcome(OperationOutcome outcome) {
        if (outcome == null) {
            return null;
        }

        var hasCriticalIssue = outcome.getIssue().stream()
            .map(OperationOutcome.OperationOutcomeIssueComponent::getSeverity)
            .anyMatch(CRITICAL_SEVERITIES::contains);

            if (hasCriticalIssue) {
                // Sort issues by severity for better readability in error messages
                var sortedIssues = outcome.getIssue().stream()
                    .sorted(Comparator.comparing(OperationOutcome.OperationOutcomeIssueComponent::getSeverity))
                    .toList();
                outcome.setIssue(sortedIssues);
                throw FhirUtils.exception(UnprocessableEntityException::new, outcome, VALIDATION_FAILED_MESSAGE);
            }
        return outcome;
    }
}
