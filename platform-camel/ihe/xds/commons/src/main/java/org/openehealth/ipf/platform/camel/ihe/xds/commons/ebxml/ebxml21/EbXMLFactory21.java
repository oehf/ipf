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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectRefType;

/**
 * Factory for EbXML 2.1 objects.
 * @author Jens Riemschneider
 */
public class EbXMLFactory21 implements EbXMLFactory {
    private final ObjectLibrary objectLibrary = new ObjectLibrary();
    
    public EbXMLFactory21() {
        addObjToLib(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        addObjToLib(Vocabulary.DOC_ENTRY_CLASS_CODE_CLASS_SCHEME);
        addObjToLib(Vocabulary.DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME);
        addObjToLib(Vocabulary.DOC_ENTRY_EVENT_CODE_CLASS_SCHEME);
        addObjToLib(Vocabulary.DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME);
        addObjToLib(Vocabulary.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        addObjToLib(Vocabulary.DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME);
        addObjToLib(Vocabulary.DOC_ENTRY_TYPE_CODE_CLASS_SCHEME);
        addObjToLib(Vocabulary.DOC_ENTRY_PATIENT_ID_EXTERNAL_ID);
        addObjToLib(Vocabulary.DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID); 
        
        addObjToLib(Vocabulary.FOLDER_CODE_LIST_CLASS_SCHEME);         
        addObjToLib(Vocabulary.FOLDER_PATIENT_ID_EXTERNAL_ID);         
        addObjToLib(Vocabulary.FOLDER_UNIQUE_ID_EXTERNAL_ID);
        
        addObjToLib(Vocabulary.SUBMISSION_SET_AUTHOR_CLASS_SCHEME);         
        addObjToLib(Vocabulary.SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME);         
        addObjToLib(Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID);                 
        addObjToLib(Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID);                 
        addObjToLib(Vocabulary.SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID);                 
    }

    private void addObjToLib(String id) {
        ObjectRefType objRef = new ObjectRefType();
        objRef.setId(id);
        objectLibrary.put(id, objRef);
    }
    
    @Override
    public Classification createClassification() {
        return Classification21.create(objectLibrary);
    }

    @Override
    public ExtrinsicObject createExtrinsic(String id) {
        ExtrinsicObject21 obj = ExtrinsicObject21.create(objectLibrary, id);
        objectLibrary.put(id, obj.getInternal());
        return obj;
    }

    @Override
    public RegistryPackage createRegistryPackage(String id) {
        RegistryPackage21 obj = RegistryPackage21.create(objectLibrary, id);
        objectLibrary.put(id, obj.getInternal());
        return obj;        
    }

    @Override
    public EbXMLAssociation createAssociation() {
        return EbXMLAssociation21.create(objectLibrary);
    }

    public ObjectLibrary getObjectLibrary() {
        return objectLibrary;
    }
}
