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

import ca.uhn.fhir.rest.param.*
import org.easymock.EasyMock
import org.hl7.fhir.r4.model.IdType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.commons.core.URN
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.ihe.fhir.Constants
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.QBP_Q21
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService

/**
 *
 */
class PdqmRequestToPdqQueryTranslatorTest extends Assert {

    private PdqmRequestToPdqQueryTranslator translator
    MappingService mappingService

    @Before
    void setup() {
        mappingService = new BidiMappingService()
        mappingService.setMappingScripts( [ getClass().getResource('/mapping.map'),
                                           getClass().getResource('/META-INF/map/fhir-hl7v2-translation.map') ] as URL[])
        UriMapper mapper = new DefaultUriMapper(mappingService, 'uriToOid', 'uriToNamespace')
        translator = new PdqmRequestToPdqQueryTranslator(mapper)
        translator.pdqSupplierResourceIdentifierUri = 'urn:oid:1.3.5.7'

        Registry registry = EasyMock.createMock(Registry)
        ContextFacade.setRegistry(registry)
        EasyMock.expect(registry.bean(MappingService)).andReturn(mappingService).anyTimes()
        EasyMock.replay(registry)
    }

    @Test
    void testSuccessfulSearchTranslateWithOids() {

        // Ask for as much as possible
        Iti78SearchParameters query = Iti78SearchParameters.builder()
            .family(new StringAndListParam()
                .addAnd(new StringOrListParam().add(new StringParam("Surname1")))
                .addAnd(new StringOrListParam().add(new StringParam("Surname2"))))
            .given(new StringAndListParam()
                .addAnd(new StringOrListParam().add(new StringParam("Givenname1", true)))
                .addAnd(new StringOrListParam().add(new StringParam("Givenname2"))))
            .birthDate(new DateAndListParam()
                .addAnd(new DateOrListParam().add(new DateParam('1980'))))
            .address(new StringParam('Address'))
            .gender(new TokenParam('http://hl7.org/fhir/ValueSet/administrative-gender', 'male'))
            .telecom(new StringParam('Telecom'))
            .identifiers(new TokenAndListParam()
                .addAnd(new TokenOrListParam().add(new TokenParam('urn:oid:1.2.3.4', '4711ABC')))
                .addAnd(new TokenOrListParam().add(new TokenParam('urn:oid:1.2.3.4.5.6', '0815ABC')))
                .addAnd(new TokenOrListParam().add(new TokenParam('urn:oid:1.2.3.4.5.6', null)))).build()

        QBP_Q21 translated = translator.translateFhir(null, [(Constants.FHIR_REQUEST_PARAMETERS): query])
        String translatedString = translated.encode()

        assert (translatedString.contains('@PID.5.1^Surname1*'))
        assert (translatedString.contains('@PID.5.2^Givenname1'))
        assert (translatedString.contains('@PID.7^19800101'))
        assert (translatedString.contains('@PID.8^M'))
        assert (translatedString.contains('@PID.11.1^Address'))
        assert (translatedString.contains('@PID.13.1^Telecom'))
        assertEquals(URN.create('urn:oid:1.2.3.4.5.6').namespaceSpecificString, translated.QPD[8][4][2].value)

    }

    @Test
    void testSuccessfulGetTranslateWithOids() {
        IdType resourceId = new IdType().setValue('Patient/4711')
        QBP_Q21 translated = translator.translateFhir(resourceId, null)
        String translatedString = translated.encode()
        assert (translatedString.contains('@PID.3.1^4711'))
        assert (translatedString.contains('@PID.3.4.2^1.3.5.7'))
        assert (translatedString.contains('@PID.3.4.3^ISO'))
    }

}
