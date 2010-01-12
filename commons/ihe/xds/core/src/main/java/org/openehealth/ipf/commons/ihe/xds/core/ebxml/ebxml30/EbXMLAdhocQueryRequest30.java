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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import static org.apache.commons.lang.Validate.notNull;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.model.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.model.stub.ebrs30.query.ResponseOptionType;

import java.util.List;

/**
 * Encapsulation of {@link AdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class EbXMLAdhocQueryRequest30 implements EbXMLAdhocQueryRequest {
    private final AdhocQueryRequest request;
    
    /**
     * Constructs the wrapper using the real object.
     * @param request
     *          the ebXML 3.0 object.
     */
    public EbXMLAdhocQueryRequest30(AdhocQueryRequest request) {
        notNull(request, "request cannot be null");
        this.request = request;
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
        getSlotList().addSlot(slotName, slotValues);
    }

    @Override
    public List<String> getSlotValues(String slotName) {
        return getSlotList().getSlotValues(slotName);
    }
    
    @Override
    public String getSingleSlotValue(String slotName) {
        return getSlotList().getSingleSlotValue(slotName);
    }
    
    @Override
    public List<EbXMLSlot> getSlots() {
        return getSlotList().getSlots();
    }

    @Override
    public List<EbXMLSlot> getSlots(String slotName) {
        return getSlotList().getSlots(slotName);
    }

    @Override
    public String getHome() {
        return request.getAdhocQuery().getHome();
    }

    @Override
    public void setHome(String homeCommunityID) {
        request.getAdhocQuery().setHome(homeCommunityID);
    }

    @Override
    public AdhocQueryRequest getInternal() {
        return request;
    }

    private EbXMLSlotList30 getSlotList() {
        return new EbXMLSlotList30(request.getAdhocQuery().getSlot());
    }
}
