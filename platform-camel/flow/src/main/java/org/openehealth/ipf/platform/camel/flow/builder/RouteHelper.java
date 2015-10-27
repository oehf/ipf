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
package org.openehealth.ipf.platform.camel.flow.builder;

import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.platform.camel.flow.dedupe.Dedupe;
import org.openehealth.ipf.platform.camel.flow.process.FlowBeginProcessor;
import org.openehealth.ipf.platform.camel.flow.process.FlowEndProcessor;
import org.openehealth.ipf.platform.camel.flow.process.FlowErrorProcessor;
import org.openehealth.ipf.platform.camel.flow.process.Splitter;

import java.util.Set;

/**
 * Helper class for creating IPF extensions in Java-based route definitions.
 * 
 * @author Martin Krasser
 */
public class RouteHelper {

    private RouteBuilder routeBuilder;
    
    public RouteHelper(RouteBuilder routeBuilder) {
        this.routeBuilder = routeBuilder;
    }
    
    public void setRouteBuilder(RouteBuilder routeBuilder) {
        this.routeBuilder = routeBuilder;
    }
    
    /**
     * Returns a new {@link FlowBeginProcessor} after assigning the
     * <code>identifier</code>. The returned processor is registered at the
     * {@link org.openehealth.ipf.platform.camel.flow.ReplayStrategyRegistry}.
     * 
     * @param identifier
     *            the identifier to set on the {@link FlowBeginProcessor}.
     * @return a new {@link FlowBeginProcessor}.
     */
    public FlowBeginProcessor flowBegin(String identifier) {
        FlowBeginProcessor processor = lookup(FlowBeginProcessor.class);
        processor.identifier(identifier).register();
        return processor;
    }
    
    /**
     * Returns a new {@link FlowEndProcessor}.
     * 
     * @return a new {@link FlowEndProcessor}.
     */
    public FlowEndProcessor flowEnd() {
        return lookup(FlowEndProcessor.class);
    }

    /**
     * Returns a new {@link FlowErrorProcessor}.
     * 
     * @return a new {@link FlowErrorProcessor}.
     */
    public FlowErrorProcessor flowError() {
        return lookup(FlowErrorProcessor.class);
    }

    /**
     * Returns a new {@link Dedupe}.
     * 
     * @return a new {@link Dedupe}.
     */
    public Dedupe dedupe() {
        return lookup(Dedupe.class);
    }
    
    // ----------------------------------------------------------------
    //  Flow management-specific message processors
    // ----------------------------------------------------------------
    
    /**
     * Returns a new {@link Splitter}
     * 
     * @param splitRule
     *          expression that performs the splitting of the original exchange
     * @return the new {@link Splitter}
     */
    public Splitter split(Expression splitRule) {
        return new Splitter(splitRule, null);
    }

    private <T> T lookup(Class<T> type) {
        Set<T> processors = routeBuilder.getContext().getRegistry().findByType(type);
        if (processors.isEmpty() || processors.size() > 1) {
            throw new IllegalArgumentException("Must have exactly one processor of type " + type.getName() + ", but found " + processors.size());
        } else {
            T processor = processors.iterator().next();
            return processor;
        }

    }
}
