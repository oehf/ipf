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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti22


import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage


/**
 * Camel route for generic unit tests.
 * @author Dmytro Rud
 */
class Iti22TestRouteBuilder extends SpringRouteBuilder {

     def rsp = '''MSH|^~\\&|MESA_PD_SUPPLIER|PIM|MESA_PD_CONSUMER|MESA_DEPARTMENT|20090901140929||RSP^ZV2|356757|P|2.5
MSA|AA|1305506339
QAK|1486133081|OK
QPD|IHE PDQ Query|1486133081|@PID.11.3^KÃ¶ln
PID|1||79471^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI~78912^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI||MÃ¼ller^Hans||19400101|M|||Am Domplatz 1^^KÃ¶ln^^57000||022/235715~022/874491~01732356265|||||GLD-1-1^^^ANGID1&2.16.840.1.113883.3.37.4.1.5.2&ISO^AN|123-11-1234
PID|2||78001^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI||Mueller^Hans||19400101|M|||Teststr. 1^^KÃ¶ln^^57000||022/235715
PID|3||79653^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||MÃ¼ller^Hannes||19400101|M|||Am Domplatz 14^^KÃ¶ln^^57000||022/843274
PID|4||79233^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||MÃ¼ller^Joachim||19400101|M|||Am Domplatz 112^^KÃ¶ln^^57000||022/844275
     '''
     
     void configure() throws Exception {

         from('pdq-iti22://0.0.0.0:18220?audit=false')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 resultMessage(it).body = rsp
             }

         from('pdq-iti22://0.0.0.0:18221')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 resultMessage(it).body = rsp
             }

     }
}
 
