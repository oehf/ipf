/*
 * Copyright 2022 the original author or authors.
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

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class TestPcc44SecureSuccess extends AbstractTestPcc44 {

    private static final String CONTEXT_DESCRIPTOR = "pcc-44-secure.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR, true);
    }

    @Test
    public void testSendSecureCamelPcc44Apache() {
        test("direct:apache");
    }

    @Test
    public void testSendSecureCamelPcc44Methanol() {
        test("direct:methanol");
    }

    public void test(String endpoint) {
        var query = observationPatientReferenceParameter();
        var result = producerTemplate.requestBodyAndHeader(endpoint, query, Constants.FHIR_RESOURCE_TYPE_HEADER, "Observation", Bundle.class);

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());
    }
}
