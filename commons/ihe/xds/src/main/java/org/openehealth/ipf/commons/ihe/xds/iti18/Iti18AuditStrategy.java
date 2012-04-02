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
package org.openehealth.ipf.commons.ihe.xds.iti18;

import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLAdhocQueryRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.AdhocQueryType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Base audit strategy for ITI-18.
 * @author Dmytro Rud
 */
abstract public class Iti18AuditStrategy extends XdsAuditStrategy<Iti18AuditDataset> {

    /**
     * Constructs the audit strategy.
     * @param serverSide
     *      whether this is a server-side or a client-side strategy.
     * @param allowIncompleteAudit
     *      whether this strategy should allow incomplete audit records
     *      (parameter initially configurable via endpoint URL).
     */
    public Iti18AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }

    @Override
    public void enrichDatasetFromRequest(Object pojo, Iti18AuditDataset auditDataset) {
        AdhocQueryRequest request = (AdhocQueryRequest) pojo;
        AdhocQueryType adHocQuery = request.getAdhocQuery();
        if (adHocQuery != null) {
            auditDataset.setQueryUuid(adHocQuery.getId());
            auditDataset.setHomeCommunityId(adHocQuery.getHome());
        }
        QuerySlotHelper slotHelper = new QuerySlotHelper(new EbXMLAdhocQueryRequest30(request));
        auditDataset.setPatientId(slotHelper.toString(QueryParameter.DOC_ENTRY_PATIENT_ID));
    }

    @Override
    public Iti18AuditDataset createAuditDataset() {
        return new Iti18AuditDataset(isServerSide());
    }

    @Override
    public RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RegistryResponseType response = (RegistryResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response); 
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }
}
