/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti80;

import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsDataImportBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsEventTypeCode;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;

/**
 * Server audit strategy for ITI-80.
 *
 * @author Remco Overdevest
 * @author Christian Ohr
 * @since 3.3
 */
public class Iti80ServerAuditStrategy extends Iti80AuditStrategy {

    public Iti80ServerAuditStrategy() {
        super(true);
    }

    @Override
    public AuditMessage[] makeAuditMessage(XdsSubmitAuditDataset auditDataset) {
        return new XdsDataImportBuilder(auditDataset, XdsEventTypeCode.CrossGatewayDocumentProvide, auditDataset.getPurposesOfUse())
                .setPatient(auditDataset.getPatientId())
                .setSubmissionSet(auditDataset)
                .getMessages();
    }

}
