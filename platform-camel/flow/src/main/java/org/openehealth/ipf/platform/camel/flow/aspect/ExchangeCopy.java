/*
 * Copyright 2008-2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.flow.aspect;

import org.apache.camel.Exchange;
import org.apache.camel.processor.ExchangeCopyHelper;
import org.apache.camel.processor.MulticastProcessor;
import org.apache.camel.processor.Splitter;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openehealth.ipf.commons.flow.history.SplitHistory;


/**
 * An {@link Aspect} to update the {@link SplitHistory} of exchanges after
 * {@link MulticastProcessor#createProcessorExchangePairs(Exchange)} and
 * {@link Splitter#createProcessorExchangePairs(Exchange)} method calls.
 * 
 * @author Martin Krasser
 */
@Aspect
public class ExchangeCopy {

    @SuppressWarnings("unused")
    @Pointcut("execution(Iterable<org.apache.camel.processor.ProcessorExchangePair> " +
            "org.apache.camel.processor.MulticastProcessor.createProcessorExchangePairs(org.apache.camel.Exchange)) && args(exchange)")
    private void multicastExchangeCopy(Exchange exchange) {}
    
    @SuppressWarnings("unused")
    @Pointcut("execution(Iterable<org.apache.camel.processor.ProcessorExchangePair> " +
            "org.apache.camel.processor.Splitter.createProcessorExchangePairs(org.apache.camel.Exchange)) && args(exchange)")
    private void splitterExchangeCopy(Exchange exchange) {}
    
    @SuppressWarnings("unused")
    @Pointcut("(multicastExchangeCopy(exchange) || splitterExchangeCopy(exchange))")
    private void exchangeCopy(Exchange exchange) {}
    
    /**
     * Splits the {@link SplitHistory} of <code>exchange</code> using
     * {@link SplitHistory#split(int)} and sets the newly created
     * {@link SplitHistory} objects on the {@link Exchange} objects contained in
     * the <code>pairs</code> iterable.
     * 
     * @param exchange
     *            original exchange.
     * @param pairs
     *            split exchange processor pairs.
     * 
     * @see SplitHistory#SplitHistory(int)
     */
    @AfterReturning(pointcut="exchangeCopy(exchange)", returning="pairs")
    public void afterCopy(Exchange exchange, Iterable<?> pairs) {
        ExchangeCopyHelper.afterCopy(exchange, pairs);
    }
    
}
