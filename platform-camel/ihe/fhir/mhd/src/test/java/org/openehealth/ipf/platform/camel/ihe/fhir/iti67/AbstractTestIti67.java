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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti67;

import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.DocumentManifest;
import org.hl7.fhir.instance.model.DocumentReference;
import org.openehealth.ipf.commons.ihe.core.atna.MockedSender;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

/**
 *
 */
abstract class AbstractTestIti67 extends FhirTestContainer {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTestIti67.class);

    public static void startServer(String contextDescriptor) throws ServletException {
        IpfFhirServlet servlet = new IpfFhirServlet();
        startServer(servlet, contextDescriptor, false, FhirTestContainer.DEMO_APP_PORT, new MockedSender(), "FhirServlet");
        startClient(String.format("http://localhost:%d/", FhirTestContainer.DEMO_APP_PORT));
    }

    protected ICriterion<?> referenceParameters() {
        return new TokenClientParam("patient.identifier").exactly()
                .systemAndIdentifier("urn:oid:2.16.840.1.113883.3.37.4.1.1.2.1.1", "1");
    }

    protected Bundle sendManually(ICriterion<?> requestData) {
        return client.search()
                .forResource(DocumentReference.class)
                .where(requestData)
                .returnBundle(Bundle.class)
                .execute();
    }

    protected Bundle nextPage(Bundle bundle) {
        return client.loadPage()
                .next(bundle)
                .execute();
    }

    protected Bundle previousPage(Bundle bundle) {
        return client.loadPage()
                .previous(bundle)
                .execute();
    }


}
