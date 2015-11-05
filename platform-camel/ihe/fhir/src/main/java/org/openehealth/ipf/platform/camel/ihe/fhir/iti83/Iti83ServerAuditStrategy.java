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
package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

public class Iti83ServerAuditStrategy extends Iti83AuditStrategy {

    protected Iti83ServerAuditStrategy() {
        super(true);
    }

    @Override
    public void doAudit(RFC3881EventOutcomeCodes eventOutcome,
            Iti83AuditDataset auditDataset) {

        throw new UnsupportedOperationException("not yet implemented");

        /*
        // FIXME wrong!!
        AuditorManager.getFhirAuditor().auditIti83(
                true,
                auditDataset.getEventOutcomeCode(),
                auditDataset.getUserName(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getClientIpAddress(),
                auditDataset.getRequestPayload(),
                auditDataset.getPatientIds(),
                auditDataset.getPurposesOfUse());
                */
    }

}
