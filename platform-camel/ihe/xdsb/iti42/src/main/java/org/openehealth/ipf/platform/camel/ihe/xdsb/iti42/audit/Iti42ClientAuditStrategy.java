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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti42.audit;

import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.cxf.audit.AuditDataset;
import org.openhealthtools.ihe.atna.auditor.XDSRepositoryAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * Client audit strategy for ITI-42.
 * 
 * @author Dmytro Rud
 */
public class Iti42ClientAuditStrategy extends Iti42AuditStrategy {

    @Override
    public void doAudit(
            RFC3881EventOutcomeCodes eventOutcome, 
            AuditDataset genericAuditDataset) 
    {
        Iti42AuditDataset auditDataset = (Iti42AuditDataset)genericAuditDataset;
        
        XDSRepositoryAuditor.getAuditor().auditRegisterDocumentSetBEvent(
                eventOutcome,
                auditDataset.getUserId(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getSubmissionSetUuid(),
                /* patientId */ null);
    }

    
    @Override
    public AuditDataset createAuditDataset() {
        return new Iti42AuditDataset(false);
    }
}
