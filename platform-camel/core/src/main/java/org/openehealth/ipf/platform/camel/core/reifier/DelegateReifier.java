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
import org.apache.camel.Route;
import org.apache.camel.processor.Pipeline;
import org.apache.camel.reifier.ProcessorReifier;
import org.openehealth.ipf.platform.camel.core.model.DelegateDefinition;

import java.util.ArrayList;

/**
 * An {@link ProcessorReifier} that combines the {@link Processor} created by
 * {@link #doCreateDelegate()} and the child processor created by
 * {@link #createChildProcessor} into a {@link Pipeline}.
 * This base class supports the implementation of parameterizable DSL extensions
 * without forcing implementors to create DelegateProcessor instances.
 * Instead, plain {@link Processor} instances can be returned by
 * {@link #doCreateDelegate()} implementations.
 *
 * @author Martin Krasser
 */
public abstract class DelegateReifier<T extends DelegateDefinition> extends ProcessorReifier<T> {

    public DelegateReifier(Route route, T definition) {
        super(route, definition);
    }

    @Override
    public Processor createProcessor() throws Exception {
        var delegate = doCreateDelegate();
        var next = createChildProcessor(false);

        var processors = new ArrayList<Processor>();
        processors.add(delegate);
        if (next != null) {
            processors.add(next);
        }
        return Pipeline.newInstance(camelContext, processors);
    }

    /**
     * Creates a {@link Processor} for this DSL element.
     *
     * @return a {@link Processor} instance.
     */
    protected abstract Processor doCreateDelegate();

}
