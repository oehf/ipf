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

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException
import ca.uhn.hl7v2.HapiContext
import org.apache.commons.io.IOUtils
import org.hl7.fhir.dstu3.model.Identifier
import org.hl7.fhir.dstu3.model.Parameters
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.RSP_K23
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions

import java.nio.charset.Charset

/**
 *
 */
class PixQueryResponseToPixmResponseTranslatorTest extends Assert {

    private static final HapiContext PIX_QUERY_CONTEXT = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pix", "2.5"),
            PixPdqTransactions.ITI9)

    private PixQueryResponseToPixmResponseTranslator translator
    MappingService mappingService

    @Before
    void setup() {
        mappingService = new BidiMappingService()
        mappingService.setMappingScript(getClass().getClassLoader().getResource('mapping.map'))
        UriMapper mapper = new DefaultUriMapper(mappingService, 'uriToOid', 'uriToNamespace')
        translator = new PixQueryResponseToPixmResponseTranslator(mapper)
    }

    @Test
    void testTranslateRegularResponse() {
        RSP_K23 message = loadMessage('ok-1_Response')
        Parameters parameters = translator.translateToFhir(message, new HashMap<String, Object>())
        assertEquals(1, parameters.parameter.size())
        Parameters.ParametersParameterComponent parameter = parameters.parameter[0]
        Identifier identifier = (Identifier) parameter.getValue()
        assertEquals('78912', identifier.value)
        assertEquals('http://org.openehealth/ipf/commons/ihe/fhir/1', identifier.system)
        // System.out.println(FhirContext.forDstu3().newXmlParser().setPrettyPrint(true).encodeResourceToString(parameters))
    }

    @Test
    void testTranslateRegularResponseUnknownOid() {
        RSP_K23 message = loadMessage('ok-2_Response')
        Parameters parameters = translator.translateToFhir(message, new HashMap<String, Object>())
        assertEquals(1, parameters.parameter.size())
        Parameters.ParametersParameterComponent parameter = parameters.parameter[0]
        Identifier identifier = (Identifier) parameter.getValue()
        assertEquals('78912', identifier.value)
        assertEquals('urn:oid:1.2.3.4.5', identifier.system)
    }

    @Test
    void testTranslateEmptyResponse() {
        RSP_K23 message = loadMessage('nf-1_Response')
        Parameters parameters = translator.translateToFhir(message, new HashMap<String, Object>())
        assertEquals(0, parameters.parameter.size())
    }

    @Test(expected = ResourceNotFoundException.class)
    void testTranslateErrorResponseCase3() {
        RSP_K23 message = loadMessage('err-1_Response')
        translator.translateToFhir(message, new HashMap<String, Object>())
    }

    @Test
    void testTranslateErrorResponseCase4() {
        // not implemented yet
    }

    @Test
    void testTranslateErrorResponseCase5() {
        // not implemented yet
    }

    RSP_K23 loadMessage(String name) {
        String resourceName = "pixquery/v2/${name}.hl7"
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)
        String content = IOUtils.toString(inputStream, Charset.defaultCharset())
        return (RSP_K23) PIX_QUERY_CONTEXT.getPipeParser().parse(content)
    }
}
