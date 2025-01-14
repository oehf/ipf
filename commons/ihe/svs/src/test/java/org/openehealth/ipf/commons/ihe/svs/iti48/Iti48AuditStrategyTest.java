/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.iti48;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.queue.RecordingAuditMessageQueue;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsAuditDataset;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Iti48AuditStrategyTest {
    private static final String USER_ID = "alias<user@issuer>";
    private static final String USER_NAME = "Dr. Klaus-Peter Kohlrabi";
    private static final String SERVER_URI = "http://www.icw.int/svs/iti48-service";
    private static final String CLIENT_IP_ADDRESS = "141.44.162.126";
    private static final String VALUE_SET_ID = "1.2.840.10008.6.1.308";
    private static final String VALUE_SET_NAME = "Common Anatomic Regions Context ID 4031";
    private static final String VALUE_SET_VERSION = "20061023";

    private static final PurposeOfUse[] PURPOSES_OF_USE = {
        PurposeOfUse.of("12", "1.0.14265.1", "Law Enforcement"),
        PurposeOfUse.of("13", "1.0.14265.1", "Something Else")};

    private static final List<ActiveParticipantRoleId> USER_ROLES = List.of(
        ActiveParticipantRoleId.of("ABC", "1.2.3.4.5", "Role_ABC"),
        ActiveParticipantRoleId.of("DEF", "1.2.3.4.5.6", "Role_DEF"));

    private DefaultAuditContext auditContext;
    private RecordingAuditMessageQueue recorder;

    @BeforeEach
    public void setUp() {
        auditContext = new DefaultAuditContext();
        recorder = new RecordingAuditMessageQueue();
        auditContext.setAuditMessageQueue(recorder);
    }

    @AfterEach
    public void tearDown() {
        recorder.clear();
    }

    @Test
    public void testServerSide() {
        var strategy = new Iti48AuditStrategy(true);
        var auditDataset = this.getHl7v3AuditDataset(strategy);
        var auditMessage = this.makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();
        this.assertCommonAuditAttributes(auditMessage);

        assertEquals(EventIdCode.Export, auditMessage.getEventIdentification().getEventID());
        assertEquals(EventActionCode.Read, auditMessage.getEventIdentification().getEventActionCode());

        final var source = this.getSourceParticipant(auditMessage);
        assertEquals(AuditUtils.getProcessId(), source.getUserID());
        assertEquals(SERVER_URI, source.getAlternativeUserID());
        assertFalse(source.isUserIsRequestor());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());

        final var destination = this.getDestinationParticipant(auditMessage);
        assertNotNull(destination.getUserID());
        assertTrue(destination.isUserIsRequestor());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, destination.getNetworkAccessPointTypeCode());
        assertEquals(CLIENT_IP_ADDRESS, destination.getNetworkAccessPointID());
    }

    @Test
    public void testClientSide() {
        var strategy = new Iti48AuditStrategy(false);
        var auditDataset = this.getHl7v3AuditDataset(strategy);
        var auditMessage = this.makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();
        this.assertCommonAuditAttributes(auditMessage);

        assertEquals(EventIdCode.Import, auditMessage.getEventIdentification().getEventID());
        assertEquals(EventActionCode.Create, auditMessage.getEventIdentification().getEventActionCode());

        final var source = this.getSourceParticipant(auditMessage);
        assertEquals(SERVER_URI, source.getUserID());
        assertFalse(source.isUserIsRequestor());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, source.getNetworkAccessPointTypeCode());
        assertEquals(CLIENT_IP_ADDRESS, source.getNetworkAccessPointID());

        final var destination = this.getDestinationParticipant(auditMessage);
        assertNotNull(destination.getUserID());
        assertEquals(AuditUtils.getProcessId(), destination.getAlternativeUserID());
        assertTrue(destination.isUserIsRequestor());
        assertEquals(NetworkAccessPointTypeCode.IPAddress, destination.getNetworkAccessPointTypeCode());
    }

    private SvsAuditDataset getHl7v3AuditDataset(final Iti48AuditStrategy strategy) {
        var auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setPurposesOfUse(PURPOSES_OF_USE);
        auditDataset.getHumanUsers().add(new AuditDataset.HumanUser(USER_ID, USER_NAME, USER_ROLES));
        auditDataset.setValueSetId(VALUE_SET_ID);
        auditDataset.setValueSetName(VALUE_SET_NAME);
        auditDataset.setValueSetVersion(VALUE_SET_VERSION);
        return auditDataset;
    }

    private AuditMessage makeAuditMessage(Iti48AuditStrategy auditStrategy,
                                          AuditContext auditContext,
                                          SvsAuditDataset auditDataset) {
        return auditStrategy.makeAuditMessage(auditContext, auditDataset)[0];
    }

    private ActiveParticipantType getSourceParticipant(AuditMessage auditMessage) {
        return auditMessage.getActiveParticipants().stream()
            .filter(apt -> ActiveParticipantRoleIdCode.Source == apt.getRoleIDCodes().get(0))
            .findFirst().orElseThrow(() -> new AssertionError("Expected source participant"));
    }

    private ActiveParticipantType getDestinationParticipant(AuditMessage auditMessage) {
        return auditMessage.getActiveParticipants().stream()
            .filter(apt -> ActiveParticipantRoleIdCode.Destination == apt.getRoleIDCodes().get(0))
            .findFirst().orElseThrow(() -> new AssertionError("Expected destination active participant"));
    }

    private void assertCommonAuditAttributes(AuditMessage auditMessage) {
        assertEquals(EventOutcomeIndicator.Success, auditMessage.getEventIdentification().getEventOutcomeIndicator());
        final var eventTypeCode = auditMessage.getEventIdentification().getEventTypeCode().get(0);
        assertEquals("ITI-48", eventTypeCode.getCode());
        assertEquals("IHE Transactions", eventTypeCode.getCodeSystemName());
        assertEquals("Retrieve Value Set", eventTypeCode.getOriginalText());
        assertEquals(2, auditMessage.getEventIdentification().getPurposesOfUse().size());

        final var human = auditMessage.getActiveParticipants().stream()
            .filter(apt -> apt.getUserName() != null)
            .findFirst().orElseThrow(() -> new AssertionError("Expected human active participant"));
        assertFalse(human.isUserIsRequestor());
        assertEquals(USER_ID, human.getUserID());
        assertEquals(USER_NAME, human.getUserName());
        assertEquals(2, human.getRoleIDCodes().size());
        // ?

        assertEquals(auditContext.getAuditSourceId(), auditMessage.getAuditSourceIdentification().getAuditSourceID());
        assertEquals(auditContext.getAuditEnterpriseSiteId(), auditMessage.getAuditSourceIdentification().getAuditEnterpriseSiteID());

        final var objectIdentification = auditMessage.getParticipantObjectIdentifications().get(0);
        assertEquals(VALUE_SET_ID, objectIdentification.getParticipantObjectID());
        assertEquals(ParticipantObjectTypeCode.System, objectIdentification.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.Report, objectIdentification.getParticipantObjectTypeCodeRole());
        assertEquals("10", objectIdentification.getParticipantObjectIDTypeCode().getCode());
        assertEquals("RFC-3881", objectIdentification.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("Search Criteria", objectIdentification.getParticipantObjectIDTypeCode().getOriginalText());
        assertEquals(VALUE_SET_NAME, objectIdentification.getParticipantObjectName());
        assertArrayEquals(VALUE_SET_VERSION.getBytes(StandardCharsets.UTF_8), objectIdentification.getParticipantObjectDetails().get(0).getValue());
    }
}