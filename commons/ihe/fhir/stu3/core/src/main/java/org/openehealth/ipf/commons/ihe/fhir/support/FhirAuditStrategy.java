/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.support;

import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.fhir.audit.AbstractFhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

import java.util.Comparator;

/**
 * @author Christian Ohr
 *
 * @deprecated use {@link org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditStrategy}
 */
@Deprecated
public abstract class FhirAuditStrategy<T extends FhirAuditDataset> extends AbstractFhirAuditStrategy<T, OperationOutcome> {

    public FhirAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeCodeFromOperationOutcome(OperationOutcome response) {
        if (!response.hasIssue()) {
            return EventOutcomeIndicator.Success;
        }
        // Find out the worst issue severity
        OperationOutcome.IssueSeverity severity = response.getIssue().stream()
                .map(OperationOutcome.OperationOutcomeIssueComponent::getSeverity)
                .min(Comparator.naturalOrder())
                .orElse(OperationOutcome.IssueSeverity.NULL);
        switch (severity) {
            case FATAL:
            case ERROR:
                return EventOutcomeIndicator.MajorFailure;
            case WARNING:
                return EventOutcomeIndicator.MinorFailure;
            default:
                return EventOutcomeIndicator.Success;
        }

    }

    @Override
    public String getEventOutcomeDescriptionFromOperationOutcome(OperationOutcome response) {
        if (!response.hasIssue()) {
            return null;
        }
        return response.getIssue().get(0).getDiagnostics();
    }
}
