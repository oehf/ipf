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


import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RemoveDocumentsRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.xdsi.RetrieveImagingDocumentSetRequestType;

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
     * @param id
     *          the id of the object within the object library.
     * @param objectLibrary
     *          the object library to use.
     * @return the created object.
     */
    EbXMLAssociation createAssociation(String id, EbXMLObjectLibrary objectLibrary);
    
    /**
     * Creates a new request to submit objects.
     * @return the created object.
     */
    EbXMLSubmitObjectsRequest<SubmitObjectsRequest> createSubmitObjectsRequest();
    
    /**
     * Creates a new request to provide and register documents.
     * @param library
     *          the object library to use.
     * @return the created object.
     */
    EbXMLProvideAndRegisterDocumentSetRequest<ProvideAndRegisterDocumentSetRequestType> createProvideAndRegisterDocumentSetRequest(EbXMLObjectLibrary library);

    /**
     * Creates a new request to retrieve documents.
     * @return the created object.
     */
    EbXMLNonconstructiveDocumentSetRequest<RetrieveDocumentSetRequestType> createRetrieveDocumentSetRequest();

    /**
     * Creates a new request to remove documents.
     * @return the created object.
     */
    EbXMLNonconstructiveDocumentSetRequest<RemoveDocumentsRequestType> createRemoveDocumentsRequest();

    /**
     * Creates a new request to retrieve imaging documents.
     * @return the created object.
     */
    EbXMLRetrieveImagingDocumentSetRequest<RetrieveImagingDocumentSetRequestType> createRetrieveImagingDocumentSetRequest();

    /**
     * Creates a new request to query a registry.
     * @return the created object.
     */
    EbXMLAdhocQueryRequest<AdhocQueryRequest> createAdhocQueryRequest();

    /**
     * Creates a new response for a query request.
     * @param objectLibrary
     *          the object library to use.
     * @param returnsObjectRefs
     *          <code>true</code> if the response is meant to return object references instead
     *          of the real objects. 
     *          This parameter should be <code>true</code> to ensure that an object library
     *          is not included in the query result contains ObjectRefs. Those ObjectRefs 
     *          cannot be distinguished from the ObjectRefs of the object library and
     *          therefore the object library would produce unwanted query results. 
     * @return the created object.
     */
    EbXMLQueryResponse<AdhocQueryResponse> createAdhocQueryResponse(EbXMLObjectLibrary objectLibrary, boolean returnsObjectRefs);

    /**
     * Creates a new response for a registry request.
     * @return the created object.
     */
    EbXMLRegistryResponse<RegistryResponseType> createRegistryResponse();

    /**
     * Creates a new response for a retrieve document request.
     * @return the created object.
     */
    EbXMLRetrieveDocumentSetResponse<RetrieveDocumentSetResponseType> createRetrieveDocumentSetResponse();

    /**
     * Creates a new registry error object.
     * @return the created object.
     */
    EbXMLRegistryError createRegistryError();

    /**
     * Creates a new remove metadata request.
     * @return the created object.
     */
    EbXMLRemoveMetadataRequest<RemoveObjectsRequest> createRemoveMetadataRequest();
}
