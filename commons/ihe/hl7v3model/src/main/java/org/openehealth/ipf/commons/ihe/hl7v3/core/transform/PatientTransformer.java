/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform;

import net.ihe.gazelle.hl7v3.coctmt030007UV.COCTMT030007UVPerson;
import net.ihe.gazelle.hl7v3.coctmt150002UV01.COCTMT150002UV01Organization;
import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03Organization;
import net.ihe.gazelle.hl7v3.coctmt710007UV.COCTMT710007UVPlace;
import net.ihe.gazelle.hl7v3.datatypes.BL;
import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.datatypes.CS;
import net.ihe.gazelle.hl7v3.datatypes.EN;
import net.ihe.gazelle.hl7v3.datatypes.INT;
import net.ihe.gazelle.hl7v3.datatypes.TS;
import net.ihe.gazelle.hl7v3.prpamt201301UV02.PRPAMT201301UV02BirthPlace;
import net.ihe.gazelle.hl7v3.prpamt201301UV02.PRPAMT201301UV02OtherIDs;
import net.ihe.gazelle.hl7v3.prpamt201301UV02.PRPAMT201301UV02Patient;
import net.ihe.gazelle.hl7v3.prpamt201301UV02.PRPAMT201301UV02Person;
import net.ihe.gazelle.hl7v3.prpamt201301UV02.PRPAMT201301UV02PersonalRelationship;
import net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02BirthPlace;
import net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02OtherIDs;
import net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02OtherIDsId;
import net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02Patient;
import net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02PatientId;
import net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02PatientPatientPerson;
import net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02PatientStatusCode;
import net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02PersonalRelationship;
import net.ihe.gazelle.hl7v3.prpamt201303UV02.PRPAMT201303UV02BirthPlace;
import net.ihe.gazelle.hl7v3.prpamt201303UV02.PRPAMT201303UV02OtherIDs;
import net.ihe.gazelle.hl7v3.prpamt201303UV02.PRPAMT201303UV02Patient;
import net.ihe.gazelle.hl7v3.prpamt201303UV02.PRPAMT201303UV02Person;
import net.ihe.gazelle.hl7v3.prpamt201303UV02.PRPAMT201303UV02PersonalRelationship;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02BirthPlace;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02OtherIDs;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02Patient;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02Person;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02PersonalRelationship;
import net.ihe.gazelle.hl7v3.voc.EntityClass;
import net.ihe.gazelle.hl7v3.voc.EntityClassOrganization;
import net.ihe.gazelle.hl7v3.voc.EntityClassPlace;
import net.ihe.gazelle.hl7v3.voc.EntityDeterminer;
import net.ihe.gazelle.hl7v3.voc.XDeterminerInstanceKind;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.OtherIds;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Patient;

import java.util.Objects;

import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.copyIi;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.toIi;

/**
 * Transformations between the simplified {@link Patient} model and the patient models of the HL7v3 patient
 * registry messages: PRPA_MT201301UV02 (record added), PRPA_MT201302UV02 (record revised),
 * PRPA_MT201303UV02 (duplicates resolved) and PRPA_MT201310UV02 (query response).
 * <p>
 * The Gazelle model has distinct, but structurally identical classes for each of them, hence the repetition.
 * The registration-level custodian is not part of these models and is handled by the message transformers.
 *
 * @author Christian Ohr
 * @since 6.1
 */
public final class PatientTransformer {

    private static final String PERSONAL_RELATIONSHIP_ROLE_TYPE = "2.16.840.1.113883.5.111";
    private static final String MOTHER = "MTH";

    private PatientTransformer() {
    }

    /* --------------------- PRPA_MT201301UV02 --------------------- */

    /**
     * Transforms a simplified patient into a PRPA_MT201301UV02 patient.
     */
    public static PRPAMT201301UV02Patient toPrpaMt201301(final Patient simplePatient) {
        final var person = new PRPAMT201301UV02Person();
        person.setClassCode(EntityClass.PSN);
        person.setDeterminerCode(EntityDeterminer.INSTANCE);
        person.getName().addAll(simplePatient.getNames());
        person.getTelecom().addAll(simplePatient.getTelecoms());
        person.setAdministrativeGenderCode(toCe(simplePatient.getAdministrativeGenderCode()));
        person.setBirthTime(toTsValue(simplePatient.getBirthTime()));
        person.setDeceasedInd(toBl(simplePatient.getDeceasedInd()));
        person.setDeceasedTime(toTsValue(simplePatient.getDeceasedTime()));
        person.setMultipleBirthInd(toBl(simplePatient.getMultipleBirthInd()));
        person.setMultipleBirthOrderNumber(toInt(simplePatient.getMultipleBirthOrderNumber()));
        person.getAddr().addAll(simplePatient.getAddresses());
        for (final var otherIds : simplePatient.getOtherIds()) {
            final var asOtherIds = new PRPAMT201301UV02OtherIDs();
            asOtherIds.setClassCode("PAT");
            otherIds.getIds().forEach(id -> asOtherIds.getId().add(id));
            asOtherIds.setScopingOrganization(toScopingOrganization(otherIds.getScopingOrganizationOid()));
            person.getAsOtherIDs().add(asOtherIds);
        }
        if (simplePatient.getMothersMaidenName() != null) {
            final var relationship = new PRPAMT201301UV02PersonalRelationship();
            relationship.setClassCode("PRS");
            relationship.setCode(mother());
            relationship.setRelationshipHolder1(toRelationshipHolder(simplePatient.getMothersMaidenName()));
            person.getPersonalRelationship().add(relationship);
        }
        if (simplePatient.getBirthPlaceAddress() != null || simplePatient.getBirthPlaceName() != null) {
            final var birthPlace = new PRPAMT201301UV02BirthPlace();
            birthPlace.setClassCode("BIRTHPL");
            birthPlace.setAddr(simplePatient.getBirthPlaceAddress());
            birthPlace.setBirthplace(toBirthplace(simplePatient.getBirthPlaceName()));
            person.setBirthPlace(birthPlace);
        }

        final var patient = new PRPAMT201301UV02Patient();
        patient.setClassCode("PAT");
        simplePatient.getPatientIds().forEach(id -> patient.getId().add(id));
        patient.setStatusCode(new CS("active", null, null));
        patient.setPatientPerson(person);
        patient.setProviderOrganization(withFixedCodes(simplePatient.getProviderOrganization()));
        return patient;
    }

    /**
     * Reads a PRPA_MT201301UV02 patient into the given simplified patient.
     *
     * @return the simplified patient
     */
    public static <P extends Patient> P fromPrpaMt201301(final PRPAMT201301UV02Patient patient, final P simplePatient) {
        if (patient == null) {
            return simplePatient;
        }
        simplePatient.getPatientIds().addAll(patient.getId());
        simplePatient.setProviderOrganization(patient.getProviderOrganization());
        final var person = patient.getPatientPerson();
        if (person == null) {
            return simplePatient;
        }
        simplePatient.getNames().addAll(person.getName());
        simplePatient.getTelecoms().addAll(person.getTelecom());
        simplePatient.setAdministrativeGenderCode(fromCe(person.getAdministrativeGenderCode()));
        simplePatient.setBirthTime(fromTsValue(person.getBirthTime()));
        simplePatient.setDeceasedInd(fromBl(person.getDeceasedInd()));
        simplePatient.setDeceasedTime(fromTsValue(person.getDeceasedTime()));
        simplePatient.setMultipleBirthInd(fromBl(person.getMultipleBirthInd()));
        simplePatient.setMultipleBirthOrderNumber(fromInt(person.getMultipleBirthOrderNumber()));
        simplePatient.getAddresses().addAll(person.getAddr());
        for (final var asOtherIds : person.getAsOtherIDs()) {
            final var otherIds = new OtherIds();
            otherIds.getIds().addAll(asOtherIds.getId());
            otherIds.setScopingOrganizationOid(fromScopingOrganization(asOtherIds.getScopingOrganization()));
            simplePatient.getOtherIds().add(otherIds);
        }
        person.getPersonalRelationship().stream()
                .filter(relationship -> isMother(relationship.getCode()))
                .map(relationship -> fromRelationshipHolder(relationship.getRelationshipHolder1()))
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(simplePatient::setMothersMaidenName);
        if (person.getBirthPlace() != null) {
            simplePatient.setBirthPlaceAddress(person.getBirthPlace().getAddr());
            simplePatient.setBirthPlaceName(fromBirthplace(person.getBirthPlace().getBirthplace()));
        }
        return simplePatient;
    }

    /* --------------------- PRPA_MT201302UV02 --------------------- */

    /**
     * Transforms a simplified patient into a PRPA_MT201302UV02 patient.
     */
    public static PRPAMT201302UV02Patient toPrpaMt201302(final Patient simplePatient) {
        final var person = new PRPAMT201302UV02PatientPatientPerson();
        person.setClassCode(EntityClass.PSN);
        person.setDeterminerCode(EntityDeterminer.INSTANCE);
        person.getName().addAll(simplePatient.getNames());
        person.getTelecom().addAll(simplePatient.getTelecoms());
        person.setAdministrativeGenderCode(toCe(simplePatient.getAdministrativeGenderCode()));
        person.setBirthTime(toTsValue(simplePatient.getBirthTime()));
        person.setDeceasedInd(toBl(simplePatient.getDeceasedInd()));
        person.setDeceasedTime(toTsValue(simplePatient.getDeceasedTime()));
        person.setMultipleBirthInd(toBl(simplePatient.getMultipleBirthInd()));
        person.setMultipleBirthOrderNumber(toInt(simplePatient.getMultipleBirthOrderNumber()));
        person.getAddr().addAll(simplePatient.getAddresses());
        for (final var otherIds : simplePatient.getOtherIds()) {
            final var asOtherIds = new PRPAMT201302UV02OtherIDs();
            asOtherIds.setClassCode("PAT");
            otherIds.getIds().forEach(id -> asOtherIds.getId().add(copyIi(id, new PRPAMT201302UV02OtherIDsId())));
            asOtherIds.setScopingOrganization(toScopingOrganization(otherIds.getScopingOrganizationOid()));
            person.getAsOtherIDs().add(asOtherIds);
        }
        if (simplePatient.getMothersMaidenName() != null) {
            final var relationship = new PRPAMT201302UV02PersonalRelationship();
            relationship.setClassCode("PRS");
            relationship.setCode(mother());
            relationship.setRelationshipHolder1(toRelationshipHolder(simplePatient.getMothersMaidenName()));
            person.getPersonalRelationship().add(relationship);
        }
        if (simplePatient.getBirthPlaceAddress() != null || simplePatient.getBirthPlaceName() != null) {
            final var birthPlace = new PRPAMT201302UV02BirthPlace();
            birthPlace.setClassCode("BIRTHPL");
            birthPlace.setAddr(simplePatient.getBirthPlaceAddress());
            birthPlace.setBirthplace(toBirthplace(simplePatient.getBirthPlaceName()));
            person.setBirthPlace(birthPlace);
        }

        final var patient = new PRPAMT201302UV02Patient();
        patient.setClassCode("PAT");
        simplePatient.getPatientIds().forEach(id -> patient.getId().add(copyIi(id, new PRPAMT201302UV02PatientId())));
        final var statusCode = new PRPAMT201302UV02PatientStatusCode();
        statusCode.setCode("active");
        patient.setStatusCode(statusCode);
        patient.setPatientPerson(person);
        patient.setProviderOrganization(withFixedCodes(simplePatient.getProviderOrganization()));
        return patient;
    }

    /**
     * Reads a PRPA_MT201302UV02 patient into the given simplified patient.
     *
     * @return the simplified patient
     */
    public static <P extends Patient> P fromPrpaMt201302(final PRPAMT201302UV02Patient patient, final P simplePatient) {
        if (patient == null) {
            return simplePatient;
        }
        simplePatient.getPatientIds().addAll(patient.getId());
        simplePatient.setProviderOrganization(patient.getProviderOrganization());
        final var person = patient.getPatientPerson();
        if (person == null) {
            return simplePatient;
        }
        simplePatient.getNames().addAll(person.getName());
        simplePatient.getTelecoms().addAll(person.getTelecom());
        simplePatient.setAdministrativeGenderCode(fromCe(person.getAdministrativeGenderCode()));
        simplePatient.setBirthTime(fromTsValue(person.getBirthTime()));
        simplePatient.setDeceasedInd(fromBl(person.getDeceasedInd()));
        simplePatient.setDeceasedTime(fromTsValue(person.getDeceasedTime()));
        simplePatient.setMultipleBirthInd(fromBl(person.getMultipleBirthInd()));
        simplePatient.setMultipleBirthOrderNumber(fromInt(person.getMultipleBirthOrderNumber()));
        simplePatient.getAddresses().addAll(person.getAddr());
        for (final var asOtherIds : person.getAsOtherIDs()) {
            final var otherIds = new OtherIds();
            otherIds.getIds().addAll(asOtherIds.getId());
            otherIds.setScopingOrganizationOid(fromScopingOrganization(asOtherIds.getScopingOrganization()));
            simplePatient.getOtherIds().add(otherIds);
        }
        person.getPersonalRelationship().stream()
                .filter(relationship -> isMother(relationship.getCode()))
                .map(relationship -> fromRelationshipHolder(relationship.getRelationshipHolder1()))
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(simplePatient::setMothersMaidenName);
        if (person.getBirthPlace() != null) {
            simplePatient.setBirthPlaceAddress(person.getBirthPlace().getAddr());
            simplePatient.setBirthPlaceName(fromBirthplace(person.getBirthPlace().getBirthplace()));
        }
        return simplePatient;
    }

    /* --------------------- PRPA_MT201303UV02 --------------------- */

    /**
     * Transforms a simplified patient into a PRPA_MT201303UV02 patient.
     */
    public static PRPAMT201303UV02Patient toPrpaMt201303(final Patient simplePatient) {
        final var person = new PRPAMT201303UV02Person();
        person.setClassCode(EntityClass.PSN);
        person.setDeterminerCode(EntityDeterminer.INSTANCE);
        person.getName().addAll(simplePatient.getNames());
        person.getTelecom().addAll(simplePatient.getTelecoms());
        person.setAdministrativeGenderCode(toCe(simplePatient.getAdministrativeGenderCode()));
        person.setBirthTime(toTsValue(simplePatient.getBirthTime()));
        person.setDeceasedInd(toBl(simplePatient.getDeceasedInd()));
        person.setDeceasedTime(toTsValue(simplePatient.getDeceasedTime()));
        person.setMultipleBirthInd(toBl(simplePatient.getMultipleBirthInd()));
        person.setMultipleBirthOrderNumber(toInt(simplePatient.getMultipleBirthOrderNumber()));
        person.getAddr().addAll(simplePatient.getAddresses());
        for (final var otherIds : simplePatient.getOtherIds()) {
            final var asOtherIds = new PRPAMT201303UV02OtherIDs();
            asOtherIds.setClassCode("PAT");
            otherIds.getIds().forEach(id -> asOtherIds.getId().add(id));
            asOtherIds.setScopingOrganization(toScopingOrganization(otherIds.getScopingOrganizationOid()));
            person.getAsOtherIDs().add(asOtherIds);
        }
        if (simplePatient.getMothersMaidenName() != null) {
            final var relationship = new PRPAMT201303UV02PersonalRelationship();
            relationship.setClassCode("PRS");
            relationship.setCode(mother());
            relationship.setRelationshipHolder1(toRelationshipHolder(simplePatient.getMothersMaidenName()));
            person.getPersonalRelationship().add(relationship);
        }
        if (simplePatient.getBirthPlaceAddress() != null || simplePatient.getBirthPlaceName() != null) {
            final var birthPlace = new PRPAMT201303UV02BirthPlace();
            birthPlace.setClassCode("BIRTHPL");
            birthPlace.setAddr(simplePatient.getBirthPlaceAddress());
            birthPlace.setBirthplace(toBirthplace(simplePatient.getBirthPlaceName()));
            person.setBirthPlace(birthPlace);
        }

        final var patient = new PRPAMT201303UV02Patient();
        patient.setClassCode("PAT");
        simplePatient.getPatientIds().forEach(id -> patient.getId().add(id));
        patient.setStatusCode(new CS("active", null, null));
        patient.setPatientPerson(person);
        patient.setProviderOrganization(withFixedCodes(simplePatient.getProviderOrganization()));
        return patient;
    }

    /**
     * Reads a PRPA_MT201303UV02 patient into the given simplified patient.
     *
     * @return the simplified patient
     */
    public static <P extends Patient> P fromPrpaMt201303(final PRPAMT201303UV02Patient patient, final P simplePatient) {
        if (patient == null) {
            return simplePatient;
        }
        simplePatient.getPatientIds().addAll(patient.getId());
        simplePatient.setProviderOrganization(patient.getProviderOrganization());
        final var person = patient.getPatientPerson();
        if (person == null) {
            return simplePatient;
        }
        simplePatient.getNames().addAll(person.getName());
        simplePatient.getTelecoms().addAll(person.getTelecom());
        simplePatient.setAdministrativeGenderCode(fromCe(person.getAdministrativeGenderCode()));
        simplePatient.setBirthTime(fromTsValue(person.getBirthTime()));
        simplePatient.setDeceasedInd(fromBl(person.getDeceasedInd()));
        simplePatient.setDeceasedTime(fromTsValue(person.getDeceasedTime()));
        simplePatient.setMultipleBirthInd(fromBl(person.getMultipleBirthInd()));
        simplePatient.setMultipleBirthOrderNumber(fromInt(person.getMultipleBirthOrderNumber()));
        simplePatient.getAddresses().addAll(person.getAddr());
        for (final var asOtherIds : person.getAsOtherIDs()) {
            final var otherIds = new OtherIds();
            otherIds.getIds().addAll(asOtherIds.getId());
            otherIds.setScopingOrganizationOid(fromScopingOrganization(asOtherIds.getScopingOrganization()));
            simplePatient.getOtherIds().add(otherIds);
        }
        person.getPersonalRelationship().stream()
                .filter(relationship -> isMother(relationship.getCode()))
                .map(relationship -> fromRelationshipHolder(relationship.getRelationshipHolder1()))
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(simplePatient::setMothersMaidenName);
        if (person.getBirthPlace() != null) {
            simplePatient.setBirthPlaceAddress(person.getBirthPlace().getAddr());
            simplePatient.setBirthPlaceName(fromBirthplace(person.getBirthPlace().getBirthplace()));
        }
        return simplePatient;
    }

    /* --------------------- PRPA_MT201310UV02 --------------------- */

    /**
     * Transforms a simplified patient into a PRPA_MT201310UV02 patient.
     */
    public static PRPAMT201310UV02Patient toPrpaMt201310(final Patient simplePatient) {
        final var person = new PRPAMT201310UV02Person();
        person.setClassCode(EntityClass.PSN);
        person.setDeterminerCode(EntityDeterminer.INSTANCE);
        person.getName().addAll(simplePatient.getNames());
        person.getTelecom().addAll(simplePatient.getTelecoms());
        person.setAdministrativeGenderCode(toCe(simplePatient.getAdministrativeGenderCode()));
        person.setBirthTime(toTsValue(simplePatient.getBirthTime()));
        person.setDeceasedInd(toBl(simplePatient.getDeceasedInd()));
        person.setDeceasedTime(toTsValue(simplePatient.getDeceasedTime()));
        person.setMultipleBirthInd(toBl(simplePatient.getMultipleBirthInd()));
        person.setMultipleBirthOrderNumber(toInt(simplePatient.getMultipleBirthOrderNumber()));
        person.getAddr().addAll(simplePatient.getAddresses());
        for (final var otherIds : simplePatient.getOtherIds()) {
            final var asOtherIds = new PRPAMT201310UV02OtherIDs();
            asOtherIds.setClassCode("PAT");
            otherIds.getIds().forEach(id -> asOtherIds.getId().add(id));
            asOtherIds.setScopingOrganization(toScopingOrganization(otherIds.getScopingOrganizationOid()));
            person.getAsOtherIDs().add(asOtherIds);
        }
        if (simplePatient.getMothersMaidenName() != null) {
            final var relationship = new PRPAMT201310UV02PersonalRelationship();
            relationship.setClassCode("PRS");
            relationship.setCode(mother());
            relationship.setRelationshipHolder1(toRelationshipHolder(simplePatient.getMothersMaidenName()));
            person.getPersonalRelationship().add(relationship);
        }
        if (simplePatient.getBirthPlaceAddress() != null || simplePatient.getBirthPlaceName() != null) {
            final var birthPlace = new PRPAMT201310UV02BirthPlace();
            birthPlace.setClassCode("BIRTHPL");
            birthPlace.setAddr(simplePatient.getBirthPlaceAddress());
            birthPlace.setBirthplace(toBirthplace(simplePatient.getBirthPlaceName()));
            person.setBirthPlace(birthPlace);
        }

        final var patient = new PRPAMT201310UV02Patient();
        patient.setClassCode("PAT");
        simplePatient.getPatientIds().forEach(id -> patient.getId().add(id));
        patient.setStatusCode(new CS("active", null, null));
        patient.setPatientPerson(person);
        patient.setProviderOrganization(withFixedCodes(simplePatient.getProviderOrganization()));
        return patient;
    }

    /**
     * Reads a PRPA_MT201310UV02 patient into the given simplified patient.
     *
     * @return the simplified patient
     */
    public static <P extends Patient> P fromPrpaMt201310(final PRPAMT201310UV02Patient patient, final P simplePatient) {
        if (patient == null) {
            return simplePatient;
        }
        simplePatient.getPatientIds().addAll(patient.getId());
        simplePatient.setProviderOrganization(patient.getProviderOrganization());
        final var person = patient.getPatientPerson();
        if (person == null) {
            return simplePatient;
        }
        simplePatient.getNames().addAll(person.getName());
        simplePatient.getTelecoms().addAll(person.getTelecom());
        simplePatient.setAdministrativeGenderCode(fromCe(person.getAdministrativeGenderCode()));
        simplePatient.setBirthTime(fromTsValue(person.getBirthTime()));
        simplePatient.setDeceasedInd(fromBl(person.getDeceasedInd()));
        simplePatient.setDeceasedTime(fromTsValue(person.getDeceasedTime()));
        simplePatient.setMultipleBirthInd(fromBl(person.getMultipleBirthInd()));
        simplePatient.setMultipleBirthOrderNumber(fromInt(person.getMultipleBirthOrderNumber()));
        simplePatient.getAddresses().addAll(person.getAddr());
        for (final var asOtherIds : person.getAsOtherIDs()) {
            final var otherIds = new OtherIds();
            otherIds.getIds().addAll(asOtherIds.getId());
            otherIds.setScopingOrganizationOid(fromScopingOrganization(asOtherIds.getScopingOrganization()));
            simplePatient.getOtherIds().add(otherIds);
        }
        person.getPersonalRelationship().stream()
                .filter(relationship -> isMother(relationship.getCode()))
                .map(relationship -> fromRelationshipHolder(relationship.getRelationshipHolder1()))
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(simplePatient::setMothersMaidenName);
        if (person.getBirthPlace() != null) {
            simplePatient.setBirthPlaceAddress(person.getBirthPlace().getAddr());
            simplePatient.setBirthPlaceName(fromBirthplace(person.getBirthPlace().getBirthplace()));
        }
        return simplePatient;
    }

    /* --------------------- Shared parts --------------------- */

    private static COCTMT150002UV01Organization toScopingOrganization(final String oid) {
        if (oid == null) {
            return null;
        }
        final var organization = new COCTMT150002UV01Organization();
        organization.setClassCode(EntityClassOrganization.ORG);
        organization.setDeterminerCode(EntityDeterminer.INSTANCE);
        organization.getId().add(toIi(oid));
        return organization;
    }

    private static String fromScopingOrganization(final COCTMT150002UV01Organization organization) {
        return organization == null || organization.getId().isEmpty() ? null : organization.getId().get(0).getRoot();
    }

    private static CE mother() {
        return new CE(MOTHER, "Mother", PERSONAL_RELATIONSHIP_ROLE_TYPE);
    }

    private static boolean isMother(final CE code) {
        return code != null && MOTHER.equals(code.getCode());
    }

    private static COCTMT030007UVPerson toRelationshipHolder(final EN name) {
        final var holder = new COCTMT030007UVPerson();
        holder.setClassCode(EntityClass.PSN);
        holder.setDeterminerCode(XDeterminerInstanceKind.INSTANCE);
        holder.getName().add(name);
        return holder;
    }

    private static EN fromRelationshipHolder(final COCTMT030007UVPerson holder) {
        return holder == null || holder.getName().isEmpty() ? null : holder.getName().get(0);
    }

    private static COCTMT710007UVPlace toBirthplace(final EN name) {
        if (name == null) {
            return null;
        }
        final var place = new COCTMT710007UVPlace();
        place.setClassCode(EntityClassPlace.CITY);
        place.setDeterminerCode(EntityDeterminer.INSTANCE);
        place.getName().add(name);
        return place;
    }

    private static EN fromBirthplace(final COCTMT710007UVPlace place) {
        return place == null || place.getName().isEmpty() ? null : place.getName().get(0);
    }

    /**
     * Sets the fixed class and determiner codes required by the schema, if missing.
     */
    private static COCTMT150003UV03Organization withFixedCodes(final COCTMT150003UV03Organization organization) {
        if (organization != null) {
            if (organization.getClassCode() == null) {
                organization.setClassCode(EntityClassOrganization.ORG);
            }
            if (organization.getDeterminerCode() == null) {
                organization.setDeterminerCode(EntityDeterminer.INSTANCE);
            }
        }
        return organization;
    }

    private static CE toCe(final String code) {
        return code == null ? null : new CE(code, null, null);
    }

    private static String fromCe(final CE ce) {
        return ce == null ? null : ce.getCode();
    }

    private static TS toTsValue(final String value) {
        return value == null ? null : new TS(value);
    }

    private static String fromTsValue(final TS ts) {
        return ts == null ? null : ts.getValue();
    }

    private static BL toBl(final Boolean value) {
        return value == null ? null : new BL(value);
    }

    private static Boolean fromBl(final BL bl) {
        return bl == null ? null : bl.getValue();
    }

    private static INT toInt(final Integer value) {
        return value == null ? null : new INT(value);
    }

    private static Integer fromInt(final INT value) {
        return value == null ? null : value.getValue();
    }
}
