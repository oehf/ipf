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

package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.QueryAuditEvent;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode.MobilePatientDemographicsQuery;

/**
 * The AuditEvent of the Mobile Patient Demographics Query [ITI-78] transaction, which PDQm profiles on
 * the BALP Query pattern: the query entity is required, and the patient entity is used when the query
 * explicitly identifies one -- a search by demographics does not, and is recorded without it.
 * <p>
 * Everything derivable from the audit message is done by the base class; all this adds is the IHE
 * transaction subtype its profile fixes and the optional patient. Which of the two profiles the record
 * claims is decided by the concrete subclass.
 *
 * @author Christian Ohr
 * @since 5.3
 * @see PdqmConsumerAuditEvent
 * @see PdqmSupplierAuditEvent
 */
abstract class PdqmAuditEvent extends QueryAuditEvent {

    protected PdqmAuditEvent() {
        super();
        addTransactionSubtype(MobilePatientDemographicsQuery);
    }

    /**
     * The patient entity is optional in this profile, and used only when the query explicitly identifies
     * one, so nothing is recorded for it otherwise.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @param localRole    which end of the transaction recorded it
     */
    @Override
    protected void initializeFrom(AuditMessage auditMessage, ActiveParticipantRoleIdCode localRole) {
        super.initializeFrom(auditMessage, localRole);
        addPatientEntityIfPresent(auditMessage);
    }
}
