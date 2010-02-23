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
package org.openehealth.ipf.tutorials.osgi.ihe.xds.iti15.route

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response


/**
 * @author Jens Riemschneider
 */
public class GroovyRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xds-iti15:xds-iti15-service1')
            .validate().iti15Request()
            .process { createResponse(it) }
            .validate().iti15Response()
    
        from('xds-iti15:xds-iti15-service2?audit=false')
            .process { createResponse(it) }
    }

    def createResponse(exchange) {
        Exchanges.resultMessage(exchange).body = new Response(SUCCESS)
    }
}

