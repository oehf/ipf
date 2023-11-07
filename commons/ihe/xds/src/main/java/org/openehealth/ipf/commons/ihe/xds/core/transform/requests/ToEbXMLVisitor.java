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

import static java.util.Objects.requireNonNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query.Visitor;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.*;

/**
 * Query visitor to transform a query into its ebXML representation.
 * @author Jens Riemschneider
 */
final class ToEbXMLVisitor implements Visitor {
    private final EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML;

    /**
     * Constructs the visitor.
     * @param ebXML
     *          the ebXML result.
     */
    ToEbXMLVisitor(EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML) {
        this.ebXML = requireNonNull(ebXML, "ebXML cannot be null");
    }

    @Override
    public void visit(FindDocumentsQuery query) {
        FindDocumentsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsForMultiplePatientsQuery query) {
       FindDocumentsForMultiplePatientsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindFoldersQuery query) {
        FindFoldersQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindFoldersForMultiplePatientsQuery query) {
        FindFoldersForMultiplePatientsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetSubmissionSetsQuery query) {
        GetSubmissionSetsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetSubmissionSetAndContentsQuery query) {
        GetSubmissionSetAndContentsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetRelatedDocumentsQuery query) {
        GetRelatedDocumentsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFoldersQuery query) {
        GetFoldersQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFoldersForDocumentQuery query) {
        GetFoldersForDocumentQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFolderAndContentsQuery query) {
        GetFolderAndContentsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetDocumentsQuery query) {
        GetDocumentsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetDocumentsAndAssociationsQuery query) {
        GetDocumentsAndAssociationsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetAssociationsQuery query) {
        GetAssociationsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(GetAllQuery query) {
        GetAllQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindSubmissionSetsQuery query) {
        FindSubmissionSetsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FetchQuery query) {
        FetchQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsByReferenceIdQuery query) {
        FindDocumentsByReferenceIdQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationTreatmentPlansQuery query) {
        FindMedicationTreatmentPlansQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsQuery query) {
        FindPrescriptionsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDispensesQuery query) {
        FindDispensesQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationAdministrationsQuery query) {
        FindMedicationAdministrationsQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsForValidationQuery query) {
        FindPrescriptionsForValidationQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsForDispenseQuery query) {
        FindPrescriptionsForDispenseQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationListQuery query) {
        FindMedicationListQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsByTitleQuery query) {
        FindDocumentsByTitleQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForDocumentEntryQuery query) {
        SubscriptionForDocumentEntryQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForPatientIndependentDocumentEntryQuery query) {
        SubscriptionForPatientIndependentDocumentEntryQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForSubmissionSetQuery query) {
        SubscriptionForSubmissionSetQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForPatientIndependentSubmissionSetQuery query) {
        SubscriptionForPatientIndependentSubmissionSetQueryTransformer.getInstance().toEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForFolderQuery query) {
        SubscriptionForFolderQueryTransformer.getInstance().toEbXML(query, ebXML);
    }
}
