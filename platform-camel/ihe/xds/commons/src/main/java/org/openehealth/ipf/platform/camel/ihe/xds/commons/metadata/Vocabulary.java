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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata;

/**
 * List of XDS related vocabulary constants.
 * @author Jens Riemschneider
 */
public abstract class Vocabulary {
    private Vocabulary() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }
    
    /** Universal ID Type to be used for all HD data types used with XDS */
    public static final String UNIVERSAL_ID_TYPE_OID = "ISO";
    
    
    
    /** XDSDocumentEntry classification node */
    public static final String DOC_ENTRY_CLASS_NODE = 
        "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
    
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
    
    /** patientId ExternalIdentifier of the Document Entry */
    public static final String DOC_ENTRY_PATIENT_ID_EXTERNAL_ID =
        "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
    
    /** practiceSettingCode ExternalIdentifier of the Document Entry */
    public static final String DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME =
        "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    
    
    
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
    
    
    
    
    /** Node representation for the author classification */
    public static final String NODE_REPRESENTATION_AUTHOR = "";
    
    /** Node representation for the class code classification */
    public static final String NODE_REPRESENTATION_CLASSCODE = "classCode";    

    /** Node representation for the class code classification */
    public static final String NODE_REPRESENTATION_CONFIDENTIALITY_CODE = "confidentialityCode";
    
    
    /** Localized String used for the patient ID external identifier */
    public static final String LOCALIZED_STRING_PATIENT_ID = "XDSDocumentEntry.patientId";
}
