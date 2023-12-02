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
import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLNonconstructiveDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType.DocumentRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;

/**
 * The ebXML 3.0 version of the {@link EbXMLNonconstructiveDocumentSetRequest}.
 * @author Jens Riemschneider
 */
public class EbXMLNonconstructiveDocumentSetRequest30<T extends RetrieveDocumentSetRequestType> implements EbXMLNonconstructiveDocumentSetRequest {
    private final T request;

    /**
     * Constructs a request by wrapping the given ebXML 3.0 object.
     * @param request
     *          the object to wrap.
     */
    public EbXMLNonconstructiveDocumentSetRequest30(T request) {
        this.request = requireNonNull(request, "request cannot be null");;
    }

    @Override
    public T getInternal() {
        return request;
    }

    @Override
    public List<DocumentReference> getDocuments() {
        var docs = new ArrayList<DocumentReference>();
        for (var documentRequest : request.getDocumentRequest()) {
            var doc = new DocumentReference();
            doc.setDocumentUniqueId(documentRequest.getDocumentUniqueId());
            doc.setHomeCommunityId(documentRequest.getHomeCommunityId());
            doc.setRepositoryUniqueId(documentRequest.getRepositoryUniqueId());
            docs.add(doc);
        }
        return docs;
    }

    @Override
    public void setDocuments(List<DocumentReference> documents) {
        request.getDocumentRequest().clear();
        if (documents != null) {
            for (var doc : documents) {
                var documentRequest = new DocumentRequest();
                documentRequest.setDocumentUniqueId(doc.getDocumentUniqueId());
                documentRequest.setHomeCommunityId(doc.getHomeCommunityId());
                documentRequest.setRepositoryUniqueId(doc.getRepositoryUniqueId());
                request.getDocumentRequest().add(documentRequest);
            }
        }
    }
}
