/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.pixpdq;

import ca.uhn.fhir.context.FhirContext;
import org.openehealth.ipf.commons.ihe.fhir.support.BaseValidator;

public class PdqmValidator extends BaseValidator {

    private static final String PDQM_PACKAGE_PATH = "classpath:META-INF/profiles/pdqm/v320/ihe.iti.pdqm.tgz";
    private static final String FHIR_EXTENSION_PACKAGE_PATH = "classpath:META-INF/profiles/extensions/v520/hl7.fhir.uv.extensions.r4.tgz";

    public PdqmValidator(FhirContext fhirContext) {
        super(fhirContext, PDQM_PACKAGE_PATH, FHIR_EXTENSION_PACKAGE_PATH);
    }
}
