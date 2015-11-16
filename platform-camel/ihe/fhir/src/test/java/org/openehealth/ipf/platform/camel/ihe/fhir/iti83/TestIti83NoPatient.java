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

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.CamelFhirServlet;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;

import javax.servlet.ServletException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TestIti83NoPatient extends FhirTestContainer {

    private static final String CONTEXT_DESCRIPTOR = "iti-83-no-patient.xml";
    private static final Logger LOG = LoggerFactory.getLogger(TestIti83NoPatient.class);

    @BeforeClass
    public static void setUpClass() throws ServletException {
        CamelFhirServlet servlet = new CamelFhirServlet();
        startServer(servlet, CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT, "FhirServlet");
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT));
    }

    @Test
    public void testSendManualPixm() {
        Parameters inParams = new Parameters();
        inParams.addParameter()
                .setName(Constants.SOURCE_IDENTIFIER_NAME)
                .setValue(new StringType("urn:oid:1.2.3.4|0815"));
        inParams.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(new UriType("urn:oid:1.2.3.4.6"));

        try {
            client.operation()
                    .onType(Patient.class)
                    .named(Constants.PIXM_OPERATION_NAME)
                    .withParameters(inParams)
                    .useHttpGet()
                    .encodedXml()
                    .execute();
        } catch (InvalidRequestException e) {
            assertEquals(400, e.getStatusCode());
            // Hmm, I wonder if this could not be done automatically...
            OperationOutcome oo = context.newXmlParser().parseResource(OperationOutcome.class, e.getResponseBody());
            assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
            assertEquals(OperationOutcome.IssueType.VALUE, oo.getIssue().get(0).getCode());
        }

    }


}
