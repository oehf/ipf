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

import static org.apache.commons.lang.Validate.notNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType.Document;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryPackageType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * Encapsulation of {@link ProvideAndRegisterDocumentSetRequestType}
 * @author Jens Riemschneider
 */
public class ProvideAndRegisterDocumentSetRequest21 extends BaseEbXMLObjectContainer21 implements ProvideAndRegisterDocumentSetRequest {
    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.ObjectFactory rsFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.ObjectFactory();

    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory rimFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory();
    
    private final ProvideAndRegisterDocumentSetRequestType request;

    private ProvideAndRegisterDocumentSetRequest21(ProvideAndRegisterDocumentSetRequestType request, ObjectLibrary objectLibrary) {
        super(objectLibrary);
        notNull(request, "request cannot be null");                
        this.request = request;
    }

    static ProvideAndRegisterDocumentSetRequest21 create(ObjectLibrary objectLibrary) {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitObjectsRequest = request.getSubmitObjectsRequest();
        if (submitObjectsRequest == null) {
            submitObjectsRequest = rsFactory.createSubmitObjectsRequest();
            request.setSubmitObjectsRequest(submitObjectsRequest);
        }
        
        LeafRegistryObjectListType list = submitObjectsRequest.getLeafRegistryObjectList();
        if (list == null) {
            list = rimFactory.createLeafRegistryObjectListType();
            submitObjectsRequest.setLeafRegistryObjectList(list);
        }
        return new ProvideAndRegisterDocumentSetRequest21(request, objectLibrary);
    }

    public static ProvideAndRegisterDocumentSetRequest21 create(ProvideAndRegisterDocumentSetRequestType request) {
        ObjectLibrary objectLibrary = new ObjectLibrary();
        ProvideAndRegisterDocumentSetRequest21 request21 = new ProvideAndRegisterDocumentSetRequest21(request, objectLibrary);
        
        for (Object obj : request21.getContents()) {
            ExtrinsicObjectType extrinsic = cast(obj, ExtrinsicObjectType.class);
            if (extrinsic != null) {
                objectLibrary.put(extrinsic.getId(), extrinsic);
            }
            ObjectRefType objectRef = cast(obj, ObjectRefType.class);
            if (objectRef != null) {
                objectLibrary.put(objectRef.getId(), objectRef);
            }
            RegistryPackageType regPackage = cast(obj, RegistryPackageType.class);
            if (regPackage != null) {
                objectLibrary.put(regPackage.getId(), regPackage);
            }
        }
        
        return request21;
    }

    public ProvideAndRegisterDocumentSetRequestType getInternal() {
        return request;
    }

    @Override
    public void addDocument(String id, DataHandler dataHandler) {
        if (dataHandler != null && id != null) {        
            List<Document> documents = request.getDocument();
            Document document = new Document();
            document.setId(id);
            document.setValue(dataHandler);
            documents.add(document);
        }
    }
    
    @Override
    public Map<String, DataHandler> getDocuments() {
        Map<String, DataHandler> map = new HashMap<String, DataHandler>();
        for (Document document : request.getDocument()) {
            map.put(document.getId(), document.getValue());
        }
        return map;
    }

    @Override
    List<Object> getContents() {
        SubmitObjectsRequest submitObjectsRequest = request.getSubmitObjectsRequest();
        if (submitObjectsRequest == null) {
            return Collections.emptyList();
        }
        LeafRegistryObjectListType list = submitObjectsRequest.getLeafRegistryObjectList();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getObjectRefOrAssociationOrAuditableEvent();
    }
}
