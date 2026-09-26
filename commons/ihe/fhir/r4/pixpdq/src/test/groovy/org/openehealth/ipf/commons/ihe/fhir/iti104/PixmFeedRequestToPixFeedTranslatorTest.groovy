/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.iti104

import ca.uhn.fhir.rest.param.TokenParam
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException
import org.easymock.EasyMock
import org.hl7.fhir.r4.model.Address
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.ContactPoint
import org.hl7.fhir.r4.model.DateType
import org.hl7.fhir.r4.model.Enumerations
import org.hl7.fhir.r4.model.HumanName
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Patient
import ca.uhn.hl7v2.HapiContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.ihe.fhir.Constants
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqResponseToPdqmResponseTranslator
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmPatient
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmPatient
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions
import org.openehealth.ipf.commons.map.Mappings

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * Translation of PIXm Patient Identity Feed [ITI-104] requests into PIX Feed [ITI-8] messages
 */
class PixmFeedRequestToPixFeedTranslatorTest {

    private static final String SYSTEM = 'urn:oid:1.2.3.4'
    private static final String OTHER_SYSTEM = 'urn:oid:1.2.3.4.5.6'

    private static final HapiContext PDQ_QUERY_CONTEXT = HapiContextFactory.createHapiContext(
        CustomModelClassUtils.createFactory('pdq', '2.5'),
        PixPdqTransactions.ITI21)

    private UriMapper mapper
    private PixmFeedRequestToPixFeedTranslator translator

    @BeforeEach
    void setup() {
        Mappings mappings = Mappings.builder()
            .load(getClass().getResource('/mapping.map'))
            .load(getClass().getResource('/META-INF/map/fhir-hl7v2-translation.mapping.xml'))
            .build()
        mapper = new DefaultUriMapper(mappings, 'uriToOid', 'uriToNamespace')
        translator = new PixmFeedRequestToPixFeedTranslator(mapper)

        Registry registry = EasyMock.createMock(Registry)
        ContextFacade.setRegistry(registry)
        EasyMock.expect(registry.bean(Mappings)).andReturn(mappings).anyTimes()
        EasyMock.replay(registry)
    }

    /**
     * The FHIR to HL7v2 mappings are the same in the XML and in the YAML variant of the mapping file
     */
    @Test
    void testXmlAndYamlMappingsAreEquivalent() {
        Mappings xml = Mappings.builder().load(getClass().getResource('/META-INF/map/fhir-hl7v2-translation.mapping.xml')).build()
        Mappings yaml = Mappings.builder().load(getClass().getResource('/META-INF/map/fhir-hl7v2-translation.mapping.yaml')).build()
        def names = xml.mappingNames().findAll { it.startsWith('fhir2hl7v2-') }
        assertEquals(6, names.size())
        names.each { name ->
            def x = xml.mapping(name).orElseThrow()
            def y = yaml.mapping(name).orElseThrow()
            assertEquals(x.entries(), y.entries(), name)
            assertEquals(x.unmatched(), y.unmatched(), name)
        }
    }

    @Test
    void testAddRevisePatient() {
        def patient = patient('0815')
        patient.addIdentifier().setSystem(OTHER_SYSTEM).setValue('4711')
        patient.addIdentifier().setSystem('http://example.org/unmappable').setValue('skipped')
        def parameters = parameters('0815')

        def message = translator.translateFhir(patient, parameters)

        assertEquals('ADT', message.MSH[9][1].value)
        assertEquals('A08', message.MSH[9][2].value)
        assertEquals('A08', message.EVN[1].value)
        assertEquals('A08', parameters[PixmFeedRequestToPixFeedTranslator.PIX_FEED_EVENT_TYPE])

        // the identifier of the condition first, then the other mappable ones
        def pid3 = message.PID[3]()
        assertEquals(2, pid3.size())
        assertEquals('0815', pid3[0][1].value)
        assertEquals('1.2.3.4', pid3[0][4][2].value)
        assertEquals('ISO', pid3[0][4][3].value)
        assertEquals('4711', pid3[1][1].value)
        assertEquals('1.2.3.4.5.6', pid3[1][4][2].value)

        assertEquals('Moore', message.PID[5][1][1].value)
        assertEquals('Chip', message.PID[5][2].value)
        assertEquals('Ralph', message.PID[5][3].value)
        assertEquals('L', message.PID[5][7].value)
        assertEquals('19800102', message.PID[7][1].value)
        assertEquals('M', message.PID[8].value)
        assertEquals('Main Street 1', message.PID[11][1].value)
        assertEquals('Springfield', message.PID[11][3].value)
        assertEquals('12345', message.PID[11][5].value)
        assertEquals('H', message.PID[11][7].value)
        assertEquals('555-1234', message.PID[13][1].value)
        assertEquals('PRN', message.PID[13][2].value)
        assertEquals('chip@example.org', message.PID[13](1)[4].value)
        assertEquals('555-9876', message.PID[14][1].value)
        assertEquals('WPN', message.PID[14][2].value)
        assertEquals('M', message.PID[16][1].value)
        assertEquals('N', message.PV1[2].value)
    }

    @Test
    void testConditionalIdentifierComesFirst() {
        def patient = patient('0815')
        patient.identifier.add(0, new Identifier().setSystem(OTHER_SYSTEM).setValue('4711'))

        def message = translator.translateFhir(patient, parameters('0815'))

        def pid3 = message.PID[3]()
        assertEquals('0815', pid3[0][1].value)
        assertEquals('4711', pid3[1][1].value)
    }

    /**
     * ITI-8 requires a name type code
     */
    @Test
    void testNameTypeDefaultsToLegal() {
        def patient = patient('0815')
        patient.nameFirstRep.use = null
        def message = translator.translateFhir(patient, parameters('0815'))
        assertEquals('L', message.PID[5][7].value)
    }

    @Test
    void testCitizenship() {
        def patient = patient('0815')
        patient.addCitizenship(new PdqmPatient.Citizenship().setCode(new CodeableConcept().addCoding(
            new Coding().setSystem('urn:iso:std:iso:3166').setCode('DE'))))
        patient.addCitizenship(new PdqmPatient.Citizenship().setCode(new CodeableConcept().addCoding(
            new Coding().setSystem('http://terminology.hl7.org/CodeSystem/v3-NullFlavor').setCode('UNK'))))

        def message = translator.translateFhir(patient, parameters('0815'))

        assertEquals('DE', message.PID[26](0)[1].value)
        assertEquals('UNK', message.PID[26](1)[1].value)
    }

    /**
     * A plain Patient carries the citizenship as extension
     */
    @Test
    void testCitizenshipExtension() {
        def patient = new Patient()
        patient.addIdentifier().setSystem(SYSTEM).setValue('0815')
        patient.addName().setFamily('Moore')
        patient.addExtension().setUrl(PdqmProfile.CITIZENSHIP_EXTENSION)
            .addExtension('code', new CodeableConcept().addCoding(new Coding().setSystem('urn:iso:std:iso:3166').setCode('CH')))

        def message = translator.translateFhir(patient, parameters('0815'))

        assertEquals('CH', message.PID[26][1].value)
    }

    /**
     * Translating a PID into a Patient with {@link PdqResponseToPdqmResponseTranslator} and back yields
     * the same values, as far as they are translated both ways
     */
    @Test
    void testInverseToPdqResponseTranslation() {
        def pdqResponse = PDQ_QUERY_CONTEXT.pipeParser.parse(
            getClass().getResource('/pdqquery/v2/ok-2_Response.hl7').getText('UTF-8'))
        def pdqTranslator = new PdqResponseToPdqmResponseTranslator(mapper)
        pdqTranslator.pdqSupplierResourceIdentifierUri = 'urn:oid:1.2.3.4'
        def patient = pdqTranslator.translateToFhir(pdqResponse, [:])[0]

        def source = pdqResponse.QUERY_RESPONSE.PID
        def target = translator.translateFhir(patient, [:]).PID

        [
            [3, 1], [3, 4, 2],                      // identifier
            [5, 1, 1], [5, 2], [5, 3], [5, 7],      // name
            [6, 1, 1],                              // mother's maiden name
            [7, 1], [8],                            // birth date, gender
            [11, 1], [11, 3], [11, 5], [11, 6],     // address
            [13, 1], [13, 3],                       // telecom
            [15, 1], [16, 1], [17, 1],              // language, marital status, religion
            [23],                                   // birth place
            [25], [26, 1]                           // multiple birth, citizenship
        ].each { path ->
            assertTrue(valueAt(source, path) as boolean, "PID-${path.join('-')} is empty in the fixture")
            assertEquals(valueAt(source, path), valueAt(target, path), "PID-${path.join('-')}")
        }
    }

    private static String valueAt(pid, List<Integer> path) {
        def element = pid[path[0]]
        path.drop(1).each { element = element[it] }
        element.encode()
    }

    /**
     * A plain Patient carries religion and birth place as extensions
     */
    @Test
    void testReligionAndBirthPlaceExtensions() {
        def patient = new Patient()
        patient.addIdentifier().setSystem(SYSTEM).setValue('0815')
        patient.addName().setFamily('Moore')
        patient.addExtension(PdqmProfile.RELIGION_EXTENSION, new CodeableConcept().addCoding(
            new Coding().setSystem('http://terminology.hl7.org/CodeSystem/v3-ReligiousAffiliation').setCode('1068')))
        patient.addExtension(PdqmProfile.BIRTH_PLACE_EXTENSION, new Address().setCity('Passau'))

        def message = translator.translateFhir(patient, parameters('0815'))

        // ambiguous in reverse direction, the first HL7v2 code is taken
        assertEquals('EOT', message.PID[17][1].value)
        assertEquals('Passau', message.PID[23].value)
    }

    @Test
    void testUnknownReligionIsNotTranslated() {
        def patient = patient('0815')
        patient.addReligion(new CodeableConcept().addCoding(
            new Coding().setSystem('http://terminology.hl7.org/CodeSystem/v3-NullFlavor').setCode('UNK')))

        def message = translator.translateFhir(patient, parameters('0815'))

        assertFalse(message.PID[17][1].value as boolean)
    }

    @Test
    void testConfiguredEventType() {
        translator.addReviseEventType = 'A04'
        def message = translator.translateFhir(patient('0815'), parameters('0815'))
        assertEquals('A04', message.MSH[9][2].value)
    }

    /**
     * The A40 only carries minimal demographics: the surviving patient in PID-3 (not the other identifiers
     * of the subsumed patient) and the name
     */
    @Test
    void testResolveDuplicatePatient() {
        def patient = patient('0815')
        patient.addIdentifier().setSystem(OTHER_SYSTEM).setValue('4712')
        patient.addReplacedByLink(new Identifier().setSystem(SYSTEM).setValue('4711'))

        def message = translator.translateFhir(patient, parameters('0815'))

        assertEquals('A40', message.MSH[9][2].value)
        def pid3 = message.PATIENT.PID[3]()
        assertEquals(1, pid3.size())
        assertEquals('4711', pid3[0][1].value)
        assertEquals('0815', message.PATIENT.MRG[1][1].value)
        assertEquals('1.2.3.4', message.PATIENT.MRG[1][4][2].value)
        assertEquals('Moore', message.PATIENT.PID[5][1][1].value)
        assertEquals('L', message.PATIENT.PID[5][7].value)
        assertFalse(message.PATIENT.PID[7][1].value as boolean)
        assertFalse(message.PATIENT.PID[8].value as boolean)
        assertTrue(message.PATIENT.PID[11].empty)
        assertTrue(message.PATIENT.PID[13].empty)
    }

    @Test
    void testResolveDuplicatePatientAcrossDomains() {
        def patient = patient('0815')
        patient.addReplacedByLink(new Identifier().setSystem(OTHER_SYSTEM).setValue('4711'))
        assertThrows(InvalidRequestException) { translator.translateFhir(patient, parameters('0815')) }
    }

    @Test
    void testUnmappableConditionalIdentifier() {
        def patient = new PixmPatient()
        patient.addIdentifier().setSystem('http://example.org/unmappable').setValue('0815')
        patient.addName().setFamily('Moore')
        def parameters = [(Constants.FHIR_REQUEST_PARAMETERS): new Iti104Parameters(
            new TokenParam('http://example.org/unmappable', '0815'), null)]
        assertThrows(InvalidRequestException) { translator.translateFhir(patient, parameters) }
    }

    @Test
    void testRemovePatientIsNotSupported() {
        assertThrows(NotImplementedOperationException) {
            translator.translateFhir(new Identifier().setSystem(SYSTEM).setValue('0815'), parameters('0815'))
        }
    }

    @Test
    void testMessageCanBeEncoded() {
        def encoded = translator.translateFhir(patient('0815'), parameters('0815')).encode()
        assertFalse(encoded.contains('~~'), encoded)
    }

    private static Map<String, Object> parameters(String value) {
        [(Constants.FHIR_REQUEST_PARAMETERS): new Iti104Parameters(new TokenParam(SYSTEM, value), null)]
    }

    private static PixmPatient patient(String value) {
        def patient = new PixmPatient()
        patient.addIdentifier().setSystem(SYSTEM).setValue(value)
        patient.addName()
            .setUse(HumanName.NameUse.OFFICIAL)
            .setFamily('Moore')
            .addGiven('Chip')
            .addGiven('Ralph')
        patient.setBirthDateElement(new DateType('1980-01-02'))
        patient.setGender(Enumerations.AdministrativeGender.MALE)
        patient.addAddress()
            .setUse(Address.AddressUse.HOME)
            .addLine('Main Street 1')
            .setCity('Springfield')
            .setPostalCode('12345')
        patient.addTelecom()
            .setSystem(ContactPoint.ContactPointSystem.PHONE)
            .setUse(ContactPoint.ContactPointUse.HOME)
            .setValue('555-1234')
        patient.addTelecom()
            .setSystem(ContactPoint.ContactPointSystem.EMAIL)
            .setValue('chip@example.org')
        patient.addTelecom()
            .setSystem(ContactPoint.ContactPointSystem.PHONE)
            .setUse(ContactPoint.ContactPointUse.WORK)
            .setValue('555-9876')
        patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding()
            .setSystem('http://terminology.hl7.org/CodeSystem/v3-MaritalStatus')
            .setCode('M')))
        patient
    }
}
