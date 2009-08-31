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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14

import static org.openehealth.ipf.commons.ihe.xds.responses.Status.*

import org.apache.camel.spring.SpringRouteBuilder

import org.openehealth.ipf.commons.ihe.xds.responses.Response
import org.openehealth.ipf.commons.ihe.xds.requests.RegisterDocumentSet
import org.openehealth.ipf.platform.camel.core.util.Exchanges

/**
 * @author Jens Riemschneider
 */
public class GroovyRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xds-iti14:xds-iti14-service1')
            .validate().iti14Request()
            .process { checkValue(it, 'service 1') }
            .validate().iti14Response()
            
        from('xds-iti14:xds-iti14-service2')
            .process { checkValue(it, 'service 2') }

        from('xds-iti14:xds-iti14-service12?audit=false&allowIncompleteAudit=true')
            .process { checkValue(it, 'service 12') }
    
        from('xds-iti14:xds-iti14-service13?allowIncompleteAudit=true')
            .process { checkValue(it, 'service 13') }
    }

    def checkValue(exchange, expected) { 
        def actual = exchange.in.getBody(RegisterDocumentSet.class).documentEntries[0].comments.value        
        Exchanges.resultMessage(exchange).body = new Response(expected == actual ? SUCCESS : FAILURE)
    }
}
