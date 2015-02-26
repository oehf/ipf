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
package org.openehealth.ipf.platform.camel.core.bridge;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.copyExchange;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.DelegateProcessor;

/**
 * Bridges exchanges of any {@link ExchangePattern} to an
 * {@link ExchangePattern#InOnly} exchange. <em>Please note</em> that this
 * bridge does not propagate any processing results (faults, exceptions) to the
 * original exchange.
 * 
 * @author Martin Krasser
 */
@Deprecated
public class InOnlyBridge extends DelegateProcessor {

    /**
     * Bridges to an {@link ExchangePattern#InOnly} exchange.
     * 
     * @param exchange exchange of any {@link ExchangePattern}.
     * @throws Exception
     */
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        DefaultExchange target = new DefaultExchange(exchange.getContext());
        copyExchange(exchange, target);
        super.processNext(target);
    }

}
