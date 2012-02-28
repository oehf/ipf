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
package org.openehealth.ipf.platform.camel.ihe.xds.iti16

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsACamelValidators.*

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry

/**
 * @author Jens Riemschneider
 */
public class Iti16TestRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xds-iti16:xds-iti16-service1')
            .process(iti16RequestValidator())
            .process { checkValue(it, 'service 1') }
            .process(iti16ResponseValidator())
    
        from('xds-iti16:xds-iti16-service2')
            .process { checkValue(it, 'service 2') }
   }

    void checkValue(exchange, expected) {
        def value = exchange.in.getBody(QueryRegistry.class).query.sql        
        def response = new QueryResponse(expected == value ? SUCCESS : FAILURE)
        Exchanges.resultMessage(exchange).body = response
    }
}
