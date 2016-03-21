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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsEnum;

import javax.xml.bind.annotation.XmlType;

/**
 * All possible query types.
 * @author Jens Riemschneider
 * @author Michael Ottati
 */
@XmlType(name = "QueryType")
@EqualsAndHashCode(callSuper = true)
public class QueryType extends XdsEnum {
    private static final long serialVersionUID = 2812318793269066784L;

    /** Runs a SQL query. */
    public static final QueryType SQL = new QueryType(Type.OFFICIAL, "sql", SqlQuery.class);
    /** Searches for documents. */
    public static final QueryType FIND_DOCUMENTS = new QueryType(Type.OFFICIAL, "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d", FindDocumentsQuery.class);
    /** Searches for documents (Multi Patient Variety). */
    public static final QueryType FIND_DOCUMENTS_MPQ = new QueryType(Type.OFFICIAL, "urn:uuid:3d1bdb10-39a2-11de-89c2-2f44d94eaa9f", FindDocumentsForMultiplePatientsQuery.class);
    /** Searches for submission sets. */
    public static final QueryType FIND_SUBMISSION_SETS = new QueryType(Type.OFFICIAL, "urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9", FindSubmissionSetsQuery.class);
    /** Searches for folders. */
    public static final QueryType FIND_FOLDERS = new QueryType(Type.OFFICIAL, "urn:uuid:958f3006-baad-4929-a4de-ff1114824431", FindFoldersQuery.class);
    /** Searches for documents by reference IDs */
    public static final QueryType FIND_DOCUMENTS_BY_REFERENCE_ID = new QueryType(Type.OFFICIAL, "urn:uuid:12941a89-e02e-4be5-967c-ce4bfc8fe492", FindDocumentsByReferenceIdQuery.class);
    /** Searches for folders (Multi Patient Variety). */
    public static final QueryType FIND_FOLDERS_MPQ = new QueryType(Type.OFFICIAL, "urn:uuid:50d3f5ac-39a2-11de-a1ca-b366239e58df", FindFoldersForMultiplePatientsQuery.class);
    /** Returns everything. */
    public static final QueryType GET_ALL = new QueryType(Type.OFFICIAL, "urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3", GetAllQuery.class);
    /** Returns specific documents. */
    public static final QueryType GET_DOCUMENTS = new QueryType(Type.OFFICIAL, "urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4", GetDocumentsQuery.class);
    /** Returns specific folders. */
    public static final QueryType GET_FOLDERS = new QueryType(Type.OFFICIAL, "urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4", GetFoldersQuery.class);
    /** Returns specific associations. */
    public static final QueryType GET_ASSOCIATIONS = new QueryType(Type.OFFICIAL, "urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155", GetAssociationsQuery.class);
    /** Returns specific documents and their associations. */
    public static final QueryType GET_DOCUMENTS_AND_ASSOCIATIONS = new QueryType(Type.OFFICIAL, "urn:uuid:bab9529a-4a10-40b3-a01f-f68a615d247a", GetDocumentsAndAssociationsQuery.class);
    /** Returns specific submission sets. */
    public static final QueryType GET_SUBMISSION_SETS = new QueryType(Type.OFFICIAL, "urn:uuid:51224314-5390-4169-9b91-b1980040715a", GetSubmissionSetsQuery.class);
    /** Returns specific submission sets and their contents. */
    public static final QueryType GET_SUBMISSION_SET_AND_CONTENTS = new QueryType(Type.OFFICIAL, "urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83", GetSubmissionSetAndContentsQuery.class);
    /** Returns specific folders and their contents. */
    public static final QueryType GET_FOLDER_AND_CONTENTS = new QueryType(Type.OFFICIAL, "urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7", GetFolderAndContentsQuery.class);
    /** Returns folders for a specific document. */
    public static final QueryType GET_FOLDERS_FOR_DOCUMENT = new QueryType(Type.OFFICIAL, "urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578", GetFoldersForDocumentQuery.class);
    /** Returns all documents with a given relation to a specified entry. */
    public static final QueryType GET_RELATED_DOCUMENTS = new QueryType(Type.OFFICIAL, "urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6", GetRelatedDocumentsQuery.class);
    /** Cross-Community Fetch query (ITI-63). */
    public static final QueryType FETCH = new QueryType(Type.OFFICIAL, "urn:uuid:f2072993-9478-41df-a603-8f016706efe8", FetchQuery.class);

    public static final QueryType[] OFFICIAL_VALUES = {SQL, FIND_DOCUMENTS, FIND_DOCUMENTS_MPQ,
        FIND_SUBMISSION_SETS, FIND_FOLDERS, FIND_DOCUMENTS_BY_REFERENCE_ID, FIND_FOLDERS_MPQ,
        GET_ALL, GET_DOCUMENTS, GET_FOLDERS, GET_ASSOCIATIONS, GET_DOCUMENTS_AND_ASSOCIATIONS,
        GET_SUBMISSION_SETS, GET_SUBMISSION_SET_AND_CONTENTS, GET_FOLDER_AND_CONTENTS,
        GET_FOLDERS_FOR_DOCUMENT, GET_RELATED_DOCUMENTS, FETCH};

    /** Class implementing the query. */
    @Getter private final Class<? extends Query> implementingClass;
    
    public QueryType(XdsEnum.Type type, String ebXML, Class<? extends Query> implementingClass) {
        super(type, ebXML);
        this.implementingClass = implementingClass;
    }

    @Override
    public String getJaxbValue() {
        return (getType() == Type.OFFICIAL) ? getImplementingClass().getSimpleName() : getEbXML30();
    }
}
