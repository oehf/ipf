/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.commons.audit;

/**
 * XML Names used for marshalling/unmarshalling audit records
 *
 * @author Christian Ohr
 * @since 4.0
 */
public interface XMLNames {

    String ACCESSION = "Accession";
    String ACTIVE_PARTICIPANT = "ActiveParticipant";
    String ALTERNATIVE_USER_ID = "AlternativeUserID";
    String ANONYMIZED = "Anonymized";
    String AUDIT_ENTERPRISE_SITE_ID = "AuditEnterpriseSiteID";
    String AUDIT_MESSAGE = "AuditMessage";
    String AUDIT_SOURCE_ID = "AuditSourceID";
    String AUDIT_SOURCE_IDENTIFICATION = "AuditSourceIdentification";
    String AUDIT_SOURCE_TYPE_CODE = "AuditSourceTypeCode";
    String ENCRYPTED = "Encrypted";
    String EVENT_ACTION_CODE = "EventActionCode";
    String EVENT_DATE_TIME = "EventDateTime";
    String EVENT_ID = "EventID";
    String EVENT_IDENTIFICATION = "EventIdentification";
    String EVENT_OUTCOME_DESCRIPTION = "EventOutcomeDescription";
    String EVENT_OUTCOME_INDICATOR = "EventOutcomeIndicator";
    String EVENT_TYPE_CODE = "EventTypeCode";
    String INSTANCE = "Instance";
    String MEDIA_IDENTIFIER = "MediaIdentifier";
    String MEDIA_TYPE = "MediaType";
    String MPPS = "MPPS";
    String NETWORK_ACCESS_POINT_ID = "NetworkAccessPointID";
    String NETWORK_ACCESS_POINT_TYPE_CODE = "NetworkAccessPointTypeCode";
    String NUMBER = "Number";
    String NUMBER_OF_INSTANCES = "NumberOfInstances";
    String PARTICIPANT_OBJECT_CONTAINS_STUDY = "ParticipantObjectContainsStudy";
    String PARTICIPANT_OBJECT_DATA_LIFE_CYCLE = "ParticipantObjectDataLifeCycle";
    String PARTICIPANT_OBJECT_DESCRIPTION = "ParticipantObjectDescription";
    String PARTICIPANT_OBJECT_DETAIL = "ParticipantObjectDetail";
    String PARTICIPANT_OBJECT_ID = "ParticipantObjectID";
    String PARTICIPANT_OBJECT_IDENTIFICATION = "ParticipantObjectIdentification";
    String PARTICIPANT_OBJECT_ID_TYPE_CODE = "ParticipantObjectIDTypeCode";
    String PARTICIPANT_OBJECT_NAME = "ParticipantObjectName";
    String PARTICIPANT_OBJECT_QUERY = "ParticipantObjectQuery";
    String PARTICIPANT_OBJECT_SENSITIVITY = "ParticipantObjectSensitivity";
    String PARTICIPANT_OBJECT_TYPE_CODE = "ParticipantObjectTypeCode";
    String PARTICIPANT_OBJECT_TYPE_CODE_ROLE = "ParticipantObjectTypeCodeRole";
    String PURPOSE_OF_USE = "PurposeOfUse";
    String ROLE_ID_CODE = "RoleIDCode";
    String SOP_CLASS = "SOPClass";
    String STUDY_IDS = "StudyIDs";
    String UID = "UID";
    String USER_ID = "UserID";
    String USER_IS_REQUESTOR = "UserIsRequestor";
    String USER_NAME = "UserName";

    String CODE = "code";
    String CODE_SYSTEM_NAME = "codeSystemName";
    String CSD_CODE = "csd-code";
    String DISPLAY_NAME = "displayName";
    String ORIGINAL_TEXT = "originalText";
    String TYPE = "type";
    String VALUE = "value";
}
