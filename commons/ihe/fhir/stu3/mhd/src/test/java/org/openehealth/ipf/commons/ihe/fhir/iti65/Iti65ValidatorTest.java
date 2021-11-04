/*
 * Copyright 2020 the original author or authors.
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
import org.hl7.fhir.dstu3.model.*;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;

/**
 * @author Christian Ohr
 */
public class Iti65ValidatorTest {

    private static final String BINARY_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bb";
    private static final String REFERENCE_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bc";
    private static final String MANIFEST_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bd";

    @Test
    public void validate() throws Exception {
        var iti65Validator = new Iti65Validator(FhirContext.forDstu3());
        iti65Validator.validateRequest(provideAndRegister(), Collections.emptyMap());
    }

    protected Bundle provideAndRegister() throws Exception {
        var bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        bundle.getMeta().addProfile(Iti65Constants.ITI65_PROFILE);

        // Manifest

        var manifest = new DocumentManifest();
        manifest.setStatus(Enumerations.DocumentReferenceStatus.CURRENT)
                .setCreated(new Date())
                .setDescription("description")
                .setSource("source")
                .setSubject(new Reference("Patient/a2"))
                .setId("id");
        manifest.addContent()
                .setP(new Reference(REFERENCE_FULL_URL));
        bundle.addEntry()
                .setFullUrl(MANIFEST_FULL_URL)
                .setRequest(
                        new Bundle.BundleEntryRequestComponent()
                                .setMethod(Bundle.HTTPVerb.POST)
                                .setUrl("DocumentManifest"))
                .setResource(manifest);

        // Reference

        var documentContent = "YXNkYXNkYXNkYXNkYXNk".getBytes();

        var instant = ZonedDateTime.of(
                LocalDate.of(2013, 7, 1),
                LocalTime.of(13, 11, 13),
                ZoneId.of("UTC")
        ).toInstant();

        var timestamp = Date.from(instant);

        var reference = new DocumentReference();
        reference.getMeta().setLastUpdated(timestamp);

        reference.setMasterIdentifier(
                new Identifier()
                        .setSystem("urn:ietf:rfc:3986")
                        .setValue("urn:oid:129.6.58.92.88336"))
                .setIndexed(timestamp) // creation of document reference resource
                .setDescription("Physical")
                .setSubject(new Reference("Patient/a2"))
                .addAuthor(new Reference("Practitioner/a3"))
                .addAuthor(new Reference("Practitioner/a4"))
                .setStatus(Enumerations.DocumentReferenceStatus.CURRENT);

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

        var binary = new Binary()
                .setContentType("text/plain")
                .setContent(documentContent);
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
