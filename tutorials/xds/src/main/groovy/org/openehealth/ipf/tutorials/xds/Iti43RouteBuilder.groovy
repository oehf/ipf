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

import org.apache.camel.Exchange
import org.apache.camel.processor.aggregate.AbstractListAggregationStrategy

import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.*

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Route builder for ITI-43.
 * @author Jens Riemschneider
 */
class Iti43RouteBuilder extends SpringRouteBuilder {
    private final static Logger log = LoggerFactory.getLogger(Iti43RouteBuilder.class);
    
    @Override
    public void configure() throws Exception {
        errorHandler(noErrorHandler())
        
        // Entry point for Retrieve Document Set
        from('xds-iti43:xds-iti43')
            .log(log) { 'received iti43: ' + it.in.getBody(RetrieveDocumentSet.class) }
            // Validate and convert the request
    		.process(iti43RequestValidator())
    		.convertBodyTo(RetrieveDocumentSet.class)
    		// Retrieve each requested document and aggregate them in a list
    		.split { it.in.body.documents }
                .aggregationStrategy(new RetrievedDocumentAggregator())
    		    .retrieve()
                .end()
            // Create success response
            .transform {
                new RetrievedDocumentSet(Status.SUCCESS, it.in.body)
            }

	}

    private class RetrievedDocumentAggregator extends AbstractListAggregationStrategy<RetrievedDocument> {

        @Override
        RetrievedDocument getValue(Exchange exchange) {
            exchange.in.getBody(RetrievedDocument.class)
        }
    }
}
