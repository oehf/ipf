/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.flow.process;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.processor.ExchangeCopyHelper;
import org.apache.camel.processor.MulticastProcessor;
import org.apache.camel.processor.ProcessorExchangePair;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;
import org.springframework.util.ReflectionUtils;

/**
 * A MulticastProcessor that updates the flow split history
 * 
 * @author Mitko Kolev
 */
public class FlowMulticastProcessor extends MulticastProcessor {

    public FlowMulticastProcessor(MulticastProcessor originalProc) {
        super(originalProc.getCamelContext(), 
                originalProc.getProcessors(),
                originalProc.getAggregationStrategy(), 
                originalProc.isParallelProcessing(), 
                executorService(originalProc),
                originalProc.isStreaming(),
                originalProc.isStopOnException(), 
                originalProc.getTimeout());
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        SplitHistory original = getSplitHistory(exchange);
        super.process(exchange);
        restoreSplitHistory(original, exchange);
    }

    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
        SplitHistory original = getSplitHistory(exchange);
        boolean result = super.process(exchange, callback);
        restoreSplitHistory(original, exchange);
        return result;
    }

    @Override
    protected Iterable<ProcessorExchangePair> createProcessorExchangePairs(Exchange exchange) throws Exception {
        Iterable<ProcessorExchangePair> pairs = super.createProcessorExchangePairs(exchange);
        ExchangeCopyHelper.afterCopy(exchange, pairs);
        return pairs;
    }
    
    public static void restoreSplitHistory(SplitHistory original, Exchange exchange){
        PlatformMessage message = new PlatformMessage(exchange);
        SplitHistory current = message.getSplitHistory();
        if (!original.equals(current)) {
            message.setSplitHistory(original);
        }
    }
       
    public static ManagedMessage getMessage(Exchange exchange) {
        return new PlatformMessage(exchange);
    }
    
    public static SplitHistory getSplitHistory(Exchange exchange) {
        return new PlatformMessage(exchange).getSplitHistory();
    }
    
    /**
     * Returns the private executorService field of subclasses of MulticastProcessor
     * @param processor the MulticastProcessor instance
     * @return the value of the private executorService 
     */
    public static ExecutorService executorService(MulticastProcessor processor) {
        try {
            Field f = ReflectionUtils.findField(processor.getClass(), "executorService");
            f.setAccessible(true);
            return (ExecutorService)ReflectionUtils.getField(f, processor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
