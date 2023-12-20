/*
 * Copyright 2019 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.mhd;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.*;
import org.ietf.jgss.Oid;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdValidator;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveDocumentReference;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveProvideDocumentBundle;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveSubmissionSetList;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.Source;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.URN_IETF_RFC_3986;

/**
 * @author Christian Ohr
 */
public class MhdValidatorTest {

    private static final String BINARY_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bb";
    private static final String REFERENCE_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bc";
    private static final String MANIFEST_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bd";

    @Test
    public void testBundle() throws Exception {
        var context = FhirContext.forR4();
        MhdProfile.registerDefaultTypes(context);
        var bundle = provideAndRegister();
        try {
            var iti65Validator = new MhdValidator(context);
            iti65Validator.validateRequest(bundle, Collections.emptyMap());
        } catch (UnprocessableEntityException e) {
            var oo = (OperationOutcome) e.getOperationOutcome();
            oo.getIssue()
                    .forEach(ooc -> System.out.println(ooc.getSeverity().getDisplay() + " : " + ooc.getDiagnostics()));
        }
    }

    private Bundle provideAndRegister() throws Exception {

        var practitioner = new Practitioner();
        practitioner.getText().setStatus(Narrative.NarrativeStatus.EMPTY);
        practitioner.getText().setDivAsString("<div>empty</div>");

        var sourcePatient = new Patient();
        sourcePatient.getText().setStatus(Narrative.NarrativeStatus.EMPTY);
        sourcePatient.getText().setDivAsString("<div>empty</div>");

        var submissionSetList = new ComprehensiveSubmissionSetList();
        submissionSetList
            .linkDocumentReference(REFERENCE_FULL_URL)
            .setSubmissionSetUniqueIdIdentifier(new Oid("1.2.58.92.23"))
            .setEntryUuidIdentifier(UUID.randomUUID())
            .setSourceId(new Oid("1.2.58.92.24"))
            .setDesignationType(new CodeableConcept(
                new Coding("http://snomed.info/sct", "225728007", "")
            ))
            .addIntendedRecipient(new Reference(practitioner))
            .setSubject(new Reference("Patient/a2"))
            .setTitle("description")
            .setSource(new Source().setAuthorOrg(new Reference("Organization/4711")).setReference("Practitioner/1234"));
        submissionSetList.getText().setStatus(Narrative.NarrativeStatus.EMPTY);
        submissionSetList.getText().setDivAsString("<div>empty</div>");

        var documentContent = "YXNkYXNkYXNkYXNkYXNk".getBytes();
        var documentReference = new ComprehensiveDocumentReference();
        documentReference
            .setUniqueIdIdentifier(URN_IETF_RFC_3986, "urn:oid:129.6.58.92.88336")
            .setEntryUuidIdentifier(UUID.randomUUID())
            .addAuthor(practitioner)
            .addCategory(new CodeableConcept(
                new Coding("http://loinc.org", "11369-6", "History of Immunization Narrative")))
            .addSecurityLabel(new CodeableConcept(
                new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "N", "normal")
            ))
            .setContext(new DocumentReference.DocumentReferenceContextComponent()
                .addEvent(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "PATDOC", "PATDOC")))
                .setPracticeSetting(new CodeableConcept(
                    new Coding("http://snomed.info/sct", "408467006", "Adult mental illness")))
                .setFacilityType(new CodeableConcept(
                    new Coding("http://snomed.info/sct", "82242000", "Hospital-children's")))
                .setSourcePatientInfo(new Reference(sourcePatient))
            )
            .setDescription("Physical")
            .setSubject(new Reference("Patient/a2"));
        documentReference.getType().addCoding()
            .setSystem("http://ihe.net/connectathon/classCodes")
            .setCode("History and Physical")
            .setDisplay("History and Physical");
        documentReference.addContent()
            .setAttachment(
                new Attachment()
                    .setCreation(new Date())
                    .setContentType("text/plain")
                    .setLanguage("en/us")
                    .setSize(documentContent.length)
                    .setHash(MessageDigest.getInstance("SHA-1").digest(documentContent))
                    .setUrl(BINARY_FULL_URL))
            .setFormat(new Coding("http://ihe.net/fhir/ihe.formatcode.fhir/CodeSystem/formatcode", "urn:ihe:iti:xds-sd:text:2008", "ITI XDS-SD TEXT"));
        documentReference.getText().setStatus(Narrative.NarrativeStatus.EMPTY);
        documentReference.getText().setDivAsString("<div>empty</div>");

        // Binary

        var binary = new Binary().setContentType("text/plain");
        binary.setContent(documentContent);

        return new ComprehensiveProvideDocumentBundle()
            .addSubmissionSetList(MANIFEST_FULL_URL, submissionSetList)
            .addDocumentReference(REFERENCE_FULL_URL, documentReference)
            .addBinary(BINARY_FULL_URL, binary);
    }
}
