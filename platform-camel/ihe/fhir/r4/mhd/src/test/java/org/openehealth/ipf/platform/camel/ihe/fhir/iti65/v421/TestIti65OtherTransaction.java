/*
 * Copyright 2019 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti65.v421;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test for sending a different transaction request that is handled by
 * a different route.
 */
public class TestIti65OtherTransaction extends AbstractTestIti65 {

    private static final String CONTEXT_DESCRIPTOR = "iti-65.xml";

    @BeforeAll
    public static void setUpClass() {
        setup(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testSendManuallyReturningError() {
        try {
            sendManually(thisSucks());
            fail("expected UnprocessableEntityException");
        } catch (UnprocessableEntityException e) {
            assertTrue(e.getMessage().endsWith("This sucks"));
        }

    }

}
