/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests;

/**
 * Query parameters used for the stored queries.
 * @author Jens Riemschneider
 */
public enum QueryParameter {
    DOC_ENTRY_PATIENT_ID("$XDSDocumentEntryPatientId"),
    DOC_ENTRY_CLASS_CODE("$XDSDocumentEntryClassCode"),
    DOC_ENTRY_AUTHOR_PERSON("$XDSDocumentEntryAuthorPerson"),
    DOC_ENTRY_CREATION_TIME_FROM("$XDSDocumentEntryCreationTimeFrom"),
    DOC_ENTRY_CREATION_TIME_TO("$XDSDocumentEntryCreationTimeTo"),
    DOC_ENTRY_FORMAT_CODE("$XDSDocumentEntryFormatCode"),
    DOC_ENTRY_HEALTH_CARE_FACILITY_TYPE_CODE("$XDSDocumentEntryHealthcareFacilityTypeCode"),
    DOC_ENTRY_PRACTICE_SETTING_CODE("$XDSDocumentEntryPracticeSettingCode"),
    DOC_ENTRY_SERVICE_START_TIME_FROM("$XDSDocumentEntryServiceStartTimeFrom"),
    DOC_ENTRY_SERVICE_START_TIME_TO("$XDSDocumentEntryServiceStartTimeTo"),
    DOC_ENTRY_SERVICE_STOP_TIME_FROM("$XDSDocumentEntryServiceStopTimeFrom"),
    DOC_ENTRY_SERVICE_STOP_TIME_TO("$XDSDocumentEntryServiceStopTimeTo"),
    DOC_ENTRY_STATUS("$XDSDocumentEntryStatus"),
    DOC_ENTRY_EVENT_CODE("$XDSDocumentEntryEventCodeList"),
    DOC_ENTRY_CONFIDENTIALITY_CODE("$XDSDocumentEntryConfidentialityCode"),
    DOC_ENTRY_UUID("$XDSDocumentEntryEntryUUID"),
    DOC_ENTRY_UNIQUE_ID("$XDSDocumentEntryUniqueId"),
     
    FOLDER_CODES("$XDSFolderCodeList"),
    FOLDER_LAST_UPDATE_TIME_FROM("$XDSFolderLastUpdateTimeFrom"),
    FOLDER_LAST_UPDATE_TIME_TO("$XDSFolderLastUpdateTimeTo"),
    FOLDER_PATIENT_ID("$XDSFolderPatientId"),
    FOLDER_STATUS("$XDSFolderStatus"),
    FOLDER_UUID("$XDSFolderEntryUUID"),
    FOLDER_UNIQUE_ID("$XDSFolderUniqueId"),
    
    SUBMISSION_SET_PATIENT_ID("$XDSSubmissionSetPatientId"),
    SUBMISSION_SET_SOURCE_ID("$XDSSubmissionSetSourceId"),
    SUBMISSION_SET_SUBMISSION_TIME_FROM("$XDSSubmissionSetSubmissionTimeFrom"),
    SUBMISSION_SET_SUBMISSION_TIME_TO("$XDSSubmissionSetSubmissionTimeTo"),
    SUBMISSION_SET_AUTHOR_PERSON("$XDSSubmissionSetAuthorPerson"),
    SUBMISSION_SET_CONTENT_TYPE_CODE("$XDSSubmissionSetContentType"),
    SUBMISSION_SET_STATUS("$XDSSubmissionSetStatus"),
    SUBMISSION_SET_UUID("$XDSSubmissionSetEntryUUID"),
    SUBMISSION_SET_UNIQUE_ID("$XDSSubmissionSetUniqueId"),
    
    UUID("$uuid"),
    PATIENT_ID("$patientId"),
    HOME("$homeCommunityId"),
    ASSOCIATION_TYPE("$AssociationTypes")
    ;
    
    private final String slotName;
    
    private QueryParameter(String slotName) {
        this.slotName = slotName;
    }

    public String getSlotName() {
        return slotName;
    }
}
