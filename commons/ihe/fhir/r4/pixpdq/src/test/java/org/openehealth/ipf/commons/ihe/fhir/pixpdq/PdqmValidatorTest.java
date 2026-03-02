
package org.openehealth.ipf.commons.ihe.fhir.pixpdq;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmPatient;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.QueryPatientResourceResponseMessage;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for PdqmValidator.
 * Tests validation of FHIR Patient resources against the PDQm profile.
 */
class PdqmValidatorTest {

    private static final Logger log = LoggerFactory.getLogger(PdqmValidatorTest.class);
    private static PdqmValidator validator;

    @BeforeAll
    static void setUp() {
        FhirContext fhirContext = FhirContext.forR4();
        PdqmProfile.registerDefaultTypes(fhirContext);
        validator = new PdqmValidator(fhirContext);
    }

    @Test
    void testValidateMinimalValidPatient() {
        var patient = createMinimalValidPatient();
        var outcome = validate(patient);
        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Minimal valid patient should pass validation");
    }

    private OperationOutcome validate(PdqmPatient patient) {
        try {
            return validator.validateResponse(patient, Map.of(
                Constants.INTERACTION_RESPONSE_VALIDATION_PROFILES,
                Set.of(PdqmProfile.PDQM_PATIENT_PROFILE)));
        } catch (UnprocessableEntityException e) {
            var operationOutcome = (OperationOutcome) e.getOperationOutcome();
            var issues = ((OperationOutcome) e.getOperationOutcome()).getIssue();
            issues.forEach(issue -> FhirUtils.logValidationMessage(log, issue));
            return operationOutcome;
        }
    }

    private OperationOutcome validate(QueryPatientResourceResponseMessage bundle) {
        try {
            return validator.validateResponse(bundle, Map.of(
                Constants.INTERACTION_RESPONSE_VALIDATION_PROFILES,
                Set.of(PdqmProfile.ITI78_QUERY_PATIENT_RESOURCE_RESPONSE_MESSAGE_PROFILE)));
        } catch (UnprocessableEntityException e) {
            var operationOutcome = (OperationOutcome) e.getOperationOutcome();
            var issues = ((OperationOutcome) e.getOperationOutcome()).getIssue();
            issues.forEach(issue -> FhirUtils.logValidationMessage(log, issue));
            return operationOutcome;
        }
    }

    @Test
    void testValidateCompleteValidPatient() {
        var patient = createCompleteValidPatient();
        var outcome = validate(patient);
        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Complete valid patient should pass validation");
    }

    @Test
    void testValidateCompleteValidPatientInBundle() {
        var patient = createCompleteValidPatient();
        var bundle = new QueryPatientResourceResponseMessage();
        bundle.addPatient(patient);
        var outcome = validate(bundle);
        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Complete valid patient should pass validation");
    }

    @Test
    void testValidatePatientWithGenderIdentity() {
        var patient = createMinimalValidPatient();

        // Add gender identity using the proper class
        var genderIdentity = patient.addGenderIdentity();
        genderIdentity.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/gender-identity")
                .setCode("male")
                .setDisplay("Male")));
        genderIdentity.setPeriod(new Period()
            .setStart(new DateType("2020-01-01").getValue()));
        var outcome = validate(patient);
        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with gender identity should pass validation");
    }

    @Test
    void testValidatePatientWithMultipleGenderIdentities() {
        var patient = createMinimalValidPatient();

        // Add multiple gender identities
        var genderIdentity1 = patient.addGenderIdentity();
        genderIdentity1.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/gender-identity")
                .setCode("male")));

        var genderIdentity2 = patient.addGenderIdentity();
        genderIdentity2.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/gender-identity")
                .setCode("non-binary")));

        var outcome = validate(patient);
        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with multiple gender identities should pass validation");
    }

    @Test
    void testValidatePatientWithPronouns() {
        var patient = createMinimalValidPatient();

        // Add pronouns using the proper class
        var pronouns = patient.addPronoun();
        pronouns.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("LA29518-0")
                .setDisplay("he/him/his/his/himself")));
        pronouns.setPeriod(new Period()
            .setStart(new DateType("2020-01-01").getValue()));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with pronouns should pass validation");
    }

    @Test
    void testValidatePatientWithMultiplePronouns() {
        var patient = createMinimalValidPatient();

        var pronouns1 = patient.addPronoun();
        pronouns1.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("LA29518-0")
                .setDisplay("he/him/his/his/himself")));

        var pronouns2 = patient.addPronoun();
        pronouns2.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("LA29519-8")
                .setDisplay("she/her/her/hers/herself")));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with multiple pronouns should pass validation");
    }

    @Test
    void testValidatePatientWithRecordedSexOrGender() {
        var patient = createMinimalValidPatient();

        // Add recorded sex or gender using the proper class
        var recordedSexOrGender = patient.addRecordedSexOrGender();
        recordedSexOrGender.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-AdministrativeGender")
                .setCode("M")
                .setDisplay("Male")));
        recordedSexOrGender.setType(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("76689-9")
                .setDisplay("Sex assigned at birth")));
        recordedSexOrGender.setEffectivePeriod(new Period()
            .setStart(new DateType("1980-01-15").getValue()));
        recordedSexOrGender.setAcquisitionDateElement(new DateTimeType("1980-01-15"));
        recordedSexOrGender.setSourceDocument(new Reference("DocumentReference/example"));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with recorded sex or gender should pass validation");
    }

    @Test
    void testValidatePatientWithMultipleRecordedSexOrGender() {
        var patient = createMinimalValidPatient();

        var rsg1 = patient.addRecordedSexOrGender();
        rsg1.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-AdministrativeGender")
                .setCode("M")));
        rsg1.setType(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("76689-9")));

        var rsg2 = patient.addRecordedSexOrGender();
        rsg2.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-AdministrativeGender")
                .setCode("M")));
        rsg2.setType(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("46098-0")
                .setDisplay("Sex")));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with multiple recorded sex or gender should pass validation");
    }

    @Test
    void testValidatePatientWithMothersMaidenName() {
        var patient = createMinimalValidPatient();

        // Use the proper setter method
        patient.setMothersMaidenName("Smith");

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with mother's maiden name should pass validation");
    }

    @Test
    void testValidatePatientWithBirthPlace() {
        var patient = createMinimalValidPatient();

        // Use the proper setter method
        var birthPlace = new Address();
        birthPlace.setCity("Springfield");
        birthPlace.setState("IL");
        birthPlace.setCountry("USA");
        patient.setBirthPlace(birthPlace);

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with birth place should pass validation");
    }

    @Test
    void testValidatePatientWithCitizenship() {
        var patient = createMinimalValidPatient();

        // Add citizenship using the proper class
        var citizenship = patient.addCitizenship();
        citizenship.setCode(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("urn:iso:std:iso:3166")
                .setCode("US")
                .setDisplay("United States of America")));
        citizenship.setPeriod(new Period()
            .setStart(new DateType("1980-01-15").getValue()));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with citizenship should pass validation");
    }

    @Test
    void testValidatePatientWithMultipleCitizenships() {
        var patient = createMinimalValidPatient();

        var citizenship1 = patient.addCitizenship();
        citizenship1.setCode(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("urn:iso:std:iso:3166")
                .setCode("US")));

        var citizenship2 = patient.addCitizenship();
        citizenship2.setCode(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("urn:iso:std:iso:3166")
                .setCode("CA")
                .setDisplay("Canada")));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with multiple citizenships should pass validation");
    }

    @Test
    void testValidatePatientWithReligion() {
        var patient = createMinimalValidPatient();

        // Add religion using the proper method
        patient.addReligion(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-ReligiousAffiliation")
                .setCode("1013")
                .setDisplay("Christian (non-Catholic, non-specific)")));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with religion should pass validation");
    }

    @Test
    void testValidatePatientWithRace() {
        var patient = createMinimalValidPatient();

        // Add race using the proper class
        var race = patient.getRace();
        race.setOmbCategory(new Coding()
            .setSystem("urn:oid:2.16.840.1.113883.6.238")
            .setCode("2106-3")
            .setDisplay("White"));
        race.setDetailed(new Coding()
            .setSystem("urn:oid:2.16.840.1.113883.6.238")
            .setCode("2108-9")
            .setDisplay("European"));
        race.setText(new StringType("White"));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with race should pass validation");
    }

    @Test
    void testValidatePatientWithEthnicity() {
        var patient = createMinimalValidPatient();

        // Add ethnicity using the proper class
        var ethnicity = patient.getEthnicity();
        ethnicity.setOmbCategory(new Coding()
            .setSystem("urn:oid:2.16.840.1.113883.6.238")
            .setCode("2186-5")
            .setDisplay("Not Hispanic or Latino"));
        ethnicity.setDetailed(new Coding()
            .setSystem("urn:oid:2.16.840.1.113883.6.238")
            .setCode("2186-5")
            .setDisplay("Not Hispanic or Latino"));
        ethnicity.setText(new StringType("Not Hispanic or Latino"));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with ethnicity should pass validation");
    }

    @Test
    void testValidatePatientWithMultipleIdentifiers() {
        var patient = createMinimalValidPatient();

        patient.addIdentifier()
            .setSystem("urn:oid:2.16.840.1.113883.4.1")
            .setValue("123-45-6789")
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
                    .setCode("SS")));

        patient.addIdentifier()
            .setSystem("http://hospital.example.org/patients")
            .setValue("MRN-12345")
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
                    .setCode("MR")));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with multiple identifiers should pass validation");
    }

    @Test
    void testValidatePatientWithMultipleNames() {
        var patient = createMinimalValidPatient();

        patient.addName()
            .setUse(HumanName.NameUse.OFFICIAL)
            .setFamily("Doe")
            .addGiven("John")
            .addGiven("Michael");

        patient.addName()
            .setUse(HumanName.NameUse.NICKNAME)
            .addGiven("Johnny");

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with multiple names should pass validation");
    }

    @Test
    void testValidatePatientWithTelecom() {
        var patient = createMinimalValidPatient();

        patient.addTelecom()
            .setSystem(ContactPoint.ContactPointSystem.PHONE)
            .setValue("+1-555-123-4567")
            .setUse(ContactPoint.ContactPointUse.HOME);

        patient.addTelecom()
            .setSystem(ContactPoint.ContactPointSystem.EMAIL)
            .setValue("john.doe@example.com")
            .setUse(ContactPoint.ContactPointUse.WORK);

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with telecom should pass validation");
    }

    @Test
    void testValidatePatientWithAddress() {
        var patient = createMinimalValidPatient();

        patient.addAddress()
            .setUse(Address.AddressUse.HOME)
            .setType(Address.AddressType.BOTH)
            .addLine("123 Main Street")
            .addLine("Apt 4B")
            .setCity("Springfield")
            .setState("IL")
            .setPostalCode("62701")
            .setCountry("USA");

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with address should pass validation");
    }

    @Test
    void testValidatePatientWithAllExtensions() {
        var patient = createMinimalValidPatient();

        // Mother's maiden name
        patient.setMothersMaidenName("Smith");

        // Gender identity
        var genderIdentity = patient.addGenderIdentity();
        genderIdentity.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/gender-identity")
                .setCode("male")));

        // Pronouns
        var pronouns = patient.addPronoun();
        pronouns.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("LA29518-0")));

        // Recorded sex or gender
        var rsg = patient.addRecordedSexOrGender();
        rsg.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-AdministrativeGender")
                .setCode("M")));
        rsg.setType(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("76689-9")));

        // Birth place
        patient.setBirthPlace(new Address().setCity("Springfield").setCountry("USA"));

        // Citizenship
        var citizenship = patient.addCitizenship();
        citizenship.setCode(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("urn:iso:std:iso:3166")
                .setCode("US")));

        // Religion
        patient.addReligion(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-ReligiousAffiliation")
                .setCode("1013")));

        // Race
        var race = patient.getRace();
        race.setOmbCategory(new Coding()
            .setSystem("urn:oid:2.16.840.1.113883.6.238")
            .setCode("2106-3"));

        // Ethnicity
        var ethnicity = patient.getEthnicity();
        ethnicity.setOmbCategory(new Coding()
            .setSystem("urn:oid:2.16.840.1.113883.6.238")
            .setCode("2186-5"));

        var outcome = validate(patient);

        assertNotNull(outcome);
        assertTrue(hasNoErrors(outcome), "Patient with all extensions should pass validation");
    }

    @Test
    void testValidateEmptyPatient() {
        var patient = new PdqmPatient();
        var outcome = validate(patient);
        assertNotNull(outcome);
        assertTrue(hasErrors(outcome), "Empty patient should have errors");
    }

    // Helper methods

    private PdqmPatient createMinimalValidPatient() {
        var patient = new PdqmPatient();
        // Set the resource ID - this is what HAPI checks for in search bundles
        patient.setId(new IdType("Patient", UUID.randomUUID().toString()));
        patient.addIdentifier()
            .setSystem("http://example.org/patients")
            .setValue("12345");
        patient.addName()
            .setFamily("Doe")
            .addGiven("John");
        return patient;
    }

    private PdqmPatient createCompleteValidPatient() {
        var patient = createMinimalValidPatient();

        // Add all common attributes
        patient.setGender(Enumerations.AdministrativeGender.MALE);
        patient.setBirthDateElement(new DateType("1980-01-15"));
        patient.setActive(true);

        // Add telecom
        patient.addTelecom()
            .setSystem(ContactPoint.ContactPointSystem.PHONE)
            .setValue("+1-555-123-4567")
            .setUse(ContactPoint.ContactPointUse.HOME);

        // Add address
        patient.addAddress()
            .setUse(Address.AddressUse.HOME)
            .addLine("123 Main Street")
            .setCity("Springfield")
            .setState("IL")
            .setPostalCode("62701")
            .setCountry("USA");

        // Add marital status
        patient.setMaritalStatus(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus")
                .setCode("M")));

        // Add communication
        patient.addCommunication()
            .setLanguage(new CodeableConcept()
                .addCoding(new Coding()
                    .setSystem("urn:ietf:bcp:47")
                    .setCode("en-US")))
            .setPreferred(true);

        // Add PDQm-specific extensions using proper classes
        patient.setMothersMaidenName("Smith");

        var genderIdentity = patient.addGenderIdentity();
        genderIdentity.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/gender-identity")
                .setCode("male")));

        var pronouns = patient.addPronoun();
        pronouns.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("LA29518-0")));

        var rsg = patient.addRecordedSexOrGender();
        rsg.setValue(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-AdministrativeGender")
                .setCode("M")));
        rsg.setType(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("76689-9")));

        return patient;
    }

    private boolean hasNoErrors(OperationOutcome outcome) {
        if (outcome == null || !outcome.hasIssue()) {
            return true;
        }

        return outcome.getIssue().stream()
            .noneMatch(issue ->
                issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR
                    || issue.getSeverity() == OperationOutcome.IssueSeverity.FATAL);
    }

    private boolean hasErrors(OperationOutcome outcome) {
        return !hasNoErrors(outcome);
    }
}