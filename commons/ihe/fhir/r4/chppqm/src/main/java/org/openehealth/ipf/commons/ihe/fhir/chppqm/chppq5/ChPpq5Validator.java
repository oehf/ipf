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

package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq5;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;
import org.openehealth.ipf.commons.ihe.fhir.support.IgBasedInstanceValidator;

import java.util.Map;

public class ChPpq5Validator extends IgBasedInstanceValidator {

    public ChPpq5Validator(FhirContext fhirContext) {
        super(fhirContext);
    }

/*
    @Override
    public void validateRequest(Object payload, Map<String, Object> parameters) {
        ChPpq5SearchParameters params = (ChPpq5SearchParameters) payload;
        OperationOutcome outcome = new OperationOutcome();
        if (params.getPatientId() == null) {
            if (params.getConsentId() == null) {
                outcome.addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                        .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                        .setCode(OperationOutcome.IssueType.REQUIRED)
                        .setDiagnostics("Either consent ID or patient ID shall be provided"));
            }
        } else {
            if (params.getConsentId() != null) {
                outcome.addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                        .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                        .setCode(OperationOutcome.IssueType.INVARIANT)
                        .setDiagnostics("Either consent ID or patient ID shall be provided, but not both"));
            }
        }
        handleOperationOutcome(outcome);
    }
*/

    @Override
    public void validateResponse(Object payload, Map<String, Object> parameters) {
        handleOperationOutcome(validateProfileConformance((Resource) payload, ChPpqmUtils.Profiles.CONSENT));
    }

}
