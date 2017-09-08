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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

/**
 * Query parameters used for the stored queries.
 * @author Jens Riemschneider
 */
public enum QueryParameter {
    /** Used to filter {@link DocumentEntry#getPatientId()}. */
    DOC_ENTRY_PATIENT_ID("$XDSDocumentEntryPatientId"),
    /** Used to filter {@link DocumentEntry#getClassCode()}. */
    DOC_ENTRY_CLASS_CODE("$XDSDocumentEntryClassCode"),
    /** Used to filter {@link DocumentEntry#getTypeCode()}. */
    DOC_ENTRY_TYPE_CODE("$XDSDocumentEntryTypeCode"),
    /** Used to filter {@link DocumentEntry#getClassCode()}. */
    DOC_ENTRY_CLASS_CODE_SCHEME("$XDSDocumentEntryClassCodeScheme"),
    /** Used to filter {@link DocumentEntry#getAuthors()}. */
    DOC_ENTRY_AUTHOR_PERSON("$XDSDocumentEntryAuthorPerson"),
    /** Used to filter {@link DocumentEntry#getCreationTime()}. */
    DOC_ENTRY_CREATION_TIME_FROM("$XDSDocumentEntryCreationTimeFrom"),
    /** Used to filter {@link DocumentEntry#getCreationTime()}. */
    DOC_ENTRY_CREATION_TIME_TO("$XDSDocumentEntryCreationTimeTo"),
    /** Used to filter {@link DocumentEntry#getFormatCode()}. */
    DOC_ENTRY_FORMAT_CODE("$XDSDocumentEntryFormatCode"),
    /** Used to filter {@link DocumentEntry#getFormatCode()}. */
    DOC_ENTRY_FORMAT_CODE_SCHEME("$XDSDocumentEntryFormatCodeScheme"),
    /** Used to filter {@link DocumentEntry#getHealthcareFacilityTypeCode()}. */
    DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE("$XDSDocumentEntryHealthcareFacilityTypeCode"),
    /** Used to filter {@link DocumentEntry#getHealthcareFacilityTypeCode()}. */
    DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_SCHEME("$XDSDocumentEntryHealthcareFacilityTypeCodeScheme"),
    /** Used to filter {@link DocumentEntry#getPracticeSettingCode()}. */
    DOC_ENTRY_PRACTICE_SETTING_CODE("$XDSDocumentEntryPracticeSettingCode"),
    /** Used to filter {@link DocumentEntry#getPracticeSettingCode()}. */
    DOC_ENTRY_PRACTICE_SETTING_CODE_SCHEME("$XDSDocumentEntryPracticeSettingCodeScheme"),
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
    /** Used to filter {@link DocumentEntry#getEventCodeList()}. */
    DOC_ENTRY_EVENT_CODE_SCHEME("$XDSDocumentEntryEventCodeListScheme"),
    /** Used to filter {@link DocumentEntry#getConfidentialityCodes()}. */
    DOC_ENTRY_CONFIDENTIALITY_CODE("$XDSDocumentEntryConfidentialityCode"),
    /** Used to filter {@link DocumentEntry#getConfidentialityCodes()}. */
    DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME("$XDSDocumentEntryConfidentialityCodeScheme"),
    /** Used to filter {@link DocumentEntry#getEntryUuid()}. */
    DOC_ENTRY_UUID("$XDSDocumentEntryEntryUUID"),
    /** Used to filter {@link DocumentEntry#getUniqueId()}. */
    DOC_ENTRY_UNIQUE_ID("$XDSDocumentEntryUniqueId"),
    /** Used to filter {@link DocumentEntry#getType()}. */
    DOC_ENTRY_TYPE("$XDSDocumentEntryType"),
    /** Used to filter {@link DocumentEntry#referenceIdList}. */
    DOC_ENTRY_REFERENCE_IDS("$XDSDocumentEntryReferenceIdList"),
    /** Used to filter {@link DocumentEntry#documentAvailability}. */
    DOC_ENTRY_DOCUMENT_AVAILABILITY("$XDSDocumentEntryDocumentAvailability"),
    /** Used to filter {@link DocumentEntry#logicalUuid}. */
    DOC_ENTRY_LOGICAL_ID("$XDSDocumentEntryLogicalID"),
     
    /** Used to filter {@link Folder#getCodeList()}. */
    FOLDER_CODES("$XDSFolderCodeList"),
    /** Used to filter {@link Folder#getCodeList()}. */
    FOLDER_CODES_SCHEME("$XDSFolderCodeListScheme"),
    /** Used to filter {@link Folder#getLastUpdateTime()}. */
    FOLDER_LAST_UPDATE_TIME_FROM("$XDSFolderLastUpdateTimeFrom"),
    /** Used to filter {@link Folder#getLastUpdateTime()}. */
    FOLDER_LAST_UPDATE_TIME_TO("$XDSFolderLastUpdateTimeTo"),
    /** Used to filter {@link Folder#getPatientId()}. */
    FOLDER_PATIENT_ID("$XDSFolderPatientId"),
    /** Used to filter {@link Folder#getAvailabilityStatus()}. */
    FOLDER_STATUS("$XDSFolderStatus"),
    /** Used to filter {@link Folder#getEntryUuid()}. */
    FOLDER_UUID("$XDSFolderEntryUUID"),
    /** Used to filter {@link Folder#getUniqueId()}. */
    FOLDER_UNIQUE_ID("$XDSFolderUniqueId"),
    /** Used to filter {@link Folder#logicalUuid}. */
    FOLDER_LOGICAL_ID("$XDSFolderLogicalID"),
    
    /** Used to filter {@link SubmissionSet#getPatientId()}. */
    SUBMISSION_SET_PATIENT_ID("$XDSSubmissionSetPatientId"),
    /** Used to filter {@link SubmissionSet#getSourceId()}. */
    SUBMISSION_SET_SOURCE_ID("$XDSSubmissionSetSourceId"),
    /** Used to filter {@link SubmissionSet#getSubmissionTime()}. */
    SUBMISSION_SET_SUBMISSION_TIME_FROM("$XDSSubmissionSetSubmissionTimeFrom"),
    /** Used to filter {@link SubmissionSet#getSubmissionTime()}. */
    SUBMISSION_SET_SUBMISSION_TIME_TO("$XDSSubmissionSetSubmissionTimeTo"),
    /** Used to filter {@link SubmissionSet#getAuthors()}. */
    SUBMISSION_SET_AUTHOR_PERSON("$XDSSubmissionSetAuthorPerson"),
    /** Used to filter {@link SubmissionSet#getContentTypeCode()}. */
    SUBMISSION_SET_CONTENT_TYPE_CODE("$XDSSubmissionSetContentType"),
    /** Used to filter {@link SubmissionSet#getContentTypeCode()}. */
    SUBMISSION_SET_CONTENT_TYPE_CODE_SCHEME("$XDSSubmissionSetContentTypeScheme"),
    /** Used to filter {@link SubmissionSet#getAvailabilityStatus()}. */
    SUBMISSION_SET_STATUS("$XDSSubmissionSetStatus"),
    /** Used to filter {@link SubmissionSet#getEntryUuid()}. */
    SUBMISSION_SET_UUID("$XDSSubmissionSetEntryUUID"),
    /** Used to filter {@link SubmissionSet#getUniqueId()}. */
    SUBMISSION_SET_UNIQUE_ID("$XDSSubmissionSetUniqueId"),

    /** Used to filter {@link XDSMetaClass#getEntryUuid()}. */
    UUID("$uuid"),
    /** Used to filter {@link XDSMetaClass#getPatientId()}. */
    PATIENT_ID("$patientId"),
    /** Used to filter {@link Association#getAssociationType()}. */
    ASSOCIATION_TYPE("$AssociationTypes"),
    /** Used to filter {@link Association#getAvailabilityStatus()}. */
    ASSOCIATION_STATUS("$XDSAssociationStatus"),
    /** Used to filter {none}. */
    METADATA_LEVEL("$MetadataLevel");
    
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


    /**
     * @param slotName
     *      query slot name.
     * @return
     *      a {@link QueryParameter} element which corresponds to the given
     *      slot name, or <code>null</code> when none found.
     */
    public static QueryParameter valueOfSlotName(String slotName) {
        if (slotName == null) {
            return null;
        }

        for (QueryParameter queryParameter : QueryParameter.values()) {
            if (slotName.equals(queryParameter.getSlotName())) {
                return queryParameter;
            }
        }

        return null;
    }

}
