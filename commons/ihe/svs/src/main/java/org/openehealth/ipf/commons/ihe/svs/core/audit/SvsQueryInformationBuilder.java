package org.openehealth.ipf.commons.ihe.svs.core.audit;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;

/**
 *
 *
 * @author Quentin Ligier
 */
public class SvsQueryInformationBuilder extends QueryInformationBuilder<SvsQueryInformationBuilder> {

    public SvsQueryInformationBuilder(final AuditContext auditContext,
                                      final AuditDataset auditDataset,
                                      final EventType eventType,
                                      final PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventType, purposesOfUse);
    }

}
