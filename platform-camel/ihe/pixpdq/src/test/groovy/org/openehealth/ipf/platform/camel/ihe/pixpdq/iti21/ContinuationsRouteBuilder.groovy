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
package org.openehealth.ipf.platform.camel.ihe.pixpdq.iti21

import org.apache.camel.spring.SpringRouteBuilder;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;

/**
 * Camel route for continuations unit tests.
 * @author Dmytro Rud
 */
class ContinuationsRouteBuilder extends SpringRouteBuilder {

     static final String BIG_RESPONSE =
         'MSH|^~\\&|MESA_PD_SUPPLIER|PIM|MESA_PD_CONSUMER|MESA_DEPARTMENT|' + 
             '20090901140929||RSP^K22^RSP_K21|356757|P|2.5|||\r' +
         'MSA|AA|12345\r' +
         'QAK|1486133081|OK\n' +
         'QPD|IHE PDQ Query|1486133081|@PID.11.3^KÃ¶ln\n' +
         'PID|1||79471^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&' + 
             'ISO^PI~78912^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI|' + 
             '|MÃ¼ller^Hans||19400101|M|||Am Domplatz 1^^KÃ¶ln^^57000|' + 
             '|022/235715~022/874491~01732356265||||' +
             '|GLD-1-1^^^ANGID1&2.16.840.1.113883.3.37.4.1.5.2&ISO^AN|123-11-1234\r' +
         'PID|2||78001^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI|' +
             '|Mueller^Hans||19400101|M|||Teststr. 1^^KÃ¶ln^^57000||022/235715\r' +
         'PID|3||79653^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI|' +
             '|MÃ¼ller^Hannes||19400101|M|||Am Domplatz 14^^KÃ¶ln^^57000||022/843274\r' +
         'PID|4||79233^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI|' +
             '|MÃ¼ller^Joachim||19400101|M|||Am Domplatz 112^^KÃ¶ln^^57000||022/844275\r';

     
    void configure() throws Exception {
        from('pdq-iti21://0.0.0.0:18219' + 
                '?supportInteractiveContinuation=true&interactiveContinuationDefaultThreshold=3' + 
                '&supportSegmentFragmentation=true&segmentFragmentationThreshold=20' + 
                '&supportUnsolicitedFragmentation=true')
            .onException(Exception.class)
                .maximumRedeliveries(0)
                .end()
            .process {
                resultMessage(it).body = BIG_RESPONSE
            }
    }
}
 
