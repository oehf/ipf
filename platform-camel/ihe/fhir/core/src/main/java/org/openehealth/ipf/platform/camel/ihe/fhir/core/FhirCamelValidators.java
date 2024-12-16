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
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    private static final Logger log = LoggerFactory.getLogger(FhirCamelValidators.class);

    private FhirCamelValidators() {
    }

    public static final String VALIDATION_MODE = "fhir.validation.mode";

    public static final int MODEL = 4;
    public static final int SCHEMATRON = 2;
    public static final int SCHEMA = 1;
    public static final int OFF = 0;

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
        return exchange -> validateMessage(exchange, true);
    }

    /**
     * @return instance of FHIR validator processor that checks the FHIR resource
     * belonging to the response of the FHIR transaction
     */
    public static Processor itiResponseValidator() {
        return exchange -> validateMessage(exchange, false);
    }

    private static void validateMessage(Exchange exchange, boolean isRequest) {
        var context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
        if (context != null) {
            var body = exchange.getIn().getBody();
            if (body instanceof List list) {
                for (var bodyPart : list) {
                    validateIfResource(exchange, context, bodyPart, isRequest);
                }
            } else {
                validateIfResource(exchange, context, body, isRequest);
            }
        } else {
            log.warn("Could not validate {} because FHIR Context is unknown", isRequest ? "request" : "response");
        }
    }

    private static void validateIfResource(Exchange exchange, FhirContext context, Object payload, boolean isRequest) {
        if (payload instanceof IBaseResource resource) {
            int mode = exchange.getIn().getHeader(VALIDATION_MODE, Integer.class);
            if (isValidateSchema(mode) || isValidateSchematron(mode)) {
                validateSchema(context, resource, isValidateSchema(mode), isValidateSchematron(mode));
            }
            if (isValidateModel(mode)) {
                validateModel(exchange, context, resource, isRequest);
            }
        }
    }

    private static void validateSchema(FhirContext context, IBaseResource resource, boolean checkSchema, boolean checkSchematron) {
        var result = getValidator(context, checkSchema, checkSchematron).validateWithResult(resource);
        if (!result.isSuccessful()) {
            var outcome = result.toOperationOutcome();
            log.debug("FHIR validation failed with outcome {}", outcome);
            throw new UnprocessableEntityException("FHIR validation error", outcome);
        } else {
            log.debug("FHIR validation succeeded");
        }
    }

    private static void validateModel(Exchange exchange, FhirContext context, IBaseResource resource, boolean isRequest) {
        var fhirInteractionId = exchange.getIn().getHeader(INTERACTION_ID_NAME, FhirInteractionId.class);
        if (fhirInteractionId != null) {
            var validator = fhirInteractionId.getFhirTransactionConfiguration().getFhirValidator();
            if (isRequest) {
                validator.validateRequest(resource, exchange.getIn().getHeaders());
            } else {
                validator.validateResponse(resource, exchange.getIn().getHeaders());
            }
        } else {
            log.warn("Could not validate {} because FHIR Transaction ID is unknown", isRequest ? "request" : "response");
        }
    }

    private static FhirValidator getValidator(FhirContext context, boolean checkSchema, boolean checkSchematron) {
        var validator = context.newValidator();
        validator.setValidateAgainstStandardSchema(checkSchema);
        // Bug in HAPI: setting to false already looks up classpath for schematron lib
        if (checkSchematron) validator.setValidateAgainstStandardSchematron(true);
        return validator;
    }
}
