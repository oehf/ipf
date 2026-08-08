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

package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.SelfInitializingQueryAuditEvent;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode.MobileDocumentManifestQuery;

/**
 * The AuditEvent of the Find Document Lists [ITI-66] transaction, which MHD profiles on the BALP
 * PatientQuery pattern.
 * <p>
 * Everything derivable from the audit message is done by the base class; all this adds is the IHE
 * transaction subtype its profile fixes. Which of the two profiles the record claims is decided by the
 * concrete subclass.
 *
 * @author Christian Ohr
 * @since 5.3
 * @see FindDocumentListsConsumerAuditEvent
 * @see FindDocumentListsResponderAuditEvent
 */
abstract class FindDocumentListsAuditEvent extends SelfInitializingQueryAuditEvent {

    protected FindDocumentListsAuditEvent() {
        super();
        addTransactionSubtype(MobileDocumentManifestQuery);
    }
}
