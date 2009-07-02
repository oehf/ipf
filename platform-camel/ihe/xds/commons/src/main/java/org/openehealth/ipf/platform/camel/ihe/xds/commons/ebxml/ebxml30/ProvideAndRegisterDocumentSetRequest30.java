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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import static org.apache.commons.lang.Validate.notNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType.Document;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryObjectListType;

/**
 * Encapsulation of {@link ProvideAndRegisterDocumentSetRequestType}
 * @author Jens Riemschneider
 */
public class ProvideAndRegisterDocumentSetRequest30 extends BaseEbXMLObjectContainer30 implements ProvideAndRegisterDocumentSetRequest {
    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.ObjectFactory lcmFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.ObjectFactory();

    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory rimFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory();
    
    private final ProvideAndRegisterDocumentSetRequestType request;

    private ProvideAndRegisterDocumentSetRequest30(ProvideAndRegisterDocumentSetRequestType request) {
        notNull(request, "request cannot be null");                
        this.request = request;
    }

    static ProvideAndRegisterDocumentSetRequest30 create() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitObjectsRequest = request.getSubmitObjectsRequest();
        if (submitObjectsRequest == null) {
            submitObjectsRequest = lcmFactory.createSubmitObjectsRequest();
            request.setSubmitObjectsRequest(submitObjectsRequest);
        }
        
        RegistryObjectListType list = submitObjectsRequest.getRegistryObjectList();
        if (list == null) {
            list = rimFactory.createRegistryObjectListType();
            submitObjectsRequest.setRegistryObjectList(list);
        }
        return new ProvideAndRegisterDocumentSetRequest30(request);
    }

    public static ProvideAndRegisterDocumentSetRequest30 create(ProvideAndRegisterDocumentSetRequestType request) {
        return new ProvideAndRegisterDocumentSetRequest30(request);
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

    public ProvideAndRegisterDocumentSetRequestType getInternal() {
        return request;
    }

    @Override
    List<JAXBElement<? extends IdentifiableType>> getContents() {
        SubmitObjectsRequest submitObjectsRequest = request.getSubmitObjectsRequest();
        if (submitObjectsRequest == null) {
            return Collections.emptyList();
        }
        RegistryObjectListType list = submitObjectsRequest.getRegistryObjectList();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getIdentifiable();
    }
}
