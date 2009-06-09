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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti41.audit;

import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.cxf.audit.AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.cxf.audit.AuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xdsb.iti41.service.ProvideAndRegisterDocumentSetRequestType;


/**
 * Audit strategy for ITI-41.
 * 
 * @author Dmytro Rud
 */
abstract public class Iti41AuditStrategy implements AuditStrategy {

    @Override
    public void enrichDataset(
            Object pojo, 
            AuditDataset genericAuditDataset) 
    {
        ProvideAndRegisterDocumentSetRequestType request = (ProvideAndRegisterDocumentSetRequestType)pojo;
        Iti41AuditDataset auditDataset = (Iti41AuditDataset)genericAuditDataset;
        
        SubmitObjectsRequest submissionSet = request.getSubmitObjectsRequest();
        if(submissionSet != null) {
            auditDataset.setSubmissionSetUuid(submissionSet.getId());
        }
    }

    
    @Override
    public boolean needSavePayload() {
        return false;
    }

}
