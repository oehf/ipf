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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.AdhocQueryRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.ResponseOptionType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.AdhocQueryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;

/**
 * Encapsulation of {@link org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class AdhocQueryRequest30 implements AdhocQueryRequest {
    private final static ObjectFactory queryFactory = new ObjectFactory();
    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory rimFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory();

    private final org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryRequest request;
    
    private AdhocQueryRequest30(org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryRequest request) {
        notNull(request, "request cannot be null");
        this.request = request;
    }
    
    static AdhocQueryRequest30 create() {
        org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryRequest request = 
            queryFactory.createAdhocQueryRequest();
        
        AdhocQueryType query = rimFactory.createAdhocQueryType();                        
        ResponseOptionType responseOption = queryFactory.createResponseOptionType();
        responseOption.setReturnComposedObjects(true);
        
        request.setResponseOption(responseOption);
        request.setAdhocQuery(query);
        
        return new AdhocQueryRequest30(request);        
    }
    
    static AdhocQueryRequest30 create(org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryRequest request) {
        return new AdhocQueryRequest30(request);
    }

    @Override
    public String getSql() {
        return null;    // not supported in 3.0
    }

    @Override
    public void setSql(String sql) {
        // not supported in 3.0
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
        return request.getAdhocQuery().getId();
    }
    
    @Override
    public void setId(String id) {
        request.getAdhocQuery().setId(id);
    }

    @Override
    public void addSlot(String slotName, String... slotValues) {
        if (slotValues == null || slotValues.length == 0) {
            return;
        }

        Slot30 slot30 = Slot30.create(slotName, slotValues);
        if (!slot30.getValueList().isEmpty()) {
            List<SlotType1> slots = request.getAdhocQuery().getSlot();
            slots.add(slot30.getInternal());
        }
    }

    @Override
    public List<String> getSlotValues(String slotName) {
        notNull(slotName, "slotName cannot be null");
        List<SlotType1> slots30 = request.getAdhocQuery().getSlot();
        for (SlotType1 slot30 : slots30) {
            if (slotName.equals(slot30.getName())) {
                return slot30.getValueList().getValue();
            }
        }
        
        return Collections.emptyList();
    }
    
    @Override
    public String getSingleSlotValue(String slotName) {
        List<String> slotValues = getSlotValues(slotName);
        return slotValues.size() > 0 ? slotValues.get(0) : null;
    }
    
    @Override
    public List<Slot> getSlots() {
        List<SlotType1> slots30 = request.getAdhocQuery().getSlot();
        List<Slot> slots = new ArrayList<Slot>(slots30.size());
        for (SlotType1 slot30 : slots30) {
            slots.add(Slot30.create(slot30));
        }
        return slots;
    }

    @Override
    public List<Slot> getSlots(String slotName) {
        notNull(slotName, "slotName cannot be null");
        
        List<SlotType1> slots30 = request.getAdhocQuery().getSlot();
        List<Slot> slots = new ArrayList<Slot>(slots30.size());
        for (SlotType1 slot30 : slots30) {
            if (slotName.equals(slot30.getName())) {
                slots.add(Slot30.create(slot30));
            }
        }
        return slots;
    }

    @Override
    public String getHome() {
        return request.getAdhocQuery().getHome();
    }

    @Override
    public void setHome(String homeCommunityID) {
        request.getAdhocQuery().setHome(homeCommunityID);
    }
}
