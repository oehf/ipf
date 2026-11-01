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

import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03ContactParty;
import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03Organization;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.TEL;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import net.ihe.gazelle.hl7v3.voc.RoleClassContact;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.MatchedPatient;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PdqV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.roundTrip;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.unmarshalResource;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest.address;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest.name;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertDeviceEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertIiEquals;

public class PdqV3QueryResponseTransformerTest {

    private final PdqV3QueryResponseTransformer transformer = new PdqV3QueryResponseTransformer();

    @Test
    public void testRoundTrip() throws Exception {
        final var query = PdqV3QueryRequestTransformerTest.getSampleQuery();
        final var response = PdqV3QueryResponse.fromQuery(query);
        response.setMessageId(new II("1.2.3.4", "response-1"));
        response.getPatients().add(getSamplePatient());
        response.setResultTotalQuantity(3);
        response.setResultCurrentQuantity(1);
        response.setResultRemainingQuantity(2);
        response.setDataFound();

        final PRPAIN201306UV02Type prpa = roundTrip(transformer.toPrpa(response));
        assertEquals(1, prpa.getControlActProcess().getSubject().size());

        final var parsed = transformer.fromPrpa(prpa);
        assertIiEquals(response.getMessageId(), parsed.getMessageId());
        assertIiEquals(query.getMessageId(), parsed.getTargetMessageId());
        assertIiEquals(query.getQueryId(), parsed.getQueryId());
        assertDeviceEquals(query.getSender(), parsed.getReceiver());
        assertDeviceEquals(query.getReceiver(), parsed.getSender());
        assertEquals("AA", parsed.getAcknowledgementTypeCode());
        assertEquals("OK", parsed.getQueryResponseCode());
        assertEquals(3, parsed.getResultTotalQuantity());
        assertEquals(1, parsed.getResultCurrentQuantity());
        assertEquals(2, parsed.getResultRemainingQuantity());
        // echoed query parameters
        assertEquals(query.getOtherIDsScopingOrganizationOids(), parsed.getOtherIDsScopingOrganizationOids());
        assertEquals("19630804", parsed.getLivingSubjectBirthTime());
        assertEquals(10, parsed.getInitialQuantity());

        assertEquals(1, parsed.getPatients().size());
        final var patient = parsed.getPatients().get(0);
        assertIiEquals(new II("1.2.840.114350.1.13.99998.8734", "34827K410"), patient.getPatientIds().get(0));
        assertEquals("James", patient.getNames().get(0).getGiven().get(0).getListStringValues().get(0));
        assertEquals("M", patient.getAdministrativeGenderCode());
        assertEquals("19630804", patient.getBirthTime());
        assertEquals(Boolean.FALSE, patient.getDeceasedInd());
        assertEquals("Some City", patient.getAddresses().get(0).getCity().get(0).getListStringValues().get(0));
        assertEquals(92, patient.getMatchDegree());
        assertEquals("1.2.840.114350.1.13.99998.8734", patient.getCustodianOid());
        assertNotNull(patient.getProviderOrganization());
    }

    @Test
    public void testNoDataFoundOmitsPatients() throws Exception {
        final var response = PdqV3QueryResponse.fromQuery(PdqV3QueryRequestTransformerTest.getSampleQuery());
        response.getPatients().add(getSamplePatient());
        response.setNoDataFound();

        final PRPAIN201306UV02Type prpa = roundTrip(transformer.toPrpa(response));
        assertTrue(prpa.getControlActProcess().getSubject().isEmpty());
        final var parsed = transformer.fromPrpa(prpa);
        assertEquals("NF", parsed.getQueryResponseCode());
        assertTrue(parsed.getPatients().isEmpty());
    }

    @Test
    public void testParseSample() throws Exception {
        final PRPAIN201306UV02Type prpa = unmarshalResource("iti47/02_PDQQuery1Response.xml");
        final var response = transformer.fromPrpa(prpa);

        assertEquals("AA", response.getAcknowledgementTypeCode());
        assertEquals("OK", response.getQueryResponseCode());
        assertEquals("35423", response.getTargetMessageId().getExtension());
        assertEquals("18204", response.getQueryId().getExtension());
        assertEquals(5, response.getResultTotalQuantity());
        assertEquals(2, response.getResultCurrentQuantity());
        assertEquals(3, response.getResultRemainingQuantity());
        assertEquals(2, response.getInitialQuantity());
        assertEquals(7, response.getPatients().size());

        final var patient = response.getPatients().get(0);
        assertEquals("1", patient.getPatientIds().get(0).getExtension());
        assertEquals("Jones", patient.getNames().get(0).getFamily().get(0).getListStringValues().get(0));
        assertEquals(2, patient.getTelecoms().size());
        assertEquals("M", patient.getAdministrativeGenderCode());
        assertEquals("19630804", patient.getBirthTime());
        assertEquals(2, patient.getOtherIds().size());
        assertEquals("2.16.840.1.113883.4.1", patient.getOtherIds().get(1).getScopingOrganizationOid());
        assertEquals(92, patient.getMatchDegree());
        assertEquals("1.2.840.114350.1.13.99998.8734", patient.getCustodianOid());
        assertNotNull(patient.getProviderOrganization());
        assertEquals(85, response.getPatients().get(1).getMatchDegree());
    }

    public static MatchedPatient getSamplePatient() {
        final var patient = new MatchedPatient();
        patient.getPatientIds().add(new II("1.2.840.114350.1.13.99998.8734", "34827K410"));
        patient.getNames().add(name("James", "Jones"));
        patient.setAdministrativeGenderCode("M");
        patient.setBirthTime("19630804");
        patient.setDeceasedInd(false);
        patient.getAddresses().add(address("Some City"));
        patient.setMatchDegree(92);
        patient.setCustodianOid("1.2.840.114350.1.13.99998.8734");
        final var organization = new COCTMT150003UV03Organization();
        organization.getId().add(new II("1.2.840.114350.1.13.99998.8734", null));
        final var contactParty = new COCTMT150003UV03ContactParty();
        contactParty.setClassCode(RoleClassContact.CON);
        final var telecom = new TEL();
        telecom.setValue("tel:+1-342-555-8394");
        contactParty.getTelecom().add(telecom);
        organization.getContactParty().add(contactParty);
        patient.setProviderOrganization(organization);
        return patient;
    }
}
