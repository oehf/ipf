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
package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.Processor;
import org.apache.camel.processor.Pipeline;
import org.apache.camel.reifier.ProcessorReifier;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.model.DelegateDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link ProcessorReifier} that combines the {@link Processor} created by
 * {@link #doCreateDelegate(RouteContext)} and the child processor created by
 * {@link #createChildProcessor} into a {@link Pipeline}.
 * This base class supports the implementation of parameterizable DSL extensions
 * without forcing implementors to create DelegateProcessor instances.
 * Instead, plain {@link Processor} instances can be returned by
 * {@link #doCreateDelegate(RouteContext)} implementations.
 *
 * @author Martin Krasser
 */
public abstract class DelegateReifier<T extends DelegateDefinition> extends ProcessorReifier<T> {

    public DelegateReifier(T definition) {
        super(definition);
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor delegate = doCreateDelegate(routeContext);
        Processor next = createChildProcessor(routeContext, false);
        
        List<Processor> processors = new ArrayList<>();
        processors.add(delegate);
        if (next != null) {
            processors.add(next);
        }
        return Pipeline.newInstance(routeContext.getCamelContext(), processors);
    }

    /**
     * Creates a {@link Processor} for this DSL element.
     * 
     * @param routeContext
     *            the current route context.
     * @return a {@link Processor} instance.
     * @throws Exception
     */
    protected abstract Processor doCreateDelegate(RouteContext routeContext);
    
}
