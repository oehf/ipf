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

import static java.util.Objects.requireNonNull;

import lombok.experimental.Delegate;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;

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
        this.request = requireNonNull(request, "request cannot be null");;
    }

    @Override
    public String getReturnType() {
        var responseOption = request.getResponseOption();
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

    /**
     * Implements the {@link org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlotList} interface
     * by delegating the calls to a "proper" slot list.
     */
    @Delegate
    private EbXMLSlotList30 getSlotList() {
        return new EbXMLSlotList30(request.getAdhocQuery().getSlot());
    }
}
