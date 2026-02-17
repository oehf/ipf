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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti44

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * @author Dmytro Rud
 */
class Iti44TestRouteBuilder extends RouteBuilder {

    private static final String ACK =
            StandardTestContainer.readFile('translation/pixfeed/v3/Ack.xml')

    @Override
    public void configure() throws Exception {
        from('pixv3-iti44:pixv3-iti44-service1')
            .process(PixPdqV3CamelValidators.iti44RequestValidator())
            .setBody(constant(ACK))
            .process(PixPdqV3CamelValidators.iti44ResponseValidator())

        from('xds-iti44:xds-iti44-service1')
            .process(PixPdqV3CamelValidators.iti44RequestValidator())
            .setBody(constant(ACK))
            .process(PixPdqV3CamelValidators.iti44ResponseValidator())

        from('pixv3-iti44:pixv3-iti44-service2')
            .throwException(new RuntimeException('The queue is full'))

    }
}
