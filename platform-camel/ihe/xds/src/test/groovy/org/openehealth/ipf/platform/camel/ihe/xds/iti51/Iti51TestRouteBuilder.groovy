/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti51

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsForMultiplePatientsQuery
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti51RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti51ResponseValidator

import java.util.function.Function
/**
 * @author Jens Riemschneider
 * @author Michael Ottati
 */
class Iti51TestRouteBuilder extends RouteBuilder {
    void configure() throws Exception {
        from('xds-iti51:xds-iti51-service1')
            .id('service1route')
            .process(iti51RequestValidator())
            .process { checkValue(it, 'service 1') }
            .process(iti51ResponseValidator())
    
        from('xds-iti51:xds-iti51-service2')
            .process { checkValue(it, 'service 2') }


        from('xds-iti51:myIti51Service')
            .convertBodyTo(QueryRegistry.class)
            .choice()
                // Return an object reference for a find documents query
                .when().body({ body -> body.query instanceof FindDocumentsForMultiplePatientsQuery } as Function )
                    .process {
                        def response = new QueryResponse(SUCCESS)
                        response.references.add(new ObjectReference('document01'))
                        it.message.body = response
                    }
                // Any other query else is a failure
                .otherwise()
                    .process { it.message.body = new QueryResponse(FAILURE) }
   }

    def checkValue(exchange, expected) {
        def query = exchange.in.getBody(QueryRegistry.class).query
        def value = query.authorPersons[0]
        def response
        if (expected != value) {
            response = new QueryResponse(FAILURE)
        } else {
            response = SampleData.createQueryResponseWithLeafClass(
                    SUCCESS,
                    new Identifiable("id3", new AssigningAuthority("1.3")),
                    new Identifiable("id4", new AssigningAuthority("1.4")))
        }

        exchange.message.body = response
    }
}
