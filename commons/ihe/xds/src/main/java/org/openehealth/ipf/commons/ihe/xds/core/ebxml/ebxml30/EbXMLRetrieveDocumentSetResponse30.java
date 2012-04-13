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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType.DocumentResponse;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryErrorList;

/**
 * The ebXML 3.0 version of the {@link EbXMLRetrieveDocumentSetRequest}.
 * @author Jens Riemschneider
 */
public class EbXMLRetrieveDocumentSetResponse30 implements EbXMLRetrieveDocumentSetResponse {
    private final RetrieveDocumentSetResponseType response;

    /**
     * Constructs a response by wrapping the given ebXML 3.0 object.
     * @param response
     *          the object to wrap.
     */
    public EbXMLRetrieveDocumentSetResponse30(RetrieveDocumentSetResponseType response) {
        notNull(response, "response cannot be null");
        this.response = response;
    }
    
    @Override
    public RetrieveDocumentSetResponseType getInternal() {
        return response;
    }

    @Override
    public List<RetrievedDocument> getDocuments() {
        List<RetrievedDocument> docs = new ArrayList<RetrievedDocument>();
        for (DocumentResponse documentResponse : response.getDocumentResponse()) {
            RetrieveDocument requestData = new RetrieveDocument();
            requestData.setDocumentUniqueId(documentResponse.getDocumentUniqueId());
            requestData.setHomeCommunityId(documentResponse.getHomeCommunityId());
            requestData.setRepositoryUniqueId(documentResponse.getRepositoryUniqueId());

            RetrievedDocument doc = new RetrievedDocument();
            doc.setDataHandler(documentResponse.getDocument());
            doc.setRequestData(requestData);
            doc.setNewRepositoryUniqueId(documentResponse.getNewRepositoryUniqueId());
            doc.setNewDocumentUniqueId(documentResponse.getNewDocumentUniqueId());
            if (documentResponse.getMimeType() != null) {
                doc.setMimeType(documentResponse.getMimeType());
            } else if (documentResponse.getDocument() != null) {
                doc.setMimeType(documentResponse.getDocument().getContentType());
            }

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
                documentResponse.setNewRepositoryUniqueId(doc.getNewRepositoryUniqueId());
                documentResponse.setNewDocumentUniqueId(doc.getNewDocumentUniqueId());
                if (doc.getMimeType() != null) {
                    documentResponse.setMimeType(doc.getMimeType());
                } else if (doc.getDataHandler() != null) {
                    documentResponse.setMimeType(doc.getDataHandler().getContentType());
                }
                RetrieveDocument requestData = doc.getRequestData();
                if (requestData != null) {
                    documentResponse.setDocumentUniqueId(requestData.getDocumentUniqueId());
                    documentResponse.setHomeCommunityId(requestData.getHomeCommunityId());
                    documentResponse.setRepositoryUniqueId(requestData.getRepositoryUniqueId());
                }
                response.getDocumentResponse().add(documentResponse);
            }
        }
    }

    @Override
    public void setStatus(Status status) {
        response.getRegistryResponse().setStatus(Status.getOpcode30(status));
    }

    @Override
    public Status getStatus() {
        return Status.valueOfOpcode(response.getRegistryResponse().getStatus());
    }

    @Override
    public List<EbXMLRegistryError> getErrors() {
        RegistryErrorList list = response.getRegistryResponse().getRegistryErrorList();
        if (list == null) {
            return Collections.emptyList();
        }

        List<EbXMLRegistryError> errors = new ArrayList<EbXMLRegistryError>();
        for (RegistryError regError : list.getRegistryError()) {
            errors.add(new EbXMLRegistryError30(regError));
        }

        return errors;
    }

    @Override
    public void setErrors(List<EbXMLRegistryError> errors) {
        RegistryErrorList value = EbXMLFactory30.RS_FACTORY.createRegistryErrorList();
        response.getRegistryResponse().setRegistryErrorList(value);
        List<RegistryError> list = value.getRegistryError();
        for (EbXMLRegistryError error : errors) {
            list.add(((EbXMLRegistryError30) error).getInternal());
        }
    }
}
