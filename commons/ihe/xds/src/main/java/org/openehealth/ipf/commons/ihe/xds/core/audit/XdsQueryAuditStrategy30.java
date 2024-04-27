/*
 * Copyright 2012 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLAdhocQueryRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;

import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based query-related XDS transactions.
 *
 * @author Dmytro Rud
 */
public abstract class XdsQueryAuditStrategy30 extends XdsAuditStrategy<XdsQueryAuditDataset> {

    private static final EnumSet<QueryParameter> PATIENT_QUERY_PARAMS = EnumSet.of(QueryParameter.DOC_ENTRY_PATIENT_ID,
            QueryParameter.FOLDER_PATIENT_ID, QueryParameter.SUBMISSION_SET_PATIENT_ID, QueryParameter.PATIENT_ID);

    /**
     * Constructs an XDS query audit strategy.
     *
     * @param serverSide whether this is a server-side or a client-side strategy.
     */
    public XdsQueryAuditStrategy30(boolean serverSide) {
        super(serverSide);
    }


    @Override
    public XdsQueryAuditDataset enrichAuditDatasetFromRequest(XdsQueryAuditDataset auditDataset, Object pojo, Map<String, Object> parameters) {
        if (pojo instanceof AdhocQueryRequest request) {
            var adHocQuery = request.getAdhocQuery();
            if (adHocQuery != null) {
                auditDataset.setQueryUuid(adHocQuery.getId());
                auditDataset.setHomeCommunityId(adHocQuery.getHome());
            }

            var slotHelper = new QuerySlotHelper(new EbXMLAdhocQueryRequest30(request));

            PATIENT_QUERY_PARAMS.stream().map(slotHelper::toStringList).filter(Objects::nonNull).forEach(p -> auditDataset.getPatientIds().addAll(p));
        }
        return auditDataset;
    }

    @Override
    public XdsQueryAuditDataset createAuditDataset() {
        return new XdsQueryAuditDataset(isServerSide());
    }


}
