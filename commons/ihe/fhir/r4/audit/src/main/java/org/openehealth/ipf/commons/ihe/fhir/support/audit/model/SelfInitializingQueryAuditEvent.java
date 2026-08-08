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

import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.SelfInitializing;

/**
 * Base class for the AuditEvents of the query transactions that are profiled in BALP, doing everything
 * that can be derived from the {@link AuditMessage} generically. A transaction-specific subclass adds
 * only what the transaction's own profile fixes on top of the BALP pattern -- in practice the IHE
 * transaction subtype -- and each of its concrete subclasses declares the profile it claims plus
 * {@link #localRole()}, i.e. which end of the transaction the audit source is.
 * <p>
 * Note that the client and the server agent are built the same way on both ends of a transaction:
 * which side records the event changes neither of them, only the profile the record claims. That is
 * why a concrete subclass contributes little beyond its {@code @ResourceDef}.
 * <p>
 * An audit message these cannot represent is not rendered as one of them. The BALP query patterns fix
 * {@code outcome} to success, so a failure stays on the generic translation; their patient entity is
 * mandatory, so a query naming no patient steps down to {@link BalpQueryAuditEvent}. See
 * {@link #supports(AuditMessage)}.
 *
 * @author Christian Ohr
 * @since 5.3
 * @see SelfInitializingCreateAuditEvent
 * @see SelfInitializingReadAuditEvent
 */
public abstract class SelfInitializingQueryAuditEvent extends PatientQueryAuditEvent implements SelfInitializing {

    /**
     * @return the role the audit source plays in the transaction: {@link ActiveParticipantRoleIdCode#Source}
     *      when the audit is written by the client of the transaction, {@link ActiveParticipantRoleIdCode#Destination}
     *      when it is written by the server. It is the agent the audit source observer has to name, see
     *      {@link #setAuditSource(AuditMessage, ActiveParticipantRoleIdCode)}.
     */
    protected abstract ActiveParticipantRoleIdCode localRole();

    /**
     * The patient entity is mandatory in this pattern, so an audit message that names no patient cannot
     * be rendered as one of these. The serialization strategy then falls back to
     * {@link BalpQueryAuditEvent}, the same pattern without the patient.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @return whether it names a patient and reports a success
     */
    @Override
    public boolean supports(AuditMessage auditMessage) {
        return SelfInitializing.super.supports(auditMessage)
            && BalpAuditEventHelper.patientReference(auditMessage).isPresent();
    }

    /**
     * Fills in everything the BALP query patterns require and the audit message provides: the time the
     * event was recorded, the search subtype, the agents, the audit source and the entities.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    @Override
    public void initialize(AuditMessage auditMessage) {
        initializeFrom(auditMessage, localRole());
        BalpAuditEventHelper.patientReference(auditMessage).ifPresent(this::setPatient);
    }
}
