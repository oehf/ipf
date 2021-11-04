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
import org.openehealth.ipf.commons.core.modules.api.Predicate;

/**
 * Adapts a {@link Predicate}.
 * 
 * @author Martin Krasser
 */
public class PredicateAdapter extends AdapterSupport implements org.apache.camel.Predicate {

    private final Predicate predicate;
    
    /**
     * Creates a new {@link PredicateAdapter} and sets the delegate
     * {@link Predicate}.
     * 
     * @param predicate
     *            a predicate.
     */
    public PredicateAdapter(Predicate predicate) {
        this.predicate = predicate;
    }
    
    @Override // make use of co-variant return types to support DSL 
    public PredicateAdapter input(Expression inputExpression) {
        return (PredicateAdapter)super.input(inputExpression);
    }

    @Override // make use of co-variant return types to support DSL 
    public PredicateAdapter params(Expression paramsExpression) {
        return (PredicateAdapter)super.params(paramsExpression);
    }

    @Override // make use of co-variant return types to support DSL 
    public PredicateAdapter staticParams(Object... params) {
        return (PredicateAdapter)super.staticParams(params);
    }

    /**
     * Delegates matching to the delegate {@link Predicate} applying input- and
     * params {@link Expression}s.
     * 
     * @param exchange message exchange.
     * 
     * @see #input(Expression)
     * @see #params(Expression)
     * @see #staticParams(Object...)
     */
    @Override
    public boolean matches(Exchange exchange) {
        var input = adaptInput(exchange);
        var params = adaptParams(exchange);
        if (params == null) {
            return predicate.matches(input, (Object[])null);
        } else if (params.getClass().isArray()) {
            return predicate.matches(input, (Object[])params);
        } else {
            return predicate.matches(input, params);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.camel.Predicate#assertMatches(java.lang.String, java.lang.Object)
     */
    public void assertMatches(String text, Exchange exchange) throws AssertionError {
        if (!matches(exchange)) {
            throw new AssertionError(text);
        }
        
    }

}
