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

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openehealth.ipf.commons.ihe.core.Constants.INTERACTION_ID_NAME;

/**
 * <p>
 * Factory class for creating validating processors for FHIR resources. For
 * schematron validation you need to have the phloc-schematron library in
 * the classpath.
 * </p>
 * <p>
 * The FHIR validators are used as processors, e.g.:
 * <pre>
 *     ...
 *     .process(FhirResourceValidators.itiRequestValidator())
 *     ...
 * </pre>
 * An instance of {@link UnprocessableEntityException} is thrown if validation
 * fails. A FHIR Consumer will automatically process the exception for a 400
 * response.
 * </p>
 * <p>
 * Validation is executed depending on the presence of {@link FhirContext} in the message header.
 * The kind of validation is determined by the integer value in the {@link #VALIDATION_MODE}
 * message header, i.e. which of the following bits are set:
 * <ul>
 * <li>header not present : schema validation</li>
 * <li>{@link FhirCamelValidators#OFF} (0)        : no validation</li>
 * <li>{@link FhirCamelValidators#SCHEMA} (1)     : schema validation</li>
 * <li>{@link FhirCamelValidators#SCHEMATRON} (2) : schematron validation</li>
 * <li>{@link FhirCamelValidators#MODEL} (4)      : resource model validation</li>
 * </ul>
 * </p>
 * <p>
 * Higher value result in an higher performance impact due to the validation. Model-based validation can slow down
 * processing significantly.
 * </p>
 *
 * @author Christian Ohr
 * @since 3.2
 */
public final class FhirCamelValidators {

    private static final Logger LOG = LoggerFactory.getLogger(FhirCamelValidators.class);

    private FhirCamelValidators() {
    }

    public static String VALIDATION_MODE = "fhir.validation.mode";

    public static int MODEL = 4;
    public static int SCHEMATRON = 2;
    public static int SCHEMA = 1;
    public static int OFF = 0;

    static boolean isValidateSchema(int actualMode) {
        return (actualMode & SCHEMA) == SCHEMA;
    }

    static boolean isValidateSchematron(int actualMode) {
        return (actualMode & SCHEMATRON) == SCHEMATRON;
    }

    static boolean isValidateModel(int actualMode) {
        return (actualMode & MODEL) == MODEL;
    }

    /**
     * @return instance of FHIR validator processor that checks the FHIR resource
     * belonging to the request of the FHIR transaction
     */
    public static Processor itiRequestValidator() {
        return exchange -> {
            FhirContext context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
            if (context != null) {
                Boolean executeValidation = exchange.getIn().getHeader(ValidatorAdapter.NEED_VALIDATION_HEADER_NAME, Boolean.class);
                if (executeValidation == null || executeValidation) {
                    Integer mode = exchange.getIn().getHeader(VALIDATION_MODE, Integer.class);
                    if (mode == null) mode = SCHEMA;
                    if (isValidateSchema(mode) || isValidateSchematron(mode)) {
                        validate(exchange, context, isValidateSchema(mode), isValidateSchematron(mode));
                    }
                    if (isValidateModel(mode)) {
                        FhirInteractionId fhirInteractionId = exchange.getIn().getHeader(INTERACTION_ID_NAME, FhirInteractionId.class);
                        if (fhirInteractionId != null) {
                            FhirTransactionValidator validator = fhirInteractionId.getFhirTransactionConfiguration().getFhirValidator();
                            validator.validateRequest(context, exchange.getIn().getBody(), exchange.getIn().getHeaders());
                        } else {
                            LOG.warn("Could not validate request because FHIR Transaction ID is unknown");
                        }
                    }
                }
            } else {
                LOG.warn("Could not validate request because FHIR Context is unknown");
            }
        };
    }

    /**
     * @return instance of FHIR validator processor that checks the FHIR resource
     * belonging to the response of the FHIR transaction
     */
    public static Processor itiResponseValidator() {
        return exchange -> {
            FhirContext context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
            if (context != null) {
                int mode = exchange.getIn().getHeader(VALIDATION_MODE, Integer.class);
                if (isValidateSchema(mode) || isValidateSchematron(mode)) {
                    validate(exchange, context, isValidateSchema(mode), isValidateSchematron(mode));
                }
                if (isValidateModel(mode)) {
                    FhirInteractionId fhirInteractionId = exchange.getIn().getHeader(INTERACTION_ID_NAME, FhirInteractionId.class);
                    if (fhirInteractionId != null) {
                        FhirTransactionValidator validator = fhirInteractionId.getFhirTransactionConfiguration().getFhirValidator();
                        validator.validateResponse(context, exchange.getIn().getBody(), exchange.getIn().getHeaders());
                    } else {
                        LOG.warn("Could not validate request because FHIR Transaction ID is unknown");
                    }
                }
            } else {
                LOG.warn("Could not validate request because FHIR Context is unknown");
            }
        };
    }

    private static void validate(Exchange exchange, FhirContext context, boolean checkSchema, boolean checkSchematron) throws Exception {
        ValidationResult result = getValidator(context, checkSchema, checkSchematron)
                .validateWithResult(exchange.getIn().getBody(IBaseResource.class));
        if (!result.isSuccessful()) {
            IBaseOperationOutcome outcome = result.toOperationOutcome();
            LOG.debug("FHIR Validation failed with outcome {}", outcome);
            throw new UnprocessableEntityException("FHIR Validation Error", outcome);
        } else {
            LOG.debug("FHIR Validation succeeded");
        }
    }

    private static FhirValidator getValidator(FhirContext context, boolean checkSchema, boolean checkSchematron) {
        FhirValidator validator = context.newValidator();
        validator.setValidateAgainstStandardSchema(checkSchema);
        // Bug in HAPI: setting to false already looks up classpath for schematron lib
        if (checkSchematron) validator.setValidateAgainstStandardSchematron(true);
        return validator;
    }
}
