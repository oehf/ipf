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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.XDSMetaClass;

/**
 * Query parameters used for the stored queries.
 * @author Jens Riemschneider
 */
public enum QueryParameter {
    /** Used to filter {@link DocumentEntry#getPatientID()}. */
    DOC_ENTRY_PATIENT_ID("$XDSDocumentEntryPatientId"),
    /** Used to filter {@link DocumentEntry#getClassCode()}. */
    DOC_ENTRY_CLASS_CODE("$XDSDocumentEntryClassCode"),
    /** Used to filter {@link DocumentEntry#getAuthors()}. */
    DOC_ENTRY_AUTHOR_PERSON("$XDSDocumentEntryAuthorPerson"),
    /** Used to filter {@link DocumentEntry#getCreationTime()}. */
    DOC_ENTRY_CREATION_TIME_FROM("$XDSDocumentEntryCreationTimeFrom"),
    /** Used to filter {@link DocumentEntry#getCreationTime()}. */
    DOC_ENTRY_CREATION_TIME_TO("$XDSDocumentEntryCreationTimeTo"),
    /** Used to filter {@link DocumentEntry#getFormatCode()}. */
    DOC_ENTRY_FORMAT_CODE("$XDSDocumentEntryFormatCode"),
    /** Used to filter {@link DocumentEntry#getHealthcareFacilityTypeCode()}. */
    DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE("$XDSDocumentEntryHealthcareFacilityTypeCode"),
    /** Used to filter {@link DocumentEntry#getPracticeSettingCode()}. */
    DOC_ENTRY_PRACTICE_SETTING_CODE("$XDSDocumentEntryPracticeSettingCode"),
    /** Used to filter {@link DocumentEntry#getServiceStartTime()}. */
    DOC_ENTRY_SERVICE_START_TIME_FROM("$XDSDocumentEntryServiceStartTimeFrom"),
    /** Used to filter {@link DocumentEntry#getServiceStartTime()}. */
    DOC_ENTRY_SERVICE_START_TIME_TO("$XDSDocumentEntryServiceStartTimeTo"),
    /** Used to filter {@link DocumentEntry#getServiceStopTime()}. */
    DOC_ENTRY_SERVICE_STOP_TIME_FROM("$XDSDocumentEntryServiceStopTimeFrom"),
    /** Used to filter {@link DocumentEntry#getServiceStopTime()}. */
    DOC_ENTRY_SERVICE_STOP_TIME_TO("$XDSDocumentEntryServiceStopTimeTo"),
    /** Used to filter {@link DocumentEntry#getAvailabilityStatus()}. */
    DOC_ENTRY_STATUS("$XDSDocumentEntryStatus"),
    /** Used to filter {@link DocumentEntry#getEventCodeList()}. */
    DOC_ENTRY_EVENT_CODE("$XDSDocumentEntryEventCodeList"),
    /** Used to filter {@link DocumentEntry#getConfidentialityCodes()}. */
    DOC_ENTRY_CONFIDENTIALITY_CODE("$XDSDocumentEntryConfidentialityCode"),
    /** Used to filter {@link DocumentEntry#getEntryUUID()}. */
    DOC_ENTRY_UUID("$XDSDocumentEntryEntryUUID"),
    /** Used to filter {@link DocumentEntry#getUniqueID()}. */
    DOC_ENTRY_UNIQUE_ID("$XDSDocumentEntryUniqueId"),
     
    /** Used to filter {@link Folder#getCodeList()}. */
    FOLDER_CODES("$XDSFolderCodeList"),
    /** Used to filter {@link Folder#getLastUpdateTime()}. */
    FOLDER_LAST_UPDATE_TIME_FROM("$XDSFolderLastUpdateTimeFrom"),
    /** Used to filter {@link Folder#getLastUpdateTime()}. */
    FOLDER_LAST_UPDATE_TIME_TO("$XDSFolderLastUpdateTimeTo"),
    /** Used to filter {@link Folder#getPatientID()}. */
    FOLDER_PATIENT_ID("$XDSFolderPatientId"),
    /** Used to filter {@link Folder#getAvailabilityStatus()}. */
    FOLDER_STATUS("$XDSFolderStatus"),
    /** Used to filter {@link Folder#getEntryUUID()}. */
    FOLDER_UUID("$XDSFolderEntryUUID"),
    /** Used to filter {@link Folder#getUniqueID()}. */
    FOLDER_UNIQUE_ID("$XDSFolderUniqueId"),
    
    /** Used to filter {@link SubmissionSet#getPatientID()}. */
    SUBMISSION_SET_PATIENT_ID("$XDSSubmissionSetPatientId"),
    /** Used to filter {@link SubmissionSet#getSourceID()}. */
    SUBMISSION_SET_SOURCE_ID("$XDSSubmissionSetSourceId"),
    /** Used to filter {@link SubmissionSet#getSubmissionTime()}. */
    SUBMISSION_SET_SUBMISSION_TIME_FROM("$XDSSubmissionSetSubmissionTimeFrom"),
    /** Used to filter {@link SubmissionSet#getSubmissionTime()}. */
    SUBMISSION_SET_SUBMISSION_TIME_TO("$XDSSubmissionSetSubmissionTimeTo"),
    /** Used to filter {@link SubmissionSet#getAuthor()}. */
    SUBMISSION_SET_AUTHOR_PERSON("$XDSSubmissionSetAuthorPerson"),
    /** Used to filter {@link SubmissionSet#getContentTypeCode()}. */
    SUBMISSION_SET_CONTENT_TYPE_CODE("$XDSSubmissionSetContentType"),
    /** Used to filter {@link SubmissionSet#getAvailabilityStatus()}. */
    SUBMISSION_SET_STATUS("$XDSSubmissionSetStatus"),
    /** Used to filter {@link SubmissionSet#getEntryUUID()}. */
    SUBMISSION_SET_UUID("$XDSSubmissionSetEntryUUID"),
    /** Used to filter {@link SubmissionSet#getUniqueID()}. */
    SUBMISSION_SET_UNIQUE_ID("$XDSSubmissionSetUniqueId"),
    
    /** Used to filter {@link XDSMetaClass#getEntryUUID()}. */
    UUID("$uuid"),
    /** Used to filter {@link XDSMetaClass#getPatientID()}. */
    PATIENT_ID("$patientId"),
    /** Used to filter {@link XDSMetaClass#getHomeCommunityId()}. */
    HOME("$homeCommunityId"),
    /** Used to filter {@link Association#getAssociationType()}. */
    ASSOCIATION_TYPE("$AssociationTypes");
    
    private final String slotName;
    
    private QueryParameter(String slotName) {
        this.slotName = slotName;
    }

    /**
     * @return name of the slot used in the ebXML representation of the parameter.
     */
    public String getSlotName() {
        return slotName;
    }
}
