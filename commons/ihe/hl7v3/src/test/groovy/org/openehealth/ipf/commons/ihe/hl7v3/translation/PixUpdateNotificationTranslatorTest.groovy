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

import org.junit.*;

import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles;
import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.ITI_46
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters

/**
 * Test for PIX Update Notification translator.
 * @author Marek Václavík, Dmytro Rud
 */
class PixUpdateNotificationTranslatorTest extends Hl7TranslationTestContainer {

    @BeforeClass
    static void setUpClass() {
        doSetUp('pixupdatenotification',
                new PixAck3to2Translator(),
                new PixUpdateNotification2to3Translator())
    } 

    @Test
    void test1() {
        String v2notification = getFileContent('adt-a31-1', false, true)
        MessageAdapter msg = MessageAdapters.make(v2notification)
        String v3notification = v2tov3Translator.translateV2toV3(msg, null, 'UTF-8')
        V3_VALIDATOR.validate(v3notification, Hl7v3ValidationProfiles.getRequestValidationProfile(ITI_46))
    }
    
}
