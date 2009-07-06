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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml;


/**
 * Serves as a factory for ebXML objects.
 * <p>
 * This factory is used to provide version independent creation of
 * ebXML objects.
 * @author Jens Riemschneider
 */
public interface EbXMLFactory {
    /**
     * @return a new instance of an object library filled with object references used
     *          for ebXML requests/responses.
     */
    EbXMLObjectLibrary createObjectLibrary();

    /**
     * Creates a new classification.
     * @param objectLibrary
     *          the object library to use.
     * @return the created object.
     */
    EbXMLClassification createClassification(EbXMLObjectLibrary objectLibrary);
    
    /**
     * Creates a new extrinsic object and adds it to the object library.
     * @param id
     *          the id of the object within the object library.
     * @param objectLibrary
     *          the object library to use.
     * @return the created object.
     */
    EbXMLExtrinsicObject createExtrinsic(String id, EbXMLObjectLibrary objectLibrary);

    /**
     * Creates a new registry package and adds it to the object library.
     * @param id
     *          the id of the object within the object library.
     * @param objectLibrary
     *          the object library to use.
     * @return the created object.
     */
    EbXMLRegistryPackage createRegistryPackage(String id, EbXMLObjectLibrary objectLibrary);

    /**
     * Creates a new association.
     * @param objectLibrary
     *          the object library to use.
     * @return the created object.
     */
    EbXMLAssociation createAssociation(EbXMLObjectLibrary objectLibrary);
    
    /**
     * Creates a new request to submit objects.
     * @return the created object.
     */
    EbXMLSubmitObjectsRequest createSubmitObjectsRequest();
    
    /**
     * Creates a new request to provide and register documents.
     * @param objectLibrary
     *          the object library to use.
     * @return the created object.
     */
    EbXMLProvideAndRegisterDocumentSetRequest createProvideAndRegisterDocumentSetRequest(EbXMLObjectLibrary library);
    
    /**
     * Creates a new request to retrieve documents.
     * @return the created object.
     */
    EbXMLRetrieveDocumentSetRequest createRetrieveDocumentSetRequest();

    /**
     * Creates a new request to query a registry.
     * @return the created object.
     */
    EbXMLAdhocQueryRequest createAdhocQueryRequest();

    /**
     * Creates a new response for a query request.
     * @param objectLibrary
     *          the object library to use.
     * @return the created object.
     */
    EbXMLQueryResponse createAdhocQueryResponse(EbXMLObjectLibrary objectLibrary);

    /**
     * Creates a new response for a registry request.
     * @return the created object.
     */
    EbXMLRegistryResponse createRegistryResponse();

    /**
     * Creates a new response for a retrieve document request.
     * @return the created object.
     */
    EbXMLRetrieveDocumentSetResponse createRetrieveDocumentSetResponse();    
}
