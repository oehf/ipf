package org.openehealth.ipf.commons.ihe.fhir.audit.events;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.event.PHIImportBuilder;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

import static org.openehealth.ipf.commons.ihe.fhir.audit.events.BalpJwtUtils.addJwtParticipant;

public class BalpPHIImportBuilder extends PHIImportBuilder<BalpPHIImportBuilder> {


    public BalpPHIImportBuilder(AuditContext auditContext,
                                FhirAuditDataset auditDataset,
                                EventType eventType,
                                PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventType, purposesOfUse);
        addJwtId(auditDataset);
    }

    public BalpPHIImportBuilder(AuditContext auditContext,
                                FhirAuditDataset auditDataset,
                                EventActionCode eventActionCode,
                                EventType eventType,
                                PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventActionCode, eventType, purposesOfUse);
        addJwtId(auditDataset);
    }

    public BalpPHIImportBuilder(AuditContext auditContext, FhirAuditDataset auditDataset, EventOutcomeIndicator eventOutcomeIndicator, String eventOutcomeDescription, EventActionCode eventActionCode, EventType eventType, PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventOutcomeIndicator, eventOutcomeDescription, eventActionCode, eventType, purposesOfUse);
        addJwtId(auditDataset);
    }

    public void addJwtId(FhirAuditDataset auditDataset) {
        addJwtParticipant(delegate, auditDataset, getAuditContext());
    }
}
