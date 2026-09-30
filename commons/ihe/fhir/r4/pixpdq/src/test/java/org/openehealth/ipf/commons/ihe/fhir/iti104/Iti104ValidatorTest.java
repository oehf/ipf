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

package org.openehealth.ipf.commons.ihe.fhir.iti104;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PIXM;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmPatient;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmPatient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates Patients of the Patient Identity Feed FHIR [ITI-104] as the Manager does: against the PIXm or
 * PDQm Patient profile if they claim it, and against the base FHIR Patient otherwise.
 *
 * @author Christian Ohr
 */
public class Iti104ValidatorTest {

    private static FhirTransactionValidator validator;
    private static Map<String, Object> parameters;

    @BeforeAll
    public static void setUp() {
        var configuration = PIXM.Interactions.ITI_104.getFhirTransactionConfiguration();
        validator = configuration.getFhirValidator();
        parameters = Map.of(Constants.INTERACTION_REQUEST_VALIDATION_PROFILES, configuration.getRequestValidationProfiles());
    }

    @Test
    public void testValidPixmPatient() {
        assertValid(pixmPatient());
    }

    @Test
    public void testPixmPatientWithoutNameIsInvalid() {
        var patient = pixmPatient();
        patient.getName().clear();
        assertInvalid(patient);
    }

    @Test
    public void testResolveDuplicatePatient() {
        var patient = pixmPatient();
        patient.addReplacedByLink(new Identifier().setSystem("urn:oid:1.3.6.1.4.1.21367.13.20.1000").setValue("IHERED-1000"));
        assertValid(patient);
    }

    @Test
    public void testValidPdqmPatient() {
        var patient = new PdqmPatient();
        patient.addIdentifier().setSystem("urn:oid:1.3.6.1.4.1.21367.13.20.1000").setValue("IHERED-994");
        patient.addName().setFamily("Doe").addGiven("John");
        assertValid(patient);
    }

    /**
     * A Patient that claims no PIXm or PDQm conformance is still accepted, as base FHIR Patient.
     */
    @Test
    public void testUntaggedPatientWithoutName() {
        var patient = new Patient();
        patient.addIdentifier().setSystem("urn:oid:1.3.6.1.4.1.21367.13.20.1000").setValue("IHERED-994");
        patient.setGender(Enumerations.AdministrativeGender.MALE);
        assertValid(patient);
    }

    @Test
    public void testUntaggedPatientViolatingBaseProfile() {
        var patient = new Patient();
        patient.addIdentifier().setSystem("urn:oid:1.3.6.1.4.1.21367.13.20.1000").setValue("IHERED-994");
        // Patient.link.other is mandatory
        patient.addLink().setType(Patient.LinkType.REPLACEDBY);
        assertInvalid(patient);
    }

    @Test
    public void testPatientClaimingUnknownProfile() {
        var patient = new Patient();
        patient.getMeta().addProfile("http://example.org/StructureDefinition/unknown");
        patient.addIdentifier().setSystem("urn:oid:1.3.6.1.4.1.21367.13.20.1000").setValue("IHERED-994");
        patient.addName().setFamily("Doe");
        assertInvalid(patient);
    }

    private static PixmPatient pixmPatient() {
        var patient = new PixmPatient();
        patient.addIdentifier().setSystem("urn:oid:1.3.6.1.4.1.21367.13.20.1000").setValue("IHERED-994");
        patient.addName().setFamily("Doe").addGiven("John");
        patient.setGender(Enumerations.AdministrativeGender.MALE);
        return patient;
    }

    private static void assertValid(Patient patient) {
        try {
            var outcome = validator.validateRequest(patient, parameters);
            assertTrue(((OperationOutcome) outcome).getIssue().stream()
                .noneMatch(issue -> issue.getSeverity().ordinal() <= OperationOutcome.IssueSeverity.ERROR.ordinal()));
        } catch (UnprocessableEntityException e) {
            var issues = ((OperationOutcome) e.getOperationOutcome()).getIssue().stream()
                .map(issue -> issue.getSeverity() + " " + issue.getLocation() + ": " + issue.getDiagnostics())
                .toList();
            throw new AssertionError(String.join("\n", issues), e);
        }
    }

    private static void assertInvalid(Patient patient) {
        assertThrows(UnprocessableEntityException.class, () -> validator.validateRequest(patient, parameters));
    }
}
