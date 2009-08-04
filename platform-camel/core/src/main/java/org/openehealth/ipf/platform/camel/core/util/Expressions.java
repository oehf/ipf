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
    public static <E extends Exchange> Expression headersExpression() {
        return new Expression() {
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return (T)exchange.getIn().getHeaders();
            }

            @Override
            public String toString() {
                return "headers()";
            }
        };
    }
    
    public static Expression builderExpression() {
        return new Expression() {
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return (T)MarkupBuilder.newInstance();
            }

            @Override
            public String toString() {
                return "headersAndBuilder()";
            }
        };
    }

    public static <E extends Exchange> Expression headersAndBuilderExpression() {
        return new Expression() {
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                Object[] result = new Object[2];
                result [0] = exchange.getIn().getHeaders();
                result [1] = MarkupBuilder.newInstance();
                return (T)result;
            }

            @Override
            public String toString() {
                return "headersAndBuilder()";
            }
        };
    }

    public static Expression exceptionObjectExpression() {
        return new Expression() {
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return (T)exception(exchange);
            }
        };
    }
    
    public static Expression exceptionMessageExpression() {
        return new Expression() {
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                Throwable throwable = exception(exchange);
                return (T)(throwable == null ? null : throwable.getMessage());
            }
        };
    }

    private static Throwable exception(Exchange exchange) {
        Throwable throwable = exchange.getException();
        if (throwable != null) {
            return throwable;
        }
        return (Throwable)exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
    }
    
}
