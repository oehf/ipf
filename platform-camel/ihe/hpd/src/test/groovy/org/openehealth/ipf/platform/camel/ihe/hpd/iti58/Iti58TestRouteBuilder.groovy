/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hpd.iti58

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse

/**
 * @author Dmytro Rud
 */
class Iti58TestRouteBuilder extends RouteBuilder {

    void configure() throws Exception {
        from('hpd-iti58:hpd-service1')
            .process {
                it.out.body = new BatchResponse()
            }
    }
}
