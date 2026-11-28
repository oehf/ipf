/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.tutorials.xds.model;

import lombok.Getter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Message body of the ITI-18 routes while a stored query is being answered.
 * <p>
 * Besides the request and the response under construction, it holds the intermediate results that
 * the multistep queries (GetFoldersForDocument, GetRelatedDocuments, Get...AndContents) collect
 * before they arrive at the objects they return. Each intermediate result has a field of its own, so
 * that the steps of a query can only exchange what is declared here.
 *
 * @author Christian Ohr
 * @since 6.2
 */
@Getter
@ToString(of = {"req", "resp"})
public class QueryContext {

    private final QueryRegistry req;
    private final QueryResponse resp;

    /** The document entry a GetFoldersForDocument or GetRelatedDocuments query starts from. */
    private final List<DocumentEntry> doc = new ArrayList<>();

    /** UUIDs of the source objects of the associations found so far. */
    private final List<String> sources = new ArrayList<>();

    /** UUIDs of the target objects of the associations found so far. */
    private final List<String> targets = new ArrayList<>();

    /** UUIDs of the members of the folder or submission set a Get...AndContents query asks for. */
    private final List<String> members = new ArrayList<>();

    /** Documents related to {@link #doc} as the source of an association. */
    private final List<DocumentEntry> sourceDocs = new ArrayList<>();

    /** Documents related to {@link #doc} as the target of an association. */
    private final List<DocumentEntry> targetDocs = new ArrayList<>();

    public QueryContext(QueryRegistry req, QueryResponse resp) {
        this.req = Objects.requireNonNull(req, "req");
        this.resp = Objects.requireNonNull(resp, "resp");
    }

    /**
     * @param type the query type, or a supertype, that declares the parameters the caller reads
     * @return the stored query of the request
     * @throws ClassCastException if the query is of another type, which means it was routed to a search
     *                            for another query
     */
    public <Q extends Query> Q query(Class<Q> type) {
        return type.cast(req.getQuery());
    }
}
