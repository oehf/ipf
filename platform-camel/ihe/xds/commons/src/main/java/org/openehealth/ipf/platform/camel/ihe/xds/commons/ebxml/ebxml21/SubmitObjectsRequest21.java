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
import java.util.List;


import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;

/**
 * Encapsulation of {@link SubmitObjectsRequest}
 * @author Jens Riemschneider
 */
public class SubmitObjectsRequest21 extends BaseEbXMLObjectContainer21 implements org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.SubmitObjectsRequest {
    private static final org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.ObjectFactory rsFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.ObjectFactory();
    
    private static final org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory rimFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory();

    private final SubmitObjectsRequest submitObjectsRequest;
    
    private SubmitObjectsRequest21(SubmitObjectsRequest submitObjectsRequest, ObjectLibrary objectLibrary) {
        super(objectLibrary);
        
        notNull(submitObjectsRequest, "submitObjectsRequest cannot be null");
        
        this.submitObjectsRequest = submitObjectsRequest;
    }

    static SubmitObjectsRequest21 create(ObjectLibrary objectLibrary) {
        SubmitObjectsRequest request = rsFactory.createSubmitObjectsRequest();
        LeafRegistryObjectListType list = request.getLeafRegistryObjectList();
        if (list == null) {
            list = rimFactory.createLeafRegistryObjectListType();
            request.setLeafRegistryObjectList(list);
        }
        return new SubmitObjectsRequest21(request, objectLibrary);
    }

    public static SubmitObjectsRequest21 create(SubmitObjectsRequest submitObjectsRequest) {
        ObjectLibrary objectLibrary = new ObjectLibrary();
        SubmitObjectsRequest21 submitObjectsRequest21 = new SubmitObjectsRequest21(submitObjectsRequest, objectLibrary);        
        fillObjectLibrary(objectLibrary, submitObjectsRequest21.getContents());        
        return submitObjectsRequest21;
    }

    public SubmitObjectsRequest getInternal() {
        return submitObjectsRequest;
    }

    @Override
    List<Object> getContents() {
        LeafRegistryObjectListType list = submitObjectsRequest.getLeafRegistryObjectList();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getObjectRefOrAssociationOrAuditableEvent();
    }
}
