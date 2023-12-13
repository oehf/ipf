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
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Validator for ITI-65 transactions.
 *
 * THIS does not work properly yet!
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti65Validator extends FhirTransactionValidator.Support {

    private static final Logger LOG = LoggerFactory.getLogger(Iti65Validator.class);
    private static final String IHE_PROFILE_PREFIX = "http://ihe.net/fhir/StructureDefinition/";

    private final FhirContext fhirContext;
    private IValidationSupport validationSupport;

    public Iti65Validator(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        LOG.info("Initializing Validator for ITI-65 bundles");
        validationSupport = loadStructureDefinitions(new DefaultProfileValidationSupport(fhirContext), "Minimal");
        validationSupport = loadStructureDefinitions(validationSupport, "Comprehensive");
        LOG.info("Initialized Validator for ITI-65 bundles");
    }

    @Override
    public void validateRequest(Object payload, Map<String, Object> parameters) {

        var transactionBundle = (Bundle) payload;

        validateBundleConsistency(transactionBundle);

        var validator = fhirContext.newValidator();
        validator.setValidateAgainstStandardSchema(false);
        validator.setValidateAgainstStandardSchematron(false);
        var instanceValidator = new FhirInstanceValidator(validationSupport);
        instanceValidator.setNoTerminologyChecks(false);
        instanceValidator.setErrorForUnknownProfiles(true);
        instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Hint);
        validator.registerValidatorModule(instanceValidator);
        var validationResult = validator.validateWithResult(transactionBundle);
        if (!validationResult.isSuccessful()) {
            var operationOutcome = validationResult.toOperationOutcome();
            throw FhirUtils.exception(UnprocessableEntityException::new, operationOutcome, "Validation Failed");
        }
    }

    public ValidationSupportChain loadStructureDefinitions(IValidationSupport baseValidationSupport, String kind) {
        var validationSupport = new PrePopulatedValidationSupport(fhirContext);
        var supportChain = new ValidationSupportChain(
                validationSupport,
                baseValidationSupport,
                new InMemoryTerminologyServerValidationSupport(baseValidationSupport.getFhirContext()),
                new CommonCodeSystemsTerminologyService(baseValidationSupport.getFhirContext()));
        findProfile(supportChain, String.format("IHE_MHD_%s_List", kind))
                .ifPresent(validationSupport::addStructureDefinition);
        findProfile(supportChain, String.format("IHE_MHD_Provide_%s_DocumentReference", kind))
                .ifPresent(validationSupport::addStructureDefinition);
        findProfile(supportChain, String.format("IHE_MHD_Query_%s_DocumentReference", kind))
                .ifPresent(validationSupport::addStructureDefinition);
        findProfile(supportChain, String.format("IHE_MHD_%s_DocumentManifest", kind))
                .ifPresent(validationSupport::addStructureDefinition);
        findProfile(supportChain, String.format("IHE_MHD_Provide_%s_DocumentBundle", kind))
                .ifPresent(validationSupport::addStructureDefinition);
        return supportChain;
    }

    private Optional<StructureDefinition> findProfile(
            ValidationSupportChain snaphotGenerationSupport,
            String name) {
        var path = "META-INF/profiles/320/" + name + ".xml";
        var url = IHE_PROFILE_PREFIX + name;
        var is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (is != null) {
            try (var scanner = new Scanner(is, StandardCharsets.UTF_8)) {
                var profileText = scanner.useDelimiter("\\A").next();
                var parser = EncodingEnum.detectEncodingNoDefault(profileText).newParser(fhirContext);
                var structureDefinition = parser.parseResource(StructureDefinition.class, profileText);
                return Optional.of(structureDefinition.hasSnapshot() ? structureDefinition
                        : (StructureDefinition) new SnapshotGeneratingValidationSupport(fhirContext).generateSnapshot(
                                new ValidationSupportContext(snaphotGenerationSupport), structureDefinition, url, url,
                                name));
            }
        }
        return Optional.empty();
    }


    /**
     * Verifies that bundle has expected content and consistent patient references
     *
     * @param bundle transaction bundle
     */
    protected void validateBundleConsistency(Bundle bundle) {

        var entries = FhirUtils.getBundleEntries(bundle);

        // Verify that the bundle has all required resources
        // This should be done by the StructureDefinition, but apparently HAPI has a problem with slices...

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


        var patientReferences = new HashSet<String>();
        var expectedBinaryFullUrls = new HashSet<String>();
        var expectedReferenceFullUrls = new HashSet<String>();
        entries.values().stream()
                .flatMap(Collection::stream)
                .map(Bundle.BundleEntryComponent::getResource)
                .forEach(resource -> {
                    if (resource instanceof DocumentManifest) {
                        var dm = (DocumentManifest) resource;
                        for (var content : dm.getContent()) {
                            try {
                                expectedReferenceFullUrls.add(content.getReference());
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
