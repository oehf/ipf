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
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.iti83.Iti83Constants;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmQueryParametersIn;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmProfile;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base test class for ITI-83 (PIX Query for Mobile) tests
 */
abstract class AbstractTestIti83 extends FhirTestContainer {

    private static final Logger log = LoggerFactory.getLogger(AbstractTestIti83.class);

    public static void startServer(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");
        PixmProfile.registerDefaultTypes(serverFhirContext);
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT));
    }

    protected PixmQueryParametersIn validQueryParameters() {
        var identifier = new Identifier()
                .setSystem("urn:oid:1.2.3.4")
                .setValue("0815");
        
        return new PixmQueryParametersIn()
                .setSourceIdentifier(identifier)
                .addTargetSystem("urn:oid:1.2.3.4.6");
    }

    protected PixmQueryParametersIn validReadParameters() {
        var identifier = new Identifier()
                .setValue("0815");
        
        return new PixmQueryParametersIn()
                .setSourceIdentifier(identifier)
                .addTargetSystem("urn:oid:1.2.3.4.6");
    }

    protected PixmQueryParametersIn validTargetSystemParameters() {
        return new PixmQueryParametersIn()
                .addTargetSystem("urn:oid:1.2.3.4.6");
    }

    protected Parameters sendManuallyOnType(PixmQueryParametersIn queryParameters) {
        return client.operation()
                .onType(Patient.class)
                .named(Iti83Constants.PIXM_OPERATION_NAME)
                .withParameters(queryParameters)
                .useHttpGet()
                .encodedXml()
                .returnResourceType(Parameters.class)
                .execute();
    }

    protected Parameters sendManuallyOnInstance(String resourceId, PixmQueryParametersIn queryParameters) {
        return client.operation()
                .onInstance(new IdType("Patient", resourceId))
                .named(Iti83Constants.PIXM_OPERATION_NAME)
                .withParameters(queryParameters)
                .useHttpGet()
                .encodedXml()
                .returnResourceType(Parameters.class)
                .execute();
    }

    protected Parameters sendViaProducer(PixmQueryParametersIn requestData) {
        return producerTemplate.requestBody("direct:input", requestData, Parameters.class);
    }

}
