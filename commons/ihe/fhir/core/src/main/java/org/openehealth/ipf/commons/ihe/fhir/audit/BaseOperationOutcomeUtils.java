/*
 * Copyright 2022 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.audit;

import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

/**
 * Helper class to obtain details from {@link IBaseOperationOutcome} implementations
 * independently of the actually used FHIR version.
 */
public class BaseOperationOutcomeUtils {

    public static boolean hasIssue(FhirContext fhirContext, IBaseOperationOutcome operationOutcome) {
        return OperationOutcomeUtil.hasIssues(fhirContext, operationOutcome);
    }

    public static String getDiagnostics(FhirContext fhirContext, IBaseOperationOutcome operationOutcome) {
        return OperationOutcomeUtil.getFirstIssueDetails(fhirContext, operationOutcome);
    }

    public static String getWorstIssueSeverity(FhirContext fhirContext, IBaseOperationOutcome operationOutcome) {
        return getFirstIssueStringPart(fhirContext, operationOutcome, "severity");
    }

    // Basically a copy of the private method in OperationOutcomeUtil
    private static String getFirstIssueStringPart(FhirContext fhirContext, IBaseOperationOutcome operationOutcome, String name) {
        if (operationOutcome == null) {
            return null;
        }

        var resourceDefinition = fhirContext.getResourceDefinition(operationOutcome);
        var issueChild = resourceDefinition.getChildByName("issue");
        var issues = issueChild.getAccessor().getValues(operationOutcome);
        if (issues.isEmpty()) {
            return null;
        }

        var issue = issues.get(0);
        var issueElement = (BaseRuntimeElementCompositeDefinition<?>) fhirContext.getElementDefinition(issue.getClass());
        var detailsChild = issueElement.getChildByName(name);

        var details = detailsChild.getAccessor().getValues(issue);
        if (details.isEmpty()) {
            return null;
        }
        return ((IPrimitiveType<?>) details.get(0)).getValueAsString();
    }
}




