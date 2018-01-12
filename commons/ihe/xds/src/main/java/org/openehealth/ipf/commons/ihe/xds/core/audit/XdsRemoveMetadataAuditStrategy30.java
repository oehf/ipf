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
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectRefType;

import java.util.List;
import java.util.Map;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based transactions related to XDS Metadata removal.
 *
 * @author Boris Stanojevic
 */
public abstract class XdsRemoveMetadataAuditStrategy30 extends XdsAuditStrategy<XdsRemoveMetadataAuditDataset> {

    public XdsRemoveMetadataAuditStrategy30(boolean serverSide) {
        super(serverSide);
    }


    @Override
    public XdsRemoveMetadataAuditDataset enrichAuditDatasetFromRequest(XdsRemoveMetadataAuditDataset auditDataset, Object pojo, Map<String, Object> parameters) {
        RemoveObjectsRequest request = (RemoveObjectsRequest) pojo;

        List<ObjectRefType> references = request.getObjectRefList().getObjectRef();
        if (references != null) {
            int size = references.size();
            auditDataset.setObjectIds(new String[size]);
            for (int i = 0; i < size; ++i) {
                ObjectRefType reference = references.get(i);
                auditDataset.getObjectIds()[i] = reference.getId();
            }
        }
        return auditDataset;
    }

    @Override
    public XdsRemoveMetadataAuditDataset createAuditDataset() {
        return new XdsRemoveMetadataAuditDataset(isServerSide());
    }

}
