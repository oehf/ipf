/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.xds

import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole
import org.openehealth.ipf.commons.audit.model.*
import org.openehealth.ipf.commons.audit.types.CodedValueType
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEAuditMessageBuilder
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import java.nio.charset.StandardCharsets

/**
 * @author Christian Ohr
 */
class XdsStandardTestContainer extends StandardTestContainer {

    List<AuditMessage> getAudit(EventActionCode actionCode, String addr) {
        getAuditSender().messages.findAll {
            it.eventIdentification.eventActionCode == actionCode
        }.findAll {
            it.activeParticipants.any { obj -> obj.userID == addr } ||
                    it.participantObjectIdentifications.any { obj -> obj.participantObjectID == addr }
        }
    }

    void checkCode(CodedValueType actual, String code, String scheme) {
        assert actual.code == code && actual.codeSystemName == scheme
    }

    void checkEvent(EventIdentificationType event, String code, String iti, EventActionCode actionCode, EventOutcomeIndicator outcome) {
        checkCode(event.eventID, code, 'DCM')
        checkCode(event.eventTypeCode[0], iti, 'IHE Transactions')
        assert event.eventActionCode == actionCode
        assert event.eventDateTime != null
        assert event.eventOutcomeIndicator == outcome
    }

    void checkSource(ActiveParticipantType source, String httpAddr, boolean requestor) {
        checkSource(source, requestor, true)
        assert source.userID == httpAddr
    }

    void checkSource(ActiveParticipantType source, boolean requestor, boolean userIdRequired = false) {
        // This should be something useful, but it isn't fully specified yet (see CP-402)
        if (userIdRequired) {
            assert source.userID != null && source.userID != ''
        }
        assert source.userIsRequestor == requestor
        assert source.networkAccessPointTypeCode
        assert source.networkAccessPointID
        // This will be required soon:
        // assert source.@AlternativeUserID != null && source.@AlternativeUserID != ''
        checkCode(source.roleIDCodes[0], '110153', 'DCM')
    }

    void checkDestination(ActiveParticipantType destination, String httpAddr, boolean requestor) {
        checkDestination(destination, requestor, true)
        assert destination.userID == httpAddr
    }

    void checkDestination(ActiveParticipantType destination, boolean requestor, boolean userIdRequired = true) {
        // This should be something useful, but it isn't fully specified yet (see CP-402)
        if (userIdRequired) {
            assert destination.userID != null && destination.userID != ''
        }
        assert destination.userIsRequestor == requestor
        assert destination.networkAccessPointTypeCode
        assert destination.networkAccessPointID
        // This will be required soon:
        // assert source.@AlternativeUserID != null && source.@AlternativeUserID != ''
        checkCode(destination.roleIDCodes[0], '110152', 'DCM')
    }

    void checkAuditSource(AuditSourceIdentificationType auditSource, String sourceId) {
        assert auditSource.auditSourceID == sourceId
    }

    void checkHumanRequestor(ActiveParticipantType human, String id, String name, List<CodedValueType> roles = []) {
        assert human.userIsRequestor
        assert human.userID == id
        assert human.userName == name
        assert human.roleIDCodes.size() == roles.size()
        assert human.isUserIsRequestor()
        roles.eachWithIndex { CodedValueType cvt, int i ->
            assert human.roleIDCodes[i].code == cvt.code
            assert human.roleIDCodes[i].codeSystemName == cvt.codeSystemName
            assert human.roleIDCodes[i].originalText == cvt.originalText
        }
    }

    void checkPatient(ParticipantObjectIdentificationType patient, String... allowedIds = ['id3^^^&1.3&ISO']) {
        assert patient.participantObjectTypeCode == ParticipantObjectTypeCode.Person
        assert patient.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Patient
        checkCode(patient.participantObjectIDTypeCode, '2', 'RFC-3881')
        assert patient.participantObjectID in allowedIds
    }

    void checkQuery(ParticipantObjectIdentificationType query, String iti, String queryText, String queryUuid) {
        assert query.participantObjectTypeCode == ParticipantObjectTypeCode.System
        assert query.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Query
        checkCode(query.participantObjectIDTypeCode, iti, 'IHE Transactions')
        assert query.participantObjectID == queryUuid
        String decoded = new String(query.participantObjectQuery, StandardCharsets.UTF_8)
        assert decoded.contains(queryText)
    }

    void checkUri(ParticipantObjectIdentificationType uri, String docUri, String docUniqueId) {
        assert uri.participantObjectTypeCode == ParticipantObjectTypeCode.System
        assert uri.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Report
        checkCode(uri.participantObjectIDTypeCode, '12', 'RFC-3881')
        assert uri.participantObjectID == docUri
        checkParticipantObjectDetail(uri.participantObjectDetails[0], "bla", docUniqueId)
    }

    void checkDocument(ParticipantObjectIdentificationType doc, String docUniqueId, String homeId, String repoId) {
        assert doc.participantObjectTypeCode == ParticipantObjectTypeCode.System
        assert doc.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Report
        checkCode(doc.participantObjectIDTypeCode, '9', 'RFC-3881')
        assert doc.participantObjectID == docUniqueId

        checkParticipantObjectDetail(doc.participantObjectDetails[0], IHEAuditMessageBuilder.REPOSITORY_UNIQUE_ID, repoId)
        checkParticipantObjectDetail(doc.participantObjectDetails[1], IHEAuditMessageBuilder.IHE_HOME_COMMUNITY_ID, homeId)
    }

    void checkImageDocument(ParticipantObjectIdentificationType doc, String docUniqueId, String homeId, String repoId, String studyId, String seriesId) {
        assert doc.participantObjectTypeCode == ParticipantObjectTypeCode.System
        assert doc.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Report
        checkCode(doc.participantObjectIDTypeCode, '9', 'RFC-3881')
        assert doc.participantObjectID == docUniqueId

        checkParticipantObjectDetail(doc.participantObjectDetails[0], IHEAuditMessageBuilder.STUDY_INSTANCE_UNIQUE_ID, studyId)
        checkParticipantObjectDetail(doc.participantObjectDetails[1], IHEAuditMessageBuilder.SERIES_INSTANCE_UNIQUE_ID, seriesId)
        checkParticipantObjectDetail(doc.participantObjectDetails[2], IHEAuditMessageBuilder.REPOSITORY_UNIQUE_ID, repoId)
        checkParticipantObjectDetail(doc.participantObjectDetails[3], IHEAuditMessageBuilder.IHE_HOME_COMMUNITY_ID, homeId)
    }

    void checkParticipantObjectDetail(TypeValuePairType detail, String expectedType, String expectedValue) {
        assert detail.type == expectedType
        String actualValue = new String(detail.value, StandardCharsets.UTF_8)
        assert expectedValue == actualValue
    }

    void checkSubmissionSet(ParticipantObjectIdentificationType submissionSet) {
        assert submissionSet.participantObjectTypeCode == ParticipantObjectTypeCode.System
        assert submissionSet.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Job
        checkCode(submissionSet.participantObjectIDTypeCode, 'urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd', 'IHE XDS Metadata')
        assert submissionSet.participantObjectID == '123'
    }

    void checkRegistryObjectParticipantObjectDetail(ParticipantObjectIdentificationType detail, String typeCode, String registryObjectUuid) {
        assert detail.participantObjectTypeCode == ParticipantObjectTypeCode.System
        assert detail.participantObjectTypeCodeRole == ParticipantObjectTypeCodeRole.Report
        checkCode(detail.participantObjectIDTypeCode, typeCode, 'IHE XDS Metadata')
        assert detail.participantObjectID == registryObjectUuid
    }
}
