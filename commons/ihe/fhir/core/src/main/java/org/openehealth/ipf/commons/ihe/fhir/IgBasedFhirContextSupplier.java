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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import lombok.experimental.UtilityClass;
import org.hl7.fhir.common.hapi.validation.support.*;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;

import java.io.IOException;

/**
 * Supplier of FHIR contexts that use Implementation Guides for validation of resources.
 *
 * @author Dmytro Rud
 */
@UtilityClass
public class IgBasedFhirContextSupplier {

    public static FhirContext getContext(FhirContext fhirContext, String... igResources) throws IOException {
        fhirContext.setRestfulClientFactory(new SslAwareApacheRestfulClientFactory(fhirContext));

        NpmPackageValidationSupport npmValidationSupport = new NpmPackageValidationSupport(fhirContext);
        for (String igResource : igResources) {
            npmValidationSupport.loadPackageFromClasspath(igResource);
        }

        CachingValidationSupport validationSupport = new CachingValidationSupport(new ValidationSupportChain(
                npmValidationSupport,
                new CommonCodeSystemsTerminologyService(fhirContext),
                new DefaultProfileValidationSupport(fhirContext),
                new InMemoryTerminologyServerValidationSupport(fhirContext)));

        return fhirContext.setFhirValidatorFactory(ctx -> {
            FhirValidator validator = new FhirValidator(ctx);
            FhirInstanceValidator instanceValidator = new FhirInstanceValidator(validationSupport);
            validator.registerValidatorModule(instanceValidator);
            return validator;
        });
    }

}
