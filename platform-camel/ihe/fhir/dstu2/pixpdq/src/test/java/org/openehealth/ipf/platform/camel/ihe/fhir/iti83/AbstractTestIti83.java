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

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.Parameters;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.UriType;
import org.openehealth.ipf.commons.ihe.core.atna.MockedSender;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.iti83.Iti83Constants;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
abstract class AbstractTestIti83 extends FhirTestContainer {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTestIti83.class);

    public static void startServer(String contextDescriptor) {
        IpfFhirServlet servlet = new IpfFhirServlet(FhirVersionEnum.DSTU2_HL7ORG);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, new MockedSender(), "FhirServlet");
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT));
    }

    protected Parameters validQueryParameters() {
        Parameters inParams = new Parameters();
        inParams.addParameter()
                .setName(Constants.SOURCE_IDENTIFIER_NAME)
                .setValue(new StringType("urn:oid:1.2.3.4|0815"));
        inParams.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(new UriType("urn:oid:1.2.3.4.6"));
        return inParams;
    }

    protected Parameters validReadParameters() {
        Parameters inParams = new Parameters();
        inParams.addParameter()
                .setName(Constants.SOURCE_IDENTIFIER_NAME)
                .setValue(new StringType("|0815"));
        inParams.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(new UriType("urn:oid:1.2.3.4.6"));
        return inParams;
    }

    protected Parameters validTargetSystemParameters() {
        Parameters inParams = new Parameters();
        inParams.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(new UriType("urn:oid:1.2.3.4.6"));
        return inParams;
    }

    protected Parameters sendManuallyOnType(Parameters queryParameters) {
        Parameters result = client.operation()
                .onType(Patient.class)
                .named(Iti83Constants.PIXM_OPERATION_NAME)
                .withParameters(queryParameters)
                .useHttpGet()
                .execute();
        return result;
    }

    protected Parameters sendManuallyOnInstance(String resourceId, Parameters queryParameters) {
        Parameters result = client.operation()
                .onInstance(new IdType("Patient", resourceId))
                .named(Iti83Constants.PIXM_OPERATION_NAME)
                .withParameters(queryParameters)
                .useHttpGet()
                .execute();
        return result;
    }

}
