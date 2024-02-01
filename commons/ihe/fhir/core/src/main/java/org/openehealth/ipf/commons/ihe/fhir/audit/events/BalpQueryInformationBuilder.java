package org.openehealth.ipf.commons.ihe.fhir.audit.events;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

import static org.openehealth.ipf.commons.ihe.fhir.audit.events.BalpJwtUtils.addJwtParticipant;

public class BalpQueryInformationBuilder extends QueryInformationBuilder<BalpQueryInformationBuilder> {

    public BalpQueryInformationBuilder(AuditContext auditContext,
                                       FhirAuditDataset auditDataset,
                                       EventType eventType,
                                       PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventType, purposesOfUse);
        addJwtId(auditDataset);
    }

    public void addJwtId(FhirAuditDataset auditDataset) {
        addJwtParticipant(delegate, auditDataset, getAuditContext());
    }
}