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
package org.openehealth.ipf.commons.ihe.xds.requests.query;

import static org.apache.commons.lang.Validate.notNull;

import java.io.Serializable;

/**
 * Base class for all query requests.
 * @author Jens Riemschneider
 */
public abstract class Query implements Serializable {
    private static final long serialVersionUID = 7597105342752455732L;
    
    private final QueryType type;

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected Query(QueryType type) {
        notNull(type, "type cannot be null");
        this.type = type;
    }

    /**
     * @return the type of the query.
     */
    public QueryType getType() {
        return type;
    }

    /**
     * Visitor interface used for this class to implement the visitor pattern.
     */
    public interface Visitor {
        void visit(SqlQuery query);
        void visit(FindDocumentsQuery query);
        void visit(FindFoldersQuery query);
        void visit(GetSubmissionSetsQuery query);
        void visit(GetSubmissionSetAndContentsQuery query);
        void visit(GetRelatedDocumentsQuery query);
        void visit(GetFoldersQuery query);
        void visit(GetFoldersForDocumentQuery query);
        void visit(GetFolderAndContentsQuery query);
        void visit(GetDocumentsQuery query);
        void visit(GetDocumentsAndAssociationsQuery query);
        void visit(GetAssociationsQuery query);
        void visit(GetAllQuery query);
        void visit(FindSubmissionSetsQuery query);
    }
    
    /**
     * Accept a visitor to process an instance of this class.
     * @param visitor
     *          the visitor implementation.
     */
    public abstract void accept(Visitor visitor);
}
