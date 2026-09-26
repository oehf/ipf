/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti104;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmProfile;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmPatient;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.util.Map;

import static org.hl7.fhir.r4.model.Enumerations.AdministrativeGender.MALE;

/**
 * Base test class for ITI-104 (Patient Identity Feed FHIR) tests
 */
abstract class AbstractTestIti104 extends FhirTestContainer {

    protected static final String SYSTEM = "urn:oid:1.3.6.1.4.1.21367.13.20.1000";
    protected static final String EXISTING_PATIENT = "IHERED-994";
    protected static final String NEW_PATIENT = Iti104TestRouteBuilder.NEW_PATIENT_PREFIX + "IHERED-995";

    public static void startServer(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");
        PixmProfile.registerDefaultTypes(serverFhirContext);
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT));
    }

    protected static Identifier identifier(String value) {
        return new Identifier().setSystem(SYSTEM).setValue(value);
    }

    protected static PixmPatient pixmPatient(String value) {
        var patient = new PixmPatient();
        patient.addIdentifier(identifier(value));
        patient.addName().setFamily("Moore").addGiven("Chip");
        patient.setGender(MALE);
        return patient;
    }

    protected MethodOutcome sendConditionalUpdate(Patient patient, String value) {
        return client.update()
            .resource(patient)
            .conditional()
            .where(Patient.IDENTIFIER.exactly().systemAndIdentifier(SYSTEM, value))
            .execute();
    }

    protected MethodOutcome sendConditionalDelete(String value) {
        return client.delete()
            .resourceConditionalByType(Patient.class)
            .where(Patient.IDENTIFIER.exactly().systemAndIdentifier(SYSTEM, value))
            .execute();
    }

    protected MethodOutcome sendViaProducer(Object body, Map<String, Object> headers) {
        return producerTemplate.requestBodyAndHeaders("direct:input", body, headers, MethodOutcome.class);
    }
}
