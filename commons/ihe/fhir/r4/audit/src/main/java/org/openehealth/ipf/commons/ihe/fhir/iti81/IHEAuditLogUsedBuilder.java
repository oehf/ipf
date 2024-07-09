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

package org.openehealth.ipf.commons.ihe.fhir.iti81;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.event.AuditLogUsedBuilder;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEAuditMessageBuilder;

/**
 * Builder for IHE-/FHIR-based AuditLogUsed events
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class IHEAuditLogUsedBuilder extends
        IHEAuditMessageBuilder<IHEAuditLogUsedBuilder, AuditLogUsedBuilder> {

    public IHEAuditLogUsedBuilder(AuditContext auditContext, FhirAuditEventQueryAuditDataset auditDataset) {
        super(auditContext, auditDataset, new AuditLogUsedBuilder(
                auditDataset.getEventOutcomeIndicator(),
                auditDataset.getEventOutcomeDescription()
        ));

        // First the source, then the destination
        if (auditDataset.isServerSide()) {
            setRemoteParticipant(auditDataset);
            addHumanRequestor(auditDataset);
            setLocalParticipant(auditDataset);
        } else {
            setLocalParticipant(auditDataset);
            addHumanRequestor(auditDataset);
            setRemoteParticipant(auditDataset);
        }

        addAuditEventUris(auditDataset);
    }

    public IHEAuditLogUsedBuilder addAuditEventUris(FhirAuditEventQueryAuditDataset auditDataset) {
        auditDataset.getAuditEventUris().forEach(delegate::addAuditLogIdentity);
        return this;
    }

}
