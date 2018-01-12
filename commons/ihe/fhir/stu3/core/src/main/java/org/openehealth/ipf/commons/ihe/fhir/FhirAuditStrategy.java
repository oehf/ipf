package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.fhir.audit.AbstractFhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

import java.util.Comparator;

/**
 * @author Christian Ohr
 */
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
