/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.pharm1

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDispensesQuery
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindMedicationListQuery
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.*

/**
 * Test routes for PHARM-1.
 * @author Jens Riemschneider
 * @author Quentin Ligier
 */
class Pharm1TestRouteBuilder extends RouteBuilder {
    void configure() throws Exception {
        from('cmpd-pharm1:cmpd-pharm1-service1')
            .id('service1route')
            .process(pharm1RequestValidator())
            .process { checkValue(it, 'service 1') }
            .process(pharm1ResponseValidator())

        from('cmpd-pharm1:cmpd-pharm1-service2')
            .process { checkValue(it, 'service 2') }

        // three endpoints intended for SOAP version check
        from('cmpd-pharm1:cmpd-pharm1-service21')
            .process { checkValue(it, 'implicit SOAP 1.2') }
        from('cmpd-pharm1:cmpd-pharm1-service22')
            .process { checkValue(it, 'SOAP 1.2') }
        from('cmpd-pharm1:cmpd-pharm1-service23')
            .process { checkValue(it, 'SOAP 1.1') }


        from('cmpd-pharm1:myPharm1Service?features=#loggingFeature')
            .convertBodyTo(QueryRegistry.class)
            .choice()
                // Return an object reference for a find dispenses query or a find medication list query
                .when { it.in.body.query instanceof FindDispensesQuery }
                    .process {
                        def response = new QueryResponse(SUCCESS)
                        response.references.add(new ObjectReference('document01'))
                        resultMessage(it).body = response
                    }
                .when { it.in.body.query instanceof FindMedicationListQuery }
                    .process {
                        def response = new QueryResponse(SUCCESS)
                        response.references.add(new ObjectReference('document01'))
                        resultMessage(it).body = response
                    }
                // Any other query else is a failure
            .otherwise()
                .process { resultMessage(it).body = new QueryResponse(FAILURE) }

        from('cmpd-pharm1:featuresTest?features=#policyFeature,#gzipFeature')
                .process(pharm1RequestValidator())
                .process { checkValue(it, 'service 1') }
                .process(pharm1ResponseValidator())

    }

    def checkValue(exchange, expected) {
        def query = exchange.in.getBody(QueryRegistry.class).query
        def value = query.authorPersons[0]
        def response = new QueryResponse(expected == value ? SUCCESS : FAILURE)
        resultMessage(exchange).body = response
    }
}
