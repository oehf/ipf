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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti21

import ca.uhn.hl7v2.AcknowledgmentCode
import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.openehealth.ipf.platform.camel.hl7.HL7v2.ack

/**
 * Camel route for generic unit tests.
 * @author Dmytro Rud
 */
class Iti21TestRouteBuilder extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(Iti21TestRouteBuilder)

     def rsp = '''MSH|^~\\&|MESA_PD_SUPPLIER|PIM|MESA_PD_CONSUMER|MESA_DEPARTMENT|20090901140929||RSP^K22^RSP_K21|356757|P|2.5
MSA|AA|1305506339
QAK|1486133081|OK
QPD|IHE PDQ Query|1486133081|@PID.11.3^KÃ¶ln
PID|1||79471^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI~78912^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI||MÃ¼ller^Hans||19400101|M|||Am Domplatz 1^^KÃ¶ln^^57000||022/235715~022/874491~01732356265|||||GLD-1-1^^^ANGID1&2.16.840.1.113883.3.37.4.1.5.2&ISO^AN|123-11-1234
PID|2||78001^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI||Mueller^Hans||19400101|M|||Teststr. 1^^KÃ¶ln^^57000||022/235715
PID|3||79653^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||MÃ¼ller^Hannes||19400101|M|||Am Domplatz 14^^KÃ¶ln^^57000||022/843274
PID|4||79233^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||MÃ¼ller^Joachim||19400101|M|||Am Domplatz 112^^KÃ¶ln^^57000||022/844275
     '''
     
     void configure() throws Exception {

         from('pdq-iti21://0.0.0.0:18887?audit=false')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .transform(constant(rsp))

         from('pdq-iti21://0.0.0.0:18210')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .transform(constant(rsp))

         from('pdq-iti21://0.0.0.0:18211?secure=true&sslContextParameters=#sslContextParameters')
             .transform(constant(rsp))

         // for cancel messages
         from('pdq-iti21://0.0.0.0:18212')
             .transform(ack())
             
         // for automatic NAK 
         from('pdq-iti21://0.0.0.0:18213')
             .process {
                 throw new RuntimeException('12345')
             }

         from('pdq-iti21://0.0.0.0:18214?interceptorFactories=#dummyInterceptor,#authenticationInterceptor')
                 .transform(constant(rsp))

         // for NAK with magic header
         from('pdq-iti21://0.0.0.0:18219')
             .process {
                 it.out.body = null
                 it.out.headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AcknowledgmentCode.AE
             }

         // wait "QPD-2" seconds and properly respond with a valid answer
         from('pdq-iti21://0.0.0.0:18220')
                 .process {
                     int wait = Long.parseLong(it.in.body.QPD[2].value)
                     LOG.debug("Waiting $wait ms")
                     Thread.sleep(wait)
                 }
                 .transmogrify {
                     def response = MessageUtils.response(it, 'RSP', 'K22')
                     response.QPD = it.QPD
                     response.QAK[1] = response.QPD[2].value
                     response.MSA[1] = 'AA'
                     LOG.debug("Now responding after waiting ${response.QAK[1].value} ms")
                     response
                 }

         /*
         from('pdq-iti21://0.0.0.0:18225?interceptorFactories=#receiveTracingData')
                 .to("mock:trace")
                 .transform(constant(rsp))
                 */
     }
}
 
