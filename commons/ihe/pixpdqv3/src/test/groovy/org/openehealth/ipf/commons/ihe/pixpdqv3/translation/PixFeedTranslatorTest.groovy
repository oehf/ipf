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

/**
 * Test for PIX Feed translator.
 * @author Marek Václavík, Dmytro Rud
 */
class PixFeedTranslatorTest extends Hl7TranslationTestContainer {
 
    @BeforeClass
    static void setUpClass() {
        doSetUp('pixfeed',
                new PixFeedRequest3to2Translator(),
                new PixFeedAck2to3Translator(),
                'pixfeed-translation.map')
    }        
  
    @Test
    void testMaximalMergeRequest() {
        doTestV3toV2RequestTranslation('PIX_FEED_MERGE_Maximal_Request', 8, 44)
    }
  
    @Test
    void testMaximalRegistrationRequest() {
        doTestV3toV2RequestTranslation('PIX_FEED_REG_Maximal_Request', 8, 44)
    }
  
    @Test
    void testMaximalRevRequest() {
        doTestV3toV2RequestTranslation('PIX_FEED_REV_Maximal_Request', 8, 44)
    }
  
}
