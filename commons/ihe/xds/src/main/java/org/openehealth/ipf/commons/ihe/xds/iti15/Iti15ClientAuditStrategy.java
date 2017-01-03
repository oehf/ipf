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
package org.openehealth.ipf.commons.ihe.xds.iti15;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;

/**
 * Client audit strategy for ITI-15.
 * @author Dmytro Rud
 */
public class Iti15ClientAuditStrategy extends Iti15AuditStrategy {

    public Iti15ClientAuditStrategy() {
        super(false);
    }

    @Override
    public void doAudit(XdsSubmitAuditDataset auditDataset) {
        AuditorManager.getSourceAuditor().auditProvideAndRegisterDocumentSetEvent(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getUserName(),
                auditDataset.getSubmissionSetUuid(),
                auditDataset.getPatientId());
    }

}
