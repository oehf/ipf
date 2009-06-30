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
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryObjectListType;

/**
 * Encapsulation of {@link SubmitObjectsRequest}
 * @author Jens Riemschneider
 */
public class SubmitObjectsRequest30 extends BaseProvideDocumentSetRequest30 {
    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.ObjectFactory lcmFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.ObjectFactory();
    
    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory rimFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory();
    
    private final SubmitObjectsRequest submitObjectsRequest;

    private SubmitObjectsRequest30(SubmitObjectsRequest submitObjectsRequest) {
        notNull(submitObjectsRequest, "submitObjectsRequest cannot be null");
        this.submitObjectsRequest = submitObjectsRequest;
    }

    static SubmitObjectsRequest30 create() {
        SubmitObjectsRequest request = lcmFactory.createSubmitObjectsRequest();
        RegistryObjectListType list = request.getRegistryObjectList();
        if (list == null) {
            list = rimFactory.createRegistryObjectListType();
            request.setRegistryObjectList(list);
        }
        return new SubmitObjectsRequest30(request);
    }
    
    public static SubmitObjectsRequest30 create(SubmitObjectsRequest submitObjectsRequest) {
        return new SubmitObjectsRequest30(submitObjectsRequest);
    }

    @Override
    List<JAXBElement<? extends IdentifiableType>> getContents() {
        RegistryObjectListType list = submitObjectsRequest.getRegistryObjectList();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getIdentifiable();
    }
}
