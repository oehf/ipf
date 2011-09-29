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
import java.util.List;


import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * Encapsulation of {@link SubmitObjectsRequest}
 * @author Jens Riemschneider
 */
public class EbXMLSubmitObjectsRequest21 extends EbXMLObjectContainer21 implements EbXMLSubmitObjectsRequest {
    private final SubmitObjectsRequest submitObjectsRequest;
    
    /**
     * Constructs a request by wrapping the given ebXML 2.1 object.
     * @param submitObjectsRequest
     *          the object to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLSubmitObjectsRequest21(SubmitObjectsRequest submitObjectsRequest, EbXMLObjectLibrary objectLibrary) {
        super(objectLibrary);        
        notNull(submitObjectsRequest, "submitObjectsRequest cannot be null");        
        this.submitObjectsRequest = submitObjectsRequest;
    }

    /**
     * Constructs a request by wrapping the given ebXML 2.1 object using a new object library.
     * <p>
     * The object library is created from the objects contained in the request data.
     * @param submitObjectsRequest
     *          the object to wrap.
     */
    public EbXMLSubmitObjectsRequest21(SubmitObjectsRequest submitObjectsRequest) {
        this(submitObjectsRequest, new EbXMLObjectLibrary());        
        fillObjectLibrary();        
    }

    @Override
    public SubmitObjectsRequest getInternal() {
        return submitObjectsRequest;
    }

    @Override
    protected List<Object> getContents() {
        LeafRegistryObjectListType list = submitObjectsRequest.getLeafRegistryObjectList();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getObjectRefOrAssociationOrAuditableEvent();
    }
}
