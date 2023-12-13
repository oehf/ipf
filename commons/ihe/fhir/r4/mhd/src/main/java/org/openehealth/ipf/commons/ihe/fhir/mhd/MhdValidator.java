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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.common.hapi.validation.support.*;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validator for MHD transactions.
 *
 * @author Christian Ohr
 * @since 4.8
 */
public class MhdValidator extends FhirTransactionValidator.Support {

    private static final Logger log = LoggerFactory.getLogger(MhdValidator.class);

    private final FhirValidator validator;

    public MhdValidator(FhirContext fhirContext) {
        log.info("Initializing Validator for MHD");
        var supportChain = new ValidationSupportChain();
        var rootValidator = new DefaultProfileValidationSupport(fhirContext);
        supportChain.addValidationSupport(rootValidator);
        supportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(fhirContext));
        supportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(fhirContext));

        var prePopulatedSupport = new PrePopulatedValidationSupport(fhirContext);
        loadStructureDefinitions(rootValidator).forEach(prePopulatedSupport::addStructureDefinition);
        loadValueSets(fhirContext).forEach(prePopulatedSupport::addValueSet);
        loadCodeSystems(fhirContext).forEach(prePopulatedSupport::addCodeSystem);
        supportChain.addValidationSupport(prePopulatedSupport);
        var instanceValidator = new FhirInstanceValidator(new CachingValidationSupport(supportChain));
        instanceValidator.setNoTerminologyChecks(false);
        instanceValidator.setErrorForUnknownProfiles(true);
        instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Hint);
        instanceValidator.setAnyExtensionsAllowed(true);

        validator = fhirContext.newValidator()
            .setValidateAgainstStandardSchema(false)
            .setValidateAgainstStandardSchematron(false)
            .setConcurrentBundleValidation(true)
            .registerValidatorModule(instanceValidator);

        log.info("Initialized Validator for MHD bundles");
    }

    @Override
    public void validateRequest(Object payload, Map<String, Object> parameters) {
        var resource = (IBaseResource) payload;
        var validationResult = validator.validateWithResult(resource);
        if (!validationResult.isSuccessful()) {
            var operationOutcome = validationResult.toOperationOutcome();
            throw FhirUtils.exception(UnprocessableEntityException::new, operationOutcome, "Validation Failed");
        }
    }

    private Collection<StructureDefinition> loadStructureDefinitions(IValidationSupport rootValidator) {
        return Arrays.stream(MhdProfile.values())
            .map(profile -> loadStructureDefinition(
                profile.getStructureDefinitionResourceName(),
                rootValidator,
                profile.getUrl(),
                profile.getStructureDefinitionName()))
            .collect(Collectors.toList());
    }

    private StructureDefinition loadStructureDefinition(String fileName,
                                                        IValidationSupport rootValidation,
                                                        String url,
                                                        String structureDefinitionName) {
        var structureDefinition = loadResource(fileName, StructureDefinition.class, rootValidation.getFhirContext());
        return structureDefinition.hasSnapshot() ?
            structureDefinition :
            (StructureDefinition) new SnapshotGeneratingValidationSupport(rootValidation.getFhirContext()).generateSnapshot(
                new ValidationSupportContext(rootValidation),
                structureDefinition,
                url,
                url,
                structureDefinitionName);
    }

    private Collection<ValueSet> loadValueSets(FhirContext fhirContext) {
        var valueSets = new ArrayList<ValueSet>();
        valueSets.add(loadResource("ValueSet-DocumentReferenceStats.xml", ValueSet.class, fhirContext));
        valueSets.add(loadResource("ValueSet-formatcode.xml", ValueSet.class, fhirContext));
        valueSets.add(loadResource("ValueSet-MHDlistTypesVS.xml", ValueSet.class, fhirContext));
        valueSets.add(loadResource("ValueSet-MHDprovideFolderActions.xml", ValueSet.class, fhirContext));
        valueSets.add(loadResource("ValueSet-MHDprovidePatientActions.xml", ValueSet.class, fhirContext));
        return valueSets;
    }

    private Collection<CodeSystem> loadCodeSystems(FhirContext fhirContext) {
        var codeSystems = new ArrayList<CodeSystem>();
        codeSystems.add(loadResource("CodeSystem-MHDlistTypes.xml", CodeSystem.class, fhirContext));
        return codeSystems;
    }

    private <T extends IBaseResource> T loadResource(String fileName, Class<T> resourceClass, FhirContext fhirContext) {
        var prefix = "META-INF/profiles/v421/";
        var path = prefix + fileName;
        try (var is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            var parser = fhirContext.newXmlParser();
            return parser.parseResource(resourceClass, is);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
