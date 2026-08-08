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
package org.openehealth.ipf.commons.ihe.fhir.audit.events;

import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;

/**
 * Common interface for AuditEvents that initialize themselves given a populated AuditMessage, i.e. that
 * convert the ATNA audit record of a transaction into their own profiled shape instead of leaving it to
 * the generic translation.
 * <p>
 * How much of that is generic is a matter of the FHIR version and of the profile, so it is not decided
 * here: the FHIR-version-agnostic part of IPF cannot reference AuditEvent at all. The R4 implementations
 * inherit theirs from the BALP pattern their profile derives from, in
 * {@code org.openehealth.ipf.commons.ihe.fhir.support.audit.model}.
 * <p>
 * An implementation is instantiated per audit message by the FHIR serialization strategy, through a
 * public no-arg constructor, and is not reused. What the constructor may set is what the profile fixes
 * for every record of the transaction; everything derived from the concrete transaction belongs in
 * {@link #initialize(AuditMessage)}.
 *
 * @author Christian Ohr
 * @since 5.3
 */
public interface SelfInitializing {

    /**
     * Populates this AuditEvent from the given audit message. Called instead of the generic
     * AuditMessage-to-AuditEvent translation, so everything the resulting resource is to carry has to be
     * set here -- what the constructor fixes is only what the profile fixes.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    void initialize(AuditMessage auditMessage);

    /**
     * Whether this AuditEvent can represent the given audit message conformantly. What it cannot
     * represent must not be rendered as this profile at all -- a record claiming a profile it does not
     * satisfy is worse than one translated generically -- so returning false here drops the message to
     * the generic AuditMessage-to-AuditEvent translation.
     * <p>
     * By default, only the outcome is examined. Most IHE transaction profiles build on a BALP pattern
     * that fixes the outcome to success, so a failed transaction is not one of them; profiles that
     * constrain the outcome without fixing it -- MHD's Provide Document Bundle, for instance -- override
     * this. A required element the audit message does not carry is not a reason to refuse it: a
     * mandatory patient that the transaction cannot determine, say, is recorded as explicitly absent
     * instead, which keeps the record on the profile its transaction prescribes.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @return whether this AuditEvent may be used for it
     */
    default boolean supports(AuditMessage auditMessage) {
        return EventOutcomeIndicator.Success == auditMessage.getEventIdentification().getEventOutcomeIndicator();
    }

}
