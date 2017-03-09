/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.NonconstructiveDocumentSetRequest;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Transforms between a {@link NonconstructiveDocumentSetRequest} and its ebXML representation.
 * @author Jens Riemschneider
 */
abstract public class AbstractRepositoryDocumentSetRequestTransformer<T extends NonconstructiveDocumentSetRequest> {
    private final EbXMLFactory factory;

    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects.
     */
    protected AbstractRepositoryDocumentSetRequestTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
    }
    
    /**
     * Transforms the request into its ebXML representation.
     * @param request
     *          the request. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLRetrieveDocumentSetRequest toEbXML(T request) {
        if (request == null) {
            return null;
        }
        
        EbXMLRetrieveDocumentSetRequest ebXML = factory.createRetrieveDocumentSetRequest();
        ebXML.setDocuments(request.getDocuments());        
        return ebXML;
    }
    
    /**
     * Transforms the ebXML representation into a request.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the request. <code>null</code> if the input was <code>null</code>.
     */
    public T fromEbXML(EbXMLRetrieveDocumentSetRequest ebXML) {
        if (ebXML == null) {
            return null;
        }
            
        T request = createRequest();
        request.getDocuments().addAll(ebXML.getDocuments());        
        return request;
    }

    abstract protected T createRequest();
}
