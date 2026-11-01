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
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.XcpdQueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.XcpdQueryRequestTransformerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.roundTrip;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.unmarshalResource;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertIiEquals;

public class XcpdQueryResponseTransformerTest {

    private final XcpdQueryResponseTransformer transformer = new XcpdQueryResponseTransformer();

    @Test
    public void testRoundTrip() throws Exception {
        final var query = XcpdQueryRequestTransformerTest.getSampleQuery();
        final var response = XcpdQueryResponse.fromQuery(query);
        response.setMessageId(new II("1.2.3.4", "response-1"));
        final var patient1 = PdqV3QueryResponseTransformerTest.getSamplePatient();
        final var patient2 = PdqV3QueryResponseTransformerTest.getSamplePatient();
        patient2.setCustodianCode(new CE("SupportsHealthDataLocator", null,
                XcpdQueryResponseTransformer.HEALTH_DATA_LOCATOR_CODE_SYSTEM));
        response.getPatients().add(patient1);
        response.getPatients().add(patient2);
        response.setDataFound();

        final PRPAIN201306UV02Type prpa = roundTrip(transformer.toPrpa(response));
        // The query was sent by the Initiating Gateway, so its homeCommunityId is now in the receiver
        assertEquals("1.2.3.928.955",
                prpa.getReceiver().get(0).getDevice().getAsAgent().getRepresentedOrganization().getId().get(0).getRoot());

        final var parsed = transformer.fromPrpa(prpa);
        assertIiEquals(query.getMessageId(), parsed.getTargetMessageId());
        assertEquals("OK", parsed.getQueryResponseCode());
        assertEquals("1.2.840.114350.1.13.99997.2.3412", parsed.getLivingSubjectIds().get(0).getRoot());
        assertEquals("tel:+41-44-555-1234", parsed.getPatientTelecom().getValue());
        assertEquals(2, parsed.getPatients().size());
        // default Health Data Locator flag
        assertEquals("NotHealthDataLocator", parsed.getPatients().get(0).getCustodianCode().getCode());
        assertEquals(XcpdQueryResponseTransformer.HEALTH_DATA_LOCATOR_CODE_SYSTEM,
                parsed.getPatients().get(0).getCustodianCode().getCodeSystem());
        assertEquals("SupportsHealthDataLocator", parsed.getPatients().get(1).getCustodianCode().getCode());
    }

    @Test
    public void testParseSample() throws Exception {
        final PRPAIN201306UV02Type prpa = unmarshalResource("iti55/iti55-sample-response.xml");
        final var response = transformer.fromPrpa(prpa);

        assertEquals("AA", response.getAcknowledgementTypeCode());
        assertEquals("OK", response.getQueryResponseCode());
        assertEquals("1.2.3.928.955", response.getReceiver().getRepresentedOrganizationIds().get(0).getRoot());
        assertEquals(2, response.getLivingSubjectIds().size());
        assertEquals(2, response.getPatients().size());
        final var patient = response.getPatients().get(0);
        assertEquals("34827K410", patient.getPatientIds().get(0).getExtension());
        assertEquals("1.2.840.114350.1.13.99998.8734", patient.getCustodianOid());
        assertEquals("SupportsHealthDataLocator", patient.getCustodianCode().getCode());
        assertEquals(92, patient.getMatchDegree());
        assertEquals("NotHealthDataLocator", response.getPatients().get(1).getCustodianCode().getCode());
    }
}
