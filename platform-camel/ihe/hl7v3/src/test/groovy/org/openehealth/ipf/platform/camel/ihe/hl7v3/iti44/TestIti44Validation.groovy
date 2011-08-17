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

import org.junit.Test
import org.apache.commons.io.IOUtils
import org.openehealth.ipf.commons.xml.CombinedXmlValidator
import org.openehealth.ipf.commons.xml.CombinedXmlValidationProfile
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId

/**
 * @author Dmytro Rud
 */
class TestIti44Validation {

    private String readFile(String fn) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fn)
        String s = IOUtils.toString(inputStream)
        return s
    }

    @Test
    void testIti44Validation() {
        CombinedXmlValidator validator = new CombinedXmlValidator()
        CombinedXmlValidationProfile validationProfile =
            Hl7v3ValidationProfiles.getRequestValidationProfile(IpfInteractionId.ITI_44_PIX)
        String message

        // 301
        message = readFile('iti44/PIX_FEED_REG_Maximal_Request.xml')
        validator.validate(message, validationProfile)

        // 302
        message = readFile('iti44/PIX_FEED_REV_Maximal_Request.xml')
        validator.validate(message, validationProfile)

        // 304
        message = readFile('iti44/PIX_FEED_MERGE_Maximal_Request.xml')
        validator.validate(message, validationProfile)
    }
}
