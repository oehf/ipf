package org.openehealth.ipf.commons.ihe.xds.iti43;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset.HumanUser;
import org.openehealth.ipf.commons.ihe.xds.atna.XdsAuditorTestBase;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRetrieveAuditStrategy30;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Iti43AuditStrategyTest extends XdsAuditorTestBase  {
    @Test
    public void testDocumentRepositoryAudit() {
        var auditMessage = testRequest(new Iti43ServerAuditStrategy());
        assertCommonXdsAuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                EventIdCode.Export,
                EventActionCode.Read,
                false,
                true);
    }

    @Test
    public void testConsumerAudit() {
        var auditMessage = testRequest(new Iti43ClientAuditStrategy());
        assertCommonXdsAuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                EventIdCode.Import,
                EventActionCode.Create,
                true,
                true);
    }

    private AuditMessage testRequest(XdsRetrieveAuditStrategy30 strategy) {
        var auditDataset = getXdsAuditDataset(strategy);
        var auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();
        

        assertEquals(3, auditMessage.findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectTypeCodeRole() == ParticipantObjectTypeCodeRole.Report).size());
        
        return auditMessage;
    }

    private XdsNonconstructiveDocumentSetRequestAuditDataset getXdsAuditDataset(XdsRetrieveAuditStrategy30 strategy) {
        var auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setSourceUserName("");
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.setPurposesOfUse(PURPOSES_OF_USE);
        auditDataset.getPatientIds().add(PATIENT_IDS[0]);
        auditDataset.getHumanUsers().add(new HumanUser(USER_ID, USER_NAME, USER_ROLES));

        for (var i = 0; i < 3; i++) {
            auditDataset.getDocuments().add(
                    new XdsNonconstructiveDocumentSetRequestAuditDataset.Document(
                            DOCUMENT_OIDS[i],
                            REPOSITORY_OIDS[i],
                            HOME_COMMUNITY_IDS[i],
                            null,
                            null,
                            XdsNonconstructiveDocumentSetRequestAuditDataset.Status.SUCCESSFUL));
        }
        return auditDataset;
    }
}
