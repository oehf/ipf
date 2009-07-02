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
 * @author Jens Riemschneider
 */
public interface EbXMLFactory {
    ObjectLibrary createObjectLibrary();

    Classification createClassification(ObjectLibrary objectLibrary);
    ExtrinsicObject createExtrinsic(String id, ObjectLibrary objectLibrary);
    RegistryPackage createRegistryPackage(String id, ObjectLibrary objectLibrary);
    EbXMLAssociation createAssociation(ObjectLibrary objectLibrary);
    SubmitObjectsRequest createSubmitObjectsRequest(ObjectLibrary objectLibrary);
    ProvideAndRegisterDocumentSetRequest createProvideAndRegisterDocumentSetRequest(ObjectLibrary library);
    RetrieveDocumentSetRequest createRetrieveDocumentSetRequest();
    AdhocQueryRequest createAdhocQueryRequest();
    EbXMLQueryResponse createAdhocQueryResponse(ObjectLibrary objectLibrary);
    RegistryResponse createRegistryResponse();
    RetrieveDocumentSetResponse createRetrieveDocumentSetResponse();    
}
