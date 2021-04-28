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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import static org.apache.commons.lang3.Validate.notNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query.Visitor;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.*;

/**
 * Query visitor to transform a query into its ebXML representation.
 * @author Jens Riemschneider
 */
final class ToEbXMLVisitor implements Visitor {
    private final EbXMLAdhocQueryRequest ebXML;

    /**
     * Constructs the visitor.
     * @param ebXML
     *          the ebXML result.
     */
    ToEbXMLVisitor(EbXMLAdhocQueryRequest ebXML) {
        notNull(ebXML, "ebXML cannot be null");
        this.ebXML = ebXML;
    }

    @Override
    public void visit(FindDocumentsQuery query) {
        new FindDocumentsQueryTransformer<>().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsForMultiplePatientsQuery query) {
       new FindDocumentsForMultiplePatientsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindFoldersQuery query) {
        new FindFoldersQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindFoldersForMultiplePatientsQuery query) {
        new FindFoldersForMultiplePatientsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetSubmissionSetsQuery query) {
        new GetSubmissionSetsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetSubmissionSetAndContentsQuery query) {
        new GetSubmissionSetAndContentsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetRelatedDocumentsQuery query) {
        new GetRelatedDocumentsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFoldersQuery query) {
        new GetFoldersQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFoldersForDocumentQuery query) {
        new GetFoldersForDocumentQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFolderAndContentsQuery query) {
        new GetFolderAndContentsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetDocumentsQuery query) {
        new GetDocumentsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetDocumentsAndAssociationsQuery query) {
        new GetDocumentsAndAssociationsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetAssociationsQuery query) {
        new GetAssociationsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetAllQuery query) {
        new GetAllQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindSubmissionSetsQuery query) {
        new FindSubmissionSetsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FetchQuery query) {
        new FetchQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsByReferenceIdQuery query) {
        new FindDocumentsByReferenceIdQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationTreatmentPlansQuery query) {
        new FindMedicationTreatmentPlansQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsQuery query) {
        new FindPrescriptionsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDispensesQuery query) {
        new FindDispensesQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationAdministrationsQuery query) {
        new FindMedicationAdministrationsQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsForValidationQuery query) {
        new FindPrescriptionsForValidationQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsForDispenseQuery query) {
        new FindPrescriptionsForDispenseQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationListQuery query) {
        new FindMedicationListQueryTransformer().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsByTitleQuery query) {
        new FindDocumentsByTitleQueryTransformer().toEbXML(query, ebXML);
    }
}
