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

import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.PatientReadAuditEvent;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode.MobileDocumentRetrieval;

/**
 * The AuditEvent of the Retrieve Document [ITI-68] transaction, which MHD profiles on the BALP
 * PatientRead pattern, making the patient entity mandatory.
 * <p>
 * The transaction itself names no patient: it downloads a binary from the URL that a preceding
 * DocumentReference query handed out, and its audit dataset knows only the document, repository and
 * home community. So unless the deployment enriches the audit dataset with a patient of its own -- it
 * is the responder, after all, that resolves the URL to a document -- the record cannot satisfy the
 * mandatory slice, and the serialization strategy steps it down to the plain BALP read pattern. That
 * is the intended outcome, not a defect: a conformant weaker record beats a non-conformant stronger one.
 * <p>
 * Everything derivable from the audit message is done by the base class; all this adds is the IHE
 * transaction subtype its profile fixes. Which of the two profiles the record claims is decided by the
 * concrete subclass.
 *
 * @author Christian Ohr
 * @since 5.3
 * @see RetrieveDocumentConsumerAuditEvent
 * @see RetrieveDocumentResponderAuditEvent
 */
abstract class RetrieveDocumentAuditEvent extends PatientReadAuditEvent {

    protected RetrieveDocumentAuditEvent() {
        super();
        addTransactionSubtype(MobileDocumentRetrieval);
    }
}
