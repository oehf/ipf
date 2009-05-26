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
package org.apache.camel.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.MulticastProcessor.ProcessorExchangePair;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;


/**
 * An {@link Aspect} to update the {@link SplitHistory} of exchanges after
 * {@link MulticastProcessor#createProcessorExchangePairs(Exchange)} and
 * {@link Splitter#createProcessorExchangePairs(Exchange)} method calls.
 * 
 * @author Martin Krasser
 */
@Aspect
public class ExchangeCopy {

    @SuppressWarnings("unused") private Splitter splitter;
    @SuppressWarnings("unused") private MulticastProcessor multicast;
    @SuppressWarnings("unused") private ProcessorExchangePair pair;
    
    @SuppressWarnings("unused")
    @Pointcut("execution(Iterable<ProcessorExchangePair> " +
            "org.apache.camel.processor.MulticastProcessor.createProcessorExchangePairs(org.apache.camel.Exchange)) && args(exchange)")
    private void multicastExchangeCopy(Exchange exchange) {}
    
    @SuppressWarnings("unused")
    @Pointcut("execution(Iterable<ProcessorExchangePair> " +
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
    public void afterCopy(Exchange exchange, Iterable<ProcessorExchangePair> pairs) {
        ArrayList<ManagedMessage> copies = new ArrayList<ManagedMessage>(countElements(pairs));
        for (ProcessorExchangePair pair : pairs) {
            copies.add(new PlatformMessage(pair.getExchange()));
        }
        afterCopy(new PlatformMessage(exchange), copies);
    }
    
    private void afterCopy(ManagedMessage message, List<ManagedMessage> copies) {
        if (copies.size() < 2) {
            return;
        }
        SplitHistory history = message.getSplitHistory();
        SplitHistory[] splits = history.split(copies.size());
        
        for (int i = 0; i < splits.length; i++) {
            copies.get(i).setSplitHistory(splits[i]);
        }
        
    }
    
    private static int countElements(Iterable<ProcessorExchangePair> pairs) {
        // Currently we can only handle split results
        // that are lists because we need its size.
        if (pairs instanceof List) {
             return ((List<ProcessorExchangePair>)pairs).size();
        } else {
            throw new IllegalArgumentException("Cannot determine split size from split result.");
        }
        
    }
    
}
