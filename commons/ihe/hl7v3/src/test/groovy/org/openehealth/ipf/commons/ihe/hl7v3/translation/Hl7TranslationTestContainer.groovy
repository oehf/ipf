/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.translation

import ca.uhn.hl7v2.parser.Parser
import org.apache.commons.io.IOUtils
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import org.openehealth.ipf.commons.ihe.core.InteractionId
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles

import org.openehealth.ipf.commons.ihe.hl7v2.MessageAdapterValidator
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.extend.MappingExtension
import org.openehealth.ipf.commons.xml.CombinedXmlValidator
import org.openehealth.ipf.modules.hl7.extend.HapiModelExtension
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters
import org.springframework.core.io.ClassPathResource

/**
 * Test container for HL7 v3-v2 transformation routines.
 * @author Dmytro Rud
 */
class Hl7TranslationTestContainer {
    private static final boolean V3       = true
    private static final boolean V2       = false
    private static final boolean REQUEST  = true
    private static final boolean RESPONSE = false

    static {
        ExpandoMetaClass.enableGlobally()
    }
    
    protected static final MessageAdapterValidator V2_VALIDATOR = new MessageAdapterValidator()
    protected static final CombinedXmlValidator V3_VALIDATOR = new CombinedXmlValidator()
    
    static String transactionName
    static Hl7TranslatorV3toV2 v3tov2Translator
    static Hl7TranslatorV2toV3 v2tov3Translator
    
    static void doSetUp(
            String transactionName, 
            Hl7TranslatorV3toV2 v3tov2Translator,
            Hl7TranslatorV2toV3 v2tov3Translator)
    {
        def mappingService = new BidiMappingService()
        mappingService.addMappingScript(new ClassPathResource('META-INF/map/hl7-v2-v3-translation.map'))
        def mappingExtension = new MappingExtension()
        mappingExtension.mappingService = mappingService
        mappingExtension.extensions()

        def hapiExtension = new HapiModelExtension()
        hapiExtension.setMappingService(mappingService)
        hapiExtension.extensions()

        Hl7TranslationTestContainer.transactionName = transactionName
        
        XMLUnit.setCompareUnmatched(true)
        XMLUnit.setIgnoreAttributeOrder(true)
        XMLUnit.setIgnoreComments(true)
        XMLUnit.setIgnoreWhitespace(true)
        
        Hl7TranslationTestContainer.v3tov2Translator = v3tov2Translator
        Hl7TranslationTestContainer.v2tov3Translator = v2tov3Translator
    }      

    
    /**
     * Helper method to read in an HL7 message.
     * @param fn
     *      name root of the file
     * @param v3
     *      whether a v3 (<tt>true</tt>) or a v2 (<tt>false</tt>)
     *      message should be read
     * @param request
     *      whether a request (<tt>true</tt>) or a response (<tt>false</tt>)
     *      message should be read
     */
    String getFileContent(String fn, boolean v3, boolean request) {
        String resourceName = new StringBuilder()
            .append('translation/')
            .append(transactionName)
            .append(v3 ? '/v3/' : '/v2/')
            .append(fn)
            .append(request ? '' : '_Response')
            .append(v3 ? '.xml' : '.hl7')
            .toString()
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)
        return IOUtils.toString(inputStream)
    }


    void doTestV3toV2RequestTranslation(String fn, int v2index, InteractionId v3Id) {
        String v3request = getFileContent(fn, V3, REQUEST)
        V3_VALIDATOR.validate(v3request, Hl7v3ValidationProfiles.getRequestValidationProfile(v3Id))
        
        String expectedV2request = getFileContent(fn, V2, REQUEST)
        MessageAdapter translatedV2request = v3tov2Translator.translateV3toV2(v3request, null)
        V2_VALIDATOR.validate(translatedV2request, null)
        assert translatedV2request.toString().trim() == expectedV2request.trim()
    }


    void doTestV2toV3ResponseTranslation(String fn, int v2index, InteractionId v3Id, Parser parser) {
        String v3request = getFileContent(fn, V3, REQUEST)
        String v2response = getFileContent(fn, V2, RESPONSE)
        MessageAdapter msg = MessageAdapters.make(parser, v2response)
        V2_VALIDATOR.validate(msg, null)

        String expectedV3response = getFileContent(fn, V3, RESPONSE)
        String translatedV3response = v2tov3Translator.translateV2toV3(msg, v3request, 'UTF-8')
        V3_VALIDATOR.validate(translatedV3response, Hl7v3ValidationProfiles.getResponseValidationProfile(v3Id))

        Diff diff = new Diff(expectedV3response, translatedV3response)
        DetailedDiff detDiff = new DetailedDiff(diff)
        List differences = detDiff.getAllDifferences()
        assert differences.size() == 1
        assert differences[0].toString().contains('creationTime')
    }
    
    void doTestV2toV3RequestTranslation(String fn, int v2index, InteractionId v3Id, ca.uhn.hl7v2.parser.Parser parser) {
        String v2request = getFileContent(fn, V2, REQUEST)
        org.openehealth.ipf.modules.hl7dsl.MessageAdapter msg = org.openehealth.ipf.modules.hl7dsl.MessageAdapters.make(parser, v2request)
        V2_VALIDATOR.validate(msg, null)

        String expectedV3response = getFileContent(fn, V3, RESPONSE)
        String translatedV3response = v2tov3Translator.translateV2toV3(msg, null, 'UTF-8')
        V3_VALIDATOR.validate(translatedV3response, Hl7v3ValidationProfiles.getRequestValidationProfile(v3Id))

        org.custommonkey.xmlunit.Diff diff = new org.custommonkey.xmlunit.Diff(expectedV3response, translatedV3response)
        org.custommonkey.xmlunit.DetailedDiff detDiff = new org.custommonkey.xmlunit.DetailedDiff(diff)
        List differences = detDiff.getAllDifferences()
        assert differences.size() == 1
        assert differences[0].toString().contains('creationTime')
    }

}
