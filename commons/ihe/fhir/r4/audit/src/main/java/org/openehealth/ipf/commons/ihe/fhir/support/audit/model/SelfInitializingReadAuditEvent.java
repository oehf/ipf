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

import org.hl7.fhir.r4.model.codesystems.ObjectRole;
import org.hl7.fhir.r4.model.codesystems.RestfulInteraction;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.SelfInitializing;

import java.util.Date;

/**
 * Base class for the AuditEvents of transactions that read a resource and are profiled on the BALP
 * PatientRead pattern, doing everything derivable from the audit message generically. A
 * transaction-specific subclass adds only what the transaction's own profile fixes on top of the
 * pattern -- in practice the IHE transaction subtype -- and each of its concrete subclasses declares
 * the profile it claims plus {@link #localRole()}, i.e. which end of the transaction the audit source is.
 * <p>
 * Note the agent codes: the BALP read patterns give the client 110152 and the server 110153, i.e. the
 * other way round than the query and create patterns do. Which participant is the client does not
 * change -- only the code its slice is fixed to.
 * <p>
 * The pattern fixes {@code outcome} to success, so an audit message reporting a failure is not rendered
 * with one of these at all: the serialization strategy keeps it on the generic translation, which is
 * free to report the failure.
 *
 * @author Christian Ohr
 * @since 5.3
 * @see SelfInitializingQueryAuditEvent
 * @see SelfInitializingCreateAuditEvent
 */
public abstract class SelfInitializingReadAuditEvent extends PatientReadAuditEvent implements SelfInitializing {

    /**
     * @return the role the audit source plays in the transaction, used to name an observer when the
     *      audit context does not.
     */
    protected abstract ActiveParticipantRoleIdCode localRole();

    /**
     * Fills in everything the BALP PatientRead pattern requires and the audit message provides: the time
     * the event was recorded, the read subtype, the agents, the audit source, the patient, and the
     * resource that was read.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    @Override
    public void initialize(AuditMessage auditMessage) {
        setRecorded(Date.from(auditMessage.getEventIdentification().getEventDateTime()));

        // the pattern requires a read subtype next to the transaction subtype the subclass adds
        addSubtype()
            .setCode(RestfulInteraction.READ.toCode())
            .setSystem(RestfulInteraction.READ.getSystem());

        setClientAndServer(auditMessage);
        addUserAgents(auditMessage);
        setAuditSource(auditMessage, localRole());
        BalpAuditEventHelper.patientReference(auditMessage).ifPresent(this::setPatient);
        BalpAuditEventHelper.requestId(auditMessage).ifPresent(this::addTransactionEntity);

        // the document that was retrieved
        auditMessage.findParticipantObjectIdentifications(
                poi -> ParticipantObjectTypeCodeRole.Report == poi.getParticipantObjectTypeCodeRole())
            .stream()
            .findFirst()
            .ifPresent(poi -> setData(BalpAuditEventHelper.reference(poi.getParticipantObjectID()), ObjectRole._3));
    }
}
