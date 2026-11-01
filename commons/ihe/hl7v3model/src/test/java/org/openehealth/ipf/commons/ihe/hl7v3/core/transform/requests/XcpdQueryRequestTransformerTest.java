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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests;

import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.TEL;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.XcpdQueryRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.roundTrip;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.unmarshalResource;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest.address;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest.name;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertDeviceEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertIiEquals;

public class XcpdQueryRequestTransformerTest {

    private final XcpdQueryRequestTransformer transformer = new XcpdQueryRequestTransformer();

    @Test
    public void testRoundTrip() throws Exception {
        final var query = getSampleQuery();
        query.setDeferredResponse("https://initiating.example.org/xcpd-deferred");

        final PRPAIN201305UV02Type prpa = roundTrip(transformer.toPrpa(query));
        assertEquals("D", prpa.getControlActProcess().getQueryByParameter().getResponsePriorityCode().getCode());
        assertEquals(1, prpa.getRespondTo().size());
        assertEquals("1.2.3.928.955",
                prpa.getSender().getDevice().getAsAgent().getRepresentedOrganization().getId().get(0).getRoot());

        final var parsed = transformer.fromPrpa(prpa);
        assertIiEquals(query.getMessageId(), parsed.getMessageId());
        assertIiEquals(query.getQueryId(), parsed.getQueryId());
        assertDeviceEquals(query.getSender(), parsed.getSender());
        assertEquals("1.2.3.928.955", parsed.getSender().getRepresentedOrganizationIds().get(0).getRoot());
        assertTrue(parsed.isDeferred());
        assertEquals("https://initiating.example.org/xcpd-deferred", parsed.getRespondTo());
        assertEquals("1.2.840.114350.1.13.99997.2.3412", parsed.getCommunityPatientIdAssigningAuthority());
        assertEquals(1, parsed.getLivingSubjectIds().size());
        assertEquals(1, parsed.getLivingSubjectNames().size());
        assertEquals("F", parsed.getLivingSubjectAdministrativeGenderCode());
        assertEquals("19700101", parsed.getLivingSubjectBirthTime());
        assertEquals("tel:+41-44-555-1234", parsed.getPatientTelecom().getValue());
        assertEquals("Smith", parsed.getMothersMaidenName().getFamily().get(0).getListStringValues().get(0));
        assertEquals("Zurich", parsed.getLivingSubjectBirthPlaceAddress().getCity().get(0).getListStringValues().get(0));
        assertEquals(1, parsed.getPrincipalCareProviderIds().size());
    }

    @Test
    public void testParseSample() throws Exception {
        final PRPAIN201305UV02Type prpa = unmarshalResource("iti55/iti55-sample-request.xml");
        final var query = transformer.fromPrpa(prpa);

        assertEquals("1.2.3.928.955", query.getSender().getRepresentedOrganizationIds().get(0).getRoot());
        assertEquals("1.2.840.114350.1.13.99997.2.3412", query.getCommunityPatientIdAssigningAuthority());
        assertFalse(query.isDeferred());
        assertNull(query.getRespondTo());
        assertEquals(2, query.getLivingSubjectIds().size());
        assertEquals("58910", query.getLivingSubjectIds().get(1).getExtension());
        assertEquals("hund", query.getMatchAlgorithm());
        assertEquals(100, query.getMinimumDegreeMatch());
        assertEquals("19630805", query.getLivingSubjectBirthTime());
    }

    @Test
    public void testParseDeferredSample() throws Exception {
        final PRPAIN201305UV02Type prpa = unmarshalResource("iti55/iti55-sample-request-deferred.xml");
        final var query = transformer.fromPrpa(prpa);
        assertTrue(query.isDeferred());
        assertEquals("http://initiating.gateway/deferred", query.getRespondTo());
    }

    public static XcpdQueryRequest getSampleQuery() {
        final var pixQuery = PixV3QueryRequestTransformerTest.getSampleQuery();
        final var query = new XcpdQueryRequest();
        query.setMessageId(new II("1.2.840.114350.1.13.0.1.7.1.1", "35423"));
        query.setQueryId(new II("1.2.840.114350.1.13.28.1.18.5.999", "18204"));
        query.setSender(pixQuery.getSender());
        query.getSender().getRepresentedOrganizationIds().add(new II("1.2.3.928.955", null));
        query.setReceiver(pixQuery.getReceiver());
        query.setCommunityPatientIdAssigningAuthority("1.2.840.114350.1.13.99997.2.3412");
        query.getLivingSubjectIds().add(new II("1.2.840.114350.1.13.99997.2.3412", "1234"));
        query.getLivingSubjectNames().add(name("Jane", "Doe"));
        query.setLivingSubjectAdministrativeGenderCode("F");
        query.setLivingSubjectBirthTime("19700101");
        final var telecom = new TEL();
        telecom.setValue("tel:+41-44-555-1234");
        query.setPatientTelecom(telecom);
        query.setMothersMaidenName(name("Mary", "Smith"));
        query.setLivingSubjectBirthPlaceAddress(address("Zurich"));
        query.getPrincipalCareProviderIds().add(new II("2.16.756.5.30.1.123", "4711"));
        return query;
    }
}
