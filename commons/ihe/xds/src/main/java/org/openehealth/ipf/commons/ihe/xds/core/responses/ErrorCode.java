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
package org.openehealth.ipf.commons.ihe.xds.core.responses;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Error codes specified by the XDS specification.
 * @author Jens Riemschneider
 */
@XmlType(name = "ErrorCode")
@XmlEnum(String.class)
public enum ErrorCode {

    /* ----- codes from IHE ITI TF, Revision 7.0, Vol. 3, Table 4.1-11 ----- */
    /** Document entry exists in metadata with no corresponding attached document. */
    @XmlEnumValue("XDSMissingDocument") MISSING_DOCUMENT("XDSMissingDocument"),
    /** MIME package contains MIME part with content-id header not found in metadata. */
    @XmlEnumValue("XDSMissingDocumentMetadata") MISSING_DOCUMENT_METADATA("XDSMissingDocumentMetadata"),
    /** Repository was unable to access the registry. */
    @XmlEnumValue("XDSRegistryNotAvailable") REGISTRY_NOT_AVAILABLE("XDSRegistryNotAvailable"),
    /** Internal error in registry. */
    @XmlEnumValue("XDSRegistryError") REGISTRY_ERROR("XDSRegistryError"),
    /** Internal error in repository. */
    @XmlEnumValue("XDSRepositoryError") REPOSITORY_ERROR("XDSRepositoryError"),
    /** The registry found a unique ID value that was used more than once within the submission. 
     *  The Code Context indicates the duplicate unique ID. */
    @XmlEnumValue("XDSRegistryDuplicateUniqueIdInMessage") REGISTRY_DUPLICATE_UNIQUE_ID_IN_MESSAGE("XDSRegistryDuplicateUniqueIdInMessage"),
    /** The repository found a unique ID value that was used more than once within the submission. 
     *  The Code Context indicates the duplicate unique ID. */
    @XmlEnumValue("XDSRepositoryDuplicateUniqueIdInMessage") REPOSITORY_DUPLICATE_UNIQUE_ID_IN_MESSAGE("XDSRepositoryDuplicateUniqueIdInMessage"),
    /** A unique ID received for a submission set or folder was not unique within the registry. 
     *  The code context indicates the value of the non-unique ID and if it was a folder or submission set. 
     *  Never returned for a document entry. */
    @XmlEnumValue("XDSDuplicateUniqueIdInRegistry") DUPLICATE_UNIQUE_ID_IN_REGISTRY("XDSDuplicateUniqueIdInRegistry"),
    /** Document being registered was a duplicate (unique ID already in registry) but the hash codes 
     *  do not match. The code context indicates the unique ID. */
    @XmlEnumValue("XDSNonIdenticalHash") NON_IDENTICAL_HASH("XDSNonIdenticalHash"),
    /** Document being registered was a duplicate (uniqueId already in registry)
     *  but size does not match. CodeContext indicates UniqueId. */
    @XmlEnumValue("XDSNonIdenticalSize") NON_IDENTICAL_SIZE("XDSNonIdenticalSize"),
    /** Too much activity in the registry to process the request. */
    @XmlEnumValue("XDSRegistryBusy") REGISTRY_BUSY("XDSRegistryBusy"),
    /** Too much activity in the repository to process the request. */
    @XmlEnumValue("XDSRepositoryBusy") REPOSITORY_BUSY("XDSRepositoryBusy"),
    /** Resources are too low within the registry to process the request. */
    @XmlEnumValue("XDSRegistryOutOfResources") REGISTRY_OUT_OF_RESOURCES("XDSRegistryOutOfResources"),
    /** Resources are too low within the repository to process the request. */
    @XmlEnumValue("XDSRepositoryOutOfResources") REPOSITORY_OUT_OF_RESOURCES("XDSRepositoryOutOfResources"),
    /** The registry detected an error in the meta data. The actor name indicates where  
     *  error detected. The code context indicates the nature of the problem. */
    @XmlEnumValue("XDSRegistryMetadataError") REGISTRY_METADATA_ERROR("XDSRegistryMetadataError"),
    /** The repository detected an error in the meta data. The actor name indicates where  
     *  error detected. The code context indicates the nature of the problem. */
    @XmlEnumValue("XDSRepositoryMetadataError") REPOSITORY_METADATA_ERROR("XDSRepositoryMetadataError"),
    /** A request produced too many results to finish the request. */
    @XmlEnumValue("XDSTooManyResults") TOO_MANY_RESULTS("XDSTooManyResults"),
    /** Warning returned if extra meta data was present but not saved in the registry. */
    @XmlEnumValue("XDSExtraMetadataNotSaved") EXTRA_METADATA_NO_SAVED("XDSExtraMetadataNotSaved"),
    /** The patient ID referenced in the meta data is not known to the registry actor 
     *  via the Patient Identity Feed or is unknown because of patient identifier merge 
     *  or other reasons. The code context includes the value of the problematic patient ID. */
    @XmlEnumValue("XDSUnknownPatientId") UNKNOWN_PATIENT_ID("XDSUnknownPatientId"),
    /** A patient ID that is required to be identical in the document entries, folders and 
     *  submission sets contained in the request did not match. The code context indicates
     *  the value of the patient ID and the nature of the conflict. */
    @XmlEnumValue("XDSPatientIdDoesNotMatch") PATIENT_ID_DOES_NOT_MATCH("XDSPatientIdDoesNotMatch"),
    /** The query ID provided in the request is not recognized. */
    @XmlEnumValue("XDSUnknownStoredQuery") UNKNOWN_STORED_QUERY("XDSUnknownStoredQuery"),
    /** A required parameter to a stored query is missing. */
    @XmlEnumValue("XDSStoredQueryMissingParam") STORED_QUERY_MISSING_PARAM("XDSStoredQueryMissingParam"),
    /** A parameter which only accepts a single value is coded with multiple values. */
    @XmlEnumValue("XDSStoredQueryParamNumber") STORED_QUERY_PARAM_NUMBER("XDSStoredQueryParamNumber"),
    /** A register transaction was rejected because it submitted an association referencing
     *  a deprecated document. */
    @XmlEnumValue("XDSRegistryDeprecatedDocumentError") REGISTRY_DEPRECATED_DOCUMENT_ERROR("XDSRegistryDeprecatedDocumentError"),
    /** The unique ID of a repository could not be resolved to a valid document repository
     *  or the value does not match that of the document repository. */
    @XmlEnumValue("XDSUnknownRepositoryId") UNKNOWN_REPOSITORY_ID("XDSUnknownRepositoryId"),
    /** The document associated with the DocumentUniqueId is not available. */
    @XmlEnumValue("XDSDocumentUniqueIdError") DOCUMENT_UNIQUE_ID_ERROR("XDSDocumentUniqueIdError"),
    /** A query resulted in returning information about multiple patients,
     *  which is forbidden because of security reasons. */
    @XmlEnumValue("XDSResultNotSinglePatient") RESULT_NOT_SINGLE_PATIENT("XDSResultNotSinglePatient"),

    /* --- codes for XDR --- */
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Folder semantics. */
    @XmlEnumValue("PartialFolderContentNotProcessed") PARTIAL_FOLDER_CONTENT_NOT_PROCESSED("PartialFolderContentNotProcessed"),
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Replacement semantics. */
    @XmlEnumValue("PartialReplaceContentNotProcessed") PARTIAL_REPLACE_CONTENT_NOT_PROCESSED("PartialReplaceContentNotProcessed"),
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Transform semantics. */
    @XmlEnumValue("PartialTransformNotProcessed") PARTIAL_TRANSFORM_NOT_PROCESSED("PartialTransformNotProcessed"),
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Append semantics. */
    @XmlEnumValue("PartialAppendContentNotProcessed") PARTIAL_APPEND_CONTENT_NOT_PROCESSED("PartialAppendContentNotProcessed"),
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Transform and Replace semantics. */
    @XmlEnumValue("PartialTransformReplaceNotProcessed") PARTIAL_TRANSFORM_REPLACE_NOT_PROCESSED("PartialTransformReplaceNotProcessed"),
    /** An XDR Recipient queued the document for future manual matching to a patient. */
    @XmlEnumValue("DocumentQueued") DOCUMENT_QUEUED("DocumentQueued"),

    /* --- codes for XCA --- */
    /** A value for the homeCommunityId is not recognized */
    @XmlEnumValue("XDSUnknownCommunity") UNKNOWN_COMMUNITY("XDSUnknownCommunity"),
    /** A value for the homeCommunityId is required and has not been specified */
    @XmlEnumValue("XDSMissingHomeCommunityId") MISSING_HOME_COMMUNITY_ID("XDSMissingHomeCommunityId"),
    /** A community which would have been contacted was not available */
    @XmlEnumValue("XDSUnavailableCommunity") UNAVAILABLE_COMMUNITY("XDSUnavailableCommunity"),

    /* --- codes for XCF --- */
    /** The requested document cannot be provided due to a transcoding/translation error. */
    @XmlEnumValue("TranscodingError") TRANSCODING_ERROR("TranscodingError"),

    /* --- codes for ITI-16 (obsolete XDS.a profile) --- */
    /** An error occurred when executing an SQL query. */
    @XmlEnumValue("XDSSqlError") SQL_ERROR("XDSSqlError"),

    /* --- special value for custom user-defined error codes --- */
    @XmlEnumValue("_UserDefined") _USER_DEFINED("_UserDefined");


    private final String opcode;
    
    private ErrorCode(String opcode) {
        this.opcode = opcode;
    }

    /**
     * @return string representation for usage in ebXML values.
     */
    public String getOpcode() {
        return opcode;
    }

    /**
     * <code>null</code>-safe version of {@link #getOpcode()}.
     * @param errorCode
     *          the error code. Can be <code>null</code>.
     * @return the string representation or <code>null</code> if errorCode was <code>null</code>.
     */
    public static String getOpcode(ErrorCode errorCode) {
        return errorCode != null ? errorCode.getOpcode() : null;
    }
 
    /**
     * Returns the error code that corresponds to the given opcode.
     * @param opcode
     *          the opcode. Can be <code>null</code>.
     * @return the error code.
     *      <code>null</code> when the opcode is <code>null</code> or empty,
     *      {@link #_USER_DEFINED} when the code is not a standard one.
     */
    public static ErrorCode valueOfOpcode(String opcode) {
        if ((opcode == null) || (opcode.trim().length() == 0)) {
            return null;
        }
        
        for (ErrorCode code : values()) {
            if (opcode.equals(code.getOpcode())) {
                return code;
            }
        }
        
        return _USER_DEFINED;
    }
}
