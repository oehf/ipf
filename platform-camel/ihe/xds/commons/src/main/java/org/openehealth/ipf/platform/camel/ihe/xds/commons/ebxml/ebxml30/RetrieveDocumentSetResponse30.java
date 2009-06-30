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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RetrieveDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RetrieveDocumentSetResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RetrieveDocumentSetResponseType.DocumentResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorCode;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Severity;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryError;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryErrorList;

/**
 * The ebXML 3.0 version of the {@link RetrieveDocumentSetRequest}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetResponse30 implements RetrieveDocumentSetResponse {
    private final static ObjectFactory rsFactory = new ObjectFactory();
    private final RetrieveDocumentSetResponseType response;
    
    private RetrieveDocumentSetResponse30(RetrieveDocumentSetResponseType response) {
        notNull(response, "response cannot be null");
        this.response = response;
    }
    
    static RetrieveDocumentSetResponse30 create() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        response.setRegistryResponse(rsFactory.createRegistryResponseType());
        return new RetrieveDocumentSetResponse30(response);
    }
    
    RetrieveDocumentSetResponseType getInternal() {
        return response;
    }

    @Override
    public List<RetrievedDocument> getDocuments() {
        List<RetrievedDocument> docs = new ArrayList<RetrievedDocument>();
        for (DocumentResponse documentResponse : response.getDocumentResponse()) {
            RetrievedDocument doc = new RetrievedDocument();            
            doc.setDataHandler(documentResponse.getDocument());
            RetrieveDocument requestData = new RetrieveDocument();
            doc.setRequestData(requestData);
            requestData.setDocumentUniqueID(documentResponse.getDocumentUniqueId());
            requestData.setHomeCommunityID(documentResponse.getHomeCommunityId());
            requestData.setRepositoryUniqueID(documentResponse.getRepositoryUniqueId());
            docs.add(doc);
        }
        return docs;
    }

    @Override
    public void setDocuments(List<RetrievedDocument> documents) {
        response.getDocumentResponse().clear();
        if (documents != null) {
            for (RetrievedDocument doc : documents) {
                DocumentResponse documentResponse = new DocumentResponse();
                documentResponse.setDocument(doc.getDataHandler());
                if (doc.getDataHandler() != null) {
                    documentResponse.setMimeType(doc.getDataHandler().getContentType());
                }
                RetrieveDocument requestData = doc.getRequestData();
                if (requestData != null) {
                    documentResponse.setDocumentUniqueId(requestData.getDocumentUniqueID());
                    documentResponse.setHomeCommunityId(requestData.getHomeCommunityID());
                    documentResponse.setRepositoryUniqueId(requestData.getRepositoryUniqueID());
                }
                response.getDocumentResponse().add(documentResponse);
            }
        }
    }

    public void setStatus(Status status) {
        response.getRegistryResponse().setStatus(Status.getOpcode30(status));
    }
    
    public Status getStatus() {
        return Status.valueOfOpcode(response.getRegistryResponse().getStatus());
    }    

    @Override
    public List<ErrorInfo> getErrors() {
        RegistryErrorList list = response.getRegistryResponse().getRegistryErrorList();
        if (list == null) {
            return Collections.emptyList();
        }
        
        List<ErrorInfo> errors = new ArrayList<ErrorInfo>();
        for (RegistryError regError : list.getRegistryError()) {
            ErrorInfo error = new ErrorInfo();
            error.setCodeContext(regError.getCodeContext());
            error.setLocation(regError.getLocation());
            error.setErrorCode(ErrorCode.valueOfOpcode(regError.getErrorCode()));
            error.setServerity(Severity.valueOfOpcode30(regError.getSeverity()));
            errors.add(error);
        }
        
        return errors;
    }

    @Override
    public void setErrors(List<ErrorInfo> errors) {
        RegistryErrorList value = rsFactory.createRegistryErrorList();
        response.getRegistryResponse().setRegistryErrorList(value);
        List<RegistryError> list = value.getRegistryError();
        for (ErrorInfo error : errors) {
            RegistryError regError = rsFactory.createRegistryError();
            regError.setErrorCode(ErrorCode.getOpcode(error.getErrorCode()));
            regError.setCodeContext(error.getCodeContext());
            regError.setSeverity(Severity.getOpcode30(error.getServerity()));
            regError.setLocation(error.getLocation());
            list.add(regError);
        }
    }
}
