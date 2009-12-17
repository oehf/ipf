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
package org.openehealth.ipf.commons.ihe.transform.pdqquery

import org.openehealth.ipf.commons.ihe.transform.core.Hl7TranslationTestContainer
import org.junit.BeforeClassimport org.junit.Test
/**
 * Unit test for PDQ Query translator.
 * @author Marek Václavík, Dmytro Rud
 */
class PdqQueryTranslatorTest extends Hl7TranslationTestContainer {
 
    @BeforeClass
    static void setUpClass() {
        doSetUp('pdqv3v2-mappings.map',
                new PdqQueryRequest3to2Translator(), 
                new PdqQueryResponse2to3Translator())
    }      

    @Test
    void testPdqQuery() {
        doTestRequestTranslation('PDQ_Maximal_Query')
        doTestResponseTranslation('PDQ_Maximal_Query')
    }
}
