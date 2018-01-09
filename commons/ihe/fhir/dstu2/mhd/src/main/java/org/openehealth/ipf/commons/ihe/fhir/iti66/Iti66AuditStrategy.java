/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti66;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEQueryBuilder;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditStrategy;


/**
 * @author Christian Ohr
 * @since 3.2
 */
public class Iti66AuditStrategy extends FhirQueryAuditStrategy {

    public Iti66AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirQueryAuditDataset auditDataset) {
        return new IHEQueryBuilder<>(auditContext, auditDataset, FhirEventTypeCode.MobileDocumentManifestQuery)
                .addPatients(auditDataset.getPatientIds())
                .setQueryParameters("MobileDocumentManifestQuery",
                        FhirParticipantObjectIdTypeCode.MobileDocumentManifestQuery,
                        auditDataset.getQueryString())
                .getMessages();
    }

}
