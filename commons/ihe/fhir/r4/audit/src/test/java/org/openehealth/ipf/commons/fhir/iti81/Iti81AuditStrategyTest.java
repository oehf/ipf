package org.openehealth.ipf.commons.fhir.iti81;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorTestBase;
import org.openehealth.ipf.commons.ihe.fhir.iti81.FhirAuditEventQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti81.Iti81AuditStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Iti81AuditStrategyTest extends AuditorTestBase {

    @Test
    public void testServerSide() {
        testRequest(true);
    }

    @Test
    public void testClientSide() {
        testRequest(false);
    }

    private void testRequest(boolean serverSide) {
        var strategy = new Iti81AuditStrategy(serverSide);
        var auditDataset = getXdsAuditDataset(strategy);
        var auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();

        // System.out.println(printAuditMessage(auditMessage));

        assertCommonAuditAttributes(auditMessage, EventOutcomeIndicator.Success, EventIdCode.AuditLogUsed,
                EventActionCode.Read,
                REPLY_TO_URI, SERVER_URI, serverSide, false);

        assertEquals(2, auditMessage.findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectTypeCode() == ParticipantObjectTypeCode.System &&
                poit.getParticipantObjectIDTypeCode() == ParticipantObjectIdTypeCode.URI)
                .size());
    }

    private FhirAuditEventQueryAuditDataset getXdsAuditDataset(Iti81AuditStrategy strategy) {
        var auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.getAuditEventUris().add("uri1");
        auditDataset.getAuditEventUris().add("uri2");
        auditDataset.getHumanUsers().add(new AuditDataset.HumanUser(USER_ID, USER_NAME, USER_ROLES));

        return auditDataset;
    }
}
