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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78;

import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.audit.queue.AbstractMockedAuditMessageQueue;

import javax.servlet.ServletException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TestIti78SecureSuccess extends AbstractTestIti78 {

    private static final String CONTEXT_DESCRIPTOR = "iti-78-secure.xml";

    @BeforeClass
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR, true);
    }

    @Test
    public void testSendEndpointPdqmCriterion() {
        Bundle result = getProducerTemplate().requestBody("direct:input", familyParameters(), Bundle.class);
        // printAsXML(result);

        // Check ATNA Audit
        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }

    @Test
    public void testSendEndpointPdqmString() {
        Bundle result = getProducerTemplate().requestBody("direct:input", "Patient?family=Test", Bundle.class);
        // printAsXML(result);

        // Check ATNA Audit
        AbstractMockedAuditMessageQueue sender = getAuditSender();
        assertEquals(2, sender.getMessages().size());
    }




}
