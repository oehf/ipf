/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.iti65;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65Constants;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.time.*;
import java.util.Date;

/**
 *
 */
abstract class AbstractTestIti65 extends FhirTestContainer {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTestIti65.class);

    private static final String BINARY_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bb";
    private static final String REFERENCE_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bc";
    private static final String MANIFEST_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bd";

    public static void startServer(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.DSTU3);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT));
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
                .setCreated(timestamp)
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

    protected Bundle thisSucks() {
        var bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        bundle.getMeta().addProfile("http://thissucks.com");
        return bundle;
    }

    protected Bundle sendManually(Bundle bundle) {
        return client.transaction().withBundle(bundle).encodedXml().execute();
    }

    @Override
    protected void printAsXML(IBaseResource resource) {
        LOG.info(context.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource));
    }


}
