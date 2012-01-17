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
package org.openehealth.ipf.platform.camel.flow.model;

import org.apache.camel.CamelContext;
import org.apache.camel.model.FilterDefinition;
import org.apache.camel.model.language.ExpressionDefinition;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.processor.FilterProcessor;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.platform.camel.core.util.Contexts;
import org.openehealth.ipf.platform.camel.flow.dedupe.Dedupe;

/**
 * @author Martin Krasser
 */
public class DedupeDefinition extends FilterDefinition {

    public DedupeDefinition() {
        // configure a fictive expression to avoid NPE in
        // org.apache.camel.model.language.ExpressionDefinition.createExpression()
        super(new SimpleExpression());
    }

    @Override
    public FilterProcessor createProcessor(RouteContext routeContext) throws Exception {
        setExpression(new ExpressionDefinition(createDedupe(routeContext)));
        return super.createProcessor(routeContext);
    }

    private static Dedupe createDedupe(RouteContext routeContext) {
        CamelContext camelContext = routeContext.getCamelContext();
        Dedupe dedupe = Contexts.beanOrNull(Dedupe.class, camelContext);
        
        if (dedupe != null) {
            return dedupe;
        }
        
        dedupe = new Dedupe();
        dedupe.setFlowManager(Contexts.bean(FlowManager.class, camelContext));
        return dedupe;
    }
    
}
