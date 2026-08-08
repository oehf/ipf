/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.support.audit.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.SelfInitializing;

import java.util.Optional;

import static org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpConstants.BALP_QUERY_AUDIT_PROFILE;

/**
 * What a query transaction is audited as when it identifies no patient.
 * <p>
 * The transaction profiles of MHD, PIXm and PDQm all derive from the BALP PatientQuery pattern, whose
 * patient entity is mandatory -- they exist for queries about a patient. A query that names none, a
 * Find Document References by author say, cannot be rendered as one of them: a record claiming a profile
 * it does not satisfy is worse than one claiming a weaker profile it does. It is rendered as the plain
 * {@link QueryAuditEvent} pattern instead, which is the same record minus the patient entity, and keeps
 * the subtype naming the IHE transaction so the record is still recognizable as one.
 * <p>
 * Which profile a record ends up claiming is therefore not fixed per transaction but decided per audit
 * message, by {@link SelfInitializingQueryAuditEvent#supports(AuditMessage)}.
 *
 * @author Christian Ohr
 * @since 5.3
 */
@ResourceDef(name = "AuditEvent", id = "BalpQueryAuditEvent", profile = BALP_QUERY_AUDIT_PROFILE)
public class BalpQueryAuditEvent extends QueryAuditEvent implements SelfInitializing {

    /**
     * Fills in everything the BALP query pattern requires and the audit message provides. Unlike the
     * transaction-specific AuditEvents, this one is not built per transaction, so it takes the subtype
     * naming the transaction and the end that wrote the record from the audit message.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    @Override
    public void initialize(AuditMessage auditMessage) {
        transactionOf(auditMessage).ifPresent(this::addTransactionSubtype);
        initializeFrom(auditMessage, localRoleOf(auditMessage));
    }

    /**
     * @param auditMessage the audit message of the transaction being audited
     * @return the IHE transaction it is about, if it names one
     */
    private static Optional<FhirEventTypeCode> transactionOf(AuditMessage auditMessage) {
        return auditMessage.getEventIdentification().getEventTypeCode().stream()
            .filter(FhirEventTypeCode.class::isInstance)
            .map(FhirEventTypeCode.class::cast)
            .findFirst();
    }

    /**
     * @param auditMessage the audit message of the transaction being audited
     * @return which end of the transaction wrote it
     */
    private static ActiveParticipantRoleIdCode localRoleOf(AuditMessage auditMessage) {
        return auditMessage.isServerSide() ?
            ActiveParticipantRoleIdCode.Destination :
            ActiveParticipantRoleIdCode.Source;
    }

}
