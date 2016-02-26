/*
 * Copyright 2015 the original author or authors.
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

import org.hl7.fhir.instance.model.Identifier
import org.hl7.fhir.instance.model.Parameters
import org.hl7.fhir.instance.model.UriType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.commons.core.URN
import org.openehealth.ipf.commons.ihe.fhir.Constants
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslationException
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.QBP_Q21
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService

/**
 *
 */
class PixmRequestToPixQueryTranslatorTest extends Assert {

    private PixmRequestToPixQueryTranslator translator
    MappingService mappingService

    @Before
    public void setup() {
        mappingService = new BidiMappingService()
        mappingService.setMappingScript(getClass().getResource('/mapping.map'))
        UriMapper mapper = new DefaultUriMapper(mappingService, 'uriToOid', 'uriToNamespace')
        translator = new PixmRequestToPixQueryTranslator(mapper)
    }

    @Test
    public void testSuccessfulTranslateWithOids() {
        Identifier systemIdentifier = new Identifier()
                .setSystem('urn:oid:1.2.3.4')
                .setValue('4711ABC')
        UriType domainsReturned = new UriType('urn:oid:1.2.3.5.6')
        Parameters params = new Parameters()
        params.addParameter()
                .setName(Constants.SOURCE_IDENTIFIER_NAME)
                .setValue(systemIdentifier)
        params.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(domainsReturned)
        QBP_Q21 translated = translator.translateFhirToHL7v2(params, null)

        assertEquals(systemIdentifier.value, translated.QPD[3][1].value)
        assertEquals(URN.create(systemIdentifier.system).namespaceSpecificString, translated.QPD[3][4][2].value)
        assertEquals(URN.create(domainsReturned.value).namespaceSpecificString, translated.QPD[4][4][2].value)
    }

    @Test
    public void testSuccessfulTranslateWithUris() {
        Identifier systemIdentifier = new Identifier()
                .setSystem('http://org.openehealth/ipf/commons/ihe/fhir/1')
                .setValue('4711ABC')
        UriType domainsReturned = new UriType('http://org.openehealth/ipf/commons/ihe/fhir/2')
        Parameters params = new Parameters()
        params.addParameter()
                .setName(Constants.SOURCE_IDENTIFIER_NAME)
                .setValue(systemIdentifier)
        params.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(domainsReturned)
        QBP_Q21 translated = translator.translateFhirToHL7v2(params, null)

        assertEquals(systemIdentifier.value, translated.QPD[3][1].value)
        assertEquals(mappingService.get('uriToOid', systemIdentifier.system), translated.QPD[3][4][2].value)
        assertEquals(mappingService.get('uriToOid', domainsReturned.value), translated.QPD[4][4][2].value)
    }

    @Test(expected = FhirTranslationException)
    public void testUnknownURNScheme() {
        Identifier systemIdentifier = new Identifier()
                .setSystem('urn:isbn:1.2.3.4')
                .setValue('4711ABC')
        UriType domainsReturned = new UriType('urn:oid:1.2.3.5.6')
        Parameters params = new Parameters()
        params.addParameter()
                .setName(Constants.SOURCE_IDENTIFIER_NAME)
                .setValue(systemIdentifier)
        params.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(domainsReturned)
        translator.translateFhirToHL7v2(params, null)
    }
}
