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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.activation.DataHandler;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Document;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.SqlQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorCode;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Severity;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;

/**
 * Utility class to create sample data used in tests.
 * @author Jens Riemschneider
 */
public abstract class SampleData {
    private SampleData() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * @return a sample query response.
     */
    public static QueryResponse createQueryResponse() {
        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setEntryUUID("submissionSet01");
        submissionSet.setTitle(new LocalizedString("Submission Set 01", "en-US", "UTF8"));
        
        DocumentEntry docEntry = new DocumentEntry();
        docEntry.setEntryUUID("document01");
        docEntry.setTitle(new LocalizedString("Document 01", "en-US", "UTF8"));
        docEntry.setMimeType("application/octet-stream");
        
        Folder folder = new Folder();
        folder.setEntryUUID("folder01");
        folder.setTitle(new LocalizedString("Folder 01", "en-US", "UTF8"));
        
        Association docAssociation = new Association();
        docAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docAssociation.setSourceUUID("submissionSet01");
        docAssociation.setTargetUUID("document01");        

        Association folderAssociation = new Association();
        folderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        folderAssociation.setSourceUUID("submissionSet01");
        folderAssociation.setTargetUUID("folder01");
        
        Association docFolderAssociation = new Association();
        docFolderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docFolderAssociation.setSourceUUID("folder01");
        docFolderAssociation.setTargetUUID("document01");
        
        QueryResponse response = new QueryResponse();
        response.getSubmissionSets().add(submissionSet);
        response.getDocumentEntries().add(docEntry);
        response.getFolders().add(folder);
        response.getAssociations().add(docAssociation);
        response.getAssociations().add(folderAssociation);
        response.getAssociations().add(docFolderAssociation);
        
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
        
        DataHandler dataHandler1 = new DataHandler(createUrl());
        
        RetrievedDocument doc1 = new RetrievedDocument();
        doc1.setRequestData(requestData1);
        doc1.setDataHandler(dataHandler1);

        RetrieveDocument requestData2 = new RetrieveDocument();
        requestData2.setDocumentUniqueID("doc2");
        requestData2.setHomeCommunityID("home2");
        requestData2.setRepositoryUniqueID("repo2");
        
        DataHandler dataHandler2 = new DataHandler(createUrl());        
        RetrievedDocument doc2 = new RetrievedDocument();
        doc2.setRequestData(requestData2);
        doc2.setDataHandler(dataHandler2);
        
        RetrievedDocumentSet response = new RetrievedDocumentSet();
        response.getDocuments().add(doc1);
        response.getDocuments().add(doc2);
        
        return response;
    }

    /**
     * @return a sample request to provide and register documents.
     */
    public static ProvideAndRegisterDocumentSet createProvideAndRegisterDocumentSet() {
        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setEntryUUID("submissionSet01");
        submissionSet.setTitle(new LocalizedString("Submission Set 01", "en-US", "UTF8"));
        
        DocumentEntry docEntry = new DocumentEntry();
        docEntry.setEntryUUID("document01");
        docEntry.setTitle(new LocalizedString("Document 01", "en-US", "UTF8"));
        docEntry.setMimeType("application/octet-stream");
        
        Folder folder = new Folder();
        folder.setEntryUUID("folder01");
        folder.setTitle(new LocalizedString("Folder 01", "en-US", "UTF8"));
        
        Association docAssociation = new Association();
        docAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docAssociation.setSourceUUID("submissionSet01");
        docAssociation.setTargetUUID("document01");        

        Association folderAssociation = new Association();
        folderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        folderAssociation.setSourceUUID("submissionSet01");
        folderAssociation.setTargetUUID("folder01");
        
        Association docFolderAssociation = new Association();
        docFolderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docFolderAssociation.setSourceUUID("folder01");
        docFolderAssociation.setTargetUUID("document01");
        
        DataHandler dataHandler = new DataHandler(createUrl());
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

    /**
     * @return a sample request to register a document set.
     */
    public static RegisterDocumentSet createRegisterDocumentSet() {
        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setEntryUUID("submissionSet01");
        submissionSet.setTitle(new LocalizedString("Submission Set 01", "en-US", "UTF8"));
        
        DocumentEntry docEntry = new DocumentEntry();
        docEntry.setEntryUUID("document01");
        docEntry.setTitle(new LocalizedString("Document 01", "en-US", "UTF8"));
        docEntry.setMimeType("application/octet-stream");
        
        Folder folder = new Folder();
        folder.setEntryUUID("folder01");
        folder.setTitle(new LocalizedString("Folder 01", "en-US", "UTF8"));
        
        Association docAssociation = new Association();
        docAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docAssociation.setSourceUUID("submissionSet01");
        docAssociation.setTargetUUID("document01");        

        Association folderAssociation = new Association();
        folderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        folderAssociation.setSourceUUID("submissionSet01");
        folderAssociation.setTargetUUID("folder01");
        
        Association docFolderAssociation = new Association();
        docFolderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docFolderAssociation.setSourceUUID("folder01");
        docFolderAssociation.setTargetUUID("document01");
        
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
        RetrieveDocument doc1 = new RetrieveDocument();
        doc1.setDocumentUniqueID("doc1");
        doc1.setHomeCommunityID("home1");
        doc1.setRepositoryUniqueID("repo1");

        RetrieveDocument doc2 = new RetrieveDocument();
        doc2.setDocumentUniqueID("doc2");
        doc2.setHomeCommunityID("home2");
        doc2.setRepositoryUniqueID("repo2");
        
        RetrieveDocumentSet request = new RetrieveDocumentSet();
        request.getDocuments().add(doc1);
        request.getDocuments().add(doc2);
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
     * @return a sample stored query for find documents.
     */
    public static QueryRegistry createFindDocumentsQuery() {
        FindDocumentsQuery query = new FindDocumentsQuery();
        
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("name1", "uni1", "uniType1")));
        query.getClassCodes().add(new Code("code1", null, "scheme1"));
        query.getClassCodes().add(new Code("code2", null, "scheme2"));
        query.getPracticeSettingCodes().add(new Code("code3", null, "scheme3"));
        query.getPracticeSettingCodes().add(new Code("code4", null, "scheme4"));
        query.getCreationTime().setFrom("1");
        query.getCreationTime().setTo("2");
        query.getServiceStartTime().setFrom("3");
        query.getServiceStartTime().setTo("4");
        query.getServiceStopTime().setFrom("5");
        query.getServiceStopTime().setTo("6");
        query.getHealthCareFacilityTypeCodes().add(new Code("code5", null, "scheme5"));
        query.getHealthCareFacilityTypeCodes().add(new Code("code6", null, "scheme6"));
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
        query.getFormatCodes().add(new Code("code13", null, null));
        query.getFormatCodes().add(new Code("code14", null, null));
        query.getStatus().add(AvailabilityStatus.APPROVED);
        query.getStatus().add(AvailabilityStatus.SUBMITTED);
        
        return new QueryRegistry(query);
    }
    
    private static URL createUrl() throws AssertionError {
        try {
            return new URL("http://file");
        } catch (MalformedURLException e) {
            throw new AssertionError("Not a valid URL");
        }
    }
}

