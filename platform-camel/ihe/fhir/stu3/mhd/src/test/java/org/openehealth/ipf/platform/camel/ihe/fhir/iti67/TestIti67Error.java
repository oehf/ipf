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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti67;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class TestIti67Error extends AbstractTestIti67 {

    private static final String CONTEXT_DESCRIPTOR = "iti-67-error.xml";

    @BeforeClass
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test(expected = InternalErrorException.class)
    public void testSendManuallyReturningError() {
        try {
            sendManually(referencePatientIdentifierParameter());
        } catch (InternalErrorException e) {
            assertAndRethrow(e, OperationOutcome.IssueType.PROCESSING);
        }

    }

}
