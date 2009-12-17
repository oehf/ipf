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
package org.openehealth.ipf.commons.ihe.transform.core

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters
import org.openehealth.ipf.modules.hl7.parser.PipeParser
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory
import org.openehealth.ipf.modules.hl7.extend.HapiModelExtension
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.extend.MappingExtension
import org.custommonkey.xmlunit.XMLUnitimport org.custommonkey.xmlunit.Diff
import org.springframework.core.io.ClassPathResource
import org.apache.commons.io.IOUtils

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
    
    static Hl73to2RequestTranslator requestTranslator
    static Hl72to3ResponseTranslator responseTranslator
    
    static void doSetUp(
            String mappingResourceName,
            Hl73to2RequestTranslator requestTranslator,
            Hl72to3ResponseTranslator responseTranslator) 
    {
        def mappingService = new BidiMappingService()
        mappingService.mappingScript = new ClassPathResource(mappingResourceName)
        def mappingExtension = new MappingExtension()
        mappingExtension.mappingService = mappingService
        mappingExtension.extensions()
        
        def hapiExtension = new HapiModelExtension()
        hapiExtension.setMappingService(mappingService)
        hapiExtension.extensions()
        
        XMLUnit.setCompareUnmatched(true)
        XMLUnit.setIgnoreAttributeOrder(true)
        XMLUnit.setIgnoreComments(true)
        XMLUnit.setIgnoreWhitespace(true)
        
        Hl7TranslationTestContainer.requestTranslator = requestTranslator
        Hl7TranslationTestContainer.responseTranslator = responseTranslator
    }      

    
    /**
     * Helper method to read in a HL7 message.
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
            .append(v3 ? 'v3/' : 'v2/')
            .append(fn)
            .append(request ? '' : '_Response')
            .append(v3 ? '.xml' : '.txt')
            .toString()
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)
        return IOUtils.toString(inputStream)
    }
    
    String doTestRequestTranslation(String fn) {
        String v3request = getFileContent(fn, V3, REQUEST)
        String expectedV2request = getFileContent(fn, V2, REQUEST)
        MessageAdapter translatedV2request = requestTranslator.translateRequest(v3request)
        assert translatedV2request.toString().trim() == expectedV2request.trim()
    }
    
    String doTestResponseTranslation(String fn) {
        String v3request = getFileContent(fn, V3, REQUEST)
        String v2response = getFileContent(fn, V2, RESPONSE)
        String expectedV3response = getFileContent(fn, V3, RESPONSE)
        String translatedV3response = responseTranslator.translateResponse(
                MessageAdapters.make(v2response),
                v3request)
        Diff diff = new Diff(expectedV3response, translatedV3response)
        assert diff.identical()
    }

}
