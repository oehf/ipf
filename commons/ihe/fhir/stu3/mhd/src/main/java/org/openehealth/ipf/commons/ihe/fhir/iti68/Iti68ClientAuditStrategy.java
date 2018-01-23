/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti68;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.event.PHIImportBuilder;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;

/**
 * @author Christian Ohr
 */
public class Iti68ClientAuditStrategy extends AuditStrategySupport<Iti68AuditDataset> {

    public Iti68ClientAuditStrategy() {
        super(false);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, Iti68AuditDataset auditDataset) {
        return new PHIImportBuilder(auditContext, auditDataset,
                EventActionCode.Create,
                FhirEventTypeCode.MobileDocumentRetrieval)
                .setPatient(auditDataset.getPatientId())
                .getMessages();
    }

    @Override
    public Iti68AuditDataset createAuditDataset() {
        return new Iti68AuditDataset(false);
    }
}
