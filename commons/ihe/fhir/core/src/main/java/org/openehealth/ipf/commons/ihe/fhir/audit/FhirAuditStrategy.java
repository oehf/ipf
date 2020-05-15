/*
 * Copyright 2019 the original author or authors.
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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;

/**
 * @author Christian Ohr
 */
public abstract class FhirAuditStrategy<T extends FhirAuditDataset> extends AbstractFhirAuditStrategy<T, IBaseOperationOutcome> {

    private final IBaseOperationOutcomeOperations operations;

    public FhirAuditStrategy(boolean serverSide, IBaseOperationOutcomeOperations operations) {
        super(serverSide);
        this.operations = operations;
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeCodeFromOperationOutcome(IBaseOperationOutcome response) {
        if (!operations.hasIssue(response)) {
            return EventOutcomeIndicator.Success;
        }
        var severity = operations.getWorstIssueSeverity(response);
        switch (severity) {
            case "fatal":
            case "error":
                return EventOutcomeIndicator.MajorFailure;
            case "warning":
                return EventOutcomeIndicator.MinorFailure;
            default:
                return EventOutcomeIndicator.Success;
        }

    }

    @Override
    public String getEventOutcomeDescriptionFromOperationOutcome(IBaseOperationOutcome response) {
        if (!operations.hasIssue(response)) {
            return null;
        }
        return operations.getDiagnostics(response);
    }
}
