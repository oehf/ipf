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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;

/**
 * All error messages that can occur during validation.
 * @author Jens Riemschneider
 */
public enum ValidationMessage {
    FOLDER_INVALID_AVAILABILITY_STATUS("Invalid availability status for a folder: %1s"),
    SUBMISSION_SET_INVALID_AVAILABILITY_STATUS("Invalid availability status for a submission set: %1s"),
    DOC_ENTRY_INVALID_AVAILABILITY_STATUS("Invalid availability status for a document entry: %1s"),
    INVALID_AVAILABILITY_STATUS("Invalid availability status: %1s"),
    EXACTLY_ONE_SUBMISSION_SET_MUST_EXIST("Exactly one submission set must be specified"),
    INVALID_TITLE_ENCODING("Invalid encoding for document entry title: %1s"),
    TITLE_TOO_LONG("Document entry title too long: %1s"),
    UNIQUE_ID_MISSING("Document entries, folders and submission sets are required to define a unique ID"),
    UNIQUE_ID_TOO_LONG("Unique IDs must not be longer than 128 characters"),
    UNIQUE_ID_NOT_UNIQUE("Duplicate ID found"),
    UUID_NOT_UNIQUE("Duplicate UUID found"),
    DOC_ENTRY_PATIENT_ID_WRONG("Document entry and submission set must define the same patient ID", ErrorCode.PATIENT_ID_DOES_NOT_MATCH),
    FOLDER_PATIENT_ID_WRONG("Folder and submission set must define the same patient ID", ErrorCode.PATIENT_ID_DOES_NOT_MATCH),
    INVALID_ASSOCIATION_TYPE("Unsupported association type"),
    TOO_MANY_SUBMISSION_SET_STATES("Exactly one submission set status must be defined for each association involving a document entry as its source"),
    INVALID_SUBMISSION_SET_STATUS("Association specifies an unknown submission set status label"),
    MISSING_ORIGINAL("Association specifies an original document entry, but it was not provided"),
    SOURCE_UUID_NOT_FOUND("The source of an association for a document relationship did not specify a valid UUID of a document entry contained in the request"),
    WRONG_NUMBER_OF_CLASSIFICATIONS("Unexpected amount of classifications of scheme %1s, allowed = [%2s-%3s], Was = %4s"),
    NO_CLASSIFIED_OBJ("Classification does not classify any object: %1s"),
    NO_CLASSIFICATION_NAME_OBJ("Required a display name element for classification scheme %s and classified object %s. The name is used to communicate the meaning of scheme %2$s to a human reader."),
    WRONG_CLASSIFIED_OBJ("Classification does not classify expected object: %1s, Was = %2s"),
    WRONG_NODE_REPRESENTATION("Classification does not specify its node representation. Scheme = %1s"),
    CX_TOO_MANY_COMPONENTS("Only the ID number and the assigning authority can be defined for a CX value"),
    CX_NEEDS_ID("ID number must be specified for a CX/CXi value"),
    CXI_TOO_MANY_COMPONENTS("Only the ID number, assigning authority, and identifier type code can be defined for a CXi value"),
    CXI_INCOMPLETE_ASSIGNING_AUTHORITY("Either namespace ID or universal ID + universal ID type must be present in a CXi assigning authority"),
    CXI_NEEDS_ID_TYPE_CODE("ID type code must be specified for a CXi value"),
    HD_MUST_NOT_HAVE_NAMESPACE_ID("The namespace ID should not be defined for an assigning authority: %1s"),
    UNIVERSAL_ID_TYPE_MUST_BE_ISO("The universal ID type of an assigning authority must be ISO: %1s"),
    HD_NEEDS_UNIVERSAL_ID("The universal ID must be defined for an assigning authority: %1s"),
    MISSING_EXTERNAL_IDENTIFIER("External identifier value is missing: %1s"),
    INVALID_HASH_CODE("Invalid format of hash code: %1s"),
    INVALID_LANGUAGE_CODE("Invalid format of language code: %1s"),
    OID_TOO_LONG("OID must not be longer than 64 characters: %1s"),
    INVALID_OID("OID contains invalid characters: %1s"),
    INVALID_PID("Invalid HL7 field name (only PID fields are supported): %1s"),
    INVALID_UUID("UUID contains invalid characters: %1s"),
    UNSUPPORTED_PID("PID field with this number is prohibited in XDS: %1s"),
    INVALID_NUMBER_FORMAT("Invalid number format: %1s"),
    RECIPIENT_LIST_EMPTY("Recipient list should not be empty"),
    RECIPIENT_EMPTY("Recipient list value should not be empty"),
    INVALID_RECIPIENT("Invalid format of recipient list element: %1s"),
    SLOT_VALUE_TOO_LONG("Slot value length exceeds ebXML limit in slot: %1s"),
    MISSING_SLOT_NAME("Missing slot name"),
    DUPLICATE_SLOT_NAME("Duplicate slot name: %1s"),
    WRONG_QUERY_SLOT_NAME("Slot name must be preceded by '$': %s"),
    WRONG_NUMBER_OF_SLOT_VALUES("Slot contains incorrect amount of values. Slot = %1s, allowed = [%2s-%3s], Was = %4s"),
    EMPTY_SLOT_VALUE("Slot value is undefined. Slot = %1s"),
    INVALID_TIME("Invalid time format: %1s"),
    NULL_URI("URI slot did not contain a value"),
    EMPTY_URI("Empty URI although slot is specified"),
    INVALID_URI("Invalid URI: %1s"),
    NULL_URI_PART("A URI part did not contain a value"),
    INVALID_URI_PART("Invalid URI part syntax: %1s"),
    MISSING_URI_PART("Missing URI part with index: %1s"),
    PERSON_MISSING_NAME_AND_ID("Either an id number or a name has to be specified for a person: %1s"),
    PERSON_HD_MISSING("If an id number is specified for a person, the assigning authority has to be specified as well: %1s"),
    ORGANIZATION_NAME_MISSING("An organization name has to be specified for an organization: %1s"),
    ORGANIZATION_TOO_MANY_COMPONENTS("An organization should not specify data other than its ID and name"),
    MISSING_DOC_ENTRY_FOR_DOCUMENT("A document was provided without a corresponding document entry. UUID=%1s", ErrorCode.MISSING_DOCUMENT_METADATA),
    MISSING_DOCUMENT_FOR_DOC_ENTRY("A document entry was provided without a corresponding document. UUID=%1s", ErrorCode.MISSING_DOCUMENT),
    DOCUMENT_NOT_ALLOWED_IN_DOC_ENTRY("Document attachment is not allowed in entry with UUID=%1s"),
    UNKNOWN_QUERY_TYPE("Unknown query type: %1s"),
    UNSUPPORTED_QUERY_TYPE("Query type not supported: %s"),
    MISSING_SQL_QUERY_TEXT("Missing SQL query text"),
    UNKNOWN_RETURN_TYPE("Unknown return type: %1s"),
    MISSING_REQUIRED_QUERY_PARAMETER("Missing required query parameter: %1s"),
    INVALID_QUERY_PARAMETER_VALUE("Invalid value for query parameter: %1s"),
    QUERY_PARAMETERS_CANNOT_BE_SET_TOGETHER("Query contains parameters that are mutually exclusive to each other: %1s"),
    TOO_MANY_VALUES_FOR_QUERY_PARAMETER("Too many values for query parameter: %1s"),
    PARAMETER_VALUE_NOT_STRING("Query parameter value is not specified as a string: %1s"),
    PARAMETER_VALUE_NOT_STRING_LIST("Query parameter value is not specified as a list of strings: %1s"),
    STUDY_INSTANCE_UID_MUST_BE_SPECIFIED("The imaging document Study Instance UID is missing"),
    TRANSFER_SYNTAX_UID_LIST_MUST_BE_SPECIFIED("The transfer syntax UID list is empty"),
    SERIES_INSTANCE_UID_MUST_BE_SPECIFIED("The imaging document Series Instance UID is missing"),
    REPO_ID_MUST_BE_SPECIFIED("The repository Unique ID is missing"),
    DOC_ID_MUST_BE_SPECIFIED("The document Unique ID is missing"),
    ON_DEMAND_DOC_ID_MUST_DIFFER("New ID of the document should differ from the On-Demand document entry ID"),
    WRONG_DOCUMENT_ENTRY_TYPE("Wrong document entry type (stable/on-demand): %s"),
    MIME_TYPE_MUST_BE_SPECIFIED("The document MIME type is missing"),
    INVALID_STATUS_IN_RESPONSE("Invalid status in response"),
    INVALID_ERROR_INFO_IN_RESPONSE("Invalid error info in response"),
    INVALID_ERROR_CODE_IN_RESPONSE("Invalid error code in response"),
    INVALID_SEVERITY_IN_RESPONSE("Invalid severity in response"),
    MISSING_OBJ_REF("Missing object reference"),
    DEPRECATED_OBJ_CANNOT_BE_TRANSFORMED("A deprecated entry cannot be transformed or appended", ErrorCode.REGISTRY_DEPRECATED_DOCUMENT_ERROR),
    DIFFERENT_HASH_CODE_IN_RESUBMISSION("A document was resubmitted with a different hash code", ErrorCode.NON_IDENTICAL_HASH),
    UNKNOWN_PATIENT_ID("Patient Id is unknown", ErrorCode.UNKNOWN_PATIENT_ID),
    INCORRECT_HASH("Hash of submitted document does not match value supplied in the meta data"),
    INCORRECT_SIZE("Size of submitted document does not match value supplied in the meta data"),
    DOC_CODE_NOT_ALLOWED_ON_HAS_MEMBER("HasMember association may not specify a relationship type"),
    RESULT_NOT_SINGLE_PATIENT("Query result contains entries for multiple patients", ErrorCode.RESULT_NOT_SINGLE_PATIENT),
    HOME_COMMUNITY_ID_MUST_BE_SPECIFIED("Home community ID is missing", ErrorCode.MISSING_HOME_COMMUNITY_ID),
    WRONG_QUERY_RETURN_TYPE("Wrong query return type: %s"),
    AUTHOR_INCOMPLETE("At least an authorPerson, authorTelecommunication, or authorInstitution sub-attribute must be present in %s"),
    MISSING_SNAPSHOT_ASSOCIATION("IsSnapshot Association specifies an %s document entry, but it was not provided: %s"),
    WRONG_SNAPSHOT_ASSOCIATION_STATUS("The targetObject DocumentEntry has not availabilityStatus of Approved"),
    LOGICAL_ID_MISSING("Logical ID is missing on Update Document Set request"),
    LOGICAL_ID_EQUALS_ENTRY_UUID("Logical ID: %s required to have a different value then entryUUID: %s on Update Document Set request"),
    LOGICAL_ID_SAME("Same logical ID %s appears more than once in a single request"),
    VERSION_INFO_MISSING("Version Info is missing on Update Document Set request"),
    MISSING_PREVIOUS_VERSION("A previous version must be defined for each association involving a document update"),
    MISSING_ORIGINAL_STATUS("UpdateAvailabilityStatus Association must specify an original document status"),
    MISSING_NEW_STATUS("UpdateAvailabilityStatus Association must specify an new document status"),
    MISSING_HAS_MEMBER_ASSOCIATION("No SubmissionSet to DocumentEntry/Folder HasMember Association found for the entry: %s"),
    MISSING_SUBMISSION_SET("Association specifies a sourceObject: %s submission set, but it was not provided"),
    MISSING_ASSOCIATION("Association specifies a targetObject: %s association, but it was not provided"),
    OBJECT_SHALL_NOT_BE_SPECIFIED("%s shall not be specified."),
    EMPTY_REFERENCE_LIST("No object references specified for %s"),
    WRONG_TELECOM_USE("Wrong telecom use code (XTN-2): %s"),
    WRONG_TELECOM_TYPE("Wrong telecom type code (XTN-3): %s"),
    MISSING_TELECOM_PARAM("A required telecom parameter is missing: %s"),
    INCONSISTENT_TELECOM_PARAM("Inconsistent telecom parameters: %s"),
    SUBMISSION_SET_STATUS_MANDATORY("Submission set status is mandatory for each association involving a submission set as a target and document entry as its source"),
    INVALID_DOCUMENT_AVAILABILITY("Unsupported document availability: %s"),
    WRONG_REFERENCE_ID_TYPE("Wrong reference ID type: %s"),
    LIMITED_METADATA_REQUIRED("limitedMetadata classification must be provided in %s"),
    LIMITED_METADATA_PROHIBITED("limitedMetadata classification is not allowed in %s"),
    MISSING_FOLDER_NAME("Folder name not provided in %s"),
    TIME_PRECISION_TOO_LOW("Precision of the timestamp is too low: %s"),
    ASSOCIATION_ID_MISSING("Attribute 'id' must be provided in the Association"),
    ;


    private final String text;
    private final ErrorCode errorCode;
    
    private ValidationMessage(String text, ErrorCode errorCode) {
        this.text = text;
        this.errorCode = errorCode;
    }

    private ValidationMessage(String text) {
        this.text = text;
        errorCode = null;
    }

    /**
     * @return a textual representation of this message.
     */
    public String getText() {
        return text;
    }

    /**
     * @return the error code associated with this message.
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}