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

package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq3;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;
import org.openehealth.ipf.commons.ihe.fhir.support.IgBasedInstanceValidator;

import java.util.Map;

public class ChPpq3Validator extends IgBasedInstanceValidator {

    public ChPpq3Validator(FhirContext fhirContext) {
        super(fhirContext);
    }

    @Override
    public void validateRequest(Object payload, Map<String, Object> headers) {
        handleOperationOutcome(doValidateRequest(payload, headers));
    }

    private OperationOutcome doValidateRequest(Object payload, Map<String, Object> headers) {
        String method = headers.get(Constants.HTTP_METHOD).toString();
        switch (method) {
            case "POST":
            case "PUT":
                return validateProfileConformance((Resource) payload, ChPpqmUtils.Profiles.CONSENT);

            case "DELETE":
                String resourceId = ChPpqmUtils.extractResourceIdForDelete(payload);
                if (StringUtils.isBlank(resourceId)) {
                    return new OperationOutcome()
                            .addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                                    .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                                    .setCode(OperationOutcome.IssueType.REQUIRED)
                                    .setDiagnostics("Identifier must be present for DELETE"));
                }
                return null;

            default:
                return new OperationOutcome()
                        .addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                                .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                                .setCode(OperationOutcome.IssueType.NOTSUPPORTED)
                                .setDiagnostics("Unsupported HTTP method: " + method));
        }
    }

}
