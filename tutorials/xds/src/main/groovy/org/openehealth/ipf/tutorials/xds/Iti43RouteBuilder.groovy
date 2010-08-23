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
package org.openehealth.ipf.tutorials.xds

import org.apache.camel.spring.SpringRouteBuilder
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.*

/**
 * Route builder for ITI-43.
 * @author Jens Riemschneider
 */
class Iti43RouteBuilder extends SpringRouteBuilder {
    private final static Log log = LogFactory.getLog(Iti43RouteBuilder.class);
    
    @Override
    public void configure() throws Exception {
        errorHandler(noErrorHandler())
        
        // Entry point for Retrieve Document Set
        from('xds-iti43:xds-iti43')
            .log(log) { 'received iti43: ' + it.in.getBody(RetrieveDocumentSet.class) }
            // Validate and convert the request
    		.validate().iti43Request()
    		.convertBodyTo(RetrieveDocumentSet.class)
    		// Retrieve each requested document and aggregate them in a list
    		.ipf().split { it.in.body.documents }
    		.aggregationStrategy { target, next -> target.out.body.addAll(next.out.body) }
    		.retrieve()    		
    		.end()
            // Create success response
            .transform { new RetrievedDocumentSet(Status.SUCCESS, it.in.body) }
	}
}
