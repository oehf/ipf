/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.tutorials.osgi.ihe.xds.iti62.route

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.core.util.Exchanges

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti62RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti62ResponseValidator

/**
 * @author Boris Stanojevic
 */
public class GroovyRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xds-iti62:xds-iti62-service1')
            .process(iti62RequestValidator())
            .process { createResponse(it) }
            .process(iti62ResponseValidator())
            

        from('xds-iti62:xds-iti62-service2?audit=false')
            .process { createResponse(it) }
    }

    def createResponse(exchange) {
        Exchanges.resultMessage(exchange).body = new Response(SUCCESS)
    }
}
