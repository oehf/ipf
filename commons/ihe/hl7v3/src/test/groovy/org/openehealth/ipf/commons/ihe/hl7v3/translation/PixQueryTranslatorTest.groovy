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

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.Message
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.ihe.hl7v3.PIXV3
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions

/**
 * Test for PIX Query translator.
 * @author Marek Václavik, Dmytro Rud
 */
class PixQueryTranslatorTest extends Hl7TranslationTestContainer {
 
    @BeforeClass
    static void setUpClass() {
        doSetUp('pixquery',
                new PixQueryRequest3to2Translator(),
                new PixQueryResponse2to3Translator(),
                HapiContextFactory.createHapiContext(PixPdqTransactions.ITI9))
    }        
  
    @Test
    void test1() {
        String v3request = getFileContent('NistPixpdq_Mesa10501-04_Example_01', true, true)
        Message translatedV2request = v3tov2Translator.translateV3toV2(v3request, null)
    }

	@Test
	void test2() {
		String v3request = getFileContent('NistPixpdq_Mesa10501-05_Example_01', true, true)
        Message translatedV2request = v3tov2Translator.translateV3toV2(v3request, null)
	}
	
	@Test
	void test3() throws HL7Exception {
		String v3request = getFileContent('NistPixpdq_Mesa10501-05_Example_01', true, true)
	    String v2response = getFileContent('ok-4', false, false)
        Message abrakadapter = context.pipeParser.parse(v2response)
		String v3response = v2tov3Translator.translateV2toV3(abrakadapter, v3request, 'UTF-8')
        V3_VALIDATOR.validate(v3response, PIXV3.Interactions.ITI_45.responseValidationProfile)
	}
    
   
}
