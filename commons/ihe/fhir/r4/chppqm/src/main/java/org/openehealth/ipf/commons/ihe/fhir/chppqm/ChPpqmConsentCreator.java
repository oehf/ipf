/*
 * Copyright 2023 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.chppqm;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.ConsentScope;
import org.hl7.fhir.r4.model.codesystems.V3ActCode;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants;
import org.openehealth.ipf.commons.ihe.xacml20.model.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.xacml20.model.SubjectRole;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChPpqmConsentCreator {

    private static Consent createConsent(
            String consentId,
            String templateId,
            String eprSpid,
            String referencedPolicySetId,
            Consent.provisionActorComponent actor,
            Date startDate,
            Date endDate,
            List<PurposeOfUse> purposesOfUse)
    {
        Consent consent = new Consent()
                .addIdentifier(new Identifier()
                        .setType(new CodeableConcept(new Coding()
                                .setSystem(ChPpqmUtils.CodingSystems.CONSENT_IDENTIFIER_TYPE)
                                .setCode(ChPpqmUtils.ConsentIdTypes.POLICY_SET_ID)))
                        .setValue(consentId))
                .addIdentifier(new Identifier()
                        .setType(new CodeableConcept(new Coding()
                                .setSystem(ChPpqmUtils.CodingSystems.CONSENT_IDENTIFIER_TYPE)
                                .setCode(ChPpqmUtils.ConsentIdTypes.TEMPLATE_ID)))
                        .setValue(templateId))
                .setStatus(Consent.ConsentState.ACTIVE)
                .setScope(new CodeableConcept(new Coding(
                        ConsentScope.PATIENTPRIVACY.getSystem(),
                        ConsentScope.PATIENTPRIVACY.toCode(),
                        ConsentScope.PATIENTPRIVACY.getDisplay())))
                .addCategory(new CodeableConcept(new Coding(
                        V3ActCode.INFA.getSystem(),
                        V3ActCode.INFA.toCode(),
                        V3ActCode.INFA.getDisplay())))
                .setPatient(new Reference().setIdentifier(new Identifier()
                        .setSystem("urn:oid:" + PpqConstants.CodingSystemIds.SWISS_PATIENT_ID)
                        .setValue(eprSpid)))
                .setPolicyRule(new CodeableConcept(new Coding()
                        .setSystem(Constants.URN_IETF_RFC_3986)
                        .setCode(referencedPolicySetId)))
                .setProvision(new Consent.provisionComponent()
                        .setPeriod(createPeriod(startDate, endDate))
                        .addActor(actor)
                        .setPurpose(purposesOfUse.stream()
                                .map(pou -> new Coding(
                                        "urn:oid:" + pou.getCode().getCodeSystem(),
                                        pou.getCode().getCode(),
                                        pou.getCode().getDisplayName()))
                                .collect(Collectors.toList())));

        consent.getMeta().addProfile(ChPpqmUtils.Profiles.CONSENT);
        return consent;
    }

    private static Period createPeriod(Date startDate, Date endDate) {
        if (endDate == null) {
            if (startDate == null) {
                return null;
            } else {
                throw new IllegalArgumentException("If the start date is provided, the end date shall be provided as well");
            }
        }
        return new Period()
                .setStart(startDate, TemporalPrecisionEnum.DAY)
                .setEnd(endDate, TemporalPrecisionEnum.DAY);
    }

    private static Consent.provisionActorComponent createActor(SubjectRole role) {
        return new Consent.provisionActorComponent()
                .setRole(new CodeableConcept(new Coding()
                        .setSystem("urn:oid:" + role.getCode().getCodeSystem())
                        .setCode(role.getCode().getCode())
                        .setDisplay(role.getCode().getDisplayName())));
    }

    private static Consent.provisionActorComponent createInstanceActor(
            SubjectRole role,
            String idQualifier,
            String id)
    {
        return createActor(role)
                .setReference(new Reference()
                        .setIdentifier(new Identifier()
                                .setType(new CodeableConcept(new Coding()
                                        .setSystem(Constants.URN_IETF_RFC_3986)
                                        .setCode(idQualifier)))
                                .setValue(id)));
    }

    private static Consent.provisionActorComponent createRoleActor(SubjectRole role) {
        return createActor(role)
                .setReference(new Reference().setDisplay("all"));
    }

    public static String createUuid() {
        return "urn:uuid:" + UUID.randomUUID();
    }

    // template 201 -- full access for the patient
    public static Consent create201Consent(String id, String eprSpid) {
        return createConsent(
                id,
                "201",
                eprSpid,
                "urn:e-health-suisse:2015:policies:access-level:full",
                createInstanceActor(SubjectRole.PATIENT, "urn:e-health-suisse:2015:epr-spid", eprSpid),
                null,
                null,
                Collections.emptyList());
    }

    // template 202 -- access in case of emergency
    public static Consent create202Consent(String id, String eprSpid, String policyIdReference) {
        return createConsent(
                id,
                "202",
                eprSpid,
                policyIdReference,
                createRoleActor(SubjectRole.PROFESSIONAL),
                null,
                null,
                List.of(PurposeOfUse.EMERGENCY));
    }

    // template 203 -- minimally allowed confidentiality code
    public static Consent create203Consent(String id, String eprSpid, String policyIdReference) {
        return createConsent(
                id,
                "203",
                eprSpid,
                policyIdReference,
                createRoleActor(SubjectRole.PROFESSIONAL),
                null,
                null,
                List.of(PurposeOfUse.NORMAL, PurposeOfUse.AUTO, PurposeOfUse.DICOM_AUTO));
    }

    // template 301 -- read access for a particular HCP
    public static Consent create301Consent(
            String id,
            String eprSpid,
            String gln,
            String policyIdReference,
            Date startDate,
            Date endDate)
    {
        if (policyIdReference.contains("delegation") && (endDate == null)) {
            // TODO: In Swiss EPR spec revision 2024, delegation will be moved to template 304.
            throw new IllegalArgumentException("In delegation policies, the end date shall be provided");
        }
        return createConsent(
                id,
                "301",
                eprSpid,
                policyIdReference,
                createInstanceActor(SubjectRole.PROFESSIONAL, "urn:gs1:gln", gln),
                startDate,
                endDate,
                List.of(PurposeOfUse.NORMAL));
    }

    // template 302 -- read access for a particular group of HCPs
    public static Consent create302Consent(
            String id,
            String eprSpid,
            String groupOid,
            String policyIdReference,
            Date startDate,
            Date endDate)
    {
        if (endDate == null) {
            throw new IllegalArgumentException("In group policies, the end date shall be provided");
        }
        return createConsent(
                id,
                "302",
                eprSpid,
                policyIdReference,
                createInstanceActor(SubjectRole.PROFESSIONAL, "urn:oasis:names:tc:xspa:1.0:subject:organization-id", groupOid),
                startDate,
                endDate,
                List.of(PurposeOfUse.NORMAL));
    }

    // template 303 -- full access for a particular representative
    public static Consent create303Consent(
            String id,
            String eprSpid,
            String representativeId,
            Date startDate,
            Date endDate)
    {
        return createConsent(
                id,
                "303",
                eprSpid,
                "urn:e-health-suisse:2015:policies:access-level:full",
                createInstanceActor(SubjectRole.REPRESENTATIVE, "urn:e-health-suisse:representative-id", representativeId),
                startDate,
                endDate,
                Collections.emptyList());
    }

}
