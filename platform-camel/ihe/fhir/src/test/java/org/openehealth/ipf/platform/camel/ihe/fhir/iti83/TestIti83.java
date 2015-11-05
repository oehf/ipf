/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.CamelFhirServlet;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TestIti83 extends FhirTestContainer {

    private static final String CONTEXT_DESCRIPTOR = "iti-83.xml";
    private static final Logger LOG = LoggerFactory.getLogger(TestIti83.class);

    static final String SERVICE = String.format("pixm-iti83://localhost:%d/patient/", DEMO_APP_PORT);

    @BeforeClass
    public static void setUpClass() throws ServletException {
        CamelFhirServlet servlet = new CamelFhirServlet();
        startServer(servlet, CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT, "FhirServlet");
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT));
    }

    @Test
    public void testGetConformance() {
        Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();

        assertEquals(1, conf.getRest().size());
        Conformance.ConformanceRestComponent component = conf.getRest().iterator().next();
        Conformance.ConformanceRestOperationComponent operation = component.getOperation().iterator().next();
        assertEquals(Constants.PIXM_OPERATION_NAME, operation.getName());

        printAsXML(conf);
    }

    @Test
    public void testSendPixm() {
        Parameters inParams = new Parameters();
        inParams.addParameter()
                .setName(Constants.SOURCE_IDENTIFIER_NAME)
                .setValue(new StringType("urn:oid:1.2.3.4|0815"));
        inParams.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(new UriType("urn:oid:1.2.3.4.6"));

        Parameters result = client.operation()
                .onType(Patient.class)
                .named(Constants.PIXM_OPERATION_NAME)
                .withParameters(inParams)
                .useHttpGet()
                .execute();

        Parameters.ParametersParameterComponent parameter = result.getParameter().iterator().next();
        assertEquals(Iti9Responder.getRESULT_VALUE(), ((Identifier)parameter.getValue()).getValue());

        printAsXML(result);

    }

    private void printAsXML(IBaseResource resource) {
        LOG.info(context.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource));
    }

}
