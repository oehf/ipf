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

package org.openehealth.ipf.commons.ihe.fhir.iti65;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;

import static org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65Constants.ITI65_MINIMAL_DOCUMENT_MANIFEST_PROFILE;
import static org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65Constants.ITI65_MINIMAL_DOCUMENT_REFERENCE_PROFILE;

/**
 * @author Christian Ohr
 */
public class Iti65ValidatorTest {

    private static final String BINARY_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bb";
    private static final String REFERENCE_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bc";
    private static final String MANIFEST_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bd";

    @Test
    public void testBundle() throws Exception {
        FhirContext context = FhirContext.forR4();
        Bundle bundle = provideAndRegister();
        try {
            Iti65Validator iti65Validator = new Iti65Validator(context);
            iti65Validator.initialize(context);
            iti65Validator.validateRequest(bundle, Collections.emptyMap());
        } catch (UnprocessableEntityException e) {
            OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
            oo.getIssue()
                    .forEach(ooc -> System.out.println(ooc.getSeverity().getDisplay() + " : " + ooc.getDiagnostics()));
        }
    }

    private Bundle provideAndRegister() throws Exception {
        Bundle bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        bundle.getMeta().addProfile(Iti65Constants.ITI65_MINIMAL_METADATA_PROFILE);

        // Manifest

        DocumentManifest manifest = new DocumentManifest();
        manifest.getMeta().addProfile(ITI65_MINIMAL_DOCUMENT_MANIFEST_PROFILE);
        manifest.setMasterIdentifier(
                new Identifier()
                        .setSystem("urn:ietf:rfc:3986")
                        .setValue("urn:oid:129.6.58.92.88336"))
                .addIdentifier(new Identifier()
                        .setSystem("urn:ietf:rfc:3986")
                        .setValue("urn:oid:129.6.58.92.88336"))
                .setStatus(Enumerations.DocumentReferenceStatus.CURRENT)
                .setDescription("description")
                .setSource("source")
                .setSubject(new Reference("http://server/Patient/a2"))
                .setId("id");
        manifest.getText().setStatus(Narrative.NarrativeStatus.EMPTY);
        manifest.getText().setDivAsString("<div>empty</div>");
        manifest.addContent().setReference(REFERENCE_FULL_URL);
        bundle.addEntry()
                .setFullUrl(MANIFEST_FULL_URL)
                .setRequest(
                        new Bundle.BundleEntryRequestComponent()
                                .setMethod(Bundle.HTTPVerb.POST)
                                .setUrl("DocumentManifest"))
                .setResource(manifest);

        // Reference

        byte[] documentContent = "YXNkYXNkYXNkYXNkYXNk".getBytes();

        Date timestamp = new DateTime()
                .withDate(2013, 7, 1)
                .withTime(13, 11, 33, 0)
                .withZone(DateTimeZone.UTC).toDate();

        Practitioner practitioner = new Practitioner();
        DocumentReference reference = new DocumentReference();
        reference.getMeta().addProfile(ITI65_MINIMAL_DOCUMENT_REFERENCE_PROFILE);
        reference.getMeta().setLastUpdated(timestamp);

        reference
                .setMasterIdentifier(
                        new Identifier()
                                .setSystem("urn:ietf:rfc:3986")
                                .setValue("urn:oid:129.6.58.92.88336"))
                .addIdentifier(new Identifier()
                        .setSystem("urn:ietf:rfc:3986")
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
                                .setUrl(BINARY_FULL_URL))
                .setFormat(new Coding("urn:oid:1.3.6.1.4.1.19376.1.2.3", "urn:ihe:pcc:handp:2008", null));
        bundle.addEntry()
                .setFullUrl(REFERENCE_FULL_URL)
                .setRequest(
                        new Bundle.BundleEntryRequestComponent()
                                .setMethod(Bundle.HTTPVerb.POST)
                                .setUrl("DocumentReference"))
                .setResource(reference);

        // Binary

        Binary binary = new Binary().setContentType("text/plain");
        binary.setContent(documentContent);
        binary.getMeta().setLastUpdated(timestamp);
        bundle.addEntry()
                .setFullUrl(BINARY_FULL_URL)
                .setRequest(new Bundle.BundleEntryRequestComponent()
                        .setMethod(Bundle.HTTPVerb.POST)
                        .setUrl("Binary"))
                .setResource(binary);

        return bundle;
    }
}
