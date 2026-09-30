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

public class PixmValidator extends BaseValidator {

    /**
     * Classpath location of the implementation guide package this transaction validates against.
     * Public so that the audit records of the transaction can be validated against the very same
     * profiles, rather than against a second copy of the path.
     */
    public static final String PIXM_PACKAGE_PATH = "classpath:META-INF/profiles/pixm/v310/ihe.iti.pixm.tgz";

    /**
     * The PIXm Patient profile is derived from the PDQm Patient profile, so the PDQm package and the
     * FHIR extensions it uses are loaded as well.
     *
     * @param fhirContext FHIR context
     */
    public PixmValidator(FhirContext fhirContext) {
        super(fhirContext,
            PIXM_PACKAGE_PATH,
            PdqmValidator.PDQM_PACKAGE_PATH,
            PdqmValidator.FHIR_EXTENSION_PACKAGE_PATH);
    }
}
