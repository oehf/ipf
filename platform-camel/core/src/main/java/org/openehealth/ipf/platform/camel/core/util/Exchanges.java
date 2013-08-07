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

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;

/**
 * Utility related to Camel {@link Exchange}s.
 * 
 * @author Martin Krasser
 */
public class Exchanges {

    /**
     * Creates a {@link ProducerTemplate} from the given {@link Exchange}
     * 
     * @param exchange message exchange.
     * @return a producer template.
     */
    public static ProducerTemplate producerTemplate(Exchange exchange) {
        return exchange.getContext().createProducerTemplate();
    }
    
    /**
     * Returns the message where to write results.
     * 
     * @param exchange message exchange.
     * @return result message.
     */
    public static Message resultMessage(Exchange exchange) {
        if (exchange.getPattern().isOutCapable()) {
            return exchange.getOut();
        } else {
            return exchange.getIn();
        }
    }

    /**
     * Returns the message where to write results. This method copies the
     * in-message to the out-message if the exchange is out-capable.
     * 
     * @param exchange message exchange.
     * @return result message.
     */
    public static Message prepareResult(Exchange exchange) {
        Message result = resultMessage(exchange);
        if (exchange.getPattern().isOutCapable()) {
            result.copyFrom(exchange.getIn());
        }
        return result;
    }
    
    /**
     * Creates a new {@link Exchange} instance from the given
     * <code>exchange</code>. The resulting exchange's pattern is defined by
     * <code>pattern</code>.
     * 
     * @param source
     *            exchange to copy from.
     * @param pattern
     *            exchange pattern to set.
     * @return created exchange.
     */
    public static Exchange createExchange(Exchange source, ExchangePattern pattern) {
        DefaultExchange target = new DefaultExchange(source.getContext());
        copyExchange(source, target);
        target.setPattern(pattern);
        return target;
    }
    
    /**
     * Creates a new {@link Exchange} instance using <code>context</code>.
     * The resulting exchange's pattern is defined by <code>pattern</code>.
     * 
     * @param context
     *            Camel context.
     * @param pattern
     *            exchange pattern.
     * @return created exchange.
     */
    public static Exchange createExchange(CamelContext context, ExchangePattern pattern) {
        DefaultExchange target = new DefaultExchange(context);
        target.setPattern(pattern);
        return target;
    }

    /**
     * Copies the exchange's in-message to the out-message if the exchange
     * pattern is {@link ExchangePattern#InOut}.
     * 
     * @param exchange
     *            message exchange.
     */
    public static void copyInput(Exchange exchange) {
        prepareResult(exchange);
    }
    
    /**
     * Copies the <code>source</code> exchange to <code>target</code> exchange
     * preserving the {@link ExchangePattern} of <code>target</code>.  
     * 
     * @param source source exchange.
     * @param target target exchange.
     * 
     * @see #resultMessage(Exchange)
     */
    public static void copyExchange(Exchange source, Exchange target) {
        if (source == target) {
            // no need to copy
            return;
        }
        
        // copy in message
        target.getIn().copyFrom(source.getIn());
    
        // copy out message
        if (source.hasOut()) {
            resultMessage(target).copyFrom(source.getOut());
        }
        
        // copy exception
        target.setException(source.getException());

        // copy properties
        target.getProperties().putAll(source.getProperties());
        
    }


    /**
     * Extracts the exception handled while processing the given
     * exchange (if any), and marks the exchange as non-failed.
     *
     * @param exchange
     *      Camel exchange, should be not <code>null</code>.
     * @return
     *      an {@link Exception} instance, or <code>null</code> when no exception was handled.
     */
    public static Exception extractException(Exchange exchange) {
        Exception exception = null;
        if (exchange.isFailed()) {
            exception = exchange.getException();
            exchange.setException(null);
        }
        else if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            exchange.removeProperty(Exchange.EXCEPTION_CAUGHT);
            exchange.removeProperty(Exchange.ERRORHANDLER_HANDLED);
        }
        return exception;
    }

}
