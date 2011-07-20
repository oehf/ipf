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

import static org.openehealth.ipf.platform.camel.flow.process.FlowMulticastProcessor.executorService;
import static org.openehealth.ipf.platform.camel.flow.process.FlowMulticastProcessor.getSplitHistory;
import static org.openehealth.ipf.platform.camel.flow.process.FlowMulticastProcessor.restoreSplitHistory;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.processor.ExchangeCopyHelper;
import org.apache.camel.processor.ProcessorExchangePair;
import org.openehealth.ipf.commons.flow.history.SplitHistory;

/**
 * A SplitProcessor that updates the flow split history
 * 
 * @author Mitko Kolev
 */
public class FlowSplitProcessor extends org.apache.camel.processor.Splitter {

   public FlowSplitProcessor(org.apache.camel.processor.Splitter originalProc) {
        super(originalProc.getCamelContext(), 
              originalProc.getExpression(),
              originalProc.getProcessors().iterator().next(), 
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
   protected Iterable<ProcessorExchangePair> createProcessorExchangePairs(Exchange exchange) {
       Iterable<ProcessorExchangePair> pairs = super.createProcessorExchangePairs(exchange);
       ExchangeCopyHelper.afterCopy(exchange, pairs);
       return pairs;
   }
}
