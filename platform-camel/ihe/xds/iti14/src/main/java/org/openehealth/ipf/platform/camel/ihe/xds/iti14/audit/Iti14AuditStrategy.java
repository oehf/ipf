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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14.audit;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ItiAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLSubmitObjectsRequest21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * Audit strategy for ITI-14.
 * 
 * @author Dmytro Rud
 */
abstract public class Iti14AuditStrategy extends ItiAuditStrategy {

    public Iti14AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }

    @Override
    public void enrichDataset(Object pojo, ItiAuditDataset genericAuditDataset) {
        SubmitObjectsRequest request = (SubmitObjectsRequest) pojo;
        EbXMLSubmitObjectsRequest ebXML = new EbXMLSubmitObjectsRequest21(request);
        genericAuditDataset.enrichDatasetFromSubmitObjectsRequest(ebXML);
    }
}
