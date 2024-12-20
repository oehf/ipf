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

package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq4;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;
import org.openehealth.ipf.commons.ihe.fhir.support.IgBasedInstanceValidator;

import java.util.Map;

public class ChPpq4Validator extends IgBasedInstanceValidator {

    public ChPpq4Validator(FhirContext fhirContext) {
        super(fhirContext);
    }

    @Override
    public void validateRequest(Object payload, Map<String, Object> parameters) {
        handleOperationOutcome(validateProfileConformance((Resource) payload, ChPpqmUtils.Profiles.FEED_REQUEST_BUNDLE));
    }

    @Override
    public void validateResponse(Object payload, Map<String, Object> parameters) {
        var bundle = (Bundle) payload;
        var outcome = validateProfileConformance(bundle, "http://hl7.org/fhir/StructureDefinition/Bundle");
        if (bundle.getType() != Bundle.BundleType.TRANSACTIONRESPONSE) {
            outcome.addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                    .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                    .setCode(OperationOutcome.IssueType.CODEINVALID)
                    .setDiagnostics("Response bundle type shall be " + Bundle.BundleType.TRANSACTIONRESPONSE.toCode()));
        }
        handleOperationOutcome(outcome);
    }

}
