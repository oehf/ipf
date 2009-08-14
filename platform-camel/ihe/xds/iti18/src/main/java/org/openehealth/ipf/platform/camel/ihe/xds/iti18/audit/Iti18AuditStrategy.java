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
package org.openehealth.ipf.platform.camel.ihe.xds.iti18.audit;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ItiAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.AdhocQueryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryResponseType;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Base audit strategy for ITI-18.
 * 
 * @author Dmytro Rud
 */
abstract public class Iti18AuditStrategy extends ItiAuditStrategy {

    public Iti18AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }

    @Override
    public void enrichDataset(Object pojo, ItiAuditDataset genericAuditDataset) {
        AdhocQueryRequest request = (AdhocQueryRequest) pojo;
        Iti18AuditDataset auditDataset = (Iti18AuditDataset) genericAuditDataset;

        AdhocQueryType adHocQuery = request.getAdhocQuery();
        if (adHocQuery != null) {
            auditDataset.setQueryUuid(adHocQuery.getId());
        }
    }

    @Override
    public boolean needSavePayload() {
        return true;
    }
    
    @Override
    public ItiAuditDataset createAuditDataset() {
        return new Iti18AuditDataset(isServerSide());
    }

    @Override
    public RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RegistryResponseType response = (RegistryResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response); 
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }
}
