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
package org.openehealth.ipf.platform.camel.flow.model;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.platform.camel.flow.process.Splitter;

/**
 * {@link ProcessorDefinition} for the {@link Splitter} processor of the flow manager
 * This class is needed to create a {@link Splitter} that hands on the
 * sub exchanges to a specified processor. The {@link Splitter} requires 
 * explicit definition of this processor because it has to send multiple 
 * exchanges to the processor. Usually processors only send a single exchange
 * to the next processor in the route, which is done automatically by Camel.
 * This class extends the core model {@link org.openehealth.ipf.platform.camel.core.model.SplitterType}
 * with flow manager specific functionality, especially bookkeeping of the
 * split history.
 * 
 * @author Jens Riemschneider
 */
public class SplitterType extends
        org.openehealth.ipf.platform.camel.core.model.SplitterType {

    /**
     * Creates a split type, i.e. a builder for {@link Splitter}
     * @param expression
     *          The expression to be passed to the {@link Splitter} upon 
     *          creation
     */
    public SplitterType(Expression expression) {
        super(expression);
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.core.model.SplitterType#createSplitterInstance(org.apache.camel.Expression, org.apache.camel.Processor)
     */
    @Override
    protected Splitter createSplitterInstance(Expression expression, Processor processor) {
        return new Splitter(expression, processor);
    }
}
