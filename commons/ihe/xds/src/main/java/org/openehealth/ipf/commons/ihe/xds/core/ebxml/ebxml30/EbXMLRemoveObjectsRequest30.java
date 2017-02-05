/*
 * Copyright 2013 the original author or authors.
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

import lombok.experimental.Delegate;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectRefListType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectRefType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Encapsulation of {@link org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest}
 * @author Boris Stanojevic
 */
public class EbXMLRemoveObjectsRequest30 implements EbXMLRemoveObjectsRequest {
    private final RemoveObjectsRequest removeObjectsRequest;

    /**
     * Constructs a request by wrapping the given ebXML 3.0 object.
     * @param removeObjectsRequest
     *          the object to wrap.
     */
    public EbXMLRemoveObjectsRequest30(RemoveObjectsRequest removeObjectsRequest) {
        notNull(removeObjectsRequest, "removeObjectsRequest cannot be null");
        this.removeObjectsRequest = removeObjectsRequest;
    }

    @Override
    public void setReferences(List<ObjectReference> references) {
        ObjectRefListType list = removeObjectsRequest.getObjectRefList();
        if (list != null) {
            list.getObjectRef().clear();
        } else {
            removeObjectsRequest.setObjectRefList(new ObjectRefListType());
        }
        if (references != null) {
            for (ObjectReference reference : references) {
                ObjectRefType referenceType = new ObjectRefType();
                referenceType.setHome(reference.getHome());
                referenceType.setId(reference.getId());
                removeObjectsRequest.getObjectRefList().getObjectRef().add(referenceType);
            }
        }
    }

    @Override
    public List<ObjectReference> getReferences() {
        ObjectRefListType list = removeObjectsRequest.getObjectRefList();
        if (list == null) {
            return Collections.emptyList();
        }
        List<ObjectReference> objectReferenceList = new ArrayList<>();
        for (ObjectRefType objRefType: list.getObjectRef()){
            ObjectReference ref = new ObjectReference(objRefType.getId(), objRefType.getHome());
            objectReferenceList.add(ref);
        }
        return objectReferenceList;
    }

    @Override
    public String getDeletionScope() {
        return removeObjectsRequest.getDeletionScope();
    }

    @Override
    public void setDeletionScope(String deletionScope) {
        removeObjectsRequest.setDeletionScope(deletionScope);
    }

    /**
     * Implements the {@link org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlotList} interface
     * by delegating the calls to a "proper" slot list.
     */
    @Delegate
    private EbXMLSlotList30 getSlotList() {
        return new EbXMLSlotList30(removeObjectsRequest.getAdhocQuery().getSlot());
    }

    @Override
    public void setSql(String sql) {
        //noop
    }

    @Override
    public String getSql() {
        return null;
    }

    @Override
    public void setReturnType(String returnType) {
        //noop
    }

    @Override
    public String getReturnType() {
        return null;
    }

    @Override
    public void setId(String id) {
        removeObjectsRequest.getAdhocQuery().setId(id);
    }

    @Override
    public String getId() {
        return removeObjectsRequest.getAdhocQuery().getId();
    }

    @Override
    public void setHome(String homeCommunityID) {
        removeObjectsRequest.getAdhocQuery().setHome(homeCommunityID);
    }

    @Override
    public String getHome() {
        return removeObjectsRequest.getAdhocQuery().getHome();
    }

    @Override
    public RemoveObjectsRequest getInternal() {
        return removeObjectsRequest;
    }
}
