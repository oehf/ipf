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
package org.openehealth.ipf.tutorials.osgi.ihe.pix.iti8.route

import org.apache.camel.builder.RouteBuilder

import static org.openehealth.ipf.platform.camel.hl7.HL7v2.ack
import static org.openehealth.ipf.platform.camel.hl7.HL7v2.validatingProcessor


/**
 * @author Dmytro Rud
 * @author Boris Stanojevic
 */
class GroovyRouteBuilder extends RouteBuilder {

    void configure() throws Exception {

        // normal processing without auditing
        from('xds-iti8://0.0.0.0:8881?audit=false')
            .ack()

        // normal processing with auditing
        from('pix-iti8://0.0.0.0:8882')
            .process(validatingProcessor())
            .transform(ack())
            .process(validatingProcessor())
    }
}