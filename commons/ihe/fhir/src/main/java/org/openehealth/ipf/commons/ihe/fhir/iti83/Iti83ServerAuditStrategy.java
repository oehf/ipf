/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.iti83;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditDataset;

/**
 * Strategy for auditing ITI-83 transactions on the server side
 *
 * @since 3.1
 */
public class Iti83ServerAuditStrategy extends Iti83AuditStrategy {

    public Iti83ServerAuditStrategy() {
        super(true);
    }

    @Override
    public void doAudit(FhirQueryAuditDataset auditDataset) {
        AuditorManager.getFhirAuditor().auditIti83(
                true,
                auditDataset.getEventOutcomeCode(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getClientIpAddress(),
                auditDataset.getQueryString());
    }

}
