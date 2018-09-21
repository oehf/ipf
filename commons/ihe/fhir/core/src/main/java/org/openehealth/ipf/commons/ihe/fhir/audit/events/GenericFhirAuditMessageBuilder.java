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

package org.openehealth.ipf.commons.ihe.fhir.audit.events;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.event.CustomAuditMessageBuilder;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEAuditMessageBuilder;
import org.openehealth.ipf.commons.ihe.fhir.audit.GenericFhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventIdCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Builder for audit events related to generic FHIR transactions, using the resource type
 * and operation type for the participants. The audit messages follow the pattern shared
 * with audit messages of IHE transactions. Some exceptions:
 * <ul>
 * <li>the Event ID is always EV("rest", "http://hl7.org/fhir/audit-event-type", "RESTful Operation") (as described
 * in https://www.hl7.org/fhir/valueset-audit-event-type.html)</li>
 * <li>and the Event Action corresponds with the FHIR operation (see https://www.hl7.org/fhir/valueset-audit-event-action.html)</li>
 * <li>the query/resource participant object's ParticipantObjectIdType is the resource type</li>
 * </ul>
 * This also allows an easy conversion into a FHIR AuditEvent.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class GenericFhirAuditMessageBuilder extends
        IHEAuditMessageBuilder<GenericFhirAuditMessageBuilder, CustomAuditMessageBuilder> {

    public GenericFhirAuditMessageBuilder(AuditContext auditContext, GenericFhirAuditDataset auditDataset) {
        super(auditContext, new CustomAuditMessageBuilder(
                auditDataset.getEventOutcomeIndicator(),
                auditDataset.getEventOutcomeDescription(),
                eventActionCode(auditDataset.getOperation()),
                FhirEventIdCode.RestfulOperation,
                FhirEventTypeCode.fromRestOperationType(auditDataset.getOperation())
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
    }

    public GenericFhirAuditMessageBuilder addPatients(GenericFhirAuditDataset auditDataset) {
        if (auditDataset.getPatientIds() != null)
        auditDataset.getPatientIds().stream()
            .filter(Objects::nonNull)
            .forEach(patientId ->
                delegate.addParticipantObjectIdentification(
                        ParticipantObjectIdTypeCode.PatientNumber,
                        null,
                        null,
                        null,
                        patientId,
                        ParticipantObjectTypeCode.Person,
                        ParticipantObjectTypeCodeRole.Patient,
                        null,
                        null));
        return self();
    }

    /**
     * @param auditDataset Audit Dataset
     * @return this
     */
    public GenericFhirAuditMessageBuilder addQueryParticipantObject(GenericFhirAuditDataset auditDataset) {
        delegate.addParticipantObjectIdentification(
                FhirParticipantObjectIdTypeCode.fromResourceType(
                        auditDataset.getAffectedResourceType() != null ?
                                auditDataset.getAffectedResourceType() :
                                getAuditContext().getAuditValueIfMissing()),
                auditDataset.getAffectedResourceType(),
                auditDataset.getQueryString().getBytes(StandardCharsets.UTF_8),
                null,
                "FHIR Restful Query",
                ParticipantObjectTypeCode.System,
                ParticipantObjectTypeCodeRole.Query,
                null,
                null);

        return self();
    }

    /**
     * @param auditDataset Audit Dataset
     * @return this
     */
    public GenericFhirAuditMessageBuilder addResourceParticipantObject(GenericFhirAuditDataset auditDataset) {
        delegate.addParticipantObjectIdentification(
                FhirParticipantObjectIdTypeCode.fromResourceType(
                        auditDataset.getAffectedResourceType() != null ?
                                auditDataset.getAffectedResourceType() :
                                getAuditContext().getAuditValueIfMissing()),
                null,
                null,
                null,
                auditDataset.getResourceId().getValue(),
                ParticipantObjectTypeCode.System,
                ParticipantObjectTypeCodeRole.Job,
                null,
                auditDataset.getSecurityLabel());
        return self();
    }


    private static EventActionCode eventActionCode(RestOperationTypeEnum operation) {
        switch (operation) {
            case CREATE:
                return EventActionCode.Create;
            case READ:
            case VREAD:
                return EventActionCode.Read;
            case UPDATE:
            case PATCH:
                return EventActionCode.Update;
            case DELETE:
                return EventActionCode.Delete;
            default:
                return EventActionCode.Execute;
        }
    }
}
