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
package org.openehealth.ipf.tutorials.osgi.route.web

import org.apache.camel.builder.RouteBuilder

/**
 * @author Martin Krasser
 */
public class SampleRouteBuilder extends RouteBuilder {

     void configure() {

         from('jetty:http://0.0.0.0:8080/tutorial')
             .initFlow(this.class.package.name)
                 .application('osgi-web')
                 .renderer('initRenderer')
             .unmarshal().hl7()
             .verify().hl7()
             .transmogrify('admissionTransmogrifier')
             .dedupeFlow()
             .marshal().hl7()
             .inOnly()
             .to('jms:queue:delivery-web')
             
         from('jms:queue:delivery-web')
             .setFilename('output.hl7')
             .to('file:workspace/output')
             .ackFlow().renderer('ackRenderer')

     }
    
}
