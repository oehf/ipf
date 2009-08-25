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
package org.openehealth.ipf.platform.camel.ihe.pix.iti8

import static junit.framework.Assert.*
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter


/**
 * Helper methods for HL7-related unit tests.
 * 
 * @author Dmytro Rud
 */
class Hl7TestUtils {

    static void checkHappyCase(MessageAdapter msg) {
        assertTrue(msg.MSH[9][1].value == 'ACK')  
    }
    
    static void checkNAK(MessageAdapter msg) {
        assertTrue(msg.MSA[1].value[1] in ['R', 'E'])
        assertNotNull(msg.ERR)
    }
    
}
