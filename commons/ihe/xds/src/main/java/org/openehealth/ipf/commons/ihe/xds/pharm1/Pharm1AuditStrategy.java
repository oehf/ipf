package org.openehealth.ipf.commons.ihe.xds.pharm1;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditStrategy30;
import org.openehealth.ipf.commons.ihe.xds.core.audit.codes.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.codes.XdsParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.event.XdsQueryInformationBuilder;

public class Pharm1AuditStrategy extends XdsQueryAuditStrategy30 {
    public Pharm1AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    public AuditMessage[] makeAuditMessage(AuditContext auditContext, XdsQueryAuditDataset auditDataset) {
        return ((XdsQueryInformationBuilder)(new XdsQueryInformationBuilder(auditContext, auditDataset, XdsEventTypeCode.RegistryStoredQuery, auditDataset.getPurposesOfUse())).addPatients(new String[]{auditDataset.getPatientId()})).setQueryParameters(auditDataset, XdsParticipantObjectIdTypeCode.RegistryStoredQuery).getMessages();
    }
}
