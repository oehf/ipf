/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core.atna;

import org.junit.After;
import org.junit.Before;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.queue.RecordingAuditMessageQueue;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.EventId;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Dmytro Rud
 */
public class AuditorTestBase {

    protected static final String REPLY_TO_URI = "http://141.44.162.126:8090/services/iti55-response";
    protected static final String USER_ID = "alias<user@issuer>";
    protected static final String USER_NAME = "Dr. Klaus-Peter Kohlrabi";
    protected static final String QUERY_PAYLOAD = "<query><coffee /></query>";
    protected static final String SERVER_URI = "http://www.icw.int/pxs/iti55-service";
    protected static final String QUERY_ID = "queryIdExtension@queryIdRoot";
    protected static final String MESSAGE_ID = "messageIdExtension@messageIdRoot";
    protected static final String HOME_COMMUNITY_ID = "urn:oid:3.14.15.926";
    protected static final String CLIENT_IP_ADDRESS = "141.44.162.126";
    protected static final String SUBMISSION_SET_UUID = "6d334214-2a2e-43ef-a362-cbe6e77b91a0";
    protected static final String[] PATIENT_IDS = new String[]{"1234^^^&1.2.3.4.5.6&ISO", "durak^^^&6.7.8.9.10&KRYSO"};

    protected static final PurposeOfUse[] PURPOSES_OF_USE = {
            PurposeOfUse.of("12", "1.0.14265.1", "Law Enforcement"),
            PurposeOfUse.of("13", "1.0.14265.1", "Something Else")};

    protected static final List<ActiveParticipantRoleId> USER_ROLES = Arrays.asList(
        ActiveParticipantRoleId.of("ABC", "1.2.3.4.5", "Role_ABC"),
        ActiveParticipantRoleId.of("DEF", "1.2.3.4.5.6", "Role_DEF"));


    protected DefaultAuditContext auditContext;
    protected RecordingAuditMessageQueue recorder;

    @Before
    public void setUp() {
        auditContext = new DefaultAuditContext();
        recorder = new RecordingAuditMessageQueue();
        auditContext.setAuditMessageQueue(recorder);
    }

    @After
    public void tearDown() {
        recorder.clear();
    }


    protected void assertCommonAuditAttributes(AuditMessage auditMessage,
                                             EventOutcomeIndicator eventOutcomeIndicator,
                                             EventId eventId,
                                             EventActionCode eventActionCode,
                                             String sourceUserId,
                                             String destinationUserId,
                                             boolean serverSide,
                                             boolean requiresPatient) {
        assertEquals(eventOutcomeIndicator, auditMessage.getEventIdentification().getEventOutcomeIndicator());
        assertEquals(eventActionCode, auditMessage.getEventIdentification().getEventActionCode());
        assertEquals(eventId, auditMessage.getEventIdentification().getEventID());

        ActiveParticipantType human = auditMessage.getActiveParticipants().stream()
                .filter(apt -> apt.getUserName() != null)
                .findFirst().orElse(null);

        ActiveParticipantType source = auditMessage.getActiveParticipants().stream()
                .filter(apt -> ActiveParticipantRoleIdCode.Source == apt.getRoleIDCodes().get(0))
                .findFirst().orElseThrow(() -> new AssertionError("Expected source participant"));

        assertEquals(human == null, source.isUserIsRequestor());
        assertEquals(sourceUserId, source.getUserID());
        if (!serverSide) {
            assertNotNull(source.getAlternativeUserID());
        }

        ActiveParticipantType destination = auditMessage.getActiveParticipants().stream()
                .filter(apt -> ActiveParticipantRoleIdCode.Destination == apt.getRoleIDCodes().get(0))
                .findFirst().orElseThrow(() -> new AssertionError("Expected destination active participant"));
        assertFalse(destination.isUserIsRequestor());
        assertEquals(destinationUserId, destination.getUserID());
        if (serverSide) {
            assertNotNull(destination.getAlternativeUserID());
        }

        if (requiresPatient) {
            ParticipantObjectIdentificationType patient = auditMessage.getParticipantObjectIdentifications().stream()
                    .filter(poi -> ParticipantObjectIdTypeCode.PatientNumber == poi.getParticipantObjectIDTypeCode())
                    .findFirst().orElseThrow(() -> new AssertionError("Expected patient participant object"));

            assertEquals(PATIENT_IDS[0], patient.getParticipantObjectID());
            assertEquals(ParticipantObjectTypeCode.Person, patient.getParticipantObjectTypeCode());
            assertEquals(ParticipantObjectTypeCodeRole.Patient, patient.getParticipantObjectTypeCodeRole());


            // Feeds
            if (!patient.getParticipantObjectDetails().isEmpty()) {
                assertEquals(MESSAGE_ID, new String(
                        Base64.getDecoder().decode(patient.getParticipantObjectDetails().get(0).getValue()),
                        StandardCharsets.UTF_8));
            }

            // Queries
            if (patient.getParticipantObjectQuery() != null) {
                assertEquals(QUERY_PAYLOAD, new String(
                        Base64.getDecoder().decode(patient.getParticipantObjectQuery()),
                        StandardCharsets.UTF_8));
            }
        }

        /*
        auditDataset.setUserName(USER_NAME);
        auditDataset.getPurposesOfUse().addAll(NEW_PURPOSES_OF_USE);
        auditDataset.getUserRoles().addAll(NEW_USER_ROLES);
        */
    }

    protected static <T extends AuditDataset> AuditMessage[] makeAuditMessages(AuditStrategySupport<T> auditStrategy, AuditContext auditContext, T auditDataset) {
        return auditStrategy.makeAuditMessage(auditContext, auditDataset);
    }

    protected static <T extends AuditDataset> AuditMessage makeAuditMessage(AuditStrategySupport<T> auditStrategy, AuditContext auditContext, T auditDataset) {
        return makeAuditMessages(auditStrategy, auditContext, auditDataset)[0];
    }

    protected static <T extends AuditDataset> String printAuditMessage(AuditStrategySupport<T> auditStrategy, AuditContext auditContext, T auditDataset) {
        return Current.toString(makeAuditMessages(auditStrategy, auditContext, auditDataset)[0], true);
    }

    protected static String printAuditMessage(AuditMessage auditMessage) {
        return Current.toString(auditMessage, true);
    }
}