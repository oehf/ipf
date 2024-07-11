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

import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * All possible query types.
 * @author Jens Riemschneider
 * @author Michael Ottati
 * @author Quentin Ligier
 */
@XmlType(name = "QueryType")
@XmlEnum
public enum QueryType {
    /** Searches for documents. */
    @XmlEnumValue("FindDocuments") FIND_DOCUMENTS("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d", FindDocumentsQuery.class),
    /** Searches for documents (Multi Patient Variety). */
    @XmlEnumValue("FindDocumentsForMultiplePatients") FIND_DOCUMENTS_MPQ("urn:uuid:3d1bdb10-39a2-11de-89c2-2f44d94eaa9f", FindDocumentsForMultiplePatientsQuery.class),
    /** Searches for submission sets. */
    @XmlEnumValue("FindSubmissionSets") FIND_SUBMISSION_SETS("urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9", FindSubmissionSetsQuery.class),
    /** Searches for folders. */
    @XmlEnumValue("FindFolders") FIND_FOLDERS("urn:uuid:958f3006-baad-4929-a4de-ff1114824431", FindFoldersQuery.class),
    /** Searches for documents by reference IDs */
    @XmlEnumValue("FindDocumentsByReferenceId") FIND_DOCUMENTS_BY_REFERENCE_ID("urn:uuid:12941a89-e02e-4be5-967c-ce4bfc8fe492", FindDocumentsByReferenceIdQuery.class),
    /** Searches for documents by reference IDs (Multi Patient Variety). */
    @XmlEnumValue("FindDocumentsByReferenceIdForMultiplePatients") FIND_DOCUMENTS_BY_REFERENCE_ID_MPQ("urn:uuid:1191642d-86c4-42d8-b784-f95445f9f0d5", FindDocumentsByReferenceIdForMultiplePatientsQuery.class),
    /** Searches for folders (Multi Patient Variety). */
    @XmlEnumValue("FindFoldersForMultiplePatients") FIND_FOLDERS_MPQ("urn:uuid:50d3f5ac-39a2-11de-a1ca-b366239e58df", FindFoldersForMultiplePatientsQuery.class),
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
    @XmlEnumValue("Fetch") FETCH("urn:uuid:f2072993-9478-41df-a603-8f016706efe8", FetchQuery.class),
    /** Find planned medication documents and their related documents (PHARM-1). */
    @XmlEnumValue("FindMedicationTreatmentPlans") FIND_MEDICATION_TREATMENT_PLANS("urn:uuid:c85f5ade-81c1-44b6-8f7c-48b9cd6b9489", FindMedicationTreatmentPlansQuery.class),
    /** Find prescriptions and their related documents (PHARM-1). */
    @XmlEnumValue("FindPrescriptions") FIND_PRESCRIPTIONS("urn:uuid:0e6095c5-dc3d-47d9-a219-047064086d92", FindPrescriptionsQuery.class),
    /** Find dispense documents and their related documents (PHARM-1). */
    @XmlEnumValue("FindDispenses") FIND_DISPENSES("urn:uuid:ac79c7c7-f21b-4c88-ab81-57e4889e8758", FindDispensesQuery.class),
    /** Find administered medication documents and their related documents (PHARM-1). */
    @XmlEnumValue("FindMedicationAdministrations") FIND_MEDICATION_ADMINISTRATIONS("urn:uuid:fdbe8fb8-7b5c-4470-9383-8abc7135f462", FindMedicationAdministrationsQuery.class),
    /** Find prescriptions and their related documents containing Prescription Items ready to be validated (PHARM-1). */
    @XmlEnumValue("FindPrescriptionsForValidation") FIND_PRESCRIPTIONS_FOR_VALIDATION("urn:uuid:c1a43b20-0254-102e-8469-a6af440562e8", FindPrescriptionsForValidationQuery.class),
    /** Find prescriptions and their related documents containing Prescription Items ready to be dispensed (PHARM-1). */
    @XmlEnumValue("FindPrescriptionsForDispense") FIND_PRESCRIPTIONS_FOR_DISPENSE("urn:uuid:c875eb9c-0254-102e-8469-a6af440562e8", FindPrescriptionsForDispenseQuery.class),
    /** Find the medication list to the patient (PHARM-1). */
    @XmlEnumValue("FindMedicationList") FIND_MEDICATION_LIST("urn:uuid:80ebbd83-53c1-4453-9860-349585962af6", FindMedicationListQuery.class),
    /** Searches for documents by title (DE:GEMATIK). */
    @XmlEnumValue("FindDocumentsByTitle") FIND_DOCUMENTS_BY_TITLE("urn:uuid:ab474085-82b5-402d-8115-3f37cb1e2405", FindDocumentsByTitleQuery.class),
    @XmlEnumValue("SubscriptionForDocumentEntry") SUBSCRIPTION_FOR_DOCUMENT_ENTRY("urn:uuid:aa2332d0-f8fe-11e0-be50-0800200c9a66", SubscriptionForDocumentEntryQuery.class),
    @XmlEnumValue("SubscriptionForPatientIndepedentDocumentEntry") SUBSCRIPTION_FOR_PATIENT_INDEPENDENT_DOCUMENT_ENTRY("urn:uuid:742790e0-aba6-43d6-9f1f-e43ed9790b79", SubscriptionForPatientIndependentDocumentEntryQuery.class),
    @XmlEnumValue("SubscriptionForSubmissionSet") SUBSCRIPTION_FOR_SUBMISSION_SET("urn:uuid:fbede94e-dbdc-4f6b-bc1f-d730e677cece", SubscriptionForSubmissionSetQuery.class),
    @XmlEnumValue("SubscriptionForPatientIndependentSubmissionSet") SUBSCRIPTION_FOR_PATIENT_INDEPENDENT_SUBMISSION_SET("urn:uuid:868cad3d-ec09-4565-b66c-1be10d034399", SubscriptionForPatientIndependentSubmissionSetQuery.class),
    @XmlEnumValue("SubscriptionForFolder") SUBSCRIPTION_FOR_FOLDER("urn:uuid:9376254e-da05-41f5-9af3-ac56d63d8ebd", SubscriptionForFolderQuery.class);

    private final String id;
    private final Class<? extends Query> type;

    QueryType(String id, Class<? extends Query> type) {
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

        for (var type : values()) {
            if (id.equals(type.getId())) {
                return type;
            }
        }

        throw new XDSMetaDataException(ValidationMessage.UNKNOWN_QUERY_TYPE, id);
    }
}
