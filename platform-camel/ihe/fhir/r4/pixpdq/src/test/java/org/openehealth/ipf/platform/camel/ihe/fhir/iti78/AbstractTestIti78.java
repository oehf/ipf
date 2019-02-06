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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.gclient.ICriterion;
import org.hl7.fhir.r4.model.Bundle;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqPatient;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
abstract class AbstractTestIti78 extends FhirTestContainer {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTestIti78.class);

    public static void startServer(String contextDescriptor, boolean secure) {
        IpfFhirServlet servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        startServer(servlet, contextDescriptor, secure, FhirTestContainer.DEMO_APP_PORT, "FhirServlet");
    }

    public static void startClient() {
        startClient(String.format("http://localhost:%d/", FhirTestContainer.DEMO_APP_PORT));
    }

    protected ICriterion<?> familyParameters() {
        return PdqPatient.FAMILY.matches().value("Test");
    }

    protected Bundle sendManually(ICriterion<?> requestData) {
        return client.search()
                .forResource(PdqPatient.class)
                .where(requestData)
                .returnBundle(Bundle.class)
                .execute();
    }

    protected Bundle sendManuallyWithCount(ICriterion<?> requestData, int count) {
        return client.search()
                .forResource(PdqPatient.class)
                .where(requestData)
                .count(count)
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
