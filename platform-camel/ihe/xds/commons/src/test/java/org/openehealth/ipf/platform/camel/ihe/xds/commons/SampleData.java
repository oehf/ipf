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
package org.openehealth.ipf.platform.camel.ihe.xds.commons;

import java.util.Arrays;

import javax.activation.DataHandler;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Address;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationLabel;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Author;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Document;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Name;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Organization;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.PatientInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Recipient;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.SqlQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorCode;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Severity;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.LargeDataSource;

/**
 * Utility class to create sample data used in tests.
 * @author Jens Riemschneider
 */
public abstract class SampleData {
    private SampleData() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * @return a sample query response using leaf class return type.
     */
    public static QueryResponse createQueryResponseWithLeafClass() {
        Identifiable patientID = new Identifiable("id3", new AssigningAuthority("1.3"));

        SubmissionSet submissionSet = createSubmissionSet(patientID);        
        DocumentEntry docEntry = createDocumentEntry(patientID);
        Folder folder = createFolder(patientID);
        
        Association docAssociation = createAssociationDocEntryToSubmissionSet();
        Association folderAssociation = createAssociationFolderToSubmissionSet();
        Association docFolderAssociation = createAssociationDocEntryToFolder();
        
        QueryResponse response = new QueryResponse();
        response.getSubmissionSets().add(submissionSet);
        response.getDocumentEntries().add(docEntry);
        response.getFolders().add(folder);
        response.getAssociations().add(docAssociation);
        response.getAssociations().add(folderAssociation);
        response.getAssociations().add(docFolderAssociation);
        response.setStatus(Status.PARTIAL_SUCCESS);
        
        return response;
    }   

    /**
     * @return a sample response.
     */
    public static Response createResponse() {
        Response response = new Response();
        response.setStatus(Status.FAILURE);
        response.getErrors().addAll(Arrays.asList(
                new ErrorInfo(ErrorCode.PATIENT_ID_DOES_NOT_MATCH, "context1", Severity.ERROR, "location1"), 
                new ErrorInfo(ErrorCode.SQL_ERROR, "context2", Severity.WARNING, null)));
        return response;
    }    

    /**
     * @return a sample response for retrieved documents.
     */
    public static RetrievedDocumentSet createRetrievedDocumentSet() {
        RetrieveDocument requestData1 = new RetrieveDocument();
        requestData1.setDocumentUniqueID("doc1");
        requestData1.setHomeCommunityID("home1");
        requestData1.setRepositoryUniqueID("repo1");
        
        DataHandler dataHandler1 = createDataHandler();
        
        RetrievedDocument doc1 = new RetrievedDocument();
        doc1.setRequestData(requestData1);
        doc1.setDataHandler(dataHandler1);

        RetrieveDocument requestData2 = new RetrieveDocument();
        requestData2.setDocumentUniqueID("doc2");
        requestData2.setHomeCommunityID("home2");
        requestData2.setRepositoryUniqueID("repo2");
        
        DataHandler dataHandler2 = createDataHandler();        
        RetrievedDocument doc2 = new RetrievedDocument();
        doc2.setRequestData(requestData2);
        doc2.setDataHandler(dataHandler2);
        
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
        
        return request;
    }

    private static Association createAssociationDocEntryToFolder() {
        Association docFolderAssociation = new Association();
        docFolderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docFolderAssociation.setSourceUUID("folder01");
        docFolderAssociation.setTargetUUID("document01");
        docFolderAssociation.setDocCode(new Code("docCode3", new LocalizedString("docCode3"), "docScheme3"));
        docFolderAssociation.setEntryUUID("docFolderAss");
        return docFolderAssociation;
    }

    private static Association createAssociationFolderToSubmissionSet() {
        Association folderAssociation = new Association();
        folderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        folderAssociation.setSourceUUID("submissionSet01");
        folderAssociation.setTargetUUID("folder01");
        folderAssociation.setDocCode(new Code("docCode2", new LocalizedString("docCode2"), "docScheme2"));        
        folderAssociation.setEntryUUID("folderAss");
        return folderAssociation;
    }

    private static Association createAssociationDocEntryToSubmissionSet() {
        Association docAssociation = new Association();
        docAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docAssociation.setSourceUUID("submissionSet01");
        docAssociation.setTargetUUID("document01");
        docAssociation.setDocCode(new Code("docCode1", new LocalizedString("docCode1"), "docScheme1"));
        docAssociation.setLabel(AssociationLabel.ORIGINAL);
        docAssociation.setEntryUUID("docAss");
        return docAssociation;
    }

    private static Folder createFolder(Identifiable patientID) {
        Folder folder = new Folder();
        folder.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        folder.getCodeList().add(new Code("code7", new LocalizedString("code7"), "scheme7"));
        folder.setComments(new LocalizedString("comments3"));
        folder.setEntryUUID("folder01");
        folder.setLastUpdateTime("198209");
        folder.setPatientID(patientID);
        folder.setTitle(new LocalizedString("Folder 01", "en-US", "UTF8"));
        folder.setUniqueID("48574589");
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
        
        Author author1 = new Author();
        author1.setAuthorPerson(new Person(new Identifiable("id1", new AssigningAuthority("1.1")), new Name("Otto")));

        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setAuthor(author1);
        submissionSet.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        submissionSet.setComments(new LocalizedString("comments1"));
        submissionSet.setContentTypeCode(new Code("code1", new LocalizedString("code1"), "scheme1"));
        submissionSet.setEntryUUID("submissionSet01");
        submissionSet.getIntendedRecipients().add(recipient);
        submissionSet.setPatientID(patientID);
        submissionSet.setSourceID("1.2.3");
        submissionSet.setSubmissionTime("1980");
        submissionSet.setTitle(new LocalizedString("Submission Set 01", "en-US", "UTF8"));
        submissionSet.setUniqueID("123");
        return submissionSet;
    }

    private static DocumentEntry createDocumentEntry(Identifiable patientID) {
        Author author2 = new Author();
        author2.setAuthorPerson(new Person(new Identifiable("id2", new AssigningAuthority("1.2")), new Name("Norbi")));
        author2.getAuthorInstitution().add(new Organization("authorOrg", null, null));
        
        Address address = new Address();
        address.setStreetAddress("hier");
        
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setAddress(address);
        patientInfo.setDateOfBirth("12334");
        patientInfo.setGender("M");
        patientInfo.setName(new Name("Susi"));
        
        DocumentEntry docEntry = new DocumentEntry();
        docEntry.getAuthors().add(author2);
        docEntry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        docEntry.setClassCode(new Code("code2", new LocalizedString("code2"), "scheme2"));
        docEntry.setComments(new LocalizedString("comment2"));
        docEntry.getConfidentialityCodes().add(new Code("code8", new LocalizedString("code8"), "scheme8"));
        docEntry.setCreationTime("1981");
        docEntry.setEntryUUID("document01");
        docEntry.getEventCodeList().add(new Code("code9", new LocalizedString("code9"), "scheme9"));
        docEntry.setFormatCode(new Code("code3", new LocalizedString("code3"), "scheme3"));
        docEntry.setHash("1234567890123456789012345678901234567890");
        docEntry.setHealthcareFacilityTypeCode(new Code("code4", new LocalizedString("code4"), "scheme4"));
        docEntry.setLanguageCode("en-US");
        docEntry.setLegalAuthenticator(new Person(new Identifiable("legal", new AssigningAuthority("1.7")), new Name("Gustav")));
        docEntry.setMimeType("application/octet-stream");
        docEntry.setPatientID(patientID);
        docEntry.setPracticeSettingCode(new Code("code5", new LocalizedString("code5"), "scheme5"));
        docEntry.setRepositoryUniqueId("34978634");
        docEntry.setServiceStartTime("198012");
        docEntry.setServiceStopTime("198101");
        docEntry.setSize(123L);
        docEntry.setSourcePatientID(new Identifiable("source", new AssigningAuthority("4.1")));
        docEntry.setSourcePatientInfo(patientInfo);
        docEntry.setTitle(new LocalizedString("Document 01", "en-US", "UTF8"));
        docEntry.setTypeCode(new Code("code6", new LocalizedString("code6"), "scheme6"));
        docEntry.setUniqueID("32848902348");
        docEntry.setUri("http://hierunten.com");
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
        
        return request;
    }

    /**
     * @return a sample request to retrieve a document set.
     */
    public static RetrieveDocumentSet createRetrieveDocumentSet() {
        RetrieveDocumentSet request = new RetrieveDocumentSet();
        request.getDocuments().add(new RetrieveDocument("repo1", "doc1", "home1"));
        request.getDocuments().add(new RetrieveDocument("repo2", "doc2", "home2"));
        return request;
    }
    
    /**
     * @return a sample sql query.
     */
    public static QueryRegistry createSqlQuery() {
        SqlQuery query = new SqlQuery();        
        query.setSql("SELECT * FROM INTERNET");
        QueryRegistry queryRegistry = new QueryRegistry(query);
        return queryRegistry;
    }
    
    /**
     * @return a sample stored query for get documents.
     */
    public static QueryRegistry createGetDocumentsQuery() {
        GetDocumentsQuery query = new GetDocumentsQuery();
        query.setHomeCommunityID("home1");
        query.getUUIDs().add("document01");
        
        return new QueryRegistry(query);
    }
    
    /**
     * @return a sample stored query for find documents.
     */
    public static QueryRegistry createFindDocumentsQuery() {
        FindDocumentsQuery query = new FindDocumentsQuery();
        
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("1.2")));
        query.getClassCodes().add(new Code("code1", null, "scheme1"));
        query.getClassCodes().add(new Code("code2", null, "scheme2"));
        query.getPracticeSettingCodes().add(new Code("code3", null, "scheme3"));
        query.getPracticeSettingCodes().add(new Code("code4", null, "scheme4"));
        query.getCreationTime().setFrom("1980");
        query.getCreationTime().setTo("1981");
        query.getServiceStartTime().setFrom("1982");
        query.getServiceStartTime().setTo("1983");
        query.getServiceStopTime().setFrom("1984");
        query.getServiceStopTime().setTo("1985");
        query.getHealthcareFacilityTypeCodes().add(new Code("code5", null, "scheme5"));
        query.getHealthcareFacilityTypeCodes().add(new Code("code6", null, "scheme6"));
        query.getEventCodes().getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        query.getEventCodes().getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.getConfidentialityCodes().getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        query.getConfidentialityCodes().getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        query.getAuthorPersons().add("per'son1");
        query.getAuthorPersons().add("person2");
        query.getFormatCodes().add(new Code("code13", null, "scheme13"));
        query.getFormatCodes().add(new Code("code14", null, "scheme14"));
        query.getStatus().add(AvailabilityStatus.APPROVED);
        query.getStatus().add(AvailabilityStatus.SUBMITTED);
        
        return new QueryRegistry(query);
    }
}

