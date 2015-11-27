/*
 * Copyright 2015 the original author or authors.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Factory class for creating validating processors for FHIR resources. For
 * schematron validation you need to have the phloc-schematron library in
 * the classpath.
 * The validating processors create a {@link FhirValidator} instance while
 * validating the first resource, and reuses this instance afterwards.
 * </p>
 * <p>
 * The FHIR validators are used as processors, e.g.:
 * <pre>
 *     ...
 *     .process(FhirResourceValidators.validateSchema())
 *     ...
 * </pre>
 * An instance of {@link UnprocessableEntityException} is thrown if validation
 * fails. A FHIRConsumer will automatically process the exception for a 400
 * response.
 * </p>
 *
 * @since 3.1
 */
public final class FhirResourceValidators {

    private static final Logger LOG = LoggerFactory.getLogger(FhirResourceValidators.class);

    private FhirResourceValidators() {
    }

    /**
     * @return instance of FHIR validator that checks the underlying XML schema only
     */
    public static Processor validateSchema() {
        return new ValidatingFhirProcessor(true, false);
    }

    /**
     * @return instance of FHIR validator that checks both the underlying XML schema and
     * the schematron rules for the given resource.
     */
    public static Processor validateSchematron() {
        return new ValidatingFhirProcessor(true, true);
    }


    private static class ValidatingFhirProcessor implements Processor {

        private volatile FhirContext context;
        private volatile FhirValidator validator;
        private final boolean checkSchema;
        private final boolean checkSchematron;

        public ValidatingFhirProcessor(boolean checkSchema, boolean checkSchematron) {
            this.checkSchema = checkSchema;
            this.checkSchematron = checkSchematron;
        }

        @Override
        public void process(Exchange exchange) throws Exception {
            if (validator == null) {
                synchronized (this) {
                    context = exchange.getIn().getHeader(Constants.FHIR_CONTEXT, FhirContext.class);
                    if (context != null) {
                        context = FhirContext.forDstu2Hl7Org();
                    }
                    validator = context.newValidator();
                    validator.setValidateAgainstStandardSchema(checkSchema);
                    validator.setValidateAgainstStandardSchematron(checkSchematron);
                }
            }
            ValidationResult result = validator.validateWithResult(exchange.getIn().getBody(IBaseResource.class));
            if (!result.isSuccessful()) {
                IBaseOperationOutcome outcome = result.toOperationOutcome();
                LOG.debug("FHIR Validation failed with outcome {}", outcome);
                throw new UnprocessableEntityException("FHIR Validation Error", outcome);
            } else {
                LOG.debug("FHIR Validation succeeded");
            }
        }
    }
}
