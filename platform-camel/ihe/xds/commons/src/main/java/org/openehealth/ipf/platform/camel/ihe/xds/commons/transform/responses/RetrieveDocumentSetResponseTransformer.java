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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses;

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RetrieveDocumentSetResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocumentSet;

/**
 * Transforms between a {@link RetrieveDocumentSetResponse} and its ebXML representation. 
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetResponseTransformer {
    private final EbXMLFactory factory;
    
    public RetrieveDocumentSetResponseTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
    }
    
    public RetrieveDocumentSetResponse toEbXML(RetrievedDocumentSet response) {
        if (response == null) {
            return null;
        }
        
        RetrieveDocumentSetResponse ebXML = factory.createRetrieveDocumentSetResponse();
        ebXML.setErrors(response.getErrors());
        ebXML.setStatus(response.getStatus());
        ebXML.setDocuments(response.getDocuments());        
        return ebXML;
    }
    
    public RetrievedDocumentSet fromEbXML(RetrieveDocumentSetResponse ebXML) {
        if (ebXML == null) {
            return null;
        }
            
        RetrievedDocumentSet response = new RetrievedDocumentSet();
        response.getDocuments().addAll(ebXML.getDocuments());
        response.getErrors().addAll(ebXML.getErrors());
        response.setStatus(ebXML.getStatus());
        return response;
    }
}
