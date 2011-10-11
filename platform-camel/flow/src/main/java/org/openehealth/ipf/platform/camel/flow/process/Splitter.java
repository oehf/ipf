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
package org.openehealth.ipf.platform.camel.flow.process;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.core.process.splitter.SplitIndex;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;

/**
 * A processor that splits an exchange into multiple exchanges by using a rule.
 * The rule generates the individual sub exchanges (i.e. the result exchanges
 * of the split).
 * This class extends the core model {@link org.openehealth.ipf.platform.camel.core.process.splitter.Splitter}
 * with flow manager specific functionality, especially bookkeeping of the
 * split history.
 * 
 * @author Jens Riemschneider
 */
public class Splitter extends
        org.openehealth.ipf.platform.camel.core.process.splitter.Splitter {

    /**
     * Creates a splitter
     *  
     * @param splitRule
     *          expression that performs the splitting of the original exchange
     * @param processor
     *          destination processor for all sub exchanges. Can be {@code null}
     *          if the destination is set later via {@link #setProcessor(Processor)},
     *          e.g. via an intercept.                  
     */
    public Splitter(Expression splitRule, Processor processor) {
        super(splitRule, processor);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.core.process.splitter.Splitter#finalizeAggregate(org.apache.camel.Exchange, org.apache.camel.Exchange)
     */
    @Override
    protected void finalizeAggregate(Exchange origExchange, Exchange aggregate) {
        ManagedMessage origMessage = new PlatformMessage(origExchange);
        SplitHistory origHistory = origMessage.getSplitHistory();
        
        // The aggregate needs to be copied to ensure that we can change it
        // without affecting the input into the destination processor of the
        // splitter.
        if (aggregate != null){
            Exchange copiedAggregate = aggregate.copy();
            ManagedMessage message = new PlatformMessage(copiedAggregate);
            SplitHistory aggregateHistory = message.getSplitHistory();
            if (!aggregateHistory.equals(origHistory)) {
                message.setSplitHistory(origHistory);
            }
            super.finalizeAggregate(origExchange, copiedAggregate);
        } else {
            super.finalizeAggregate(origExchange, aggregate);
        }
        
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.core.process.splitter.Splitter#finalizeSubExchange(org.apache.camel.Exchange, org.apache.camel.Exchange, org.openehealth.ipf.platform.camel.core.process.splitter.SplitIndex)
     */
    @Override
    protected void finalizeSubExchange(Exchange origExchange,
            Exchange subExchange, SplitIndex index) {
        super.finalizeSubExchange(origExchange, subExchange, index);

        if (splitOnlyHasSingleResult(index)) {
            return;
        }
        
        ManagedMessage origMessage = new PlatformMessage(origExchange);
        SplitHistory origHistory = origMessage.getSplitHistory();

        SplitHistory subHistory = origHistory.split(index.getIndex(), index.isLast());
        
        ManagedMessage subMessage = new PlatformMessage(subExchange);
        subMessage.setSplitHistory(subHistory);
    }

    private boolean splitOnlyHasSingleResult(SplitIndex index) {
        return index.getIndex() == 0 && index.isLast();
    }
}
