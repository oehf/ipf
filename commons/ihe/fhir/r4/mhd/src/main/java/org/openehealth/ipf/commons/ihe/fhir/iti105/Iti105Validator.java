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

package org.openehealth.ipf.commons.ihe.fhir.iti105;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;
import org.openehealth.ipf.commons.ihe.fhir.support.IgBasedInstanceValidator;

import java.util.Map;

/**
 * Validator for ITI-105 transactions.
 *
 *
 * @author Boris Stanojevic
 * @since 4.8
 * @deprecated use {@link org.openehealth.ipf.commons.ihe.fhir.mhd.MhdValidator}
 */
@Deprecated(forRemoval = true)
public class Iti105Validator extends IgBasedInstanceValidator {

    public static final String ITI105_PROFILE =
        "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.SimplifiedPublish.DocumentReference";

    public Iti105Validator(FhirContext fhirContext) {
        super(fhirContext);
    }

    @Override
    public OperationOutcome validateRequest(IBaseResource payload, Map<String, Object> parameters) {
         return handleOperationOutcome(validateProfileConformance((Resource) payload, MhdProfile.SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE));
    }
}
