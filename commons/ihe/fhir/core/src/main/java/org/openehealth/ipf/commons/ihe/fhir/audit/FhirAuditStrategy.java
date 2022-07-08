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

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;

import static org.openehealth.ipf.commons.ihe.fhir.audit.BaseOperationOutcomeUtils.getDiagnostics;
import static org.openehealth.ipf.commons.ihe.fhir.audit.BaseOperationOutcomeUtils.getWorstIssueSeverity;
import static org.openehealth.ipf.commons.ihe.fhir.audit.BaseOperationOutcomeUtils.hasIssue;

/**
 * @author Christian Ohr
 */
public abstract class FhirAuditStrategy<T extends FhirAuditDataset> extends AbstractFhirAuditStrategy<T, IBaseOperationOutcome> {

    public FhirAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeCodeFromOperationOutcome(FhirContext fhirContext, IBaseOperationOutcome response) {
        if (!hasIssue(fhirContext, response)) {
            return EventOutcomeIndicator.Success;
        }
        var severity = getWorstIssueSeverity(fhirContext, response);
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
    public String getEventOutcomeDescriptionFromOperationOutcome(FhirContext fhirContext, IBaseOperationOutcome response) {
        if (!hasIssue(fhirContext, response)) {
            return null;
        }
        return getDiagnostics(fhirContext, response);
    }

}
