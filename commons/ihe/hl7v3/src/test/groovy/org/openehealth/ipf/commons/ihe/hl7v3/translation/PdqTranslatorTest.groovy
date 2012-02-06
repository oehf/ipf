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

import static org.junit.Assert.*
import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.ITI_47
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.HL7V3_NSURI
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.getBuilder
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.slurp
import groovy.util.slurpersupport.GPathResult
import groovy.xml.FactorySupport
import groovy.xml.MarkupBuilder

import javax.xml.parsers.SAXParserFactory

import org.apache.commons.io.IOUtils
import org.junit.*
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.xml.XmlYielder
import org.springframework.core.io.ClassPathResource

/**
 * Unit test for PDQ translator.
 * @author Marek Václavík, Dmytro Rud
 */
class PdqTranslatorTest extends Hl7TranslationTestContainer {

    static def parser
   
    @BeforeClass
    static void setUpClass() {
        doSetUp('pdq',
                new PdqRequest3to2Translator(),
                new PdqResponse2to3Translator())

        parser = CustomModelClassUtils.createParser('pdq', '2.5')
    }

   
    @Test
    void testPdqQuery() {
        doTestV3toV2RequestTranslation('PDQ_Maximal_Query', 21, ITI_47)
        doTestV2toV3ResponseTranslation('PDQ_Maximal_Query', 21, ITI_47, parser)
        doTestV2toV3ResponseTranslation('PDQ', 21, ITI_47, parser)
    }
     
    @Test
    void testResponseWithPid4() {
        doTestV2toV3ResponseTranslation('PDQ_with_PID4', 21, ITI_47, parser)
    }
    
    @Test
    void testConnectathon2011Issue() {
        doTestV2toV3ResponseTranslation('PDQ_connectathon2011_namespaces_issue', 21, ITI_47, parser)
    }

}
