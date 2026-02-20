package org.openehealth.ipf.commons.ihe.fhir.iti105;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdValidator;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.INTERACTION_REQUEST_VALIDATION_PROFILES;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.URN_IETF_RFC_3986;

public class Iti105ValidatorTest {

    private static final Logger log = LoggerFactory.getLogger(Iti105ValidatorTest.class);

    private FhirContext context;
    private MhdValidator iti105Validator;

    @BeforeEach
    void setUp() {
        context = FhirContext.forR4();
        MhdProfile.registerDefaultTypes(context);
        iti105Validator = new MhdValidator(context);
    }

    @Test
    void testValidConformance() throws Exception {
        try {
            var resource = validDocumentReference();
            log.warn(context.newJsonParser().setPrettyPrint(true).encodeResourceToString(resource));
            iti105Validator.validateRequest(validDocumentReference(), Map.of(
                INTERACTION_REQUEST_VALIDATION_PROFILES, Set.of(MhdProfile.SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE)
            ));
        } catch (UnprocessableEntityException e) {
            var issues = ((OperationOutcome) e.getOperationOutcome()).getIssue();
            issues.forEach(issue -> FhirUtils.logValidationMessage(log, issue));
            assertFalse(issues.stream()
                    .anyMatch(i -> i.getSeverity() == OperationOutcome.IssueSeverity.ERROR),
                "There are validation errors in the bundle");
        }
    }

    @Test
    void testInvalidConformance() throws Exception {
        var documentReference = invalidDocumentReference();
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () ->
            iti105Validator.validateRequest(documentReference, Map.of(
                INTERACTION_REQUEST_VALIDATION_PROFILES, Set.of(MhdProfile.SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE)
            ))
        );
        assertNotNull(exception);
        var oo = (OperationOutcome) exception.getOperationOutcome();
        oo.getIssue().forEach(ooc -> log.error("{} : {}", ooc.getSeverity().getDisplay(), ooc.getDiagnostics()));
    }

    private static DocumentReference validDocumentReference() throws NoSuchAlgorithmException {
        var reference = invalidDocumentReference();
        reference.getMeta().addProfile(MhdProfile.SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE);
        return reference;
    }

    private static DocumentReference invalidDocumentReference() throws NoSuchAlgorithmException {
        var documentContent = "Hello IHE World".getBytes();
        var reference = new DocumentReference();
        var timestamp = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        reference.getMeta().setLastUpdated(timestamp);

        reference
            .setMasterIdentifier(
                new Identifier()
                    .setSystem(URN_IETF_RFC_3986)
                    .setValue("urn:oid:129.6.58.92.88336"))
            .setDate(timestamp) // creation of document reference resource
            .setDescription("Physical")
            .setSubject(new Reference("http://server/Patient/a2"))
            .addAuthor(new Reference(new Practitioner().addName(new HumanName().setFamily("Smith").addGiven("John"))))
            .setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
        reference.getText().setStatus(Narrative.NarrativeStatus.EMPTY);
        reference.getText().setDivAsString("<div>empty</div>");
        reference.getType().addCoding()
            .setSystem("http://ihe.net/connectathon/classCodes")
            .setCode("History and Physical")
            .setDisplay("History and Physical");
        reference.addContent()
            .setAttachment(
                new Attachment()
                    .setContentType("text/plain")
                    .setLanguage("en-US")
                    .setSize(documentContent.length)
                    .setHash(MessageDigest.getInstance("SHA-1").digest(documentContent))
                    .setData(documentContent))
            .setFormat(new Coding("urn:oid:1.3.6.1.4.1.19376.1.2.3", "urn:ihe:pcc:handp:2008", null));
        return reference;
    }
}
