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
package org.openehealth.ipf.commons.ihe.xds.core;

import org.openehealth.ipf.commons.ihe.ws.utils.LargeDataSource;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;
import org.openehealth.ipf.commons.ihe.xds.core.responses.*;

import javax.activation.DataHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to create sample data used in tests.
 * @author Jens Riemschneider
 */
public abstract class SampleData {
    private SampleData() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static QueryResponse createQueryResponseWithLeafClass() {
        return createQueryResponseWithLeafClass(
                Status.PARTIAL_SUCCESS,
                new Identifiable("id3", new AssigningAuthority("1.3")));
    }


    /**
     * @return a sample query response using leaf class return type.
     */
    public static QueryResponse createQueryResponseWithLeafClass(Status status, Identifiable... patientIDs) {
        QueryResponse response = new QueryResponse();

        for (Identifiable patientID : patientIDs) {
            SubmissionSet submissionSet = createSubmissionSet(patientID);
            DocumentEntry docEntry = createDocumentEntry(patientID);
            Folder folder = createFolder(patientID);

            Association docAssociation = createAssociationDocEntryToSubmissionSet();
            Association folderAssociation = createAssociationFolderToSubmissionSet();
            Association docFolderAssociation = createAssociationDocEntryToFolder();

            response.getSubmissionSets().add(submissionSet);
            response.getDocumentEntries().add(docEntry);
            response.getFolders().add(folder);
            response.getAssociations().add(docAssociation);
            response.getAssociations().add(folderAssociation);
            response.getAssociations().add(docFolderAssociation);
        }

        response.setStatus(status);
        
        return response;
    }   

    /**
     * @return a sample query response using object reference return type.
     */
    public static QueryResponse createQueryResponseWithObjRef() {
        ObjectReference ref1 = new ObjectReference("ref1");
        ObjectReference ref2 = new ObjectReference("ref2");

        QueryResponse response = new QueryResponse();
        response.setStatus(Status.SUCCESS);
        response.getReferences().add(ref1);
        response.getReferences().add(ref2);
        
        return response;
    }   

    /**
     * @return a sample response.
     */
    public static Response createResponse() {
        Response response = new Response();
        response.setStatus(Status.FAILURE);
        response.getErrors().addAll(Arrays.asList(
                new ErrorInfo(ErrorCode.PATIENT_ID_DOES_NOT_MATCH, "context1", Severity.ERROR, "location1", null),
                new ErrorInfo(ErrorCode.SQL_ERROR, "context2", Severity.WARNING, null, null),
                new ErrorInfo(ErrorCode._USER_DEFINED, "context3", Severity.ERROR, "location3", "MyCustomErrorCode")));
        return response;
    }    

    /**
     * @return a sample response for retrieved documents.
     */
    public static RetrievedDocumentSet createRetrievedDocumentSet() {
        DocumentReference requestData1 = new DocumentReference();
        requestData1.setDocumentUniqueId("doc1");
        requestData1.setHomeCommunityId("urn:oid:1.2.3");
        requestData1.setRepositoryUniqueId("repo1");
        
        DataHandler dataHandler1 = createDataHandler();
        
        RetrievedDocument doc1 = new RetrievedDocument();
        doc1.setRequestData(requestData1);
        doc1.setDataHandler(dataHandler1);
        doc1.setMimeType("application/test1");

        DocumentReference requestData2 = new DocumentReference();
        requestData2.setDocumentUniqueId("doc2");
        requestData2.setHomeCommunityId("urn:oid:1.2.4");
        requestData2.setRepositoryUniqueId("repo2");

        DataHandler dataHandler2 = createDataHandler();        
        RetrievedDocument doc2 = new RetrievedDocument();
        doc2.setRequestData(requestData2);
        doc2.setDataHandler(dataHandler2);
        doc2.setMimeType("application/test2");
        doc2.setNewRepositoryUniqueId("repo2-new");
        doc2.setNewDocumentUniqueId("doc2-new");

        RetrievedDocumentSet response = new RetrievedDocumentSet();
        response.getDocuments().add(doc1);
        response.getDocuments().add(doc2);
        response.setStatus(Status.SUCCESS);
        
        return response;
    }

    /**
     * Creates a dummy data handler
     * @return the new data handler.
     */
    public static DataHandler createDataHandler() {
        return new DataHandler(new LargeDataSource());
    }

    /**
     * @return a sample request to provide and register documents.
     */
    public static ProvideAndRegisterDocumentSet createProvideAndRegisterDocumentSet() {
        Identifiable patientID = new Identifiable("id3", new AssigningAuthority("1.3"));
        
        SubmissionSet submissionSet = createSubmissionSet(patientID);        
        DocumentEntry docEntry = createDocumentEntry(patientID);        
        Folder folder = createFolder(patientID);
        
        Association docAssociation = createAssociationDocEntryToSubmissionSet();
        Association folderAssociation = createAssociationFolderToSubmissionSet();
        Association docFolderAssociation = createAssociationDocEntryToFolder();
        
        DataHandler dataHandler = createDataHandler();
        Document doc = new Document(docEntry, dataHandler);
        
        ProvideAndRegisterDocumentSet request = new ProvideAndRegisterDocumentSet();
        request.setSubmissionSet(submissionSet);
        request.getDocuments().add(doc);
        request.getFolders().add(folder);
        request.getAssociations().add(docAssociation);
        request.getAssociations().add(folderAssociation);
        request.getAssociations().add(docFolderAssociation);

        request.setTargetHomeCommunityId("urn:oid:1.2.3.4.5.6.2333.23");

        return request;
    }

    private static Association createAssociationDocEntryToFolder() {
        Association docFolderAssociation = new Association();
        docFolderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docFolderAssociation.setSourceUuid("folder01");
        docFolderAssociation.setTargetUuid("document01");
        docFolderAssociation.setEntryUuid("docFolderAss");
        return docFolderAssociation;
    }

    private static Association createAssociationFolderToSubmissionSet() {
        Association folderAssociation = new Association();
        folderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        folderAssociation.setSourceUuid("submissionSet01");
        folderAssociation.setTargetUuid("folder01");
        folderAssociation.setEntryUuid("folderAss");
        folderAssociation.setPreviousVersion("110");
        return folderAssociation;
    }

    private static Association createAssociationDocEntryToSubmissionSet() {
        Association docAssociation = new Association();
        docAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docAssociation.setSourceUuid("submissionSet01");
        docAssociation.setTargetUuid("document01");
        docAssociation.setLabel(AssociationLabel.ORIGINAL);
        docAssociation.setEntryUuid("docAss");
        docAssociation.setPreviousVersion("111");
        return docAssociation;
    }

    /**
     * Creates a sample folder.
     * @param patientID
     *          the patient ID to use.
     * @return the folder.                         
     */
    public static Folder createFolder(Identifiable patientID) {
        Folder folder = new Folder();
        folder.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        folder.getCodeList().add(new Code("code7", new LocalizedString("code7"), "scheme7"));
        folder.setComments(new LocalizedString("comments3"));
        folder.setEntryUuid("folder01");
        folder.setLastUpdateTime("19820910121315");
        folder.setPatientId(patientID);
        folder.setTitle(new LocalizedString("Folder 01", "en-US", "UTF8"));
        folder.setUniqueId("48574589");
        return folder;
    }

    /**
     * Creates a sample submission set.
     * @param patientID
     *          the patient ID to use.
     * @return the submission set.
     */
    public static SubmissionSet createSubmissionSet(Identifiable patientID) {
        Recipient recipient = new Recipient();
        recipient.setOrganization(new Organization("org", null, null));
        
        Author author = new Author();
        author.setAuthorPerson(new Person(new Identifiable("id1", new AssigningAuthority("1.1")),
                new XpnName("Otto", null, null, null, null, null)));

        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.getAuthors().add(author);
        submissionSet.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        submissionSet.setComments(new LocalizedString("comments1"));
        submissionSet.setContentTypeCode(new Code("code1", new LocalizedString("code1"), "scheme1"));
        submissionSet.setEntryUuid("submissionSet01");
        submissionSet.getIntendedRecipients().add(recipient);
        submissionSet.setPatientId(patientID);
        submissionSet.setSourceId("1.2.3");
        submissionSet.setSubmissionTime("1980");
        submissionSet.setTitle(new LocalizedString("Submission Set 01", "en-US", "UTF8"));
        submissionSet.setUniqueId("123");
        return submissionSet;
    }

    /**
     * Creates a document entry
     * @param patientID
     *          patient used for the document entry.
     * @return the new entry.
     */
    public static DocumentEntry createDocumentEntry(Identifiable patientID) {
        Author author = new Author();
        Name name = new XpnName();
        name.setFamilyName("Norbi");
        author.setAuthorPerson(new Person(new Identifiable("id2", new AssigningAuthority("1.2")), name));
        author.getAuthorInstitution().add(new Organization("authorOrg", null, null));
        author.getAuthorRole().add(new Identifiable("role1", new AssigningAuthority("1.2.3.1", "ISO")));
        author.getAuthorRole().add(new Identifiable("role2", null));
        author.getAuthorSpecialty().add(new Identifiable("spec1", new AssigningAuthority("1.2.3.3", "ISO")));
        author.getAuthorSpecialty().add(new Identifiable("spec2", null));
        author.getAuthorTelecom().add(new Telecom("author1@acme.org"));
        author.getAuthorTelecom().add(new Telecom("author2@acme.org"));

        Address address = new Address();
        address.setStreetAddress("hier");
        
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.getAddresses().add(address);
        patientInfo.setDateOfBirth("1980");
        patientInfo.setGender("M");
        patientInfo.getNames().add(new XpnName("Susi", null, null, null, null, null));
        
        DocumentEntry docEntry = new DocumentEntry();
        docEntry.getAuthors().add(author);
        docEntry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        docEntry.setClassCode(new Code("code2", new LocalizedString("code2"), "scheme2"));
        docEntry.setComments(new LocalizedString("comment2"));
        docEntry.getConfidentialityCodes().add(new Code("code8", new LocalizedString("code8"), "scheme8"));
        docEntry.setCreationTime("1981");
        docEntry.setEntryUuid("document01");
        docEntry.getEventCodeList().add(new Code("code9", new LocalizedString("code9"), "scheme9"));
        docEntry.setFormatCode(new Code("code3", new LocalizedString("code3"), "scheme3"));
        docEntry.setHash("1234567890123456789012345678901234567890");
        docEntry.setHealthcareFacilityTypeCode(new Code("code4", new LocalizedString("code4"), "scheme4"));
        docEntry.setLanguageCode("en-US");
        docEntry.setLegalAuthenticator(new Person(new Identifiable("legal", new AssigningAuthority("1.7")),
                new XpnName("Gustav", null, null, null, null, null)));
        docEntry.setMimeType("application/octet-stream");
        docEntry.setPatientId(patientID);
        docEntry.setPracticeSettingCode(new Code("code5", new LocalizedString("code5"), "scheme5"));
        docEntry.setRepositoryUniqueId("1.2.3.4");
        docEntry.setServiceStartTime("198012");
        docEntry.setServiceStopTime("198101");
        docEntry.setSize(123L);
        docEntry.setSourcePatientId(new Identifiable("source", new AssigningAuthority("4.1")));
        docEntry.setSourcePatientInfo(patientInfo);
        docEntry.setTitle(new LocalizedString("Document 01", "en-US", "UTF8"));
        docEntry.setTypeCode(new Code("code6", new LocalizedString("code6"), "scheme6"));
        docEntry.setUniqueId("32848902348");
        docEntry.setUri("http://hierunten.com");
        docEntry.getReferenceIdList().add(new ReferenceId(
                "ref-id-1", new CXiAssigningAuthority("ABCD", "1.1.2.3", "ISO"),
                ReferenceId.ID_TYPE_CODE_ORDER));
        docEntry.getReferenceIdList().add(new ReferenceId(
                "ref-id-2", new CXiAssigningAuthority("DEFG", "2.1.2.3", "ISO"),
                "vendor-defined"));
        return docEntry;
    }    

    /**
     * @return a sample request to register a document set.
     */
    public static RegisterDocumentSet createRegisterDocumentSet() {
        Identifiable patientID = new Identifiable("id3", new AssigningAuthority("1.3"));
        
        SubmissionSet submissionSet = createSubmissionSet(patientID);        
        DocumentEntry docEntry = createDocumentEntry(patientID);        
        Folder folder = createFolder(patientID);
        
        Association docAssociation = createAssociationDocEntryToSubmissionSet();
        Association folderAssociation = createAssociationFolderToSubmissionSet();
        Association docFolderAssociation = createAssociationDocEntryToFolder();
        
        RegisterDocumentSet request = new RegisterDocumentSet();
        request.setSubmissionSet(submissionSet);
        request.getDocumentEntries().add(docEntry);
        request.getFolders().add(folder);
        request.getAssociations().add(docAssociation);
        request.getAssociations().add(folderAssociation);
        request.getAssociations().add(docFolderAssociation);

        request.setTargetHomeCommunityId("urn:oid:1.2.3.4.5.6.2333.23");

        return request;
    }

    /**
     * @return a sample request to retrieve a document set.
     */
    public static RetrieveDocumentSet createRetrieveDocumentSet() {
        RetrieveDocumentSet request = new RetrieveDocumentSet();
        request.getDocuments().add(new DocumentReference("repo1", "doc1", "urn:oid:1.2.3"));
        request.getDocuments().add(new DocumentReference("repo2", "doc2", "urn:oid:1.2.4"));
        return request;
    }

    /**
     * @return a sample request to remove documents.
     */
    public static RemoveDocuments createRemoveDocuments() {
        RemoveDocuments request = new RemoveDocuments();
        request.getDocuments().add(new DocumentReference("1.1.1", "2.1.1", "urn:oid:1.2.3"));
        request.getDocuments().add(new DocumentReference("1.1.2", "2.1.2", "urn:oid:1.2.4"));
        request.getDocuments().add(new DocumentReference("1.1.3", "2.1.3", "urn:oid:1.2.5"));
        return request;
    }

    /**
     * @return a sample request to retrieve an imaging document set.
     */
    public static RetrieveImagingDocumentSet createRetrieveImagingDocumentSet() {
        RetrieveImagingDocumentSet request = new RetrieveImagingDocumentSet();

        List<DocumentReference> documentReferences = new ArrayList<>();
        DocumentReference documentReference1 = new DocumentReference("repo1", "doc1", "urn:oid:1.2.3");
        documentReferences.add(documentReference1);
        DocumentReference documentReference2 = new DocumentReference("repo2", "doc2", "urn:oid:1.2.4");
        documentReferences.add(documentReference2);

        List<RetrieveSeries> retrieveSerieses = new ArrayList<>();
        RetrieveSeries retrieveSeries1 = new RetrieveSeries("urn:oid:1.2.1", documentReferences);
        retrieveSerieses.add(retrieveSeries1);
        RetrieveSeries retrieveSeries2 = new RetrieveSeries("urn:oid:1.2.2", documentReferences);
        retrieveSerieses.add(retrieveSeries2);

        List<RetrieveStudy> retrieveStudies = request.getRetrieveStudies();
        RetrieveStudy retrieveStudy1 = new RetrieveStudy("urn:oid:1.1.1", retrieveSerieses);
        retrieveStudies.add(retrieveStudy1);
        RetrieveStudy retrieveStudy2 = new RetrieveStudy("urn:oid:1.1.2", retrieveSerieses);
        retrieveStudies.add(retrieveStudy2);

        request.getTransferSyntaxIds().add("1.2.840.10008.1.2.4.64");
        request.getTransferSyntaxIds().add("1.2.840.10008.1.2.4.70");

        return request;
    }

    /**
     * @return a sample sql query.
     */
    public static QueryRegistry createSqlQuery() {
        SqlQuery query = new SqlQuery();        
        query.setSql("SELECT * FROM INTERNET");
        return new QueryRegistry(query);
    }
    
    /**
     * @return a sample stored query for get documents.
     */
    public static QueryRegistry createGetDocumentsQuery() {
        GetDocumentsQuery query = new GetDocumentsQuery();
        query.setHomeCommunityId("urn:oid:1.2.3.14.15.926");
        query.setUuids(Collections.singletonList("document01"));
        
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for find documents.
     */
    public static QueryRegistry createFindDocumentsQuery() {
        FindDocumentsQuery query = new FindDocumentsQuery();
        populateDocumentsQuery(query);
        query.setPatientId(new Identifiable("id3", new AssigningAuthority("1.3")));
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        query.setDocumentEntryTypes(Arrays.asList(DocumentEntryType.STABLE));
        query.setDocumentAvailability(Arrays.asList(DocumentAvailability.ONLINE));
        query.setMetadataLevel(1);
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for find documents by reference ID.
     */
    public static QueryRegistry createFindDocumentsByReferenceIdQuery() {
        FindDocumentsByReferenceIdQuery query = new FindDocumentsByReferenceIdQuery();
        populateDocumentsQuery(query);
        query.setPatientId(new Identifiable("id3", new AssigningAuthority("1.3")));
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        query.setDocumentEntryTypes(Arrays.asList(DocumentEntryType.STABLE));

        QueryList<ReferenceId> referenceIds = new QueryList<>();
        referenceIds.getOuterList().add(Arrays.asList(
                new ReferenceId("ref-id-11", new CXiAssigningAuthority("", "1.1.1.1", "ISO"),
                        ReferenceId.ID_TYPE_CODE_UNIQUE_ID),
                new ReferenceId("ref-id-12", null, ReferenceId.ID_TYPE_WORKFLOW_INSTANCE_ID),
                new ReferenceId("ref-id-13", null, ReferenceId.ID_TYPE_CODE_REFERRAL)));
        referenceIds.getOuterList().add(Arrays.asList(
                new ReferenceId("ref-id-21", new CXiAssigningAuthority("", "1.1.1.2", "ISO"),
                        ReferenceId.ID_TYPE_CODE_ACCESSION),
                new ReferenceId("ref-id-22", null, ReferenceId.ID_TYPE_CODE_ORDER)));
        query.setTypedReferenceIds(referenceIds);

        return new QueryRegistry(query);
    }

    private static void populateDocumentsQuery(DocumentsQuery query) {
        
        query.setHomeCommunityId("12.21.41");

        query.setClassCodes(Arrays.asList(new Code("code1", null, "scheme1"), new Code("code2", null, "scheme2")));
        query.setTypeCodes(Arrays.asList(new Code("codet1", null, "schemet1"), new Code("codet2", null, "schemet2")));
        query.setPracticeSettingCodes(Arrays.asList(new Code("code3", null, "scheme3"), new Code("code4", null, "scheme4")));
        query.getCreationTime().setFrom("1980");
        query.getCreationTime().setTo("1981");
        query.getServiceStartTime().setFrom("1982");
        query.getServiceStartTime().setTo("1983");
        query.getServiceStopTime().setFrom("1984");
        query.getServiceStopTime().setTo("1985");
        query.setHealthcareFacilityTypeCodes(Arrays.asList(new Code("code5", null, "scheme5"), new Code("code6", null, "scheme6")));
        QueryList<Code> eventCodes = new QueryList<>();
        eventCodes.getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        eventCodes.getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.setEventCodes(eventCodes);
        QueryList<Code> confidentialityCodes = new QueryList<>();
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        query.setConfidentialityCodes(confidentialityCodes);
        query.setAuthorPersons(Arrays.asList("per'son1", "person2"));
        query.setFormatCodes(Arrays.asList(new Code("code13", null, "scheme13"), new Code("code14", null, "scheme14")));
    }

    /**
     * @return a sample stored query for find documents (Multi Patient).
     */
    public static QueryRegistry createFindDocumentsForMultiplePatientsQuery() {
        FindDocumentsForMultiplePatientsQuery query = new FindDocumentsForMultiplePatientsQuery();
        populateDocumentsQuery(query);
        query.setPatientIds(Arrays.asList(
                new Identifiable("id3", new AssigningAuthority("1.3")),
                new Identifiable("id4", new AssigningAuthority("1.4"))));
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        query.setDocumentEntryTypes(Arrays.asList(DocumentEntryType.STABLE));
        return new QueryRegistry(query);
    }


    /**
     * @return a sample stored query for find folders.
     */
    public static QueryRegistry createFindFoldersQuery() {
        FindFoldersQuery query = new FindFoldersQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("1.2")));
        query.getLastUpdateTime().setFrom("1980");
        query.getLastUpdateTime().setTo("1981");
        QueryList<Code> codes = new QueryList<>();
        codes.getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        codes.getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.setCodes(codes);
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        
        return new QueryRegistry(query);
    }


    /**
     * @return a sample stored query for find folders.
     */
    public static QueryRegistry createFindFoldersForMultiplePatientsQuery() {
        FindFoldersForMultiplePatientsQuery query = new FindFoldersForMultiplePatientsQuery();

        query.setHomeCommunityId("12.21.41");
        query.setPatientIds(Arrays.asList(new Identifiable("id1", new AssigningAuthority("1.2")), new Identifiable("id2", new AssigningAuthority("1.2"))));
        query.getLastUpdateTime().setFrom("1980");
        query.getLastUpdateTime().setTo("1981");
        QueryList<Code> codes = new QueryList<>();
        codes.getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        codes.getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.setCodes(codes);
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));

        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for find submission sets.
     */
    public static QueryRegistry createFindSubmissionSetsQuery() {
        FindSubmissionSetsQuery query = new FindSubmissionSetsQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("1.2")));
        query.getSubmissionTime().setFrom("1980");
        query.getSubmissionTime().setTo("1981");
        query.setAuthorPerson("per'son1");
        query.setSourceIds(Arrays.asList("1.2.3", "3.2.1"));
        query.setContentTypeCodes(Arrays.asList(new Code("code1", null, "scheme1"), new Code("code2", null, "scheme2")));
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting data from all types.
     */
    public static QueryRegistry createGetAllQuery() {
        GetAllQuery query = new GetAllQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("1.2")));
        QueryList<Code> codes = new QueryList<>();
        codes.getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        codes.getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.setConfidentialityCodes(codes);
        query.setFormatCodes(Arrays.asList(new Code("code1", null, "scheme1"), new Code("code2", null, "scheme2")));
        query.setStatusDocuments(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        query.setStatusFolders(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        query.setStatusSubmissionSets(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        query.setDocumentEntryTypes(Arrays.asList(DocumentEntryType.STABLE));

        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting associations.
     */
    public static QueryRegistry createGetAssociationsQuery() {
        GetAssociationsQuery query = new GetAssociationsQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setUuids(Arrays.asList("urn:uuid:1.2.3.4", "urn:uuid:2.3.4.5"));
        
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting associations and documents.
     */
    public static QueryRegistry createGetDocumentsAndAssociationsQuery() {
        GetDocumentsAndAssociationsQuery query = new GetDocumentsAndAssociationsQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setUuids(Arrays.asList("urn:uuid:1.2.3.4", "urn:uuid:2.3.4.5"));
        query.setUniqueIds(Arrays.asList("12.21.34", "43.56.89"));
        
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting folders and their content.
     */
    public static QueryRegistry createGetFolderAndContentsQuery() {
        GetFolderAndContentsQuery query = new GetFolderAndContentsQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setUuid("urn:uuid:1.2.3.4");
        query.setUniqueId("12.21.34");
        QueryList<Code> confidentialityCodes = new QueryList<>();
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        query.setConfidentialityCodes(confidentialityCodes);
        query.setFormatCodes(Arrays.asList(new Code("code13", null, "scheme13"), new Code("code14", null, "scheme14")));
        query.setDocumentEntryTypes(Arrays.asList(DocumentEntryType.STABLE));
        
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting folders based on their documents.
     */
    public static QueryRegistry createGetFoldersForDocumentQuery() {
        GetFoldersForDocumentQuery query = new GetFoldersForDocumentQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setUuid("urn:uuid:1.2.3.4");
        query.setUniqueId("12.21.34");
        QueryList<Code> confidentialityCodes = new QueryList<>();
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting folders.
     */
    public static QueryRegistry createGetFoldersQuery() {
        GetFoldersQuery query = new GetFoldersQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setUuids(Arrays.asList("urn:uuid:1.2.3.4", "urn:uuid:2.3.4.5"));
        query.setUniqueIds(Arrays.asList("12.21.34", "43.56.89"));
        
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting documents related to some other object.
     */
    public static QueryRegistry createGetRelatedDocumentsQuery() {
        GetRelatedDocumentsQuery query = new GetRelatedDocumentsQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setUuid("urn:uuid:1.2.3.4");
        query.setUniqueId("12.21.34");
        query.setAssociationTypes(Arrays.asList(AssociationType.APPEND, AssociationType.TRANSFORM));
        query.setDocumentEntryTypes(Arrays.asList(DocumentEntryType.STABLE));
                
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting submission sets and their contents.
     */
    public static QueryRegistry createGetSubmissionSetAndContentsQuery() {
        GetSubmissionSetAndContentsQuery query = new GetSubmissionSetAndContentsQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setUuid("urn:uuid:1.2.3.4");
        query.setUniqueId("12.21.34");
        QueryList<Code> confidentialityCodes = new QueryList<>();
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        query.setDocumentEntryTypes(Arrays.asList(DocumentEntryType.STABLE));

        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for getting submission sets.
     */
    public static QueryRegistry createGetSubmissionSetsQuery() {
        GetSubmissionSetsQuery query = new GetSubmissionSetsQuery();
        
        query.setHomeCommunityId("12.21.41");
        query.setUuids(Arrays.asList("urn:uuid:1.2.3.4", "urn:uuid:2.3.4.5"));
                
        return new QueryRegistry(query);
    }

    /**
     * @return a sample stored query for find documents.
     */
    public static QueryRegistry createFetchQuery() {
        FetchQuery query = new FetchQuery();

        query.setHomeCommunityId("urn:oid:1.2.21.41");
        query.setPatientId(new Identifiable("id3", new AssigningAuthority("1.3")));
        query.setClassCodes(Arrays.asList(new Code("code1", null, "scheme1"), new Code("code2", null, "scheme2")));
        query.setTypeCodes(Arrays.asList(new Code("codet1", null, "schemet1"), new Code("codet2", null, "schemet2")));
        query.setPracticeSettingCodes(Arrays.asList(new Code("code3", null, "scheme3"), new Code("code4", null, "scheme4")));
        query.getCreationTime().setFrom("1980");
        query.getCreationTime().setTo("1981");
        query.getServiceStartTime().setFrom("1982");
        query.getServiceStartTime().setTo("1983");
        query.getServiceStopTime().setFrom("1984");
        query.getServiceStopTime().setTo("1985");
        query.setHealthcareFacilityTypeCodes(Arrays.asList(new Code("code5", null, "scheme5"), new Code("code6", null, "scheme6")));
        QueryList<Code> eventCodes = new QueryList<>();
        eventCodes.getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        eventCodes.getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.setEventCodes(eventCodes);
        QueryList<Code> confidentialityCodes = new QueryList<>();
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        query.setConfidentialityCodes(confidentialityCodes);
        query.setAuthorPersons(Arrays.asList("per'son1", "person2"));
        query.setFormatCodes(Arrays.asList(new Code("code13", null, "scheme13"), new Code("code14", null, "scheme14")));
        //query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));

        QueryRegistry queryRegistry = new QueryRegistry(query);
        queryRegistry.setReturnType(QueryReturnType.LEAF_CLASS_WITH_REPOSITORY_ITEM);
        return queryRegistry;
    }


    public static RemoveMetadata createRemoveMetadata(){
        RemoveMetadata removeDocs = new RemoveMetadata();
        removeDocs.getReferences().add(new ObjectReference("urn:uuid:b2632452-1de7-480d-94b1-c2074d79c871", "1.2.3"));
        removeDocs.getReferences().add(new ObjectReference("urn:uuid:b2632df2-1de7-480d-1045-c2074d79aabd", "5.6.7"));

        return removeDocs;
    }

}

