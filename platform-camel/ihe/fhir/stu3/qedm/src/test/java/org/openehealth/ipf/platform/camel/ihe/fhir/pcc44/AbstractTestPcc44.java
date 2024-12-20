/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.pcc44;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Observation;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
abstract class AbstractTestPcc44 extends FhirTestContainer {

    private static final Logger log = LoggerFactory.getLogger(AbstractTestPcc44.class);

    public static void startServer(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.DSTU3);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT));
    }

    protected ICriterion<?> observationPatientReferenceParameter() {
        return new ReferenceClientParam("patient").hasId("http://fhirserver.org/Patient/1");
    }

    protected Bundle sendManually(ICriterion<?> requestData) {
        return client.search()
                .forResource(Observation.class)
                .where(requestData)
                .returnBundle(Bundle.class)
                .encodedXml()
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
