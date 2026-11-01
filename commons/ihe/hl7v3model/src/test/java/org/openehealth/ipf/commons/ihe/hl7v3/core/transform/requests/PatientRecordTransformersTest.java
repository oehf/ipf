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

import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03ContactParty;
import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03Organization;
import net.ihe.gazelle.hl7v3.datatypes.EN;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.TEL;
import net.ihe.gazelle.hl7v3.prpain201301UV02.PRPAIN201301UV02Type;
import net.ihe.gazelle.hl7v3.prpain201302UV02.PRPAIN201302UV02Type;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02Type;
import net.ihe.gazelle.hl7v3.voc.RoleClassContact;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.OtherIds;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Patient;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDuplicatesResolvedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordAddedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRevisedRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.roundTrip;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.unmarshalResource;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest.address;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest.name;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertDeviceEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertIiEquals;

/**
 * Tests the transformers of the patient registry messages (ITI-44, ITI-46).
 */
public class PatientRecordTransformersTest {

    @Test
    public void testRecordAddedRoundTrip() throws Exception {
        final var transformer = new PatientRecordAddedTransformer();
        final var request = fill(new PatientRecordAddedRequest());

        final PRPAIN201301UV02Type prpa = roundTrip(transformer.toPrpa(request));
        assertEquals("PRPA_IN201301UV02", prpa.getInteractionId().getExtension());
        assertEquals("PRPA_TE201301UV02", prpa.getControlActProcess().getCode().getCode());
        final var parsed = transformer.fromPrpa(prpa);
        assertHeaderEquals(request, parsed);
        assertPatientEquals(request.getPatient(), parsed.getPatient());
    }

    @Test
    public void testRecordRevisedRoundTrip() throws Exception {
        final var transformer = new PatientRecordRevisedTransformer();
        final var request = fill(new PatientRecordRevisedRequest());

        final PRPAIN201302UV02Type prpa = roundTrip(transformer.toPrpa(request));
        assertEquals("PRPA_IN201302UV02", prpa.getInteractionId().getExtension());
        assertEquals("PRPA_TE201302UV02", prpa.getControlActProcess().getCode().getCode());
        final var parsed = transformer.fromPrpa(prpa);
        assertHeaderEquals(request, parsed);
        assertPatientEquals(request.getPatient(), parsed.getPatient());
    }

    @Test
    public void testDuplicatesResolvedRoundTrip() throws Exception {
        final var transformer = new PatientDuplicatesResolvedTransformer();
        final var request = fill(new PatientDuplicatesResolvedRequest());
        request.getObsoletePatientIds().add(new II("1.2.840.114350.1.13.99997.2.3412", "4711"));

        final PRPAIN201304UV02Type prpa = roundTrip(transformer.toPrpa(request));
        assertEquals("PRPA_IN201304UV02", prpa.getInteractionId().getExtension());
        assertEquals("PRPA_TE201304UV02", prpa.getControlActProcess().getCode().getCode());
        final var priorRegistration = prpa.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getReplacementOf().get(0).getPriorRegistration();
        assertEquals("obsolete", priorRegistration.getStatusCode().getCode());

        final var parsed = transformer.fromPrpa(prpa);
        assertHeaderEquals(request, parsed);
        assertPatientEquals(request.getPatient(), parsed.getPatient());
        assertEquals(1, parsed.getObsoletePatientIds().size());
        assertIiEquals(request.getObsoletePatientIds().get(0), parsed.getObsoletePatientIds().get(0));
    }

    @Test
    public void testParseRecordAddedSample() throws Exception {
        final PRPAIN201301UV02Type prpa = unmarshalResource("iti44/PIX_FEED_REG_Maximal_Request.xml");
        final var patient = new PatientRecordAddedTransformer().fromPrpa(prpa).getPatient();

        assertEquals(3, patient.getPatientIds().size());
        assertEquals("PIX10515W4", patient.getPatientIds().get(1).getExtension());
        assertEquals(2, patient.getNames().size());
        assertEquals("WASHINGTON", patient.getNames().get(0).getFamily().get(0).getListStringValues().get(0));
        assertEquals(5, patient.getTelecoms().size());
        assertEquals("F", patient.getAdministrativeGenderCode());
        assertEquals("19771208", patient.getBirthTime());
        assertEquals(Boolean.FALSE, patient.getDeceasedInd());
        assertEquals(2, patient.getAddresses().size());
        assertEquals(2, patient.getOtherIds().size());
        assertEquals("2.16.840.1.113883.4.1", patient.getOtherIds().get(0).getScopingOrganizationOid());
        assertEquals("100-09-1234", patient.getOtherIds().get(0).getIds().get(0).getExtension());
        assertEquals("JOHN", patient.getMothersMaidenName().getFamily().get(0).getListStringValues().get(0));
        assertEquals("Chicago", patient.getBirthPlaceName().getListStringValues().get(0));
        assertEquals("1.2.840.114350.1.13.99998.8734", patient.getProviderOrganization().getId().get(0).getRoot());
        assertEquals("1.2.840.114350.1.13.99998.8734", patient.getCustodianOid());
    }

    @Test
    public void testParseRecordRevisedSample() throws Exception {
        final PRPAIN201302UV02Type prpa = unmarshalResource("iti44/PIX_FEED_REV_Maximal_Request.xml");
        final var patient = new PatientRecordRevisedTransformer().fromPrpa(prpa).getPatient();

        assertEquals("WASHINGTON-UPDATE", patient.getNames().get(0).getFamily().get(0).getListStringValues().get(0));
        // <administrativeGenderCode nullFlavor="UNK"/>
        assertNull(patient.getAdministrativeGenderCode());
        assertNull(patient.getDeceasedInd());
        assertEquals(3, patient.getOtherIds().size());
        assertEquals("1.12.23.34.45", patient.getOtherIds().get(1).getScopingOrganizationOid());
        assertNull(patient.getBirthPlaceName());
        assertEquals("Chicago", patient.getBirthPlaceAddress().getCity().get(0).getListStringValues().get(0));
    }

    @Test
    public void testParseDuplicatesResolvedSample() throws Exception {
        final PRPAIN201304UV02Type prpa = unmarshalResource("iti44/PIX_FEED_MERGE_Maximal_Request.xml");
        final var request = new PatientDuplicatesResolvedTransformer().fromPrpa(prpa);

        assertIiEquals(new II("1.2.3.4.5.1000", "120-01"), request.getPatient().getPatientIds().get(0));
        assertEquals(1, request.getObsoletePatientIds().size());
        assertIiEquals(new II("1.2.3.4.666", "120-02"), request.getObsoletePatientIds().get(0));
        assertEquals("domain1", request.getObsoletePatientIds().get(0).getAssigningAuthorityName());
        assertTrue(request.getPatient().getOtherIds().isEmpty());
    }

    public static <T extends PatientRecordRequest> T fill(final T request) {
        final var pixQuery = PixV3QueryRequestTransformerTest.getSampleQuery();
        request.setMessageId(new II("1.2.840.114350.1.13.0.1.7.1.1", "35423"));
        request.setSender(pixQuery.getSender());
        request.setReceiver(pixQuery.getReceiver());
        request.setPatient(getSamplePatient());
        return request;
    }

    public static Patient getSamplePatient() {
        final var patient = new Patient();
        patient.getPatientIds().add(new II("1.2.840.114350.1.13.99997.2.3412", "38273N237"));
        patient.getNames().add(name("Jimmy", "Jones"));
        final var telecom = new TEL();
        telecom.setValue("tel:+1-481-555-7684");
        patient.getTelecoms().add(telecom);
        patient.setAdministrativeGenderCode("M");
        patient.setBirthTime("19630804");
        patient.setDeceasedInd(false);
        patient.setMultipleBirthInd(true);
        patient.setMultipleBirthOrderNumber(2);
        patient.getAddresses().add(address("Some City"));
        patient.getOtherIds().add(OtherIds.of(new II("2.16.840.1.113883.4.1", "999-88-6345")));
        patient.setMothersMaidenName(name("Mary", "Smith"));
        patient.setBirthPlaceAddress(address("Birth City"));
        final var birthPlaceName = new EN();
        birthPlaceName.mixed = List.of("Birth City");
        patient.setBirthPlaceName(birthPlaceName);
        patient.setCustodianOid("1.2.840.114350.1.13.99998.8734");
        final var organization = new COCTMT150003UV03Organization();
        organization.getId().add(new II("1.2.840.114350.1.13.99998.8734", null));
        final var contactParty = new COCTMT150003UV03ContactParty();
        contactParty.setClassCode(RoleClassContact.CON);
        organization.getContactParty().add(contactParty);
        patient.setProviderOrganization(organization);
        return patient;
    }

    static void assertHeaderEquals(final PatientRecordRequest expected, final PatientRecordRequest actual) {
        assertIiEquals(expected.getMessageId(), actual.getMessageId());
        assertDeviceEquals(expected.getSender(), actual.getSender());
        assertDeviceEquals(expected.getReceiver(), actual.getReceiver());
    }

    static void assertPatientEquals(final Patient expected, final Patient actual) {
        assertEquals(expected.getPatientIds().size(), actual.getPatientIds().size());
        assertIiEquals(expected.getPatientIds().get(0), actual.getPatientIds().get(0));
        assertEquals("Jones", actual.getNames().get(0).getFamily().get(0).getListStringValues().get(0));
        assertEquals(expected.getTelecoms().get(0).getValue(), actual.getTelecoms().get(0).getValue());
        assertEquals(expected.getAdministrativeGenderCode(), actual.getAdministrativeGenderCode());
        assertEquals(expected.getBirthTime(), actual.getBirthTime());
        assertEquals(expected.getDeceasedInd(), actual.getDeceasedInd());
        assertEquals(expected.getMultipleBirthInd(), actual.getMultipleBirthInd());
        assertEquals(expected.getMultipleBirthOrderNumber(), actual.getMultipleBirthOrderNumber());
        assertEquals("Some City", actual.getAddresses().get(0).getCity().get(0).getListStringValues().get(0));
        assertEquals(1, actual.getOtherIds().size());
        assertEquals(expected.getOtherIds().get(0).getScopingOrganizationOid(),
                actual.getOtherIds().get(0).getScopingOrganizationOid());
        assertIiEquals(expected.getOtherIds().get(0).getIds().get(0), actual.getOtherIds().get(0).getIds().get(0));
        assertEquals("Smith", actual.getMothersMaidenName().getFamily().get(0).getListStringValues().get(0));
        assertEquals("Birth City", actual.getBirthPlaceAddress().getCity().get(0).getListStringValues().get(0));
        assertEquals("Birth City", actual.getBirthPlaceName().getListStringValues().get(0));
        assertEquals(expected.getCustodianOid(), actual.getCustodianOid());
        assertEquals("1.2.840.114350.1.13.99998.8734", actual.getProviderOrganization().getId().get(0).getRoot());
    }
}
