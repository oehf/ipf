/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.core.builder;

import groovy.lang.Closure;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.core.adapter.AggregatorAdapter;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingAggregator;

/**
 *
 */
public class LegacyRouteHelper extends RouteHelper {

    public LegacyRouteHelper(SpringRouteBuilder routeBuilder) {
        super(routeBuilder);
    }


    /**
     * Creates a new {@link org.openehealth.ipf.platform.camel.core.adapter.AggregatorAdapter} that adapts the given
     * <code>closure aggregator</code>.
     *
     * @param closure
     *            an aggregator closure.
     * @return an adapted aggregator.
     */
    public AggregatorAdapter aggregationStrategy(Closure closure) {
        return aggregationStrategy(new DelegatingAggregator(closure));
    }
}
