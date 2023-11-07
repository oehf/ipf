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
 * Query visitor to transform an ebXML representation into a query.
 * @author Jens Riemschneider
 */
final class FromEbXMLVisitor implements Visitor {
    private final EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML;

    /**
     * Constructs the visitor.
     * @param ebXML
     *          the ebXML object that is transformed.
     */
    FromEbXMLVisitor(EbXMLAdhocQueryRequest<AdhocQueryRequest> ebXML) {
        requireNonNull(ebXML, "ebXML cannot be null");
        this.ebXML = ebXML;
    }

    @Override
    public void visit(FindDocumentsQuery query) {
        FindDocumentsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsForMultiplePatientsQuery query) {
       FindDocumentsForMultiplePatientsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindFoldersQuery query) {
        FindFoldersQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindFoldersForMultiplePatientsQuery query) {
        FindFoldersForMultiplePatientsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetSubmissionSetsQuery query) {
        GetSubmissionSetsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetSubmissionSetAndContentsQuery query) {
        GetSubmissionSetAndContentsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetRelatedDocumentsQuery query) {
        GetRelatedDocumentsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFoldersQuery query) {
        GetFoldersQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFoldersForDocumentQuery query) {
        GetFoldersForDocumentQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetFolderAndContentsQuery query) {
        GetFolderAndContentsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetDocumentsQuery query) {
        GetDocumentsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetDocumentsAndAssociationsQuery query) {
        GetDocumentsAndAssociationsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetAssociationsQuery query) {
        GetAssociationsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(GetAllQuery query) {
        GetAllQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindSubmissionSetsQuery query) {
        FindSubmissionSetsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FetchQuery query) {
        FetchQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsByReferenceIdQuery query) {
        FindDocumentsByReferenceIdQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationTreatmentPlansQuery query) {
        FindMedicationTreatmentPlansQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsQuery query) {
        FindPrescriptionsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDispensesQuery query) {
        FindDispensesQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationAdministrationsQuery query) {
        FindMedicationAdministrationsQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsForValidationQuery query) {
        FindPrescriptionsForValidationQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindPrescriptionsForDispenseQuery query) {
        FindPrescriptionsForDispenseQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindMedicationListQuery query) {
        FindMedicationListQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(FindDocumentsByTitleQuery query) {
        FindDocumentsByTitleQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForDocumentEntryQuery query) {
        SubscriptionForDocumentEntryQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForFolderQuery query) {
        SubscriptionForFolderQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForPatientIndependentDocumentEntryQuery query) {
        SubscriptionForPatientIndependentDocumentEntryQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForSubmissionSetQuery query) {
        SubscriptionForSubmissionSetQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }

    @Override
    public void visit(SubscriptionForPatientIndependentSubmissionSetQuery query) {
        SubscriptionForPatientIndependentSubmissionSetQueryTransformer.getInstance().fromEbXML(query, ebXML);
    }
}
