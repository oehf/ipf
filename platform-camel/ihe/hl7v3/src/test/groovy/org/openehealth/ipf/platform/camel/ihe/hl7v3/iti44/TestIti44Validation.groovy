/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti44


import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hl7v3.PIXV3
import org.openehealth.ipf.commons.xml.CombinedXmlValidationProfile
import org.openehealth.ipf.commons.xml.CombinedXmlValidator
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * @author Dmytro Rud
 */
class TestIti44Validation {

    private static String readFile(String id) {
        return StandardTestContainer.readFile("translation/pixfeed/v3/PIX_FEED_${id}_Maximal_Request.xml")
    }

    @Test
    void testIti44Validation() {
        CombinedXmlValidator validator = new CombinedXmlValidator()
        CombinedXmlValidationProfile validationProfile =
            PIXV3.Interactions.ITI_44_PIX.requestValidationProfile
        String message

        // 301
        message = readFile('REG')
        validator.validate(message, validationProfile)

        // 302
        message = readFile('REV')
        validator.validate(message, validationProfile)

        // 304
        message = readFile('MERGE')
        validator.validate(message, validationProfile)
    }
}
