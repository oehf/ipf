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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42.audit;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ItiAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryResponseType;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Audit strategy for ITI-42.
 * 
 * @author Dmytro Rud
 */
abstract public class Iti42AuditStrategy extends ItiAuditStrategy {

    public Iti42AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }

    @Override
    public void enrichDataset(Object pojo, ItiAuditDataset auditDataset) {
        SubmitObjectsRequest submitObjectsRequest = (SubmitObjectsRequest) pojo;
        EbXMLSubmitObjectsRequest ebXML = new EbXMLSubmitObjectsRequest30(submitObjectsRequest);
        auditDataset.enrichDatasetFromSubmitObjectsRequest(ebXML);
    }

    @Override
    public RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RegistryResponseType response = (RegistryResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response); 
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }
}
