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
package org.openehealth.ipf.platform.camel.ihe.mllp

import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpMarshalUtils;
/**
 * @author Dmytro Rud
 */
class TestMllpMarshalUtils {
    
     @Test
     void testEnsureMaximalSegmentsLength() {
         String original = 
             'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|' + 
             '20081031112704||QBP^Q22|324406609|P|2.5|||ER\r' +
                 'QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~' + 
             '@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\r' +
                 'RCP|I|10^RD|||||\r'
                 
         String splittedAtLength10 = 
             'MSH|^~\\&|M\r' +
             'ADD|ESA_PD\r' +
             'ADD|_CONSU\r' +
             'ADD|MER|ME\r' +
             'ADD|SA_DEP\r' +
             'ADD|ARTMEN\r' +
             'ADD|T|MESA\r' +
             'ADD|_PD_SU\r' +
             'ADD|PPLIER\r' +
             'ADD||PIM|2\r' +
             'ADD|008103\r' +
             'ADD|111270\r' +
             'ADD|4||QBP\r' +
             'ADD|^Q22|3\r' +
             'ADD|244066\r' +
             'ADD|09|P|2\r' +
             'ADD|.5|||E\r' +
             'ADD|R\r'      +
             'QPD|IHE PD\r' +
             'ADD|Q Quer\r' +
             'ADD|y|1402\r' +
             'ADD|274727\r' +
             'ADD||@PID.\r' +
             'ADD|3.1^12\r' +
             'ADD|345678\r' +
             'ADD|~@PID.\r' +
             'ADD|3.2.1^\r' +
             'ADD|BLABLA\r' +
             'ADD|~@PID.\r' +
             'ADD|3.4.2^\r' +
             'ADD|1.2.3.\r' +
             'ADD|4~@PID\r' +
             'ADD|.3.4.3\r' +
             'ADD|^KRYSO\r' +
             'ADD||||||\r'  +
             'RCP|I|10^R\r' +
             'ADD|D|||||\r'

         assert splittedAtLength10 == MllpMarshalUtils.ensureMaximalSegmentsLength(original, 10)
         assert original == MllpMarshalUtils.ensureMaximalSegmentsLength(original, 1000)
     }
}
