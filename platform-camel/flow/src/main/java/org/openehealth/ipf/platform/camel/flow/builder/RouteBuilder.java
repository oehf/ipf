/*
 * Copyright 2008 the original author or authors.
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
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.flow.dedupe.Dedupe;
import org.openehealth.ipf.platform.camel.flow.process.FlowBeginProcessor;
import org.openehealth.ipf.platform.camel.flow.process.FlowEndProcessor;
import org.openehealth.ipf.platform.camel.flow.process.FlowErrorProcessor;
import org.openehealth.ipf.platform.camel.flow.process.Splitter;

/**
 * Route builder with support methods for flow management DSL extensions.
 * <p>
 * <strong>This route builder will be deprecated in one of the upcoming
 * releases.</strong>. It is recommended to use the {@link SpringRouteBuilder}
 * and {@link RouteHelper} instead.
 *
 * @author Martin Krasser
 */
public class RouteBuilder extends org.openehealth.ipf.platform.camel.core.builder.RouteBuilder {

    private final RouteHelper routeHelper;
    
    public RouteBuilder() {
        super();
        routeHelper = new RouteHelper(this);
    }
    
    public FlowBeginProcessor flowBegin(String identifier) {
        return routeHelper.flowBegin(identifier);
    }
    
    public FlowEndProcessor flowEnd() {
        return routeHelper.flowEnd();
    }

    public FlowErrorProcessor flowError() {
        return routeHelper.flowError();
    }

    public Dedupe dedupe() {
        return routeHelper.dedupe();
    }
    
    public Splitter split(Expression splitRule) {
        return routeHelper.split(splitRule);
    }

}
