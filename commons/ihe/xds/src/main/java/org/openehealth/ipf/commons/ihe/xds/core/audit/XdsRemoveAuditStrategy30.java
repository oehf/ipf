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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType.DocumentRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectRefType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import java.util.List;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based retrieval-related XDS transactions.
 *
 * @author Boris Stanojevic
 */
abstract public class XdsRemoveAuditStrategy30 extends XdsAuditStrategy<XdsRemoveAuditDataset> {

    public XdsRemoveAuditStrategy30(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }


    @Override
    public void enrichDatasetFromRequest(Object pojo, XdsRemoveAuditDataset auditDataset) {
        RemoveObjectsRequest request = (RemoveObjectsRequest) pojo;

        List<ObjectRefType> references = request.getObjectRefList().getObjectRef();
        if (references != null) {
            final int SIZE = references.size();

            String[] documentUniqueIds = new String[SIZE];
            String[] homeCommunityIds = new String[SIZE];

            for (int i = 0; i < SIZE; ++i) {
                ObjectRefType reference = references.get(i);
                documentUniqueIds[i] = reference.getId();
                homeCommunityIds[i] = reference.getHome();
            }

            auditDataset.setObjectIds(documentUniqueIds);
            auditDataset.setHomeCommunityIds(homeCommunityIds);
        }
    }


    @Override
    public XdsRemoveAuditDataset createAuditDataset() {
        return new XdsRemoveAuditDataset(isServerSide());
    }


    @Override
    public RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RegistryResponseType response = (RegistryResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response);
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }
}
