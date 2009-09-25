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

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * Contract for classes that adapts the <i>modules API</i> to Apache Camel
 * interfaces.
 * 
 * @author Martin Krasser
 * @dsl platform-camel-core
 */
public interface Adapter {

    /**
     * Sets an {@link Expression} for obtaining input data from an
     * {@link Exchange}. Input data are passed to adapted
     * modules API implementations.
     * 
     * @param inputExpression
     *            expression for obtaining input data.
     * @return this object.
     * @dsl platform-camel-core
     * @ipfdoc Core features#Transmogrifier input
     */
    public Adapter input(Expression inputExpression);
    
    /**
     * Sets an expression {@link Closure} for obtaining input data from an
     * {@link Exchange}. Input data are passed to adapted
     * modules API implementations.
     * 
     * @param inputExpressionLogic
     *            expression for obtaining input data.
     * @return this object.
     * @dsl platform-camel-core
     * @ipfdoc Core features#Transmogrifier input
     */
    public Adapter input(Closure inputExpressionLogic);
    
    /**
     * Sets an {@link Expression} for obtaining input params from an
     * {@link Exchange}. Input params are passed to adapted
     * modules API implementations.
     * 
     * @param paramsExpression
     *            expression for obtaining input params.
     * @return this object.
     * @dsl platform-camel-core
     * @ipfdoc Core features#Transmogrifier input
     */
    public Adapter params(Expression paramsExpression);
    
    /**
     * Sets an expression {@link Closure} for obtaining input params from an
     * {@link Exchange}. Input params are passed to adapted
     * modules API implementations.
     * 
     * @param paramsExpressionLogic
     *            expression for obtaining input params.
     * @return this object.
     * @dsl platform-camel-core
     * @ipfdoc Core features#Transmogrifier input
     */
    public Adapter params(Closure paramsExpressionLogic);
    
    /**
     * Configures this adapter to use the given {@code params}
     * independent of the {@link Exchange} to be processed.
     * 
     * @param paramsExpression
     *            expression for obtaining input params.
     * @return this object.
     * @dsl platform-camel-core
     * @ipfdoc Core features#Transmogrifier input
     */
    public Adapter staticParams(Object... params);
    
}
