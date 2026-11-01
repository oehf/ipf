/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v3.core;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.Hl7v3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRevisedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.Hl7v3Response;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PatientRecordTransformersTest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.XcpdQueryRequestTransformerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class Hl7v3MessageUtilsTest {

    @Test
    public void testRespondPixV3() {
        final var request = PixV3QueryRequestTransformerTest.getSampleQuery();
        final var response = Hl7v3MessageUtils.respond(request);
        assertResponseHeader(request, response);
        assertSame(request.getQueryPatientId(), response.getQueryPatientId());
        assertEquals("AE", response.getAcknowledgementTypeCode());
    }

    @Test
    public void testRespondPdqV3() {
        final var request = PdqV3QueryRequestTransformerTest.getSampleQuery();
        final var response = Hl7v3MessageUtils.respond(request);
        assertResponseHeader(request, response);
        assertEquals(request.getLivingSubjectIds(), response.getLivingSubjectIds());
        assertEquals(request.getOtherIDsScopingOrganizationOids(), response.getOtherIDsScopingOrganizationOids());
        assertEquals("AE", response.getQueryResponseCode());
    }

    @Test
    public void testRespondXcpd() {
        final var request = XcpdQueryRequestTransformerTest.getSampleQuery();
        final var response = Hl7v3MessageUtils.respond(request);
        assertResponseHeader(request, response);
        assertEquals(request.getCommunityPatientIdAssigningAuthority(), response.getCommunityPatientIdAssigningAuthority());
    }

    @Test
    public void testAck() {
        final var request = PatientRecordTransformersTest.fill(new PatientRecordRevisedRequest());
        final var ack = Hl7v3MessageUtils.ack(request);
        assertNotNull(ack.getMessageId());
        assertNotEquals(request.getMessageId(), ack.getMessageId());
        assertSame(request.getMessageId(), ack.getTargetMessageId());
        assertSame(request.getSender(), ack.getReceiver());
        assertSame(request.getReceiver(), ack.getSender());
        assertEquals("AA", ack.getAcknowledgementTypeCode());
    }

    private static <R extends Hl7v3QueryRequest & Hl7v3Response> void assertResponseHeader(
            final Hl7v3QueryRequest request, final R response) {
        assertNotNull(response.getMessageId());
        assertNotEquals(request.getMessageId(), response.getMessageId());
        assertSame(request.getMessageId(), response.getTargetMessageId());
        assertSame(request.getQueryId(), response.getQueryId());
        assertSame(request.getSender(), response.getReceiver());
        assertSame(request.getReceiver(), response.getSender());
    }
}
