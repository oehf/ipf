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

import groovy.lang.Closure;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression;

import static org.apache.camel.builder.Builder.body;

/**
 * Abstract base class for classes that adapt <i>transform support library</i>
 * interfaces to Apache Camel interfaces.
 * 
 * @author Martin Krasser
 */
public abstract class AdapterSupport implements Adapter {

    private Expression inputExpression;
    private Expression paramsExpression;
    
    /**
     * Creates an adapter that by default takes input data from the body of the
     * in-message. 
     * 
     * @see Exchange#getIn()
     * @see Message#getBody()
     */
    public AdapterSupport() {
        inputExpression = body();
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.core.adapter.Adapter#input(org.apache.camel.Expression)
     */
    @Override
    public Adapter input(Expression inputExpression) {
        this.inputExpression = inputExpression;
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.core.adapter.Adapter#input(groovy.lang.Closure)
     */
    @Override
    public Adapter input(@ClosureParams(value = SimpleType.class, options = { "org.apache.camel.Expression"})
                                     Closure inputExpressionLogic) {
        return input(new DelegatingExpression(inputExpressionLogic));
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.core.adapter.Adapter#params(org.apache.camel.Expression)
     */
    @Override
    public Adapter params(Expression paramsExpression) {
        this.paramsExpression = paramsExpression;
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.core.adapter.Adapter#params(groovy.lang.Closure)
     */
    @Override
    public Adapter params(@ClosureParams(value = SimpleType.class, options = { "org.apache.camel.Expression"})
                                      Closure paramsExpressionLogic) {
        return params(new DelegatingExpression(paramsExpressionLogic));
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.core.adapter.Adapter#staticParams(java.lang.Object[])
     */
    @Override
    public Adapter staticParams(Object... params) {
        paramsExpression = new StaticParams(params);
        return this;
    }
    
    /**
     * Applies the {@link Expression} set by {@link #input(Expression)} to
     * obtain input data from the <code>exchange</code>.
     * 
     * @param exchange
     *            message exchange.
     * @return input data or <code>null</code> if the expression evaluates to
     *         <code>null</code> or the expression object is <code>null</code>.
     */
    protected Object adaptInput(Exchange exchange) {
        if (inputExpression == null) {
            return null;
        }
        return inputExpression.evaluate(exchange, Object.class);
    }

    /**
     * Applies the {@link Expression} set by {@link #params(Expression)} (or
     * implicitly set by {@link #staticParams(Object...)}) to obtain input
     * params from the <code>exchange</code>.
     * 
     * @param exchange
     *            message exchange.
     * @return input data or <code>null</code> if the expression evaluates to
     *         <code>null</code> or the expression object is <code>null</code>.
     */
    protected Object adaptParams(Exchange exchange) {
        if (paramsExpression == null) {
            return null;
        }
        return paramsExpression.evaluate(exchange, Object.class);
    }

}
