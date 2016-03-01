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
package org.openehealth.ipf.platform.camel.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;

import static org.apache.camel.builder.ExpressionBuilder.headerExpression;
import static org.openehealth.ipf.platform.camel.core.util.Expressions.builderExpression;
import static org.openehealth.ipf.platform.camel.core.util.Expressions.headersAndBuilderExpression;
import static org.openehealth.ipf.platform.camel.core.util.Expressions.headersExpression;

/**
 * @author Martin Krasser
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ParamsDefinition {

    @XmlElementRef
    private final ProcessorAdapterDefinition processorAdapterDefinition;

    public ParamsDefinition() {
        this(null);
    }

    public ParamsDefinition(ProcessorAdapterDefinition processorAdapterDefinition) {
        this.processorAdapterDefinition = processorAdapterDefinition;
    }
    
    public ProcessorAdapterDefinition builder() {
        return processorAdapterDefinition.params(builderExpression());
    }

    public ProcessorAdapterDefinition headers() {
        return processorAdapterDefinition.params(headersExpression());
    }

    public ProcessorAdapterDefinition header(String name) {
        return processorAdapterDefinition.params(headerExpression(name));
    }

    public ProcessorAdapterDefinition headersAndBuilder() {
        return processorAdapterDefinition.params(headersAndBuilderExpression());
    }

}
