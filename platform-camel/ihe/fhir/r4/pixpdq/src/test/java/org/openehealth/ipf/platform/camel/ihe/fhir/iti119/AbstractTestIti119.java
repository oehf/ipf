/*
 * Copyright 2024 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti119;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.iti119.Iti119Constants;
import org.openehealth.ipf.commons.ihe.fhir.iti119.MatchGradeEnumInterceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

/**
 *
 */
abstract class AbstractTestIti119 extends FhirTestContainer {

    public static void startServer(String contextDescriptor, boolean secure) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        servlet.registerInterceptor(new MatchGradeEnumInterceptor());
        startServer(servlet, contextDescriptor, secure, FhirTestContainer.DEMO_APP_PORT, "FhirServlet");
    }

    public static void startClient() {
        startClient(String.format("http://localhost:%d/", FhirTestContainer.DEMO_APP_PORT));
    }

    protected Bundle sendManually(Parameters requestData) {
        return client.operation()
            .onType(Patient.class)
            .named(Iti119Constants.PDQM_MATCH_OPERATION_NAME)
            .withParameters(requestData)
            .returnResourceType(Bundle.class)
            .execute();
    }

    protected Bundle sendViaProducer(Parameters requestData) {
        return producerTemplate.requestBody("direct:input", requestData, Bundle.class);
    }

    protected Bundle sendViaProducer(Patient patient) {
        return producerTemplate.requestBody("direct:input", patient, Bundle.class);
    }


}
