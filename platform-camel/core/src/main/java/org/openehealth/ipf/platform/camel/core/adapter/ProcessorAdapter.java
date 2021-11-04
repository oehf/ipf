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
package org.openehealth.ipf.platform.camel.core.adapter;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;

/**
 * Abstract base class for classes that adapt <i>transform support library</i>
 * interfaces to Apache Camel's {@link Processor}.
 * 
 * @author Martin Krasser
 */
public abstract class ProcessorAdapter extends AdapterSupport implements Processor {

    @Override // make use of co-variant return types to support DSL 
    public ProcessorAdapter input(Expression inputExpression) {
        return (ProcessorAdapter)super.input(inputExpression);
    }

    @Override // make use of co-variant return types to support DSL 
    public ProcessorAdapter params(Expression paramsExpression) {
        return (ProcessorAdapter)super.params(paramsExpression);
    }

    @Override // make use of co-variant return types to support DSL 
    public ProcessorAdapter staticParams(Object... params) {
        return (ProcessorAdapter)super.staticParams(params);
    }

    /**
     * Dispatches the <code>exchange</code> to
     * {@link #doProcess(Exchange, Object, Object...)} implementations.
     * 
     * @see #input(Expression)
     * @see #params(Expression)
     * @see #staticParams(Object...)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        var input = adaptInput(exchange);
        var params = adaptParams(exchange);
        if (params == null) {
            doProcess(exchange, input, (Object[])null);
        } else if (params.getClass().isArray()) {
            doProcess(exchange, input, (Object[])params);
        } else {
            doProcess(exchange, input, params);
        }
    }

    /**
     * Processes input data and populates the output exchange.
     * 
     * @param exchange
     *            message exchange where to write processing results.
     * @param inputData
     *            input data.
     * @param inputParams
     *            input parameters.
     * @throws Exception
     *             if a processing error occurs.
     */
    protected abstract void doProcess(Exchange exchange, Object inputData, 
            Object... inputParams) throws Exception;
    
}
