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

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType.Document;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * Encapsulation of {@link ProvideAndRegisterDocumentSetRequestType}
 * @author Jens Riemschneider
 */
public class EbXMLProvideAndRegisterDocumentSetRequest21 extends EbXMLObjectContainer21 implements EbXMLProvideAndRegisterDocumentSetRequest {
    private final ProvideAndRegisterDocumentSetRequestType request;

    /**
     * Constructs a request by wrapping the given ebXML 2.1 object.
     * @param request
     *          the request to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLProvideAndRegisterDocumentSetRequest21(ProvideAndRegisterDocumentSetRequestType request, EbXMLObjectLibrary objectLibrary) {
        super(objectLibrary);
        notNull(request, "request cannot be null");                
        this.request = request;
    }

    /**
     * Constructs a request by wrapping the given ebXML 2.1 object and creating a new object library.
     * <p>
     * The object library is filled with the objects contained in the request.
     * @param request
     *          the request to wrap.
     */
    public EbXMLProvideAndRegisterDocumentSetRequest21(ProvideAndRegisterDocumentSetRequestType request) {
        this(request, new EbXMLObjectLibrary());
        fillObjectLibrary();
    }

    @Override
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
    public void removeDocument(String id) {
        for (Document doc : request.getDocument()) {
            if (doc.getId().equals(id)) {
                request.getDocument().remove(doc);
                return;
            }
        }        
    }
    
    @Override
    public Map<String, DataHandler> getDocuments() {
        Map<String, DataHandler> map = new HashMap<String, DataHandler>();
        for (Document document : request.getDocument()) {
            map.put(document.getId(), document.getValue());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    protected List<Object> getContents() {
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
