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
package org.openehealth.ipf.commons.ihe.xds.core.transform.responses;

import static java.util.Objects.requireNonNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;

/**
 * Transforms between a {@link EbXMLRetrieveDocumentSetResponse} and its ebXML representation.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetResponseTransformer {
    private final EbXMLFactory factory;
    private final ErrorInfoListTransformer errorInfoListTransformer;

    /**
     * Constructs the transformer.
     * @param factory
     *          the factory for ebXML objects.
     */
    public RetrieveDocumentSetResponseTransformer(EbXMLFactory factory) {
        this.factory = requireNonNull(factory, "factory cannot be null");
        this.errorInfoListTransformer = new ErrorInfoListTransformer(factory);
    }

    /**
     * Transforms a {@link RetrievedDocumentSet} to a {@link EbXMLRetrieveDocumentSetResponse}.
     * @param response
     *          the response. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLRetrieveDocumentSetResponse<RetrieveDocumentSetResponseType> toEbXML(RetrievedDocumentSet response) {
        if (response == null) {
            return null;
        }

        var ebXML = factory.createRetrieveDocumentSetResponse();
        if (!response.getErrors().isEmpty()) {
            ebXML.setErrors(errorInfoListTransformer.toEbXML(response.getErrors()));
        }
        ebXML.setStatus(response.getStatus());
        ebXML.setDocuments(response.getDocuments());
        return ebXML;
    }

    /**
     * Transforms a {@link EbXMLRetrieveDocumentSetResponse} to a {@link RetrievedDocumentSet}.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the response. <code>null</code> if the input was <code>null</code>.
     */
    public RetrievedDocumentSet fromEbXML(EbXMLRetrieveDocumentSetResponse<RetrieveDocumentSetResponseType> ebXML) {
        if (ebXML == null) {
            return null;
        }

        var response = new RetrievedDocumentSet();
        response.getDocuments().addAll(ebXML.getDocuments());
        if (!ebXML.getErrors().isEmpty()) {
            response.setErrors(errorInfoListTransformer.fromEbXML(ebXML.getErrors()));
        }
        response.setStatus(ebXML.getStatus());
        return response;
    }
}
