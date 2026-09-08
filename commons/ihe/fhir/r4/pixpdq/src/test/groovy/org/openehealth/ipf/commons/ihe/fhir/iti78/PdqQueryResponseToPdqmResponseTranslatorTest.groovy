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

package org.openehealth.ipf.commons.ihe.fhir.iti78

import ca.uhn.hl7v2.HapiContext
import org.apache.commons.io.IOUtils
import org.easymock.EasyMock
import org.hl7.fhir.r4.model.ContactPoint
import org.hl7.fhir.r4.model.Enumerations
import org.hl7.fhir.r4.model.HumanName
import org.hl7.fhir.r4.model.codesystems.GenderIdentity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmPatient
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.RSP_K21
import org.openehealth.ipf.commons.map.Mappings
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions

import java.nio.charset.StandardCharsets

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

/**
 *
 */
class PdqQueryResponseToPdqmResponseTranslatorTest {

    private static final HapiContext PDQ_QUERY_CONTEXT = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pdq", "2.5"),
            PixPdqTransactions.ITI21)

    private PdqResponseToPdqmResponseTranslator translator
    Mappings mappingService

    @BeforeEach
    void setup() {
        mappingService = Mappings.builder()
                .load(getClass().getClassLoader().getResource('mapping.map'))
                .load(getClass().getResource('/META-INF/map/fhir-hl7v2-translation.mapping.xml'))
                .build()
        UriMapper mapper = new DefaultUriMapper(mappingService, 'uriToOid', 'uriToNamespace')
        translator = new PdqResponseToPdqmResponseTranslator(mapper)
        translator.setPdqSupplierResourceIdentifierUri('urn:oid:1.2.3.4')
        translator.setNationalIdentifierUri('urn:oid:1.3.4.5.6')

        Registry registry = EasyMock.createMock(Registry)
        ContextFacade.setRegistry(registry)
        EasyMock.expect(registry.bean(Mappings)).andReturn(mappingService).anyTimes()
        EasyMock.replay(registry)
    }

    @Test
    void testTranslateRegularSearchResponse() {
        RSP_K21 message = loadMessage('ok-1_Response')
        List<PdqmPatient> patients = translator.translateToFhir(message, new HashMap<String, Object>())
        assertThat(patients, hasSize(9))
    }

    @Test
    void testTranslateRegularGetResponse() {
        RSP_K21 message = loadMessage('ok-2_Response')
        List<PdqmPatient> patients = translator.translateToFhir(message, new HashMap<String, Object>())
        assertThat(patients, hasSize(1))

        PdqmPatient patient = ++patients.iterator()

        assertThat(patient.identifier[0].system, is('http://org.openehealth/ipf/commons/ihe/fhir/1'))
        assertThat(patient.identifierFirstRep.value, is('79007'))

        patient.nameFirstRep.with {
            assertThat(it.family, is('Beckenbauer'))
            assertThat(it.given[0].value, is('Michael'))
            assertThat(it.given[1].value, is('Joachim'))
            assertThat(it.use, is(HumanName.NameUse.OFFICIAL))
        }
        patient.addressFirstRep.with {
            assertThat(it.line[0].value, is('Muenchner Freiheit 1'))
            assertThat(it.city, is('Muenchen'))
            assertThat(it.postalCode, is('89000'))
            assertThat(it.country, is('DE'))
        }

        assertThat(patient.telecomFirstRep.use, is(ContactPoint.ContactPointUse.MOBILE))
        assertThat(patient.telecomFirstRep.system, is(ContactPoint.ContactPointSystem.PHONE))
        assertThat(patient.gender, is(Enumerations.AdministrativeGender.MALE))
        assertThat(patient.genderIdentityFirstRep.value.codingFirstRep.code,
                is(GenderIdentity.MALE.toCode()))
        assertThat(patient.genderIdentityFirstRep.value.codingFirstRep.system,
                is('http://hl7.org/fhir/StructureDefinition/patient-genderIdentity'))
        assertThat(patient.telecomFirstRep.value, is('01511134556'))
        assertThat(patient.mothersMaidenName, is('Paukenbecker'))
        assertThat(patient.birthPlace.city, is('Passau'))
        assertThat(patient.citizenshipFirstRep.code.codingFirstRep.code, is('deu'))
        assertThat(patient.religionFirstRep.codingFirstRep.code, is('1041'))
        assertThat(patient.maritalStatus.codingFirstRep.code, is('M'))
        assertThat(patient.multipleBirthIntegerType.value, is(2))
    }

    RSP_K21 loadMessage(String name) {
        String resourceName = "pdqquery/v2/${name}.hl7"
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
        return (RSP_K21)PDQ_QUERY_CONTEXT.getPipeParser().parse(content)
    }
}
