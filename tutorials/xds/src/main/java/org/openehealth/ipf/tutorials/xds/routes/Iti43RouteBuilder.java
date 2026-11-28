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
package org.openehealth.ipf.tutorials.xds.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AbstractListAggregationStrategy;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;

import java.util.List;

import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti43RequestValidator;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.logExchange;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.retrieve;

/**
 * Route builder for ITI-43.
 *
 * @author Jens Riemschneider
 */
@Slf4j
public class Iti43RouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        errorHandler(noErrorHandler());

        // Entry point for Retrieve Document Set
        from("xds-iti43:xds-iti43")
            .process(logExchange(log, exchange -> "received iti43: " + exchange.getIn().getBody(RetrieveDocumentSet.class)))
            // Validate and convert the request
            .process(iti43RequestValidator())
            .convertBodyTo(RetrieveDocumentSet.class)
            // Retrieve each requested document and aggregate them in a list
            .split().body(RetrieveDocumentSet.class, RetrieveDocumentSet::getDocuments)
                .aggregationStrategy(new RetrievedDocumentAggregator())
                .process(retrieve())
            .end()
            // Create success response
            .transform().body(List.class, documents -> new RetrievedDocumentSet(Status.SUCCESS, retrieved(documents)));
    }

    @SuppressWarnings("unchecked")
    private static List<RetrievedDocument> retrieved(List<?> documents) {
        // what the RetrievedDocumentAggregator collected
        return (List<RetrievedDocument>) documents;
    }

    private static class RetrievedDocumentAggregator extends AbstractListAggregationStrategy<RetrievedDocument> {
        @Override
        public RetrievedDocument getValue(Exchange exchange) {
            return exchange.getIn().getBody(RetrievedDocument.class);
        }
    }
}
