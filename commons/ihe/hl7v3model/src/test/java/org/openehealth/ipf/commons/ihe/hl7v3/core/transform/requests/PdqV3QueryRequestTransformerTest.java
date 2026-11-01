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

import net.ihe.gazelle.hl7v3.datatypes.AD;
import net.ihe.gazelle.hl7v3.datatypes.AdxpCity;
import net.ihe.gazelle.hl7v3.datatypes.EnFamily;
import net.ihe.gazelle.hl7v3.datatypes.EnGiven;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.PN;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.core.HL7DTM;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PdqV3QueryRequest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.roundTrip;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.JaxbTestSupport.unmarshalResource;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertDeviceEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertIiEquals;

public class PdqV3QueryRequestTransformerTest {

    private final PdqV3QueryRequestTransformer transformer = new PdqV3QueryRequestTransformer();

    @Test
    public void testRoundTrip() throws Exception {
        final var query = getSampleQuery();

        final PRPAIN201305UV02Type prpa = roundTrip(transformer.toPrpa(query));
        final var queryByParameter = prpa.getControlActProcess().getQueryByParameter();
        assertEquals("PRPA_IN201305UV02", prpa.getInteractionId().getExtension());
        assertEquals("I", queryByParameter.getResponsePriorityCode().getCode());
        assertEquals("R", queryByParameter.getResponseModalityCode().getCode());

        final var parsed = transformer.fromPrpa(prpa);
        assertIiEquals(query.getMessageId(), parsed.getMessageId());
        assertIiEquals(query.getQueryId(), parsed.getQueryId());
        assertDeviceEquals(query.getSender(), parsed.getSender());
        assertDeviceEquals(query.getReceiver(), parsed.getReceiver());
        assertEquals(HL7DTM.toSimpleString(query.getCreationTime()), HL7DTM.toSimpleString(parsed.getCreationTime()));
        assertEquals(2, parsed.getLivingSubjectIds().size());
        assertIiEquals(query.getLivingSubjectIds().get(1), parsed.getLivingSubjectIds().get(1));
        assertEquals(1, parsed.getLivingSubjectNames().size());
        assertEquals("Jimmy", parsed.getLivingSubjectNames().get(0).getGiven().get(0).getListStringValues().get(0));
        assertEquals("Jones", parsed.getLivingSubjectNames().get(0).getFamily().get(0).getListStringValues().get(0));
        assertEquals("M", parsed.getLivingSubjectAdministrativeGenderCode());
        assertEquals("19630804", parsed.getLivingSubjectBirthTime());
        assertEquals("Some City", parsed.getPatientAddress().getCity().get(0).getListStringValues().get(0));
        assertEquals(Set.of("1.2.3", "2.16.840.1.113883.4.1"), parsed.getOtherIDsScopingOrganizationOids());
        assertEquals(75, parsed.getMinimumDegreeMatch());
        assertEquals("Soundex", parsed.getMatchAlgorithm());
        assertEquals(10, parsed.getInitialQuantity());
    }

    @Test
    public void testParseSample() throws Exception {
        final PRPAIN201305UV02Type prpa = unmarshalResource("iti47/01_PDQQuery1.xml");
        final var query = transformer.fromPrpa(prpa);

        assertIiEquals(new II("1.2.840.114350.1.13.0.1.7.1.1", "35423"), query.getMessageId());
        assertIiEquals(new II("1.2.840.114350.1.13.28.1.18.5.999", "18204"), query.getQueryId());
        assertEquals("http://servicelocation/PDQuery", query.getReceiver().getDeviceTelecom().get(0).getValue());
        assertEquals("1.2.840.114350.1.13.999.567", query.getSender().getIds().get(0).getRoot());
        assertEquals("M", query.getLivingSubjectAdministrativeGenderCode());
        assertEquals("19630804", query.getLivingSubjectBirthTime());
        assertEquals(1, query.getLivingSubjectIds().size());
        assertEquals("1234", query.getLivingSubjectIds().get(0).getExtension());
        assertEquals(1, query.getLivingSubjectNames().size());
        assertEquals(3, query.getOtherIDsScopingOrganizationOids().size());
        assertEquals(75, query.getMinimumDegreeMatch());
        assertNull(query.getMatchAlgorithm());
        assertEquals(2, query.getInitialQuantity());
    }

    public static PdqV3QueryRequest getSampleQuery() {
        final var pixQuery = PixV3QueryRequestTransformerTest.getSampleQuery();
        final var query = new PdqV3QueryRequest();
        query.setMessageId(new II("1.2.840.114350.1.13.0.1.7.1.1", "35423"));
        query.setQueryId(new II("1.2.840.114350.1.13.28.1.18.5.999", "18204"));
        query.setSender(pixQuery.getSender());
        query.setReceiver(pixQuery.getReceiver());
        query.getLivingSubjectIds().add(new II("1.2.3", "123"));
        query.getLivingSubjectIds().add(new II("2.16.840.1.113883.4.1", "456"));
        query.getLivingSubjectNames().add(name("Jimmy", "Jones"));
        query.setLivingSubjectAdministrativeGenderCode("M");
        query.setLivingSubjectBirthTime("19630804");
        query.setPatientAddress(address("Some City"));
        query.getOtherIDsScopingOrganizationOids().add("1.2.3");
        query.getOtherIDsScopingOrganizationOids().add("2.16.840.1.113883.4.1");
        query.setMinimumDegreeMatch(75);
        query.setMatchAlgorithm("Soundex");
        query.setInitialQuantity(10);
        return query;
    }

    public static PN name(final String given, final String family) {
        final var name = new PN();
        final var enGiven = new EnGiven();
        enGiven.mixed = List.of(given);
        name.addGiven(enGiven);
        final var enFamily = new EnFamily();
        enFamily.mixed = List.of(family);
        name.addFamily(enFamily);
        return name;
    }

    public static AD address(final String city) {
        final var address = new AD();
        final var adxpCity = new AdxpCity();
        adxpCity.mixed = List.of(city);
        address.addCity(adxpCity);
        return address;
    }
}
