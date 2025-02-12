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

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType.DocumentResponse;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;

/**
 * The ebXML 3.0 version of the {@link EbXMLRetrieveDocumentSetResponse}.
 * @author Jens Riemschneider
 */
public class EbXMLRetrieveDocumentSetResponse30 implements EbXMLRetrieveDocumentSetResponse<RetrieveDocumentSetResponseType> {
    private final RetrieveDocumentSetResponseType response;

    /**
     * Constructs a response by wrapping the given ebXML 3.0 object.
     * @param response
     *          the object to wrap.
     */
    public EbXMLRetrieveDocumentSetResponse30(RetrieveDocumentSetResponseType response) {
        this.response = requireNonNull(response, "response cannot be null");
    }

    @Override
    public RetrieveDocumentSetResponseType getInternal() {
        return response;
    }

    @Override
    public List<RetrievedDocument> getDocuments() {
        var docs = new ArrayList<RetrievedDocument>();
        for (var documentResponse : response.getDocumentResponse()) {
            var requestData = new DocumentReference();
            requestData.setDocumentUniqueId(documentResponse.getDocumentUniqueId());
            requestData.setHomeCommunityId(documentResponse.getHomeCommunityId());
            requestData.setRepositoryUniqueId(documentResponse.getRepositoryUniqueId());
            var doc = getRetrievedDocument(documentResponse, requestData);
            docs.add(doc);
        }
        return docs;
    }

    private static RetrievedDocument getRetrievedDocument(DocumentResponse documentResponse, DocumentReference requestData) {
        var doc = new RetrievedDocument();
        doc.setDataHandler(documentResponse.getDocument());
        doc.setRequestData(requestData);
        doc.setNewRepositoryUniqueId(documentResponse.getNewRepositoryUniqueId());
        doc.setNewDocumentUniqueId(documentResponse.getNewDocumentUniqueId());
        if (documentResponse.getMimeType() != null) {
            doc.setMimeType(documentResponse.getMimeType());
        } else if (documentResponse.getDocument() != null) {
            doc.setMimeType(documentResponse.getDocument().getContentType());
        }
        return doc;
    }

    @Override
    public void setDocuments(List<RetrievedDocument> documents) {
        response.getDocumentResponse().clear();
        if (documents != null) {
            for (var doc : documents) {
                var documentResponse = new DocumentResponse();
                documentResponse.setDocument(doc.getDataHandler());
                documentResponse.setNewRepositoryUniqueId(doc.getNewRepositoryUniqueId());
                documentResponse.setNewDocumentUniqueId(doc.getNewDocumentUniqueId());
                if (doc.getMimeType() != null) {
                    documentResponse.setMimeType(doc.getMimeType());
                } else if (doc.getDataHandler() != null) {
                    documentResponse.setMimeType(doc.getDataHandler().getContentType());
                }
                var requestData = doc.getRequestData();
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
        if (response.getRegistryResponse() != null){
            response.getRegistryResponse().setStatus(Status.getOpcode30(status));
        }
    }

    @Override
    public Status getStatus() {
        Status status = null;
        if (response.getRegistryResponse() != null){
            status = Status.valueOfOpcode(response.getRegistryResponse().getStatus());
        }
        return status;
    }

    @Override
    public List<EbXMLRegistryError> getErrors() {
        var list = response.getRegistryResponse().getRegistryErrorList();
        if (list == null) {
            return Collections.emptyList();
        }

        return list.getRegistryError().stream()
                .map(EbXMLRegistryError30::new)
                .collect(Collectors.toList());
    }

    @Override
    public void setErrors(List<EbXMLRegistryError> errors) {
        var value = EbXMLFactory30.RS_FACTORY.createRegistryErrorList();
        response.getRegistryResponse().setRegistryErrorList(value);
        var list = value.getRegistryError();
        list.addAll(errors.stream()
                .map(error -> ((EbXMLRegistryError30) error).getInternal())
                .toList());
    }
}
