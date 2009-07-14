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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;

/**
 * Audit strategy for ITI-42.
 * 
 * @author Dmytro Rud
 */
abstract public class Iti42AuditStrategy extends AuditStrategy {

    public Iti42AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }

    @Override
    public void enrichDataset(Object pojo, AuditDataset auditDataset) {
        SubmitObjectsRequest submitObjectsRequest = (SubmitObjectsRequest) pojo;
        EbXMLSubmitObjectsRequest ebXML = new EbXMLSubmitObjectsRequest30(submitObjectsRequest);
        auditDataset.enrichDatasetFromSubmitObjectsRequest(ebXML);
    }
}
