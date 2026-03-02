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
import org.openehealth.ipf.commons.ihe.fhir.support.BaseValidator;

/**
 * Validator for MHD transactions.
 *
 * @author Christian Ohr
 * @since 4.8
 */
public class MhdValidator extends BaseValidator {

    private static final String MHD_PACKAGE_PATH = "classpath:META-INF/profiles/v423/ihe.iti.mhd.tgz";

    public MhdValidator(FhirContext fhirContext) {
        super(fhirContext, MHD_PACKAGE_PATH);
    }

}
