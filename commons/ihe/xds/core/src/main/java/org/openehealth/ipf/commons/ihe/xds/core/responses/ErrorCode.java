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

/**
 * Error codes specified by the XDS specification.
 * @author Jens Riemschneider
 */
public enum ErrorCode {
    /** Document entry exists in metadata with no corresponding attached document. */
    MISSING_DOCUMENT("XDSMissingDocument"),
    /** MIME package contains MIME part with content-id header not found in metadata. */
    MISSING_DOCUMENT_METADATA("XDSMissingDocumentMetadata"),
    /** Repository was unable to access the registry. */
    REGISTRY_NOT_AVAILABLE("XDSRegistryNotAvailable"),
    /** Internal error in registry. */
    REGISTRY_ERROR("XDSRegistryError"),
    /** Internal error in repository. */
    REPOSITORY_ERROR("XDSRepositoryError"),
    /** The registry found a unique ID value that was used more than once within the submission. 
     *  The Code Context indicates the duplicate unique ID. */
    REGISTRY_DUPLICATE_UNIQUE_ID_IN_MESSAGE("XDSRegistryDuplicateUniqueIdInMessage"),
    /** The repository found a unique ID value that was used more than once within the submission. 
     *  The Code Context indicates the duplicate unique ID. */
    REPOSITORY_DUPLICATE_UNIQUE_ID_IN_MESSAGE("XDSRepositoryDuplicateUniqueIdInMessage"),
    /** A unique ID received for a submission set or folder was not unique within the registry. 
     *  The code context indicates the value of the non-unique ID and if it was a folder or submission set. 
     *  Never returned for a document entry. */
    DUPLICATE_UNIQUE_ID_IN_REGISTRY("XDSDuplicateUniqueIdInRegistry"),
    /** Document being registered was a duplicate (unique ID already in registry) but the hash codes 
     *  do not match. The code context indicates the unique ID. */
    NON_IDENTICAL_HASH("XDSNonIdenticalHash"),
    /** Too much activity in the registry to process the request. */
    REGISTRY_BUSY("XDSRegistryBusy"),
    /** Too much activity in the repository to process the request. */
    REPOSITORY_BUSY("XDSRepositoryBusy"),
    /** Resources are too low within the registry to process the request. */
    REGISTRY_OUT_OF_RESOURCES("XDSRegistryOutOfResources"),
    /** Resources are too low within the repository to process the request. */
    REPOSITORY_OUT_OF_RESOURCES("XDSRepositoryOutOfResources"),
    /** The registry detected an error in the meta data. The actor name indicates where  
     *  error detected. The code context indicates the nature of the problem. */
    REGISTRY_METADATA_ERROR("XDSRegistryMetadataError"),
    /** The repository detected an error in the meta data. The actor name indicates where  
     *  error detected. The code context indicates the nature of the problem. */
    REPOSITORY_METADATA_ERROR("XDSRepositoryMetadataError"),
    /** A request produced too many results to finish the request. */
    TOO_MANY_RESULTS("XDSTooManyResults"),
    /** Warning returned if extra meta data was present but not saved in the registry. */
    EXTRA_METADATA_NO_SAVED("XDSExtraMetadataNotSaved"),
    /** The patient ID referenced in the meta data is not known to the registry actor 
     *  via the Patient Identity Feed or is unknown because of patient identifier merge 
     *  or other reasons. The code context includes the value of the problematic patient ID. */
    UNKNOWN_PATIENT_ID("XDSUnknownPatientId"),
    /** A patient ID that is required to be identical in the document entries, folders and 
     *  submission sets contained in the request did not match. The code context indicates
     *  the value of the patient ID and the nature of the conflict. */
    PATIENT_ID_DOES_NOT_MATCH("XDSPatientIdDoesNotMatch"),
    /** The query ID provided in the request is not recognized. */
    UNKNOWN_STORED_QUERY("XDSUnknownStoredQuery"),
    /** A required parameter to a stored query is missing. */
    STORED_QUERY_MISSING_PARAM("XDSStoredQueryMissingParam"),
    /** A parameter which only accepts a single value is coded with multiple values. */
    STORED_QUERY_PARAM_NUMBER("XDSStoredQueryParamNumber"),
    /** An error occurred when executing an SQL query. */
    SQL_ERROR("XDSSqlError"),
    /** A register transaction was rejected because it submitted an association referencing 
     *  a deprecated document. */
    REGISTRY_DEPRECATED_DOCUMENT_ERROR("XDSRegistryDeprecatedDocumentError"),
    /** The unique ID of a repository could not be resolved to a valid document repository 
     *  or the value does not match that of the document repository. */
    UNKNOWN_REPOSITORY_ID("XDSUnknownRepositoryId"),
    /** A query resulted in returning information about multiple patients, which is forbidden
     *  because of security reasons. */ 
    RESULT_NOT_SINGLE_PATIENT("XDSResultNotSinglePatient"),

    /** Some additional error codes from XCA, dealing with homeCommunityIDs */
    
    /** A value for the homeCommunityId is not recognized */
    UNKNOWN_COMMUNITY("XDSUnknownCommunity"),
    /** A value for the homeCommunityId is required and has not been specified */
    MISSING_HOME_COMMUNITY_ID("XDSMissingHomeCommunityId"),
    /** A community which would have been contacted was not available */
    UNAVAILABLE_COMMUNITY("XDSUnavailableCommunity");
    
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
     * @return the error code. <code>null</code> if the opcode was <code>null</code>.
     */
    public static ErrorCode valueOfOpcode(String opcode) {
        if (opcode == null) {
            return null;
        }
        
        for (ErrorCode code : values()) {
            if (opcode.equals(code.getOpcode())) {
                return code;
            }
        }
        
        throw new IllegalArgumentException("Unknown upcode for error code: " + opcode);
    }
}
