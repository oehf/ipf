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

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.modules.hl7.message.MessageUtils


class RouteBuilder extends SpringRouteBuilder {

     void configure() throws Exception {
         
         // normal processing without auditing
         from('xds-iti8://0.0.0.0:8887?audit=false')
             .process {
                 it.in.body = MessageUtils.ack(it.in.body.target)
             }

         // normal processing with auditing
         from('pix-iti8://0.0.0.0:8888')
             .process {
                 it.in.body = MessageUtils.ack(it.in.body.target)
             }
         
         // normal processing with support for incomplete auditing
         from('xds-iti8://0.0.0.0:8889?allowIncompleteAudit=true')
             .process {
                 it.in.body = MessageUtils.ack(it.in.body.target)
             }
         
         // fictive route to test producer-side acceptance checking
         from('pix-iti8://0.0.0.0:8890')
             .process {
                 it.in.body.MSH[9][1] = 'DOES NOT MATTER'
                 it.in.body.MSH[9][2] = 'SHOULD FAIL IN INTERCEPTORS'
             }

         // route with normal exception
         from('xds-iti8://0.0.0.0:8891')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 throw new Exception('Why do you cry, Willy?')
             }

         // route with runtime exception
         from('pix-iti8://0.0.0.0:8892')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 throw new RuntimeException('Jump over the lazy dog, you fox.')
             }
         
     }
}
 
