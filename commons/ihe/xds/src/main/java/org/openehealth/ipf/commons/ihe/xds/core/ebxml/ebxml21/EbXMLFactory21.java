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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.ResponseOptionType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.AssociationType1;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.RegistryPackageType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * Factory for EbXML 2.1 objects.
 * @author Jens Riemschneider
 */
public class EbXMLFactory21 implements EbXMLFactory {
    /**
     * The object factory for the ebXML 2.1 rim namespace.
     */
    public final static org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ObjectFactory RIM_FACTORY =
        new org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ObjectFactory();
    
    /**
     * The object factory for the ebXML 2.1 query namespace.
     */
    public final static org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.ObjectFactory QUERY_FACTORY =
        new org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.ObjectFactory();
    
    /**
     * The object factory for the ebXML 2.1 rs namespace.
     */
    public final static org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.ObjectFactory RS_FACTORY =
        new org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.ObjectFactory();

    @Override
    public EbXMLObjectLibrary createObjectLibrary() {
        EbXMLObjectLibrary lib = new EbXMLObjectLibrary(); 
        addObjToLib(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_CLASS_CODE_CLASS_SCHEME, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_EVENT_CODE_CLASS_SCHEME, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_TYPE_CODE_CLASS_SCHEME, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, lib);
        addObjToLib(Vocabulary.DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID, lib); 
        
        addObjToLib(Vocabulary.FOLDER_CLASS_NODE, lib);         
        addObjToLib(Vocabulary.FOLDER_CODE_LIST_CLASS_SCHEME, lib);         
        addObjToLib(Vocabulary.FOLDER_PATIENT_ID_EXTERNAL_ID, lib);         
        addObjToLib(Vocabulary.FOLDER_UNIQUE_ID_EXTERNAL_ID, lib);
        
        addObjToLib(Vocabulary.SUBMISSION_SET_CLASS_NODE, lib);         
        addObjToLib(Vocabulary.SUBMISSION_SET_AUTHOR_CLASS_SCHEME, lib);         
        addObjToLib(Vocabulary.SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME, lib);         
        addObjToLib(Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID, lib);                 
        addObjToLib(Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID, lib);                 
        addObjToLib(Vocabulary.SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID, lib);
        
        addObjToLib(Vocabulary.ASSOCIATION_DOC_CODE_CLASS_SCHEME, lib);
        
        return lib;
    }

    private void addObjToLib(String id, EbXMLObjectLibrary objectLibrary) {
        ObjectRefType objRef = RIM_FACTORY.createObjectRefType();
        objRef.setId(id);
        objectLibrary.put(id, objRef);
    }
    
    @Override
    public EbXMLClassification21 createClassification(EbXMLObjectLibrary objectLibrary) {
        return new EbXMLClassification21(RIM_FACTORY.createClassificationType(), objectLibrary);
    }

    @Override
    public EbXMLExtrinsicObject21 createExtrinsic(String id, EbXMLObjectLibrary objectLibrary) {
        ExtrinsicObjectType extrinsicObjectType = RIM_FACTORY.createExtrinsicObjectType();
        extrinsicObjectType.setId(id);
        objectLibrary.put(id, extrinsicObjectType);
        return new EbXMLExtrinsicObject21(extrinsicObjectType, objectLibrary);
    }

    @Override
    public EbXMLRegistryPackage21 createRegistryPackage(String id, EbXMLObjectLibrary objectLibrary) {
        RegistryPackageType registryPackageType = RIM_FACTORY.createRegistryPackageType();
        registryPackageType.setId(id);
        objectLibrary.put(id, registryPackageType);
        return new EbXMLRegistryPackage21(registryPackageType, objectLibrary);
    }

    @Override
    public EbXMLAssociation21 createAssociation(String id, EbXMLObjectLibrary objectLibrary) {
        AssociationType1 association = RIM_FACTORY.createAssociationType1();
        association.setId(id);
        objectLibrary.put(id, association);
        return new EbXMLAssociation21(association, objectLibrary);
    }

    @Override
    public EbXMLSubmitObjectsRequest21 createSubmitObjectsRequest() {
        SubmitObjectsRequest raw = RS_FACTORY.createSubmitObjectsRequest();
        LeafRegistryObjectListType list = raw.getLeafRegistryObjectList();
        if (list == null) {
            list = RIM_FACTORY.createLeafRegistryObjectListType();
            raw.setLeafRegistryObjectList(list);
        }
        
        EbXMLObjectLibrary objectLibrary = createObjectLibrary();
        EbXMLSubmitObjectsRequest21 wrapped = new EbXMLSubmitObjectsRequest21(raw, objectLibrary);
        wrapped.getContents().addAll(objectLibrary.getObjects());
        return wrapped;
    }

    @Override    
    public EbXMLProvideAndRegisterDocumentSetRequest21 createProvideAndRegisterDocumentSetRequest(EbXMLObjectLibrary objectLibrary) {
        ProvideAndRegisterDocumentSetRequestType raw = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitObjectsRequest = raw.getSubmitObjectsRequest();
        if (submitObjectsRequest == null) {
            submitObjectsRequest = RS_FACTORY.createSubmitObjectsRequest();
            raw.setSubmitObjectsRequest(submitObjectsRequest);
        }
        
        LeafRegistryObjectListType list = submitObjectsRequest.getLeafRegistryObjectList();
        if (list == null) {
            list = RIM_FACTORY.createLeafRegistryObjectListType();
            submitObjectsRequest.setLeafRegistryObjectList(list);
        }
        
        EbXMLProvideAndRegisterDocumentSetRequest21 wrapped = new EbXMLProvideAndRegisterDocumentSetRequest21(raw, objectLibrary);
        wrapped.getContents().addAll(objectLibrary.getObjects());
        return wrapped;
    }

    @Override
    public EbXMLRegistryResponse21 createRegistryResponse() {
        return new EbXMLRegistryResponse21(RS_FACTORY.createRegistryResponse());
    }

    @Override
    public EbXMLRetrieveDocumentSetRequest createRetrieveDocumentSetRequest() {
        throw new UnsupportedOperationException("Only supported for ebXML 3.0");
    }

    @Override
    public EbXMLRetrieveDocumentSetResponse createRetrieveDocumentSetResponse() {
        throw new UnsupportedOperationException("Only supported for ebXML 3.0");
    }
    
    @Override
    public EbXMLRetrieveImagingDocumentSetRequest createRetrieveImagingDocumentSetRequest() {
        throw new UnsupportedOperationException("Only supported for ebXML 3.0");
    }

    @Override
    public EbXMLAdhocQueryRequest21 createAdhocQueryRequest() {
        AdhocQueryRequest request = QUERY_FACTORY.createAdhocQueryRequest();
        
        ResponseOptionType responseOption = QUERY_FACTORY.createResponseOptionType();
        responseOption.setReturnComposedObjects(true);
        
        request.setResponseOption(responseOption);
        return new EbXMLAdhocQueryRequest21(request);
    }

    @Override
    public EbXMLQueryResponse21 createAdhocQueryResponse(EbXMLObjectLibrary objectLibrary, boolean returnsObjectRefs) {
        AdhocQueryResponse adhocQueryResponse = QUERY_FACTORY.createAdhocQueryResponse();
        adhocQueryResponse.setSQLQueryResult(RIM_FACTORY.createRegistryObjectListType());

        RegistryResponse response = RS_FACTORY.createRegistryResponse();
        response.setAdhocQueryResponse(adhocQueryResponse);
        
        EbXMLQueryResponse21 wrapped = new EbXMLQueryResponse21(response, objectLibrary);
        if (!returnsObjectRefs) {
            // Do not include object library if query results are object references!
            wrapped.getContents().addAll(objectLibrary.getObjects());
        }
        return wrapped;        
    }

    @Override
    public EbXMLRegistryError createRegistryError() {
        RegistryError registryError = RS_FACTORY.createRegistryError();
        return new EbXMLRegistryError21(registryError);
    }
}
