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
package org.openehealth.ipf.commons.ihe.xds.core.requests;

import static java.util.Objects.requireNonNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;

import jakarta.xml.bind.annotation.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Request object for the Query Registry and Registry Stored Query transactions.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryRegistry")
@XmlRootElement(name = "queryRegistry")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class QueryRegistry implements Serializable {
    @Serial
    private static final long serialVersionUID = -7089029668323133489L;

    @XmlElementRefs({
            @XmlElementRef(type = FetchQuery.class),
            @XmlElementRef(type = FindDocumentsByReferenceIdQuery.class),
            @XmlElementRef(type = FindDocumentsForMultiplePatientsQuery.class),
            @XmlElementRef(type = FindDocumentsQuery.class),
            @XmlElementRef(type = FindFoldersForMultiplePatientsQuery.class),
            @XmlElementRef(type = FindFoldersQuery.class),
            @XmlElementRef(type = FindSubmissionSetsQuery.class),
            @XmlElementRef(type = GetAllQuery.class),
            @XmlElementRef(type = GetAssociationsQuery.class),
            @XmlElementRef(type = GetDocumentsAndAssociationsQuery.class),
            @XmlElementRef(type = GetDocumentsQuery.class),
            @XmlElementRef(type = GetFolderAndContentsQuery.class),
            @XmlElementRef(type = GetFoldersForDocumentQuery.class),
            @XmlElementRef(type = GetFoldersQuery.class),
            @XmlElementRef(type = GetRelatedDocumentsQuery.class),
            @XmlElementRef(type = GetSubmissionSetAndContentsQuery.class),
            @XmlElementRef(type = GetSubmissionSetsQuery.class)})

    @Getter private Query query;
    @XmlAttribute
    @Getter @Setter private QueryReturnType returnType = QueryReturnType.OBJECT_REF;

    /**
     * Identifier of this request, which the registry echoes in {@link
     * org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse#getRequestId()}.
     * <p>
     * Pure correlation, no server state: it lets a sequence of paged requests be recognised as one in
     * logs and audit records. A continuation token, if one is ever agreed with a registry, would be a
     * different thing and would ride in the response slot list rather than here.
     */
    @XmlAttribute
    @Getter @Setter private String requestId;

    /**
     * Index of the first result the registry is to return, or null for the whole result set.
     * <p>
     * Maps onto the ebRS 3.0 {@code startIndex} attribute. ITI-18 §3.18.4.1.2.6 notes that stored
     * queries and ebRS pagination "have never been reconciled and are not recommended for use
     * together", so this is a bilaterally agreed extension: a registry that does not implement it
     * returns the whole result set, as it always did.
     * <p>
     * A window is only meaningful over a defined order. Set {@link
     * org.openehealth.ipf.commons.ihe.xds.core.requests.query.StoredQuery#setSortOrder} as well, with
     * {@link org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrder#withStableTiebreaker()},
     * or successive pages may repeat and skip entries.
     */
    @XmlAttribute
    @Getter @Setter private Integer startIndex;

    /**
     * Maximum number of results the registry is to return, or null for the whole result set. Maps onto
     * the ebRS 3.0 {@code maxResults} attribute; see {@link #getStartIndex()}.
     */
    @XmlAttribute
    @Getter @Setter private Integer maxResults;

    /**
     * For JAXB serialization only.
     */
    public QueryRegistry() {
    }

    /**
     * Constructs the request.
     * @param query
     *          the query to use. Cannot be <code>null</code>.
     */
    public QueryRegistry(Query query) {
        requireNonNull(query, "query cannot be null");
        this.query = query;
    }

    /**
     * Constructs the request.
     * <p>
     * Kept explicitly rather than generated: the class has gained the request id and the paging
     * attributes, and an all-args constructor would both grow its signature whenever another one is
     * added and offer three positional values that read better as setters.
     *
     * @param query      the query to use. Cannot be <code>null</code>.
     * @param returnType the type of objects the query is to return.
     */
    public QueryRegistry(Query query, QueryReturnType returnType) {
        this(query);
        this.returnType = returnType;
    }

}
