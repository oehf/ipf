/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Reference;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;

import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Destination;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Source;

@ResourceDef(
    name = "AuditEvent",
    id = "FindDocumentListsConsumerAuditEvent",
    profile = MhdProfile.FIND_DOCUMENT_LISTS_CONSUMER_AUDIT_PROFILE)
public class FindDocumentListsConsumerAuditEvent extends FindDocumentListsAuditEvent {

    @Override
    public void setLocalAgent(AuditMessage auditMessage) {
        var ap = activeParticipantType(auditMessage, Source);
        setClient(
            new Reference().setDisplay(ap.getUserID()),
            ap.getNetworkAccessPointID(),
            AuditEventAgentNetworkType.fromCode(
                String.valueOf(ap.getNetworkAccessPointTypeCode().getValue()))
        );
        setSource(new AuditEventSourceComponent()
            .setObserver(new Reference().setDisplay(ap.getUserID())));
    }

    @Override
    public void setRemoteAgent(AuditMessage auditMessage) {
        var ap = activeParticipantType(auditMessage, Destination);
        setServer(
            new Reference().setDisplay(ap.getUserID()),
            ap.getNetworkAccessPointID(),
            AuditEventAgentNetworkType.fromCode(String.valueOf(
                ap.getNetworkAccessPointTypeCode().getValue()))
        );
    }

}
