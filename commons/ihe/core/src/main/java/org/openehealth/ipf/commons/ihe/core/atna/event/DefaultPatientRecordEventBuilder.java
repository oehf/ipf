/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.atna.event;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

public class DefaultPatientRecordEventBuilder extends PatientRecordEventBuilder<DefaultPatientRecordEventBuilder> {

    public DefaultPatientRecordEventBuilder(AuditContext auditContext, AuditDataset auditDataset, EventActionCode action, EventType eventType, PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, action, eventType, purposesOfUse);
    }

    public DefaultPatientRecordEventBuilder(AuditContext auditContext, AuditDataset auditDataset, EventOutcomeIndicator eventOutcomeIndicator, String eventOutcomeDescription, EventActionCode action, EventType eventType, PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventOutcomeIndicator, eventOutcomeDescription, action, eventType, purposesOfUse);
    }
}
