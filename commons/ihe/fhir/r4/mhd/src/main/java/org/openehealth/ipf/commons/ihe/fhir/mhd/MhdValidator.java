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
import org.hl7.fhir.common.hapi.validation.support.*;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

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
        supportChain.addValidationSupport(new DefaultProfileValidationSupport(fhirContext));
        supportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(fhirContext));
        supportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(fhirContext));
        supportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(fhirContext));

        try {
            var npmPackageValidationSupport = new NpmPackageValidationSupport(fhirContext);
            npmPackageValidationSupport.loadPackageFromClasspath("classpath:META-INF/profiles/v421/ihe.iti.mhd.tgz");
            supportChain.addValidationSupport(npmPackageValidationSupport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var instanceValidator = new FhirInstanceValidator(supportChain);
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

    @Override
    public void validateResponse(Object payload, Map<String, Object> parameters) {
        var resource = (IBaseResource) payload;
        var validationResult = validator.validateWithResult(resource);
        if (!validationResult.isSuccessful()) {
            var operationOutcome = validationResult.toOperationOutcome();
            throw FhirUtils.exception(UnprocessableEntityException::new, operationOutcome, "Validation Failed");
        }
    }
}
