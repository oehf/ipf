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
import lombok.ToString;

import static org.apache.commons.lang3.Validate.notNull;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Base class for all query requests.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Query")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public abstract class Query implements Serializable {

    @XmlAttribute
    @Getter private QueryType type;

    /**
     * For JAXB serialization only.
     */
    public Query() {
    }

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
     * Visitor interface used for this class to implement the visitor pattern.
     */
    public interface Visitor {
        // for ITI-18, ITI-38, ITI-51
        void visit(FindDocumentsQuery query);
        void visit(FindDocumentsForMultiplePatientsQuery query);
        void visit(FindFoldersQuery query);
        void visit(FindFoldersForMultiplePatientsQuery query);
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
        void visit(FindDocumentsByReferenceIdQuery query);
        // for ITI-63
        void visit(FetchQuery query);
        // for PHARM-1
        void visit(FindMedicationTreatmentPlansQuery query);
        void visit(FindPrescriptionsQuery query);
        void visit(FindDispensesQuery query);
        void visit(FindMedicationAdministrationsQuery query);
        void visit(FindPrescriptionsForValidationQuery query);
        void visit(FindPrescriptionsForDispenseQuery query);
        void visit(FindMedicationListQuery query);
        // extension DE:GEMATIK
        void visit(FindDocumentsByTitleQuery query);
    }

    /**
     * Accept a visitor to process an instance of this class.
     * @param visitor
     *          the visitor implementation.
     */
    public abstract void accept(Visitor visitor);
}
