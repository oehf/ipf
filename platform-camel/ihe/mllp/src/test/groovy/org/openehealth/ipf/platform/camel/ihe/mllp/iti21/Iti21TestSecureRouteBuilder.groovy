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


import org.apache.camel.builder.RouteBuilder

/**
 * Camel route for generic unit tests.
 * @author Dmytro Rud
 */
class Iti21TestSecureRouteBuilder extends RouteBuilder {

     def rsp = '''MSH|^~\\&|MESA_PD_SUPPLIER|PIM|MESA_PD_CONSUMER|MESA_DEPARTMENT|20090901140929||RSP^K22^RSP_K21|356757|P|2.5
MSA|AA|1305506339
QAK|1486133081|OK
QPD|IHE PDQ Query|1486133081|@PID.11.3^Köln
PID|1||79471^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI~78912^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI||Müller^Hans||19400101|M|||Am Domplatz 1^^Köln^^57000||022/235715~022/874491~01732356265|||||GLD-1-1^^^ANGID1&2.16.840.1.113883.3.37.4.1.5.2&ISO^AN|123-11-1234
PID|2||78001^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI||Mueller^Hans||19400101|M|||Teststr. 1^^Köln^^57000||022/235715
PID|3||79653^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||Müller^Hannes||19400101|M|||Am Domplatz 14^^Köln^^57000||022/843274
PID|4||79233^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||Müller^Joachim||19400101|M|||Am Domplatz 112^^Köln^^57000||022/844275
     '''
     
     void configure() throws Exception {

         from('pdq-iti21://0.0.0.0:18211?secure=true&sslContextParameters=#iti21SslContextRequireParameters')
             .transform(constant(rsp))

         from('pdq-iti21://0.0.0.0:18215?secure=true&sslContextParameters=#iti21SslContextParameters')
                 .transform(constant(rsp))

         from('pdq-iti21://0.0.0.0:18216?secure=true&sslContextParameters=#iti21SslContextTls13Parameters')
                 .transform(constant(rsp))

         from('pdq-iti21://0.0.0.0:18218?secure=true&sslContextParameters=#iti21SslContextCiphersParameters')
                 .transform(constant(rsp))

     }
}
 
