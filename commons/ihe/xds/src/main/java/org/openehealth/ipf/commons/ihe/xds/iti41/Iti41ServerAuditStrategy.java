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
package org.openehealth.ipf.commons.ihe.xds.iti41;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsDataImportBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;

/**
 * Server audit strategy for ITI-41.
 *
 * @author Dmytro Rud
 * @author Christian Ohr
 */
public class Iti41ServerAuditStrategy extends Iti41AuditStrategy {

    public Iti41ServerAuditStrategy() {
        super(true);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, XdsSubmitAuditDataset auditDataset) {
        return new XdsDataImportBuilder(auditContext, auditDataset, XdsEventTypeCode.ProvideAndRegisterDocumentSetB, auditDataset.getPurposesOfUse())
                .setPatient(auditDataset.getPatientId())
                .setSubmissionSet(auditDataset)
                .getMessages();
    }
}
