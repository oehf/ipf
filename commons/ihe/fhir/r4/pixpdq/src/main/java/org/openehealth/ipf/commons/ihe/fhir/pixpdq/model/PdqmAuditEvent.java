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
package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.AuditSourceType;
import org.hl7.fhir.r4.model.codesystems.RestfulInteraction;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.SelfInitializing;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpAuditEventHelper;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.QueryAuditEvent;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode.MobilePatientDemographicsQuery;

abstract class PdqmAuditEvent extends QueryAuditEvent implements SelfInitializing {

    public PdqmAuditEvent() {
        super();
        setSearchType(RestfulInteraction.SEARCH);
        addSubtype()
            .setCode(MobilePatientDemographicsQuery.getCode())
            .setSystem(MobilePatientDemographicsQuery.getCodeSystemName())
            .setDisplay(MobilePatientDemographicsQuery.getDisplayName());
    }

    @Override
    public void initialize(AuditMessage auditMessage) {
        SelfInitializing.super.initialize(auditMessage);
        getSource()
            .setSite(auditMessage.getAuditSourceIdentification().getAuditEnterpriseSiteID())
            .addType()
                .setCode(AuditSourceType._4.toCode())
                .setSystem(AuditSourceType._4.getSystem())
                .setDisplay(AuditSourceType._4.getDisplay());
        auditMessage.findParticipantObjectIdentifications(
            poi -> ParticipantObjectIdTypeCode.PatientNumber == poi.getParticipantObjectIDTypeCode())
            .stream()
            .findFirst()
            .ifPresent(poi ->
                BalpAuditEventHelper.addPatient(this,
                    new Reference().setDisplay(poi.getParticipantObjectID()))
            );
    }

    @Override
    public void setUserAgent(AuditMessage auditMessage) {
    }

    protected ActiveParticipantType activeParticipantType(AuditMessage auditMessage, ActiveParticipantRoleIdCode roleIdCode) {
        return auditMessage
            .findActiveParticipants(apt -> apt.getRoleIDCodes().contains(roleIdCode))
            .stream()
            .findFirst()
            .orElseThrow();
    }
}
