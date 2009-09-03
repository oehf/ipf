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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml;

import java.util.Map;

import javax.activation.DataHandler;

/**
 * Encapsulation of the ebXML classes for {@code ProvideAndRegisterDocumentSetRequestType}. 
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLProvideAndRegisterDocumentSetRequest extends EbXMLSubmitObjectsRequest {
    /**
     * @return the documents contained in the request. This map is read-only.
     */
    Map<String, DataHandler> getDocuments();
    
    /**
     * Adds a new document to the request.
     * @param id
     *          the id of the document.
     * @param dataHandler
     *          the data handler allowing access and describing to the document contents.
     */
    void addDocument(String id, DataHandler dataHandler);
    
    /**
     * Removes a document from the request.
     * @param id
     *          the id of the document.
     */
    void removeDocument(String id);
}
