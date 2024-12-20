package org.openehealth.ipf.commons.ihe.fhir.iti105;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.URN_IETF_RFC_3986;

public class Iti105ValidatorTest {

    private static final Logger log = LoggerFactory.getLogger(Iti105ValidatorTest.class);

    static Iti105Validator iti105Validator;

    @BeforeAll
    static void beforeAll() {
        var context = FhirContext.forR4();
        MhdProfile.registerDefaultTypes(context);
        iti105Validator = new Iti105Validator(context);
    }

    @Test
    void testValidConformance() {
        assertDoesNotThrow(() -> iti105Validator.validateRequest(validDocumentreference(), new HashMap<>()));
    }

    @Test
    void testInvalidConformance() throws Exception {
        var documentReference = invalidDocumentReference();
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () ->
            iti105Validator.validateRequest(documentReference, new HashMap<>())
        );
        assertNotNull(exception);
        var oo = (OperationOutcome) exception.getOperationOutcome();
        oo.getIssue().forEach(ooc -> log.error("{} : {}", ooc.getSeverity().getDisplay(), ooc.getDiagnostics()));
    }

    private static DocumentReference validDocumentreference() throws NoSuchAlgorithmException {
        var reference = invalidDocumentReference();
        reference.getMeta().addProfile(MhdProfile.SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE);
        return reference;
    }

    private static DocumentReference invalidDocumentReference() throws NoSuchAlgorithmException {
        var documentContent = "Hello IHE World".getBytes();
        var practitioner = new Practitioner();
        var reference = new DocumentReference();
        var timestamp = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        reference.getMeta().setLastUpdated(timestamp);

        reference
            .setMasterIdentifier(
                new Identifier()
                    .setSystem(URN_IETF_RFC_3986)
                    .setValue("urn:oid:129.6.58.92.88336"))
            .addIdentifier(new Identifier()
                .setSystem(URN_IETF_RFC_3986)
                .setValue("urn:oid:129.6.58.92.88336"))
            .setDate(timestamp) // creation of document reference resource
            .setDescription("Physical")
            .setSubject(new Reference("http://server/Patient/a2"))
            .addAuthor(new Reference(practitioner))
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
                    .setLanguage("en/us")
                    .setSize(documentContent.length)
                    .setHash(MessageDigest.getInstance("SHA-1").digest(documentContent))
                    .setUrl("urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bb"))
            .setFormat(new Coding("urn:oid:1.3.6.1.4.1.19376.1.2.3", "urn:ihe:pcc:handp:2008", null));
        return reference;
    }
}
