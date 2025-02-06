package org.openehealth.ipf.commons.ihe.xds.iti18;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset.HumanUser;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorTestBase;
import org.openehealth.ipf.commons.ihe.xds.atna.XdsAuditorTestBase;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsByReferenceIdQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetAllQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.PatientIdBasedStoredQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.StoredQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryRegistryTransformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Iti18AuditStrategyTest extends XdsAuditorTestBase {

    @Test
    public void testServerSideForGetAll() {
        testRequest(true, new GetAllQuery());
    }

    @Test
    public void testClientSideForGetAll() {
        testRequest(false, new GetAllQuery());
    }

    @Test
    public void testServerSideForFindDocuments() {
        testRequest(true, new FindDocumentsByReferenceIdQuery());
    }

    @Test
    public void testClientSideForFindDocuments() {
        testRequest(false, new FindDocumentsByReferenceIdQuery());
    }

    private void testRequest(boolean serverSide, StoredQuery query) {
        if (query instanceof PatientIdBasedStoredQuery patientQuery) {
            patientQuery.setPatientId(Identifiable.parse(AuditorTestBase.PATIENT_IDS[0]));
        }
        var strategy = new Iti18AuditStrategy(serverSide);
        var auditDataset = getXdsAuditDataset(strategy);
        var ebXmlQueryRequest = new QueryRegistryTransformer().toEbXML(new QueryRegistry(query));
        strategy.enrichAuditDatasetFromRequest(auditDataset, ebXmlQueryRequest.getInternal(), null);
        var auditMessages = makeAuditMessages(strategy, auditContext, auditDataset);

        assertEquals(1, auditMessages.length);
        for (var auditMessage : auditMessages) {
            auditMessage.validate();
        }

        assertEquals(ebXmlQueryRequest.getId(),
                auditMessages[0]
                        .findParticipantObjectIdentifications(
                                p -> p.getParticipantObjectTypeCode().equals(ParticipantObjectTypeCode.System))
                        .get(0).getParticipantObjectID());

        assertCommonXdsAuditAttributes(auditMessages[0], EventOutcomeIndicator.Success, EventIdCode.Query,
                EventActionCode.Execute, serverSide, true);
    }

    private XdsQueryAuditDataset getXdsAuditDataset(Iti18AuditStrategy strategy) {
        var auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.setPurposesOfUse(PURPOSES_OF_USE);
        auditDataset.getHumanUsers().add(new HumanUser(USER_ID, USER_NAME, USER_ROLES));

        return auditDataset;
    }

}
