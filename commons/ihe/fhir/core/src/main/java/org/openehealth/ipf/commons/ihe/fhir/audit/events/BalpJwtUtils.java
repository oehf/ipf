/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.BalpAuditContext;
import org.openehealth.ipf.commons.audit.BalpJwtExtractorProperties;
import org.openehealth.ipf.commons.audit.DefaultBalpAuditContext;
import org.openehealth.ipf.commons.audit.event.BaseAuditMessageBuilder;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.CodedValueType;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.auth.BalpJwtDataSet;
import org.openehealth.ipf.commons.ihe.fhir.audit.auth.BalpJwtParser;

import java.util.Collections;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DCM_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.OUSER_AGENT_ROLE_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.OUSER_AGENT_TYPE_OPAQUE_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.OUSER_AGENT_TYPE_SYSTEM_NAME;

public class BalpJwtUtils {

    private static final BalpJwtExtractorProperties DEFAULT_BALP_JWT_EXTRACTOR_PROPERTIES = new BalpJwtExtractorProperties();

    public static <D extends BaseAuditMessageBuilder<D>> void addJwtParticipant(D delegate,
                                                                                FhirAuditDataset auditDataset,
                                                                                AuditContext auditContext) {
        BalpJwtExtractorProperties balpJwtExtractorProperties = (auditContext instanceof BalpAuditContext)?
            ((BalpAuditContext)auditContext).getBalpJwtExtractorProperties() : DEFAULT_BALP_JWT_EXTRACTOR_PROPERTIES;
        Optional<BalpJwtDataSet> balpDataSet = BalpJwtParser.parseAuthorizationToBalpDataSet(
            auditDataset.getAuthorization(), balpJwtExtractorProperties);
        balpDataSet.ifPresent(dataSet -> {
            if (isNotBlank(dataSet.getIheBppcPatientId())) {
                delegate.addPatientParticipantObject(
                    dataSet.getIheBppcPatientId(),
                    "oAuth Token Patient ID",
                    Collections.emptyList(),
                    null);
            }
            if (isNotBlank(dataSet.getIheIuaSubjectOrganizationId())) {
                ActiveParticipantType ap = new ActiveParticipantType(dataSet.getIheIuaSubjectOrganizationId(), true);
                ap.setUserName(dataSet.getIheIuaSubjectOrganization());
                ap.getRoleIDCodes().add(
                    ActiveParticipantRoleId.of(CodedValueType.of(dataSet.getIheIuaSubjectOrganizationId(),
                        DCM_SYSTEM_NAME, "oAuth Token Subject Organization ID")));
                delegate.addActiveParticipant(ap);
            }
            if (isNotBlank(dataSet.getJwtId())) {
                ActiveParticipantType ap = new ActiveParticipantType(dataSet.getSubject(), true);
                ap.getRoleIDCodes().add(
                    ActiveParticipantRoleId.of(CodedValueType.of(dataSet.getJwtId(),
                        OUSER_AGENT_TYPE_SYSTEM_NAME, "oAuth Token ID")));
                ap.setUserName(dataSet.getIheIuaSubjectName());
                if (isNotBlank(dataSet.getIssuer())) {
                    ap.setAlternativeUserID(dataSet.getIssuer());
                }
                if (dataSet.getIheIuaPurposeOfUse() != null && !dataSet.getIheIuaPurposeOfUse().isEmpty()) {
                    dataSet.getIheIuaPurposeOfUse().forEach(purpose -> ap.getRoleIDCodes().add(
                        ActiveParticipantRoleId.of(CodedValueType.of(purpose,
                            OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME, "oAuth Token Purpose of Use"))));
                }
                if (dataSet.getIheIuaSubjectRole() != null && !dataSet.getIheIuaSubjectRole().isEmpty()) {
                    dataSet.getIheIuaSubjectRole().forEach(role -> ap.getRoleIDCodes().add(
                        ActiveParticipantRoleId.of(CodedValueType.of(role,
                            OUSER_AGENT_ROLE_SYSTEM_NAME, "oAuth Token User Role"))));
                }
                delegate.addActiveParticipant(ap);
                if (isNotBlank(dataSet.getClientId())) {
                    ActiveParticipantType clientAp = new ActiveParticipantType(
                        dataSet.getClientId(), !auditDataset.isServerSide());
                    clientAp.getRoleIDCodes().add(
                        ActiveParticipantRoleId.of(CodedValueType.of(dataSet.getClientId(),
                            DCM_SYSTEM_NAME, "oAuth Token Client ID")));
                    delegate.addActiveParticipant(clientAp);
                }
            } else if (isNotBlank(dataSet.getOpaqueJwt())) {
                ActiveParticipantType ap = new ActiveParticipantType(dataSet.getSubject(), true);
                ap.getRoleIDCodes().add(
                    ActiveParticipantRoleId.of(CodedValueType.of(dataSet.getOpaqueJwt(),
                        OUSER_AGENT_TYPE_OPAQUE_SYSTEM_NAME, "oAuth Opaque Token")));
            }
        });
    }

    private BalpAuditContext balpAuditContext(AuditContext auditContext) {
        return auditContext instanceof BalpAuditContext? (BalpAuditContext) auditContext : null;
    }

}