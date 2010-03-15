/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti55;

import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;

import javax.xml.datatype.Duration;
import javax.xml.datatype.DatatypeFactory;
import org.apache.camel.Message;
import org.apache.commons.io.IOUtils;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;


/**
 * Helper functions for ITI-55 tests.
 * @author Dmytro Rud
 */
class Iti55TestUtils {

    static String readFile(String fn) {
        InputStream stream = Iti55TestUtils.class.classLoader.getResourceAsStream(fn)
        return IOUtils.toString(stream)
    }
    
    static void setOutgoingTTL(Message message, int n) {
        message.headers[Iti55Component.XCPD_OUTPUT_TTL_HEADER_NAME] = 
            DatatypeFactory.newInstance().newDuration("P${n}Y")
    }
    
    static void testPositiveAckCode(String message) {
        GPathResult xml = new XmlSlurper().parseText(message)
        assert xml.acknowledgement.typeCode.@code == 'AA'
        assert xml.controlActProcess.queryAck.queryResponseCode.@code == 'OK'
    }
    
}
