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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti9

import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage
import java.io.File
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpComponent
import org.openehealth.ipf.modules.hl7.AckTypeCode

/**
 * Camel route for generic unit tests.
 * @author Dmytro Rud
 */
class Iti9TestRouteBuilder extends SpringRouteBuilder {

    def rsp = '''MSH|^~\\&|MESA_XREF|XYZ_HOSPITAL|MESA_PIX_CLIENT|MESA_DEPARTMENT|20090901141123||RSP^K23^RSP_K23|356813|P|2.5
         MSA|AA|10501108
         QAK|QRY10501108|OK
         QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10501108|79471^^^HZLN^PI|^^^KHKN~^^^&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO~^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO
         PID|1||79471^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI~78912^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI||~^S
             '''

    def rspWithoutPid = '''MSH|^~\\&|MESA_XREF|XYZ_HOSPITAL|MESA_PIX_CLIENT|MESA_DEPARTMENT|20090901141123||RSP^K23^RSP_K23|356813|P|2.5
         MSA|AA|10501108
         QAK|QRY10501108|OK
         QPD|||QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10501108|79471^^^HZLN^PI|^^^KHKN~^^^&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO~^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO
             '''
             
     void configure() throws Exception {

         from('pix-iti9://0.0.0.0:18091?audit=false')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 resultMessage(it).body = rsp
             }

         from('pix-iti9://0.0.0.0:18090')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 resultMessage(it).body = rspWithoutPid
             }
         
         from('pix-iti9://0.0.0.0:18092?allowIncompleteAudit=true')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 resultMessage(it).body = rspWithoutPid
             }

         // for automatic NAK 
         from('pix-iti9://0.0.0.0:18093')
             .process {
                 throw new RuntimeException('12345')
             }
         
         // for NAK with magic header
         from('pix-iti9://0.0.0.0:18094')
             .process {
                 it.out.body = null
                 it.out.headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AckTypeCode.AE
             }

     }
}
 
