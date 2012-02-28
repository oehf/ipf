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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8

import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.apache.camel.spring.SpringRouteBuilder
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage
import org.openehealth.ipf.commons.core.modules.api.ValidationException
import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators.*


/**
 * Camel route for validation-related unit tests.
 * 
 * @author Dmytro Rud
 */
class Iti8TestValidationRouteBuilder extends SpringRouteBuilder {

     void configure() throws Exception {

         // no error handling
         from('xds-iti8://0.0.0.0:18080?audit=false')
             .onException(ValidationException.class)
                 .maximumRedeliveries(0)
                 .end()
             .process(iti8RequestValidator())
             .process {
                 resultMessage(it).body = MessageUtils.ack(it.in.body.target)
             }
             .process(iti8ResponseValidator())
             
             
         // manual ACK generation on error
         from('xds-iti8://0.0.0.0:18089?audit=false')
             .onException(ValidationException.class)
                 .handled(true)
                 .process {
                     resultMessage(it).body = MessageUtils.ack(it.in.body.target) 
                 }
                 .end()
             .process(iti8RequestValidator())
             .process {
                 throw new RuntimeException('SHOULD NOT BE THROWN')
             }
             .process(iti8ResponseValidator())
             
     }
}
 
