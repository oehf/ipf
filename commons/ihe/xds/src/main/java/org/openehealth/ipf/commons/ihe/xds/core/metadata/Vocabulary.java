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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

/**
 * List of XDS related vocabulary constants.
 * @author Jens Riemschneider
 */
public abstract class Vocabulary {
    private Vocabulary() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }
    
    /**
     * XDSDocumentEntry objectType for Stable Document Entries
     * @deprecated use {@link DocumentEntryType#STABLE}.
     */
    @Deprecated
    public static final String STABLE_DOC_ENTRY = DocumentEntryType.STABLE.getUuid();
    /**
     * XDSDocumentEntry classification node.
     * @deprecated by IHE CP-ITI-544.  Please use {@link #STABLE_DOC_ENTRY} instead.
     */
    @Deprecated
    public static final String DOC_ENTRY_CLASS_NODE = STABLE_DOC_ENTRY;
    /**
     * XDSDocumentEntry objectType for On-Demand Document Entries
     * @deprecated use {@link DocumentEntryType#ON_DEMAND}.
     */
    @Deprecated
    public static final String ON_DEMAND_DOC_ENTRY = DocumentEntryType.ON_DEMAND.getUuid();


    public enum DisplayNameUsage {REQUIRED, OPTIONAL}
    
    /** Author External Classification Scheme of the Document Entry */
    public static final String DOC_ENTRY_AUTHOR_CLASS_SCHEME =
        "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";

    /** classCode External Classification Scheme of the Document Entry */
    public static final String DOC_ENTRY_CLASS_CODE_CLASS_SCHEME = 
        "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";

    /** confidentialityCode External Classification Scheme of the Document Entry */
    public static final String DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME = 
        "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
    
    /** eventCode External Classification Scheme of the Document Entry */
    public static final String DOC_ENTRY_EVENT_CODE_CLASS_SCHEME = 
        "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";    

    /** formatCode External Classification Scheme of the Document Entry */
    public static final String DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME = 
        "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    
    /** healthcareFacitilityTypeCode External Classification Scheme of the Document Entry */
    public static final String DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME = 
        "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
    
    /** practiceSettingCode ExternalIdentifier of the Document Entry */
    public static final String DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME =
        "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    
    /** typeCode ExternalIdentifier of the Document Entry */
    public static final String DOC_ENTRY_TYPE_CODE_CLASS_SCHEME =
        "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    
    /** patientId ExternalIdentifier of the Document Entry */
    public static final String DOC_ENTRY_PATIENT_ID_EXTERNAL_ID =
        "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
    
    /** uniqueId ExternalIdentifier of the Document Entry */
    public static final String DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID =
        "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    
    /** Localized String used for the patient ID external identifier of a document entry */
    public static final String DOC_ENTRY_LOCALIZED_STRING_PATIENT_ID = "XDSDocumentEntry.patientId";

    /** Localized String used for the unique ID external identifier of a document entry */
    public static final String DOC_ENTRY_LOCALIZED_STRING_UNIQUE_ID = "XDSDocumentEntry.uniqueId";


    
    
    /** XDSFolder classification node */
    public static final String FOLDER_CLASS_NODE = 
        "urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2";    
    
    /** codeList External Classification Scheme of the folder */
    public static final String FOLDER_CODE_LIST_CLASS_SCHEME =
        "urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5";    
    
    /** patientId ExternalIdentifier of the Folder */
    public static final String FOLDER_PATIENT_ID_EXTERNAL_ID =
        "urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a";
    
    /** uniqueId ExternalIdentifier of the Folder */
    public static final String FOLDER_UNIQUE_ID_EXTERNAL_ID =
        "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a";

    /** Localized String used for the patient ID external identifier of a folder */
    public static final String FOLDER_LOCALIZED_STRING_PATIENT_ID = "XDSFolder.patientId";

    /** Localized String used for the unique ID external identifier of a folder */
    public static final String FOLDER_LOCALIZED_STRING_UNIQUE_ID = "XDSFolder.uniqueId";

    
    
    

    /** XDSSubmissionSet classification node */
    public static final String SUBMISSION_SET_CLASS_NODE = 
        "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd";
    
    /** Author External Classification Scheme of the Submission Set */
    public static final String SUBMISSION_SET_AUTHOR_CLASS_SCHEME = 
        "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d";

    /** contentTypeCode External Classification Scheme of the Submission Set */
    public static final String SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME = 
        "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";    

    /** patientId ExternalIdentifier of the Submission Set */
    public static final String SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID =
        "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    
    /** uniqueId ExternalIdentifier of the Submission Set */
    public static final String SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID =
        "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";

    /** sourceId ExternalIdentifier of the Submission Set */
    public static final String SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID =
        "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832";

    /** Localized String used for the patient ID external identifier of a submission set */
    public static final String SUBMISSION_SET_LOCALIZED_STRING_PATIENT_ID = "XDSSubmissionSet.patientId";

    /** Localized String used for the unique ID external identifier of a submission set */
    public static final String SUBMISSION_SET_LOCALIZED_STRING_UNIQUE_ID = "XDSSubmissionSet.uniqueId";

    /** Localized String used for the source ID external identifier of a submission set */
    public static final String SUBMISSION_SET_LOCALIZED_STRING_SOURCE_ID = "XDSSubmissionSet.sourceId";

    
    
    /** docCode External Classification Scheme of the Association */
    public static final String ASSOCIATION_DOC_CODE_CLASS_SCHEME = 
        "urn:uuid:abd807a3-4432-4053-87b4-fd82c643d1f3";    
    
        
    
    
    /** Name of the slot that is used for author persons */
    public static final String SLOT_NAME_AUTHOR_PERSON = "authorPerson";
    
    /** Name of the slot that is used for author institutions */
    public static final String SLOT_NAME_AUTHOR_INSTITUTION = "authorInstitution";
        
    /** Name of the slot that is used for author roles */
    public static final String SLOT_NAME_AUTHOR_ROLE = "authorRole";

    /** Name of the slot that is used for author specialties */
    public static final String SLOT_NAME_AUTHOR_SPECIALTY = "authorSpecialty";

    /** Name of the slot that is used for coding schemes */
    public static final String SLOT_NAME_CODING_SCHEME = "codingScheme";

    /** Name of the slot that is used for the creation time */
    public static final String SLOT_NAME_CREATION_TIME = "creationTime";
    
    /** Name of the slot that is used for the last update time */
    public static final String SLOT_NAME_LAST_UPDATE_TIME = "lastUpdateTime";
    
    /** Name of the slot that is used for the hash code */
    public static final String SLOT_NAME_HASH = "hash";    

    /** Name of the slot that is used for the language code */
    public static final String SLOT_NAME_LANGUAGE_CODE = "languageCode";
    
    /** Name of the slot that is used for the legal authenticator */
    public static final String SLOT_NAME_LEGAL_AUTHENTICATOR = "legalAuthenticator";
    
    /** Name of the slot that is used for the service start time */
    public static final String SLOT_NAME_SERVICE_START_TIME = "serviceStartTime";

    /** Name of the slot that is used for the service stop time */
    public static final String SLOT_NAME_SERVICE_STOP_TIME = "serviceStopTime";
    
    /** Name of the slot that is used for the size */
    public static final String SLOT_NAME_SIZE = "size";
    
    /** Name of the slot that is used for the source patient ID */
    public static final String SLOT_NAME_SOURCE_PATIENT_ID = "sourcePatientId";

    /** Name of the slot that is used for the source patient info */
    public static final String SLOT_NAME_SOURCE_PATIENT_INFO = "sourcePatientInfo";
    
    /** Name of the slot that is used for the URI */
    public static final String SLOT_NAME_URI = "URI";
    
    /** Name of the slot that is used for the unique ID of the repository (using only in XDS.b) */
    public static final String SLOT_NAME_REPOSITORY_UNIQUE_ID = "repositoryUniqueId";
    
    /** Name of the slot that is used for the intended recipients */
    public static final String SLOT_NAME_INTENDED_RECIPIENT = "intendedRecipient";
    
    /** Name of the slot that is used for the submission time */
    public static final String SLOT_NAME_SUBMISSION_TIME = "submissionTime";
    
    /** Name of the slot that is used for association labeling of the submission set */
    public static final String SLOT_NAME_SUBMISSION_SET_STATUS = "SubmissionSetStatus";
    
    
    /** Universal ID Type to be used for all HD data types used with XDS */
    public static final String UNIVERSAL_ID_TYPE_OID = "ISO";

    
    
    /** Node representation for the author classification */
    public static final String NODE_REPRESENTATION_AUTHOR = "";
    
    /** Node representation for the class code classification */
    public static final String NODE_REPRESENTATION_CLASSCODE = "classCode";    

    /** Node representation for the class code classification */
    public static final String NODE_REPRESENTATION_CONFIDENTIALITY_CODE = "confidentialityCode";
}
