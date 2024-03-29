/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveImagingDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveImagingDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.xdsi.RetrieveImagingDocumentSetRequestType;

import static java.util.Objects.requireNonNull;

/**
 * Transforms between a {@link EbXMLRetrieveImagingDocumentSetRequest} and its ebXML representation.
 *
 * @author Clay Sebourn
 */
public class RetrieveImagingDocumentSetRequestTransformer
{
    private final EbXMLFactory factory;

    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects.
     */
    public RetrieveImagingDocumentSetRequestTransformer(EbXMLFactory factory) {
        requireNonNull(factory, "factory cannot be null");
        this.factory = factory;
    }
    
    /**
     * Transforms the request into its ebXML representation.
     * @param request    The request. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLRetrieveImagingDocumentSetRequest<RetrieveImagingDocumentSetRequestType> toEbXML(RetrieveImagingDocumentSet request) {
        if (request == null) {
            return null;
        }

        var ebXML = factory.createRetrieveImagingDocumentSetRequest();
        ebXML.setRetrieveStudies(request.getRetrieveStudies());
        ebXML.setTransferSyntaxUIDList(request.getTransferSyntaxIds());
        return ebXML;
    }
    
    /**
     * Transforms the ebXML representation into a request.
     * @param ebXML     The ebXML representation. Can be <code>null</code>.
     * @return the request. <code>null</code> if the input was <code>null</code>.
     */
    public RetrieveImagingDocumentSet fromEbXML(EbXMLRetrieveImagingDocumentSetRequest<RetrieveImagingDocumentSetRequestType> ebXML) {
        if (ebXML == null) {
            return null;
        }

        var request = new RetrieveImagingDocumentSet();
        request.getRetrieveStudies().addAll(ebXML.getRetrieveStudies());
        request.getTransferSyntaxIds().addAll(ebXML.getTransferSyntaxUIDList());
        return request;
    }
}
