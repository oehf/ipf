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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.ResponseOptionType;

/**
 * Encapsulation of {@link org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class AdhocQueryRequest21 implements AdhocQueryRequest {
    private final static ObjectFactory queryFactory = new ObjectFactory();
    
    private final org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest request;
    
    private AdhocQueryRequest21(org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest request) {
        notNull(request, "request cannot be null");
        this.request = request;
    }
    
    static AdhocQueryRequest21 create() {
        org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest request = 
            queryFactory.createAdhocQueryRequest();
        
        ResponseOptionType responseOption = queryFactory.createResponseOptionType();
        responseOption.setReturnComposedObjects(true);
        
        request.setResponseOption(responseOption);
        return new AdhocQueryRequest21(request);
    }
    
    public static AdhocQueryRequest21 create(org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest request) {
        return new AdhocQueryRequest21(request);
    }

    @Override
    public String getSql() {
        return request.getSQLQuery();
    }

    @Override
    public void setSql(String sql) {
        request.setSQLQuery(sql);
    }
    
    @Override
    public String getReturnType() {
        ResponseOptionType responseOption = request.getResponseOption();
        return responseOption != null ? responseOption.getReturnType() : null;
    }
    
    @Override
    public void setReturnType(String returnType) {
        request.getResponseOption().setReturnType(returnType);
    }
    
    @Override
    public String getId() {
        return QueryType.SQL.getId();
    }
    
    @Override
    public void setId(String id) {
        // not supported in 2.1
    }

    @Override
    public void addSlot(String slotName, String... slotValues) {
        // not supported in 2.1
    }

    @Override
    public String getSingleSlotValue(String slotName) {
        // not supported in 2.1
        return null;
    }

    @Override
    public List<String> getSlotValues(String slotName) {
        // not supported in 2.1
        return Collections.emptyList();
    }

    @Override
    public List<Slot> getSlots() {
        // not supported in 2.1
        return Collections.emptyList();
    }

    @Override
    public List<Slot> getSlots(String slotName) {
        // not supported in 2.1
        return Collections.emptyList();
    }
    
    @Override
    public String getHome() {
        // not supported in 2.1
        return null;
    }

    @Override
    public void setHome(String homeCommunityID) {
        // not supported in 2.1
    }

    @Override
    public Object getInternal() {
        return request;
    }
}
