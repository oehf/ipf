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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RetrieveDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RetrieveDocumentSetResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory;

/**
 * Factory for EbXML 2.1 objects.
 * @author Jens Riemschneider
 */
public class EbXMLFactory21 implements EbXMLFactory {
    private final static ObjectFactory rsFactory = new ObjectFactory();
    
    public ObjectLibrary createObjectLibrary() {
        ObjectLibrary lib = new ObjectLibrary(); 
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
        return lib;
    }

    private void addObjToLib(String id, ObjectLibrary objectLibrary) {
        ObjectRefType objRef = rsFactory.createObjectRefType();
        objRef.setId(id);
        objectLibrary.put(id, objRef);
    }
    
    @Override
    public Classification21 createClassification(ObjectLibrary objectLibrary) {
        return Classification21.create(objectLibrary);
    }

    @Override
    public ExtrinsicObject21 createExtrinsic(String id, ObjectLibrary objectLibrary) {
        ExtrinsicObject21 obj = ExtrinsicObject21.create(objectLibrary, id);
        objectLibrary.put(id, obj.getInternal());
        return obj;
    }

    @Override
    public RegistryPackage21 createRegistryPackage(String id, ObjectLibrary objectLibrary) {
        RegistryPackage21 obj = RegistryPackage21.create(objectLibrary, id);
        objectLibrary.put(id, obj.getInternal());
        return obj;        
    }

    @Override
    public EbXMLAssociation21 createAssociation(ObjectLibrary objectLibrary) {
        return EbXMLAssociation21.create(objectLibrary);
    }

    @Override
    public SubmitObjectsRequest21 createSubmitObjectsRequest() {
        return SubmitObjectsRequest21.create(createObjectLibrary());
    }

    @Override    
    public ProvideAndRegisterDocumentSetRequest21 createProvideAndRegisterDocumentSetRequest(ObjectLibrary objectLibrary) {
        return ProvideAndRegisterDocumentSetRequest21.create(objectLibrary);
    }

    @Override
    public RegistryResponse21 createRegistryResponse() {
        return RegistryResponse21.create();
    }

    @Override
    public RetrieveDocumentSetRequest createRetrieveDocumentSetRequest() {
        throw new UnsupportedOperationException("Only supported for ebXML 3.0");
    }

    @Override
    public RetrieveDocumentSetResponse createRetrieveDocumentSetResponse() {
        throw new UnsupportedOperationException("Only supported for ebXML 3.0");
    }
    
    @Override
    public AdhocQueryRequest21 createAdhocQueryRequest() {
        return AdhocQueryRequest21.create();
    }

    @Override
    public EbXMLQueryResponse21 createAdhocQueryResponse(ObjectLibrary objectLibrary) {
        return EbXMLQueryResponse21.create(objectLibrary);
    }
}
