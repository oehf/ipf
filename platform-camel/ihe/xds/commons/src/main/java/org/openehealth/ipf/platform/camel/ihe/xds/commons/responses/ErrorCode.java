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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.responses;

/**
 * Error codes specified by the XDS specification.
 * @author Jens Riemschneider
 */
public enum ErrorCode {
    MISSING_DOCUMENT("XDSMissingDocument"),
    MISSING_DOCUMENT_METADATA("XDSMissingDocumentMetadata"),
    REGISTRY_NOT_AVAILABLE("XDSRegistryNotAvailable"),
    REGISTRY_ERROR("XDSRegistryError"),
    REPOSITORY_ERROR("XDSRepositoryError"),
    REGISTRY_DUPLICATE_UNIQUE_ID_IN_MESSAGE("XDSRegistryDuplicateUniqueIdInMessage"),
    REPOSITORY_DUPLICATE_UNIQUE_ID_IN_MESSAGE("XDSRepositoryDuplicateUniqueIdInMessage"),
    DUPLICATE_UNIQUE_ID_IN_REGISTRY("XDSDuplicateUniqueIdInRegistry"),
    NON_IDENTICAL_HASH("XDSNonIdenticalHash"),
    REGISTRY_BUSY("XDSRegistryBusy"),
    REPOSITORY_BUSY("XDSRepositoryBusy"),
    REGISTRY_OUT_OF_RESOURCES("XDSRegistryOutOfResources"),
    REPOSITORY_OUT_OF_RESOURCES("XDSRepositoryOutOfResources"),
    REGISTRY_METADATA_ERROR("XDSRegistryMetadataError"),
    REPOSITORY_METADATA_ERROR("XDSRepositoryMetadataError"),
    TOO_MANY_RESULTS("XDSTooManyResults"),
    EXTRA_METADATA_NO_SAVED("XDSExtraMetadataNotSaved"),
    UNKNOWN_PATIENT_ID("XDSUnknownPatientId"),
    PATIENT_ID_DOES_NOT_MATCH("XDSPatientIdDoesNotMatch"),
    UNKNOWN_STORED_QUERY("XDSUnknownStoredQuery"),
    STORED_QUERY_MISSING_PARAM("XDSStoredQueryMissingParam"),
    STORED_QUERY_PARAM_NUMBER("XDSStoredQueryParamNumber"),
    SQL_ERROR("XDSSqlError"),
    REGISTRY_DEPRECATED_DOCUMENT_ERROR("XDSRegistryDeprecatedDocumentError"),
    UNKNOWN_REPOSITORY_ID("XDSUnknownRepositoryId");
    
    private final String representation;
    
    private ErrorCode(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }
}
