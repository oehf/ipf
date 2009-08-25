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

import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage

/**
 * Camel route for validation-related unit tests.
 * 
 * @author Dmytro Rud
 */
class ValidationRouteBuilder extends SpringRouteBuilder {

     void configure() throws Exception {

         from('xds-iti8://0.0.0.0:9999?audit=false')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .validate().iti8Request()
             .process {
                 resultMessage(it).body = MessageUtils.ack(it.in.body.target)
             }
             .validate().iti8Response()
             
     }
}
 
