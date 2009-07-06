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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests;

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRetrieveDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocumentSet;

/**
 * Transforms between a {@link EbXMLRetrieveDocumentSetRequest} and its ebXML representation. 
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetRequestTransformer {
    private final EbXMLFactory factory;
    
    public RetrieveDocumentSetRequestTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
    }
    
    public EbXMLRetrieveDocumentSetRequest toEbXML(RetrieveDocumentSet request) {
        if (request == null) {
            return null;
        }
        
        EbXMLRetrieveDocumentSetRequest ebXML = factory.createRetrieveDocumentSetRequest();
        ebXML.setDocuments(request.getDocuments());        
        return ebXML;
    }
    
    public RetrieveDocumentSet fromEbXML(EbXMLRetrieveDocumentSetRequest ebXML) {
        if (ebXML == null) {
            return null;
        }
            
        RetrieveDocumentSet request = new RetrieveDocumentSet();
        request.getDocuments().addAll(ebXML.getDocuments());        
        return request;
    }
}
