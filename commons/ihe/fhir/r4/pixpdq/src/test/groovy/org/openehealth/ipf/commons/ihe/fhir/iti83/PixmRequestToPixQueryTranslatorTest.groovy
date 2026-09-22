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

package org.openehealth.ipf.commons.ihe.fhir.iti83

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException
import org.hl7.fhir.r4.model.Identifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.openehealth.ipf.commons.core.URN
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmQueryParametersIn
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.QBP_Q21
import org.openehealth.ipf.commons.map.Mappings

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.is

/**
 * Tests for PixmRequestToPixQueryTranslator
 */
class PixmRequestToPixQueryTranslatorTest {

    private PixmRequestToPixQueryTranslator translator
    Mappings mappingService

    @BeforeEach
    void setup() {
        mappingService = Mappings.builder()
                .load(getClass().getResource('/mapping.map'))
                .build()
        UriMapper mapper = new DefaultUriMapper(mappingService, 'uriToOid', 'uriToNamespace')
        translator = new PixmRequestToPixQueryTranslator(mapper)
        translator.pixSupplierResourceIdentifierUri = 'http://org.openehealth/ipf/commons/ihe/fhir/1'
    }

    @Test
    void testSuccessfulTranslateWithOids() {
        Identifier systemIdentifier = new Identifier()
                .setSystem('urn:oid:1.2.3.4')
                .setValue('4711ABC')

        PixmQueryParametersIn params = new PixmQueryParametersIn()
                .setSourceIdentifier(systemIdentifier)
                .addTargetSystem('urn:oid:1.2.3.4.5.6')
        
        QBP_Q21 translated = translator.translateFhir(params, null)

        assertThat(translated.QPD[3][1].value, is(systemIdentifier.value))
        assertThat(translated.QPD[3][4][2].value,
                is(URN.create(systemIdentifier.system).namespaceSpecificString))
        assertThat(translated.QPD[4][4][2].value,
                is(URN.create('urn:oid:1.2.3.4.5.6').namespaceSpecificString))
    }

    @Test
    void testSuccessfulTranslateWithUris() {
        Identifier systemIdentifier = new Identifier()
                .setSystem('http://org.openehealth/ipf/commons/ihe/fhir/1')
                .setValue('4711ABC')
        
        PixmQueryParametersIn params = new PixmQueryParametersIn()
                .setSourceIdentifier(systemIdentifier)
                .addTargetSystem('http://org.openehealth/ipf/commons/ihe/fhir/2')
        
        QBP_Q21 translated = translator.translateFhir(params, null)

        assertThat(translated.QPD[3][1].value, is(systemIdentifier.value))
        assertThat(translated.QPD[3][4][2].value,
                is(mappingService.map('uriToOid', systemIdentifier.system).orElse(null)))
        assertThat(translated.QPD[4][4][2].value,
                is(mappingService.map('uriToOid', 'http://org.openehealth/ipf/commons/ihe/fhir/2').orElse(null)))
    }

    @Test
    void testSuccessfulTranslateWithMultipleTargetSystems() {
        Identifier systemIdentifier = new Identifier()
                .setSystem('urn:oid:1.2.3.4')
                .setValue('4711ABC')
        
        PixmQueryParametersIn params = new PixmQueryParametersIn()
                .setSourceIdentifier(systemIdentifier)
                .setTargetSystems(['urn:oid:1.2.3.4.5.6', 'urn:oid:1.2.3.4.5.7'])
        
        QBP_Q21 translated = translator.translateFhir(params, null)

        assertThat(translated.QPD[3][1].value, is(systemIdentifier.value))
        assertThat(translated.QPD[3][4][2].value,
                is(URN.create(systemIdentifier.system).namespaceSpecificString))
        assertThat(translated.QPD[4](0)[4][2].value,
                is(URN.create('urn:oid:1.2.3.4.5.6').namespaceSpecificString))
        assertThat(translated.QPD[4](1)[4][2].value,
                is(URN.create('urn:oid:1.2.3.4.5.7').namespaceSpecificString))
    }

    @Test
    void testSuccessfulTranslateWithInstanceOperation() {
        // System is taken from pixSupplierResourceIdentifierUri
        Identifier systemIdentifier = new Identifier().setValue('4711ABC')

        PixmQueryParametersIn params = new PixmQueryParametersIn()
                .setSourceIdentifier(systemIdentifier)
                .addTargetSystem('http://org.openehealth/ipf/commons/ihe/fhir/2')
        
        QBP_Q21 translated = translator.translateFhir(params, null)

        assertThat(translated.QPD[3][1].value, is(systemIdentifier.value))
        assertThat(translated.QPD[3][4][2].value,
                is(mappingService.map('uriToOid', translator.pixSupplierResourceIdentifierUri).orElse(null)))
        assertThat(translated.QPD[4][4][2].value,
                is(mappingService.map('uriToOid', 'http://org.openehealth/ipf/commons/ihe/fhir/2').orElse(null)))
    }

    @Test
    void testSuccessfulTranslateWithNoTargetSystem() {
        Identifier systemIdentifier = new Identifier()
                .setSystem('urn:oid:1.2.3.4')
                .setValue('4711ABC')
        
        PixmQueryParametersIn params = new PixmQueryParametersIn()
                .setSourceIdentifier(systemIdentifier)
        
        QBP_Q21 translated = translator.translateFhir(params, null)

        assertThat(translated.QPD[3][1].value, is(systemIdentifier.value))
        assertThat(translated.QPD[3][4][2].value,
                is(URN.create(systemIdentifier.system).namespaceSpecificString))
        // QPD[4] should not be populated when no target systems are specified
    }

    @Test
    void testUnknownURNScheme() {
        Identifier systemIdentifier = new Identifier()
                .setSystem('urn:isbn:1.2.3.4')
                .setValue('4711ABC')
        
        PixmQueryParametersIn params = new PixmQueryParametersIn()
                .setSourceIdentifier(systemIdentifier)
                .addTargetSystem('urn:oid:1.2.3.5.6')
        
        Assertions.assertThrows(InvalidRequestException.class, () -> translator.translateFhir(params, null))
    }

    @Test
    void testMissingSourceIdentifier() {
        PixmQueryParametersIn params = new PixmQueryParametersIn()
                .addTargetSystem('urn:oid:1.2.3.4.5.6')
        
        Assertions.assertThrows(InvalidRequestException.class, () -> translator.translateFhir(params, null))
    }
}
