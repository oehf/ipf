/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.dispatch

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.platform.camel.hl7.HL7v2

import static org.openehealth.ipf.platform.camel.hl7.HL7v2.ack
import static org.openehealth.ipf.platform.camel.hl7.HL7v2.validatingProcessor

/**
 * @author Dmytro Rud
 */
class DispatchRouteBuilder2 extends RouteBuilder {

    @Override
    void configure() throws Exception {
        from('mllp-dispatch://0.0.0.0:18503?dispatcher=#dispatcher')
                .process {}

        from('pix-iti8://0.0.0.0:18504?dispatcher=#dispatcher')
                .routeId('pixfeed') // actually we don't need a dedicated route id
                .process(validatingProcessor())
                .transform(ack())
                .process(validatingProcessor())

        from('xpid-iti64://0.0.0.0:18505?dispatcher=#dispatcher')
                .routeId('xadpid') // actually we don't need a dedicated route id
                .process(validatingProcessor())
                .transform(ack())
                .process(validatingProcessor())

    }

}
