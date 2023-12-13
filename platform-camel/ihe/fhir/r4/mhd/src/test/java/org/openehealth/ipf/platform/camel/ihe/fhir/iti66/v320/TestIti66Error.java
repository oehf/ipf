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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti66.v320;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 */
public class TestIti66Error extends AbstractTestIti66 {

    private static final String CONTEXT_DESCRIPTOR = "v320/iti-66-error.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testSendManuallyReturningError() {
        assertThrows(InternalErrorException.class, ()-> {
            try {
                sendManually(manifestPatientIdentifierParameter(), statusParameter());
            } catch (InternalErrorException e) {
                assertAndRethrow(e, OperationOutcome.IssueType.PROCESSING);
            }
        });
    }

}
