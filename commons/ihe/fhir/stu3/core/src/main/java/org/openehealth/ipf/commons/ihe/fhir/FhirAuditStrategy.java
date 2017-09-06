package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.util.Comparator;

/**
 * @author Christian Ohr
 */
public abstract class FhirAuditStrategy<T extends FhirAuditDataset> extends AbstractFhirAuditStrategy<T, OperationOutcome> {

    public FhirAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCodeFromOperationOutcome(OperationOutcome response) {
        if (response.hasIssue()) {
            return RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
        }
        // Find out the worst issue severity
        OperationOutcome.IssueSeverity severity = response.getIssue().stream()
                .map(OperationOutcome.OperationOutcomeIssueComponent::getSeverity)
                .min(Comparator.naturalOrder())
                .orElse(OperationOutcome.IssueSeverity.NULL);
        switch (severity) {
            case FATAL:
            case ERROR:
                return RFC3881EventCodes.RFC3881EventOutcomeCodes.MAJOR_FAILURE;
            case WARNING:
                return RFC3881EventCodes.RFC3881EventOutcomeCodes.MINOR_FAILURE;
            default:
                return RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
        }

    }


}
