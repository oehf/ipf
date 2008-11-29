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
package org.openehealth.ipf.platform.camel.core.util;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.openehealth.ipf.platform.camel.core.xml.MarkupBuilder;

/**
 * Utility related to Camel {@link Expression}s.
 * 
 * @author Martin Krasser
 */
public class Expressions {

    /**
     * Returns an {@link Expression} for the headers map of an {@link Exchange}'s
     * in-message.
     * 
     * @return an expression object which will return the headers map.
     */
    public static <E extends Exchange> Expression<E> headersExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                return exchange.getIn().getHeaders();
            }

            @Override
            public String toString() {
                return "headers()";
            }
        };
    }
    
    public static <E extends Exchange> Expression<E> builderExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                return MarkupBuilder.newInstance();
            }

            @Override
            public String toString() {
                return "headersAndBuilder()";
            }
        };
    }

    public static <E extends Exchange> Expression<E> headersAndBuilderExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                Object[] result = new Object[2];
                result [0] = exchange.getIn().getHeaders();
                result [1] = MarkupBuilder.newInstance();
                return result;
            }

            @Override
            public String toString() {
                return "headersAndBuilder()";
            }
        };
    }

}
