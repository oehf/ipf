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

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindDocumentsQuery
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.ObjectReference
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status

/**
 * @author Jens Riemschneider
 */
class GroovyRouteBuilder extends SpringRouteBuilder {
    void configure() throws Exception {
        from('xds-iti18:xds-iti18-service1?audit=false')
            .validate().iti18Request()
            .process(new AdhocQueryProcessor('service 1'))
            .validate().iti18Response()
    
        from('xds-iti18:xds-iti18-service2')
            .process(new AdhocQueryProcessor('service 2'))

        from('xds-iti18:myIti18Service')
            .convertBodyTo(QueryRegistry.class)
            .choice()
                // Return an object reference for a find documents query
                .when { it.in.body.query instanceof FindDocumentsQuery }                    
                    .transform {                        
                        def response = new QueryResponse(Status.SUCCESS)
                        response.getReferences().add(new ObjectReference("document01"))
                        response
                    }
                // Any other query else is a failure
                .otherwise()
                    .transform { 
                        new QueryResponse(Status.FAILURE)
                    }
   }
}
