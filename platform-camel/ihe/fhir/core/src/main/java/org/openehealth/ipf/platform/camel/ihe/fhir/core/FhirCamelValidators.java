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
import org.apache.camel.Processor;
import org.hl7.fhir.instance.model.OperationOutcome;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirInteractionId;
import org.openehealth.ipf.commons.ihe.fhir.FhirUtils;
import org.openehealth.ipf.commons.ihe.fhir.FhirValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openehealth.ipf.commons.ihe.core.Constants.INTERACTION_ID_NAME;

/**
 * Validating processors for FHIR-based transactions
 *
 * @since 3.2
 */
public final class FhirCamelValidators {

    private static final Logger LOG = LoggerFactory.getLogger(FhirCamelValidators.class);

    private FhirCamelValidators() {}

    public static Processor requestValidator() {
        return exchange -> {
            FhirValidator.Mode mode = exchange.getIn().getHeader(FhirValidator.VALIDATION_MODE, FhirValidator.Mode.class);
            if (mode != null && mode != FhirValidator.Mode.OFF) {
                FhirInteractionId fhirInteractionId = exchange.getIn().getHeader(INTERACTION_ID_NAME, FhirInteractionId.class);
                FhirContext context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
                if (context != null && fhirInteractionId == null) {
                    FhirValidator validator = fhirInteractionId.getFhirTransactionConfiguration().getFhirValidator();
                    validator.validateRequest(context, exchange.getIn().getBody(), exchange.getIn().getHeaders());
                } else {
                    LOG.warn("Could not validate request because FHIR Context or Validator are unknown");
                }
            }
        };
    }

    public static Processor requestValidator(FhirValidator validator) {
        if (validator == null) {
            throw new IllegalArgumentException("FHIR validator is null");
        }
        return exchange -> {
            FhirValidator.Mode mode = exchange.getIn().getHeader(FhirValidator.VALIDATION_MODE, FhirValidator.Mode.class);
            if (mode != null && mode != FhirValidator.Mode.OFF) {
                FhirContext context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
                if (context != null && validator != null) {
                    validator.validateRequest(context, exchange.getIn().getBody(), exchange.getIn().getHeaders());
                } else {
                    LOG.warn("Could not validate request because FHIR Context is unknown");
                }
            }
        };
    }

    public static Processor responseValidator() {
        return exchange -> {
            FhirValidator.Mode mode = exchange.getIn().getHeader(FhirValidator.VALIDATION_MODE, FhirValidator.Mode.class);
            if (mode != null && mode != FhirValidator.Mode.OFF) {
                FhirInteractionId fhirInteractionId = exchange.getIn().getHeader(INTERACTION_ID_NAME, FhirInteractionId.class);
                FhirContext context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
                if (context != null && fhirInteractionId == null) {
                    FhirValidator validator = fhirInteractionId.getFhirTransactionConfiguration().getFhirValidator();
                    validator.validateResponse(context, exchange.getIn().getBody(), exchange.getIn().getHeaders());
                } else {
                    LOG.warn("Could not validate request because FHIR Context or Validator are unknown");
                }
            }
        };
    }

    public static Processor responseValidator(FhirValidator validator) {
        if (validator == null) {
            throw new IllegalArgumentException("FHIR validator is null");
        }
        return exchange -> {
            FhirValidator.Mode mode = exchange.getIn().getHeader(FhirValidator.VALIDATION_MODE, FhirValidator.Mode.class);
            if (mode != null && mode != FhirValidator.Mode.OFF) {
                FhirContext context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
                if (context != null && validator != null) {
                    validator.validateResponse(context, exchange.getIn().getBody(), exchange.getIn().getHeaders());
                } else {
                    LOG.warn("Could not validate request because FHIR Context is unknown");
                }
            }
        };
    }

}
