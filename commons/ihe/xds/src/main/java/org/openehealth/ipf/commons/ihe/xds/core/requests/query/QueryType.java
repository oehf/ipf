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
package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * All possible query types.
 * @author Jens Riemschneider
 */
@XmlType(name = "QueryType")
@XmlEnum(String.class)
public enum QueryType {
    /** Runs a SQL query. */
    @XmlEnumValue("Sql") SQL("sql", SqlQuery.class),
    /** Searches for documents. */
    @XmlEnumValue("FindDocuments") FIND_DOCUMENTS("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d", FindDocumentsQuery.class),
    /** Searches for submission sets. */
    @XmlEnumValue("FindSubmissionSets") FIND_SUBMISSION_SETS("urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9", FindSubmissionSetsQuery.class),
    /** Searches for folders. */
    @XmlEnumValue("FindFolders") FIND_FOLDERS("urn:uuid:958f3006-baad-4929-a4de-ff1114824431", FindFoldersQuery.class),
    /** Returns everything. */
    @XmlEnumValue("GetAll") GET_ALL("urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3", GetAllQuery.class),
    /** Returns specific documents. */
    @XmlEnumValue("GetDocuments") GET_DOCUMENTS("urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4", GetDocumentsQuery.class),
    /** Returns specific folders. */
    @XmlEnumValue("GetFolders") GET_FOLDERS("urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4", GetFoldersQuery.class),
    /** Returns specific associations. */
    @XmlEnumValue("GetAssociations") GET_ASSOCIATIONS("urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155", GetAssociationsQuery.class),
    /** Returns specific documents and their associations. */
    @XmlEnumValue("GetDocumentsAndAssociations") GET_DOCUMENTS_AND_ASSOCIATIONS("urn:uuid:bab9529a-4a10-40b3-a01f-f68a615d247a", GetDocumentsAndAssociationsQuery.class),
    /** Returns specific submission sets. */
    @XmlEnumValue("GetSubmissionSets") GET_SUBMISSION_SETS("urn:uuid:51224314-5390-4169-9b91-b1980040715a", GetSubmissionSetsQuery.class),
    /** Returns specific submission sets and their contents. */
    @XmlEnumValue("GetSubmissionSetAndContents") GET_SUBMISSION_SET_AND_CONTENTS("urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83", GetSubmissionSetAndContentsQuery.class),
    /** Returns specific folders and their contents. */
    @XmlEnumValue("GetFolderAndContents") GET_FOLDER_AND_CONTENTS("urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7", GetFolderAndContentsQuery.class),
    /** Returns folders for a specific document. */
    @XmlEnumValue("GetFoldersForDocument") GET_FOLDERS_FOR_DOCUMENT("urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578", GetFoldersForDocumentQuery.class),
    /** Returns all documents with a given relation to a specified entry. */
    @XmlEnumValue("GetRelatedDocuments") GET_RELATED_DOCUMENTS("urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6", GetRelatedDocumentsQuery.class),
    /** Cross-Community Fetch query (ITI-63). */
    @XmlEnumValue("Fetch") FETCH("urn:uuid:f2072993-9478-41df-a603-8f016706efe8", FetchQuery.class);

    private final String id;
    private final Class<? extends Query> type; 
    
    private QueryType(String id, Class<? extends Query> type) {
        this.id = id;
        this.type = type;
    }

    /**
     * @return the ID of the query.
     */
    public String getId() {
        return id;
    }
    
    /**
     * @return the class implementing the query. 
     */
    public Class<? extends Query> getType() {
        return type;
    }

    /**
     * Returns a query type by its id.
     * @param id
     *          the id. Can be <code>null</code>.
     * @return the type. <code>null</code> if the id is <code>null</code>.
     */
    public static QueryType valueOfId(String id) {
        if (id == null) {
            return null;
        }

        for (QueryType type : values()) {
            if (id.equals(type.getId())) {
                return type;
            }
        }
        
        throw new XDSMetaDataException(ValidationMessage.UNKNOWN_QUERY_TYPE, id);
    }
}
