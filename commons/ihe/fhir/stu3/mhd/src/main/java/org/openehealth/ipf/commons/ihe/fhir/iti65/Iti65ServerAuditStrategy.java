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

package org.openehealth.ipf.commons.ihe.fhir.iti65;

import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEDataImportBuilder;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirEventTypeCode;

/**
 * @author Christian Ohr
 */
public class Iti65ServerAuditStrategy extends Iti65AuditStrategy {

    public Iti65ServerAuditStrategy() {
        super(true);
    }

    @Override
    public AuditMessage[] makeAuditMessage(Iti65AuditDataset auditDataset) {
        return new IHEDataImportBuilder<>(auditDataset, FhirEventTypeCode.ProvideDocumentBundle)
                .setPatient(auditDataset.getPatientId())
                .addImportedEntity(
                        auditDataset.getDocumentManifestUuid(),
                        ParticipantObjectIdTypeCode.XdsMetadata,
                        ParticipantObjectTypeCodeRole.Job,
                        null)
                .getMessages();
    }
}
