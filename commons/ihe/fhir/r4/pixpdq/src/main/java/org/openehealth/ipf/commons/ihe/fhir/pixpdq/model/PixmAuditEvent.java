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

import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.PatientQueryAuditEvent;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode.MobilePatientIdentifierCrossReferenceQuery;

/**
 * The AuditEvent of the Mobile Patient Identifier Cross-reference Query [ITI-83] transaction, which
 * PIXm profiles on the BALP PatientQuery pattern: both the query and the patient entity are required,
 * and the audit record identifies the patient the source identifier resolved from.
 * <p>
 * Everything derivable from the audit message is done by the base class; all this adds is the IHE
 * transaction subtype its profile fixes. Which of the two profiles the record claims is decided by the
 * concrete subclass.
 *
 * @author Christian Ohr
 * @since 5.3
 * @see PixmConsumerAuditEvent
 * @see PixmManagerAuditEvent
 */
abstract class PixmAuditEvent extends PatientQueryAuditEvent {

    protected PixmAuditEvent() {
        super();
        addTransactionSubtype(MobilePatientIdentifierCrossReferenceQuery);
    }
}
