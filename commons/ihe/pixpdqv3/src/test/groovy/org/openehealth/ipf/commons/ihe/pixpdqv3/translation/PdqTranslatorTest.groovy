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
package org.openehealth.ipf.commons.ihe.pixpdqv3.translation

import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.pixpdq.definitions.CustomModelClassUtils;

/**
 * Unit test for PDQ translator.
 * @author Marek Václavík, Dmytro Rud
 */
class PdqTranslatorTestorTest extends Hl7TranslationTestContainer {
 
    @BeforeClass
    static void setUpClass() {
        doSetUp('pdq',
                new PdqRequest3to2Translator(), 
                new PdqResponse2to3Translator(),
                'pdq-translation.map')
    }      

    @Test
    void testPdqQuery() {
        def parser = CustomModelClassUtils.createParser('pdq', '2.5')
        doTestV3toV2RequestTranslation('PDQ_Maximal_Query', 21, 47)
        doTestV2toV3ResponseTranslation('PDQ_Maximal_Query', 21, 47, parser)
        doTestV2toV3ResponseTranslation('PDQ', 21, 47, parser)
    }
}
