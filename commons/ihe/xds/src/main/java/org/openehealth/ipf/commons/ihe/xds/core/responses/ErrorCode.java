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

import lombok.EqualsAndHashCode;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsEnum;

/**
 * Error codes specified by the XDS specification.
 * @author Jens Riemschneider
 */
@EqualsAndHashCode(callSuper = true)
public class ErrorCode extends XdsEnum {
    private static final long serialVersionUID = 5512420009058775338L;

    /* ----- codes from IHE ITI TF, Revision 7.0, Vol. 3, Table 4.1-11 ----- */
    /** Document entry exists in metadata with no corresponding attached document. */
    public static final ErrorCode MISSING_DOCUMENT = new ErrorCode(Type.OFFICIAL, "XDSMissingDocument");
    /** MIME package contains MIME part with content-id header not found in metadata. */
    public static final ErrorCode MISSING_DOCUMENT_METADATA = new ErrorCode(Type.OFFICIAL, "XDSMissingDocumentMetadata");
    /** Repository was unable to access the registry. */
    public static final ErrorCode REGISTRY_NOT_AVAILABLE = new ErrorCode(Type.OFFICIAL, "XDSRegistryNotAvailable");
    /** Internal error in registry. */
    public static final ErrorCode REGISTRY_ERROR = new ErrorCode(Type.OFFICIAL, "XDSRegistryError");
    /** Internal error in repository. */
    public static final ErrorCode REPOSITORY_ERROR = new ErrorCode(Type.OFFICIAL, "XDSRepositoryError");
    /** The registry found a unique ID value that was used more than once within the submission. 
     *  The Code Context indicates the duplicate unique ID. */
    public static final ErrorCode REGISTRY_DUPLICATE_UNIQUE_ID_IN_MESSAGE = new ErrorCode(Type.OFFICIAL, "XDSRegistryDuplicateUniqueIdInMessage");
    /** The repository found a unique ID value that was used more than once within the submission. 
     *  The Code Context indicates the duplicate unique ID. */
    public static final ErrorCode REPOSITORY_DUPLICATE_UNIQUE_ID_IN_MESSAGE = new ErrorCode(Type.OFFICIAL, "XDSRepositoryDuplicateUniqueIdInMessage");
    /** A unique ID received for a submission set or folder was not unique within the registry. 
     *  The code context indicates the value of the non-unique ID and if it was a folder or submission set. 
     *  Never returned for a document entry. */
    public static final ErrorCode DUPLICATE_UNIQUE_ID_IN_REGISTRY = new ErrorCode(Type.OFFICIAL, "XDSDuplicateUniqueIdInRegistry");
    /** Document being registered was a duplicate (unique ID already in registry) but the hash codes 
     *  do not match. The code context indicates the unique ID. */
    public static final ErrorCode NON_IDENTICAL_HASH = new ErrorCode(Type.OFFICIAL, "XDSNonIdenticalHash");
    /** Document being registered was a duplicate (uniqueId already in registry)
     *  but size does not match. CodeContext indicates UniqueId. */
    public static final ErrorCode NON_IDENTICAL_SIZE = new ErrorCode(Type.OFFICIAL, "XDSNonIdenticalSize");
    /** Too much activity in the registry to process the request. */
    public static final ErrorCode REGISTRY_BUSY = new ErrorCode(Type.OFFICIAL, "XDSRegistryBusy");
    /** Too much activity in the repository to process the request. */
    public static final ErrorCode REPOSITORY_BUSY = new ErrorCode(Type.OFFICIAL, "XDSRepositoryBusy");
    /** Resources are too low within the registry to process the request. */
    public static final ErrorCode REGISTRY_OUT_OF_RESOURCES = new ErrorCode(Type.OFFICIAL, "XDSRegistryOutOfResources");
    /** Resources are too low within the repository to process the request. */
    public static final ErrorCode REPOSITORY_OUT_OF_RESOURCES = new ErrorCode(Type.OFFICIAL, "XDSRepositoryOutOfResources");
    /** The registry detected an error in the meta data. The actor name indicates where  
     *  error detected. The code context indicates the nature of the problem. */
    public static final ErrorCode REGISTRY_METADATA_ERROR = new ErrorCode(Type.OFFICIAL, "XDSRegistryMetadataError");
    /** The repository detected an error in the meta data. The actor name indicates where  
     *  error detected. The code context indicates the nature of the problem. */
    public static final ErrorCode REPOSITORY_METADATA_ERROR = new ErrorCode(Type.OFFICIAL, "XDSRepositoryMetadataError");
    /** A request produced too many results to finish the request. */
    public static final ErrorCode TOO_MANY_RESULTS = new ErrorCode(Type.OFFICIAL, "XDSTooManyResults");
    /** Warning returned if extra meta data was present but not saved in the registry. */
    public static final ErrorCode EXTRA_METADATA_NOT_SAVED = new ErrorCode(Type.OFFICIAL, "XDSExtraMetadataNotSaved");
    /** The patient ID referenced in the meta data is not known to the registry actor
     *  via the Patient Identity Feed or is unknown because of patient identifier merge 
     *  or other reasons. The code context includes the value of the problematic patient ID. */
    public static final ErrorCode UNKNOWN_PATIENT_ID = new ErrorCode(Type.OFFICIAL, "XDSUnknownPatientId");
    /** A patient ID that is required to be identical in the document entries, folders and 
     *  submission sets contained in the request did not match. The code context indicates
     *  the value of the patient ID and the nature of the conflict. */
    public static final ErrorCode PATIENT_ID_DOES_NOT_MATCH = new ErrorCode(Type.OFFICIAL, "XDSPatientIdDoesNotMatch");
    /** The query ID provided in the request is not recognized. */
    public static final ErrorCode UNKNOWN_STORED_QUERY = new ErrorCode(Type.OFFICIAL, "XDSUnknownStoredQuery");
    /** A required parameter to a stored query is missing. */
    public static final ErrorCode STORED_QUERY_MISSING_PARAM = new ErrorCode(Type.OFFICIAL, "XDSStoredQueryMissingParam");
    /** A parameter which only accepts a single value is coded with multiple values. */
    public static final ErrorCode STORED_QUERY_PARAM_NUMBER = new ErrorCode(Type.OFFICIAL, "XDSStoredQueryParamNumber");
    /** A register transaction was rejected because it submitted an association referencing
     *  a deprecated document. */
    public static final ErrorCode REGISTRY_DEPRECATED_DOCUMENT_ERROR = new ErrorCode(Type.OFFICIAL, "XDSRegistryDeprecatedDocumentError");
    /** The unique ID of a repository could not be resolved to a valid document repository
     *  or the value does not match that of the document repository. */
    public static final ErrorCode UNKNOWN_REPOSITORY_ID = new ErrorCode(Type.OFFICIAL, "XDSUnknownRepositoryId");
    /** The document associated with the DocumentUniqueId is not available. */
    public static final ErrorCode DOCUMENT_UNIQUE_ID_ERROR = new ErrorCode(Type.OFFICIAL, "XDSDocumentUniqueIdError");
    /** A query resulted in returning information about multiple patients,
     *  which is forbidden because of security reasons. */
    public static final ErrorCode RESULT_NOT_SINGLE_PATIENT = new ErrorCode(Type.OFFICIAL, "XDSResultNotSinglePatient");

    /* --- codes for XDR --- */
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Folder semantics. */
    public static final ErrorCode PARTIAL_FOLDER_CONTENT_NOT_PROCESSED = new ErrorCode(Type.OFFICIAL, "PartialFolderContentNotProcessed");
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Replacement semantics. */
    public static final ErrorCode PARTIAL_REPLACE_CONTENT_NOT_PROCESSED = new ErrorCode(Type.OFFICIAL, "PartialReplaceContentNotProcessed");
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Transform semantics. */
    public static final ErrorCode PARTIAL_TRANSFORM_NOT_PROCESSED = new ErrorCode(Type.OFFICIAL, "PartialTransformNotProcessed");
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Append semantics. */
    public static final ErrorCode PARTIAL_APPEND_CONTENT_NOT_PROCESSED = new ErrorCode(Type.OFFICIAL, "PartialAppendContentNotProcessed");
    /** An XDR Document Recipient did not process some part of the content.
     *  Specifically the parts not processed are Transform and Replace semantics. */
    public static final ErrorCode PARTIAL_TRANSFORM_REPLACE_NOT_PROCESSED = new ErrorCode(Type.OFFICIAL, "PartialTransformReplaceNotProcessed");
    /** An XDR Recipient queued the document for future manual matching to a patient. */
    public static final ErrorCode DOCUMENT_QUEUED = new ErrorCode(Type.OFFICIAL, "DocumentQueued");
    /** The recipient has rejected this submission because it detected that one of the documents does
     * not match the metadata or has failed other requirements for the document content. */
    public static final ErrorCode INVALID_DOCUMENT_CONTENT = new ErrorCode(Type.OFFICIAL, "InvalidDocumentContent");

    /* --- codes for XCA --- */
    /** A value for the homeCommunityId is not recognized */
    public static final ErrorCode UNKNOWN_COMMUNITY = new ErrorCode(Type.OFFICIAL, "XDSUnknownCommunity");
    /** A value for the homeCommunityId is required and has not been specified */
    public static final ErrorCode MISSING_HOME_COMMUNITY_ID = new ErrorCode(Type.OFFICIAL, "XDSMissingHomeCommunityId");
    /** A community which would have been contacted was not available */
    public static final ErrorCode UNAVAILABLE_COMMUNITY = new ErrorCode(Type.OFFICIAL, "XDSUnavailableCommunity");

    /* --- codes for XCF --- */
    /** The requested document cannot be provided due to a transcoding/translation error. */
    public static final ErrorCode TRANSCODING_ERROR = new ErrorCode(Type.OFFICIAL, "TranscodingError");

    /* --- codes for MetaDataUpdate/Delete ITI-57/62 --- */
    /* --- When reporting this errors, the codeContext attribute of the RegistryError element shall ---
       --- contain the id attribute of the metadata object causing the error. --- */

    /** General metadata update error. Use only when more specific error code is not available or appropriate */
    public static final ErrorCode META_DATA_UPDATE_ERROR = new ErrorCode(Type.OFFICIAL, "XDSMetadataUpdateError");
    /**  Update encountered error where Patient IDs did not match*/
    public static final ErrorCode PATIENT_ID_RECONCILIATION_ERROR = new ErrorCode(Type.OFFICIAL, "XDSPatientIDReconciliationError");
    /**  Document Registry/Recipient cannot decode the requested metadata update.*/
    public static final ErrorCode META_DATA_UPDATE_OPERATION_ERROR = new ErrorCode(Type.OFFICIAL, "XDSMetadataUpdateOperationError");
    /**  The version number included in the update request did not match the existing object.
     *   One cause of this is multiple simultaneous update attempts.*/
    public static final ErrorCode META_DATA_VERSION_ERROR = new ErrorCode(Type.OFFICIAL, "XDSMetadataVersionError");
    /**  An entryUUID passed in the Delete Document Set transaction does not exist in the recipient system.*/
    public static final ErrorCode UNRESOLVED_REFERENCE_EXCEPTION = new ErrorCode(Type.OFFICIAL, "UnresolvedReferenceException");
    /**  An entryUUID passed in the Delete Document Set transaction is referenced by an Association
     *   sourceObject or targetObject attribute.*/
    public static final ErrorCode REFERENCE_EXISTS_EXCEPTION = new ErrorCode(Type.OFFICIAL, "ReferencesExistException");

    /* --- codes for ITI-16 (obsolete XDS.a profile) --- */
    /** An error occurred when executing an SQL query. */
    public static final ErrorCode SQL_ERROR = new ErrorCode(Type.OFFICIAL, "XDSSqlError");

    public static final ErrorCode[] OFFICIAL_VALUES = {
            MISSING_DOCUMENT, MISSING_DOCUMENT_METADATA, REGISTRY_NOT_AVAILABLE, REGISTRY_ERROR,
            REPOSITORY_ERROR, REGISTRY_DUPLICATE_UNIQUE_ID_IN_MESSAGE, REPOSITORY_DUPLICATE_UNIQUE_ID_IN_MESSAGE,
            DUPLICATE_UNIQUE_ID_IN_REGISTRY, NON_IDENTICAL_HASH, NON_IDENTICAL_SIZE, REGISTRY_BUSY,
            REPOSITORY_BUSY, REGISTRY_OUT_OF_RESOURCES, REPOSITORY_OUT_OF_RESOURCES, REGISTRY_METADATA_ERROR,
            REPOSITORY_METADATA_ERROR, TOO_MANY_RESULTS, EXTRA_METADATA_NOT_SAVED, UNKNOWN_PATIENT_ID,
            PATIENT_ID_DOES_NOT_MATCH, UNKNOWN_STORED_QUERY, STORED_QUERY_MISSING_PARAM, STORED_QUERY_PARAM_NUMBER,
            REGISTRY_DEPRECATED_DOCUMENT_ERROR, UNKNOWN_REPOSITORY_ID, DOCUMENT_UNIQUE_ID_ERROR,
            RESULT_NOT_SINGLE_PATIENT, PARTIAL_FOLDER_CONTENT_NOT_PROCESSED, PARTIAL_REPLACE_CONTENT_NOT_PROCESSED,
            PARTIAL_TRANSFORM_NOT_PROCESSED, PARTIAL_APPEND_CONTENT_NOT_PROCESSED,
            PARTIAL_TRANSFORM_REPLACE_NOT_PROCESSED, DOCUMENT_QUEUED, INVALID_DOCUMENT_CONTENT,
            UNKNOWN_COMMUNITY, MISSING_HOME_COMMUNITY_ID, UNAVAILABLE_COMMUNITY, TRANSCODING_ERROR,
            META_DATA_UPDATE_ERROR, PATIENT_ID_RECONCILIATION_ERROR, META_DATA_UPDATE_OPERATION_ERROR,
            META_DATA_VERSION_ERROR, UNRESOLVED_REFERENCE_EXCEPTION, REFERENCE_EXISTS_EXCEPTION, SQL_ERROR};


    public ErrorCode(Type type, String ebXML) {
        super(type, ebXML);
    }

    @Override
    public String getJaxbValue() {
        return getEbXML30();
    }
}
