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
package org.openehealth.ipf.platform.camel.core.model;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.model.OutputType;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.process.Enricher;

/**
 * @see Enricher
 * 
 * @author Martin Krasser
 */
public class EnricherType extends OutputType<EnricherType> {

    private String resourceUri;
    
    private AggregationStrategy aggregationStrategy;
    
    public EnricherType(String resourceUri) {
        this(null, resourceUri);
    }
    
    public EnricherType(AggregationStrategy aggregationStrategy, String resourceUri) {
        this.aggregationStrategy = aggregationStrategy;
        this.resourceUri = resourceUri;
    }
    
    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Endpoint endpoint = routeContext.resolveEndpoint(resourceUri);
        Enricher enricher = new Enricher(null, endpoint.createProducer());
        if (aggregationStrategy == null) {
            enricher.setDefaultAggregationStrategy();
        } else {
            enricher.setAggregationStrategy(aggregationStrategy);
        }
        return enricher;
    }
    
}
