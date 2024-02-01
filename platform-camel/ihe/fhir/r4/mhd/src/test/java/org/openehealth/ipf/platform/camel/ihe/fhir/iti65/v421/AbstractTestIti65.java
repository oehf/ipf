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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti65.v421;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.apache.camel.Exchange;
import org.apache.camel.component.http.HttpConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.ietf.jgss.Oid;
import org.openehealth.ipf.commons.core.URN;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareMethanolRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.audit.auth.BalpJwtGenerator;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveDocumentReference;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveProvideDocumentBundle;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.ComprehensiveSubmissionSetList;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.Source;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpHeaders;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 *
 */
abstract class AbstractTestIti65 extends FhirTestContainer {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTestIti65.class);

    private static final String BINARY_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bb";
    private static final String REFERENCE_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bc";
    private static final String MANIFEST_FULL_URL = "urn:uuid:8da1cfcc-05db-4aca-86ad-82aa756a64bd";

    public static void setup(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");
        MhdProfile.registerDefaultTypes(serverFhirContext);
        servlet.setFhirContext(serverFhirContext);

        var loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setLogRequestSummary(false);
        loggingInterceptor.setLogRequestBody(true);
        loggingInterceptor.setLogResponseBody(true);
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT), fhirContext -> {
            var clientFactory = new SslAwareMethanolRestfulClientFactory(fhirContext);
            clientFactory.setAsync(true);
            fhirContext.setRestfulClientFactory(clientFactory);
        }).registerInterceptor(loggingInterceptor);
    }

    protected Bundle provideAndRegister() throws Exception {
        var bundle = new ComprehensiveProvideDocumentBundle();

        // SubmissionSet

        var manifest = new ComprehensiveSubmissionSetList();
        manifest
            .linkDocumentReference(REFERENCE_FULL_URL)
            .setSource(new Source().setAuthorOrg(new Reference("author")).setReference("source"))
            .setSubject(new Reference("Patient/a2"));
        bundle.addSubmissionSetList(MANIFEST_FULL_URL, manifest);

        // Reference

        var documentContent = "YXNkYXNkYXNkYXNkYXNk".getBytes();

        var instant = ZonedDateTime.of(
            LocalDate.of(2013, 7, 1),
            LocalTime.of(13, 11, 13),
            ZoneId.of("UTC")
        ).toInstant();

        var timestamp = Date.from(instant);

        var reference = new ComprehensiveDocumentReference();
        reference.getMeta().setLastUpdated(timestamp);
        reference
            .setUniqueIdIdentifier(
                Constants.URN_IETF_RFC_3986,
                new URN(new Oid("1.6.58.92.88336")).toString())
            .setEntryUuidIdentifier(UUID.randomUUID())
            .addAuthor(new Reference("Practitioner/a3"))
            .addAuthor(new Reference("Practitioner/a4"))
            .setDate(timestamp) // creation of document reference resource
            .setDescription("Physical")
            .setSubject(new Reference("Patient/a2"))
            .setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
        reference.getText()
            .setStatus(Narrative.NarrativeStatus.EMPTY)
            .setDivAsString("<div>empty</div>");
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

        bundle.addDocumentReference(REFERENCE_FULL_URL, reference);

        // Binary

        var binary = new Binary()
            .setContentType("text/plain");
        binary.setContent(documentContent);
        binary.getMeta().setLastUpdated(timestamp);
        bundle.addBinary(BINARY_FULL_URL, binary);

        return bundle;
    }

    protected Bundle thisSucks() {
        var bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        bundle.getMeta().addProfile("http://thissucks.com");
        return bundle;
    }

    protected Bundle sendManually(Bundle bundle) {
        return client.transaction().withBundle(bundle)
            .encodedXml().execute();
    }

    protected Bundle sendManuallyWithJwt(Bundle bundle) {
        var headerValue = "Bearer " + new BalpJwtGenerator().next();
        return client.transaction().withBundle(bundle)
            .withAdditionalHeader("Authorization", headerValue)
            .encodedXml().execute();
    }

    protected Bundle sendViaProducer(Bundle bundle) {
        return producerTemplate.requestBody("direct:input", bundle, Bundle.class);
    }

    protected Bundle sendViaProducerWithJwtAuthorization(Bundle bundle) {
        var headerValue = "Bearer " + new BalpJwtGenerator().next();
        return producerTemplate.requestBodyAndHeader("direct:input", bundle, "Authorization",
            headerValue, Bundle.class);
    }

    protected void printAsXML(IBaseResource resource) {
        LOG.info(clientFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource));
    }


}
