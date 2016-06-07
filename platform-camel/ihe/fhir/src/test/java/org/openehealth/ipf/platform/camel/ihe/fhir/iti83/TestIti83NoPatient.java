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

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.OperationOutcome;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;

/**
 *
 */
public class TestIti83NoPatient extends AbstractTestIti83 {

    private static final String CONTEXT_DESCRIPTOR = "iti-83-no-patient.xml";

    @BeforeClass
    public static void setUpClass() throws ServletException {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testSendManualPixm() {
        try {
            sendManually(validQueryParameters());
        } catch (ResourceNotFoundException e) {
            assertAndRethrowException(e, OperationOutcome.IssueType.NOTFOUND);
        }
    }

}
