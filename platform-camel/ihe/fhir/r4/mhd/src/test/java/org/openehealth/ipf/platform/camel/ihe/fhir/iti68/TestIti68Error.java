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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti68;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class TestIti68Error extends AbstractTestIti68 {

    private static final String CONTEXT_DESCRIPTOR = "iti-68-error.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testRetrieveDocument() {
        try {
            producerTemplate.requestBody("direct:input", null, byte[].class);
            fail();
        } catch (CamelExecutionException e) {
            assertTrue(e.getCause() instanceof HttpOperationFailedException);

            // Check ATNA Audit
            var sender = getAuditSender();
            assertEquals(1, sender.getMessages().size());
            var event = sender.getMessages().get(0);
            assertEquals(
                    EventOutcomeIndicator.MajorFailure,
                    event.getEventIdentification().getEventOutcomeIndicator());
        }
    }


}
