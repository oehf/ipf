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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses;

import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.mcciin000002UV01.MCCIIN000002UV01Type;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01AcknowledgementDetail;
import net.ihe.gazelle.hl7v3.voc.AcknowledgementDetailType;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordAddedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.AcceptAcknowledgement;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.roundTrip;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.unmarshalResource;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertDeviceEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertIiEquals;

public class AcceptAcknowledgementTransformerTest {

    private final AcceptAcknowledgementTransformer transformer = new AcceptAcknowledgementTransformer();

    @Test
    public void testRoundTrip() throws Exception {
        final var pixQuery = PixV3QueryRequestTransformerTest.getSampleQuery();
        final var request = new PatientRecordAddedRequest();
        request.setMessageId(new II("1.2.840.114350.1.13.0.1.7.1.1", "35423"));
        request.setSender(pixQuery.getSender());
        request.setReceiver(pixQuery.getReceiver());

        final var acknowledgement = AcceptAcknowledgement.fromRequest(request);
        acknowledgement.setMessageId(new II("1.2.3.4", "ack-1"));
        final var detail = new MCCIMT000200UV01AcknowledgementDetail();
        detail.setTypeCode(AcknowledgementDetailType.E);
        detail.setCode(new CE("204", "Unknown key identifier", "2.16.840.1.113883.12.357"));
        acknowledgement.getAcknowledgementDetails().add(detail);
        acknowledgement.setApplicationError();

        final MCCIIN000002UV01Type mcci = roundTrip(transformer.toMcci(acknowledgement));
        assertEquals("MCCI_IN000002UV01", mcci.getInteractionId().getExtension());
        final var parsed = transformer.fromMcci(mcci);
        assertIiEquals(acknowledgement.getMessageId(), parsed.getMessageId());
        assertIiEquals(request.getMessageId(), parsed.getTargetMessageId());
        assertDeviceEquals(request.getSender(), parsed.getReceiver());
        assertDeviceEquals(request.getReceiver(), parsed.getSender());
        assertEquals("AE", parsed.getAcknowledgementTypeCode());
        assertEquals(1, parsed.getAcknowledgementDetails().size());
        assertEquals("204", parsed.getAcknowledgementDetails().get(0).getCode().getCode());
    }

    @Test
    public void testParseSample() throws Exception {
        final MCCIIN000002UV01Type mcci = unmarshalResource("iti44/Ack.xml");
        final var acknowledgement = transformer.fromMcci(mcci);
        assertEquals("CA", acknowledgement.getAcknowledgementTypeCode());
        assertEquals("22a0f9e0-4454-11dc-a6be-3603d6866807", acknowledgement.getTargetMessageId().getRoot());
        assertEquals("1.2.840.114350.1.13.99999.4567", acknowledgement.getSender().getIds().get(0).getRoot());
        assertEquals("https://example.org/PatientFeed", acknowledgement.getSender().getDeviceTelecom().get(0).getValue());
    }
}
