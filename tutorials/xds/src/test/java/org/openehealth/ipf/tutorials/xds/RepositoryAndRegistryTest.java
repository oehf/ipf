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
package org.openehealth.ipf.tutorials.xds;

import jakarta.activation.DataHandler;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationLabel;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetDocumentsAndAssociationsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetFolderAndContentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetFoldersForDocumentQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetFoldersQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetRelatedDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetSubmissionSetAndContentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetSubmissionSetsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.tutorials.xds.store.ContentUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus.APPROVED;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus.DEPRECATED;

/**
 * Submissions to the repository and registry, retrieval of what was submitted, and the Get... queries
 * that follow the associations between the stored objects.
 *
 * @author Jens Riemschneider
 */
class RepositoryAndRegistryTest extends XdsServerTestBase {

    // --- submission and retrieval -------------------------------------------------------------------

    @Test
    void provideAndRegister() {
        var provide = SampleData.createProvideAndRegisterDocumentSet();
        var document = provide.getDocuments().get(0);
        var docEntry = document.getDocumentEntry();
        docEntry.setExtraMetadata(Map.of("urn:abc", List.of("ddd")));
        docEntry.getPatientId().setId(UUID.randomUUID().toString());
        docEntry.setUniqueId("4.3.2.1");
        docEntry.setHash(ContentUtils.sha1(document.getContent(DataHandler.class)));
        docEntry.setSize(ContentUtils.size(document.getContent(DataHandler.class)));

        var response = send(iti41, provide, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());

        var found = findDocuments(docEntry.getPatientId(), APPROVED);
        assertEquals(1, found.size());
        assertEquals(docEntry.getUniqueId(), found.get(0).getUniqueId());
        assertEquals(docEntry.getExtraMetadata(), found.get(0).getExtraMetadata());
    }

    @Test
    void register() {
        var register = SampleData.createRegisterDocumentSet();
        var docEntry = register.getDocumentEntries().get(0);
        docEntry.getPatientId().setId(UUID.randomUUID().toString());
        docEntry.setUniqueId("1.2.3.4");

        var response = send(iti42, register, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());

        var found = findDocuments(docEntry.getPatientId(), APPROVED);
        assertEquals(1, found.size());
        assertEquals(docEntry.getUniqueId(), found.get(0).getUniqueId());
    }

    @Test
    void retrieve() {
        var provide = SampleData.createProvideAndRegisterDocumentSet();
        var document = provide.getDocuments().get(0);
        var docEntry = document.getDocumentEntry();
        docEntry.setHash(ContentUtils.sha1(document.getDataHandler()));
        docEntry.setSize(ContentUtils.size(document.getDataHandler()));
        docEntry.getPatientId().setId(UUID.randomUUID().toString());

        var response = send(iti41, provide, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());

        var reference = new DocumentReference();
        reference.setDocumentUniqueId(docEntry.getUniqueId());
        reference.setRepositoryUniqueId("something");
        var retrieve = new RetrieveDocumentSet();
        retrieve.getDocuments().add(reference);
        retrieve.getDocuments().add(reference);
        var retrieveResponse = send(iti43, retrieve, RetrievedDocumentSet.class);
        assertEquals(Status.SUCCESS, retrieveResponse.getStatus(), retrieveResponse.toString());

        // the same document, asked for twice, arrives twice
        assertEquals(2, retrieveResponse.getDocuments().size());
        var expectedContents = read(SampleData.createDataHandler());
        for (var retrieved : retrieveResponse.getDocuments()) {
            assertEquals(expectedContents, read(retrieved.getDataHandler()));
        }
    }

    // --- replacement --------------------------------------------------------------------------------

    /**
     * Replacing a document deprecates it together with everything that appends to or transforms it, and
     * the replacement takes over the folder memberships of the replaced document.
     */
    @Test
    void replaceDeprecatesTheOriginalAndItsAddendaAndCopiesFolderMembership() {
        // The original document, member of a folder
        var register = SampleData.createRegisterDocumentSet();
        var patientId = register.getSubmissionSet().getPatientId();
        patientId.setId(UUID.randomUUID().toString());
        register.getSubmissionSet().setUniqueId("1.2.42." + System.nanoTime());
        register.getFolders().get(0).setUniqueId("1.2.43." + System.nanoTime());
        register.getDocumentEntries().get(0).setUniqueId("1.2.44." + System.nanoTime());
        assertEquals(Status.SUCCESS, send(iti42, register, Response.class).getStatus());
        var original = findDocuments(patientId, APPROVED).get(0);

        // An addendum to the original
        var addendum = submitRelated(patientId, AssociationType.APPEND, original.getEntryUuid());

        // The replacement
        var replacement = submitRelated(patientId, AssociationType.REPLACE, original.getEntryUuid());

        assertEquals(Set.of(replacement.getUniqueId()), uniqueIds(findDocuments(patientId, APPROVED)));
        assertEquals(Set.of(original.getUniqueId(), addendum.getUniqueId()),
                uniqueIds(findDocuments(patientId, DEPRECATED)));

        var folderQuery = new GetFolderAndContentsQuery();
        folderQuery.setUniqueId(register.getFolders().get(0).getUniqueId());
        var folderResponse = query(folderQuery);
        assertTrue(uniqueIds(folderResponse.getDocumentEntries()).contains(replacement.getUniqueId()),
                folderResponse.toString());
    }

    // --- Get... queries -----------------------------------------------------------------------------

    /**
     * The Get... queries that follow associations from one object to the next. Stored are a submission set
     * with a document and a folder containing it, and a replacement of that document, which also becomes
     * a member of the folder.
     */
    @Test
    void getQueriesFollowAssociations() {
        var register = SampleData.createRegisterDocumentSet();
        var patientId = register.getSubmissionSet().getPatientId();
        patientId.setId(UUID.randomUUID().toString());
        var setUniqueId = "1.2.47." + System.nanoTime();
        var folderUniqueId = "1.2.48." + System.nanoTime();
        register.getSubmissionSet().setUniqueId(setUniqueId);
        register.getFolders().get(0).setUniqueId(folderUniqueId);
        register.getDocumentEntries().get(0).setUniqueId("1.2.49." + System.nanoTime());
        assertEquals(Status.SUCCESS, send(iti42, register, Response.class).getStatus());
        var original = findDocuments(patientId, APPROVED).get(0);
        submitRelated(patientId, AssociationType.REPLACE, original.getEntryUuid());
        var replacement = findDocuments(patientId, APPROVED).get(0);

        // the submission set, its document and folder, and the three associations tying them together
        var setAndContentsQuery = new GetSubmissionSetAndContentsQuery();
        setAndContentsQuery.setUniqueId(setUniqueId);
        var setAndContents = query(setAndContentsQuery);
        assertEquals(List.of(setUniqueId), setAndContents.getSubmissionSets().stream().map(SubmissionSet::getUniqueId).toList());
        assertEquals(List.of(original.getUniqueId()), setAndContents.getDocumentEntries().stream().map(DocumentEntry::getUniqueId).toList());
        assertEquals(List.of(folderUniqueId), folderUniqueIds(setAndContents.getFolders()));
        assertEquals(3, setAndContents.getAssociations().size(), setAndContents.toString());
        var folderUuid = setAndContents.getFolders().get(0).getEntryUuid();

        var foldersForDocQuery = new GetFoldersForDocumentQuery();
        foldersForDocQuery.setUniqueId(original.getUniqueId());
        assertEquals(List.of(folderUniqueId), folderUniqueIds(query(foldersForDocQuery).getFolders()));

        var foldersQuery = new GetFoldersQuery();
        foldersQuery.setUuids(List.of(folderUuid));
        assertEquals(List.of(folderUniqueId), folderUniqueIds(query(foldersQuery).getFolders()));

        // the set containing the original, and the association saying so
        var setsQuery = new GetSubmissionSetsQuery();
        setsQuery.setUuids(List.of(original.getEntryUuid()));
        var sets = query(setsQuery);
        assertEquals(List.of(setUniqueId), sets.getSubmissionSets().stream().map(SubmissionSet::getUniqueId).toList());
        assertEquals(List.of(original.getEntryUuid()), sets.getAssociations().stream().map(Association::getTargetUuid).toList());

        // the original and every association pointing at it: from its set, its folder and the replacement
        var docsAndAssocsQuery = new GetDocumentsAndAssociationsQuery();
        docsAndAssocsQuery.setUuids(List.of(original.getEntryUuid()));
        var docsAndAssocs = query(docsAndAssocsQuery);
        assertEquals(List.of(original.getUniqueId()), docsAndAssocs.getDocumentEntries().stream().map(DocumentEntry::getUniqueId).toList());
        assertEquals(3, docsAndAssocs.getAssociations().size(), docsAndAssocs.toString());
        assertTrue(docsAndAssocs.getAssociations().stream()
                .allMatch(association -> association.getTargetUuid().equals(original.getEntryUuid())));

        // the replacement, the original it replaces, and the replace association between them
        var relatedQuery = new GetRelatedDocumentsQuery();
        relatedQuery.setUuid(replacement.getEntryUuid());
        relatedQuery.setAssociationTypes(List.of(AssociationType.REPLACE));
        var related = query(relatedQuery);
        assertEquals(Set.of(original.getUniqueId(), replacement.getUniqueId()), uniqueIds(related.getDocumentEntries()));
        assertEquals(List.of(AssociationType.REPLACE),
                related.getAssociations().stream().map(Association::getAssociationType).toList());
        assertEquals(replacement.getEntryUuid(), related.getAssociations().get(0).getSourceUuid());
        assertEquals(original.getEntryUuid(), related.getAssociations().get(0).getTargetUuid());
    }

    // --- rejected submissions -----------------------------------------------------------------------

    @Test
    void anUnknownPatientIsRejected() {
        var register = SampleData.createRegisterDocumentSet();
        register.getSubmissionSet().getPatientId().setId("1111111");
        assertRejected(ValidationMessage.UNKNOWN_PATIENT_ID, send(iti42, register, Response.class));
    }

    @Test
    void aWrongHashIsRejected() {
        var provide = SampleData.createProvideAndRegisterDocumentSet();
        provide.getSubmissionSet().getPatientId().setId(UUID.randomUUID().toString());
        provide.getDocuments().get(0).getDocumentEntry().setHash("0".repeat(40));
        assertRejected(ValidationMessage.INCORRECT_HASH, send(iti41, provide, Response.class));
    }

    @Test
    void aWrongSizeIsRejected() {
        var provide = SampleData.createProvideAndRegisterDocumentSet();
        provide.getSubmissionSet().getPatientId().setId(UUID.randomUUID().toString());
        var document = provide.getDocuments().get(0);
        // the sample entry carries a hash of other content, which would be rejected first
        document.getDocumentEntry().setHash(ContentUtils.sha1(document.getContent(DataHandler.class)));
        document.getDocumentEntry().setSize(1L);
        assertRejected(ValidationMessage.INCORRECT_SIZE, send(iti41, provide, Response.class));
    }

    @Test
    void aResubmissionWithAnotherHashIsRejected() {
        var uniqueId = "1.2.53." + System.nanoTime();
        var patient = UUID.randomUUID().toString();

        var first = SampleData.createRegisterDocumentSet();
        first.getSubmissionSet().getPatientId().setId(patient);
        first.getDocumentEntries().get(0).setUniqueId(uniqueId);
        first.getDocumentEntries().get(0).setHash("1".repeat(40));
        assertEquals(Status.SUCCESS, send(iti42, first, Response.class).getStatus());

        var second = SampleData.createRegisterDocumentSet();
        second.getSubmissionSet().getPatientId().setId(patient);
        second.getDocumentEntries().get(0).setUniqueId(uniqueId);
        second.getDocumentEntries().get(0).setHash("2".repeat(40));
        assertRejected(ValidationMessage.DIFFERENT_HASH_CODE_IN_RESUBMISSION, send(iti42, second, Response.class));
    }

    @Test
    void appendingToADeprecatedDocumentIsRejected() {
        var original = registerOriginal();
        submitRelated(original.getPatientId(), AssociationType.REPLACE, original.getEntryUuid());

        var append = relatedSubmission(original.getPatientId(), AssociationType.APPEND, original.getEntryUuid());
        assertRejected(ValidationMessage.DEPRECATED_OBJ_CANNOT_BE_TRANSFORMED, send(iti42, append, Response.class));
    }

    @Test
    void referencingAnotherPatientsDocumentIsRejected() {
        var original = registerOriginal();
        var otherPatient = new Identifiable(UUID.randomUUID().toString(), original.getPatientId().getAssigningAuthority());

        var replace = relatedSubmission(otherPatient, AssociationType.REPLACE, original.getEntryUuid());
        assertRejected(ValidationMessage.FOLDER_PATIENT_ID_WRONG, send(iti42, replace, Response.class));
    }

    // --- helpers ------------------------------------------------------------------------------------

    /**
     * Registers a new document that is related to a stored one by an association of the given type.
     *
     * @return the registered document entry
     */
    private DocumentEntry submitRelated(Identifiable patientId, AssociationType type, String targetUuid) {
        var register = relatedSubmission(patientId, type, targetUuid);
        var response = send(iti42, register, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        return register.getDocumentEntries().get(0);
    }

    private static RegisterDocumentSet relatedSubmission(Identifiable patientId, AssociationType type, String targetUuid) {
        var submissionSet = SampleData.createSubmissionSet(patientId);
        submissionSet.setUniqueId("1.2.45." + System.nanoTime());
        var docEntry = SampleData.createDocumentEntry(patientId);
        docEntry.setUniqueId("1.2.46." + System.nanoTime());

        var member = new Association(AssociationType.HAS_MEMBER, "ssAss", submissionSet.getEntryUuid(), docEntry.getEntryUuid());
        member.setLabel(AssociationLabel.ORIGINAL);
        var related = new Association(type, "relAss", docEntry.getEntryUuid(), targetUuid);

        var register = new RegisterDocumentSet();
        register.setSubmissionSet(submissionSet);
        register.getDocumentEntries().add(docEntry);
        register.getAssociations().addAll(List.of(member, related));
        return register;
    }

    /** Registers a single document for a new patient and returns it as stored. */
    private DocumentEntry registerOriginal() {
        var register = SampleData.createRegisterDocumentSet();
        register.getSubmissionSet().getPatientId().setId(UUID.randomUUID().toString());
        register.getSubmissionSet().setUniqueId("1.2.50." + System.nanoTime());
        register.getFolders().get(0).setUniqueId("1.2.51." + System.nanoTime());
        register.getDocumentEntries().get(0).setUniqueId("1.2.52." + System.nanoTime());
        assertEquals(Status.SUCCESS, send(iti42, register, Response.class).getStatus());
        return findDocuments(register.getSubmissionSet().getPatientId(), APPROVED).get(0);
    }

    private static void assertRejected(ValidationMessage expected, Response response) {
        assertEquals(Status.FAILURE, response.getStatus(), response.toString());
        assertEquals(expected.getText(), response.getErrors().get(0).getCodeContext(), response.toString());
    }

    private static Set<String> uniqueIds(List<DocumentEntry> entries) {
        return entries.stream().map(DocumentEntry::getUniqueId).collect(Collectors.toSet());
    }

    private static List<String> folderUniqueIds(List<Folder> folders) {
        return folders.stream().map(Folder::getUniqueId).toList();
    }
}
