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
package org.openehealth.ipf.platform.camel.ihe.xds.iti18

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.*

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference
import org.openehealth.ipf.platform.camel.core.util.Exchanges

/**
 * @author Jens Riemschneider
 */
class Iti18TestRouteBuilder extends SpringRouteBuilder {
    void configure() throws Exception {
        from('xds-iti18:xds-iti18-service1')
            .id('service1route')
            .process(iti18RequestValidator())
            .process { checkValue(it, 'service 1') }
            .process(iti18ResponseValidator())
    
        from('xds-iti18:xds-iti18-service2')
            .process { checkValue(it, 'service 2') }

        // three endpoints intended for SOAP version check
        from('xds-iti18:xds-iti18-service21')
            .process { checkValue(it, 'implicit SOAP 1.2') }
        from('xds-iti18:xds-iti18-service22')
            .process { checkValue(it, 'SOAP 1.2') }
        from('xds-iti18:xds-iti18-service23')
            .process { checkValue(it, 'SOAP 1.1') }
           
            
        from('xds-iti18:myIti18Service')
            .convertBodyTo(QueryRegistry.class)
            .choice()
                // Return an object reference for a find documents query
                .when { it.in.body.query instanceof FindDocumentsQuery }                    
                    .transform {                        
                        def response = new QueryResponse(SUCCESS)
                        response.references.add(new ObjectReference('document01'))
                        response
                    }
                // Any other query else is a failure
                .otherwise()
                    .transform { new QueryResponse(FAILURE) }
   }

    def checkValue(exchange, expected) {
        def query = exchange.in.getBody(QueryRegistry.class).query
        def value = query.authorPersons[0]        
        def response = new QueryResponse(expected == value ? SUCCESS : FAILURE)
        Exchanges.resultMessage(exchange).body = response
    }
}
