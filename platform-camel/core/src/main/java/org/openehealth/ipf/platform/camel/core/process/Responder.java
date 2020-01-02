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
package org.openehealth.ipf.platform.camel.core.process;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.copyExchange;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.processor.DelegateProcessor;

/**
 * Implements a response generation process. For generating a response the
 * incoming {@link Exchange} is sent to a
 * <code>responseGeneratorProcessor</code> (set at construction time) that is
 * generating a response message from the original {@link Exchange}. The
 * response is then communicated back to the initiator and the original exchange
 * is forwarded to the next processor as in-only {@link Exchange}.
 * 
 * @author Martin Krasser
 */
public class Responder extends DelegateProcessor {

    private Processor responseGeneratorProcessor;
    private Producer responseGeneratorProducer;
    
    /**
     * Creates a new {@link Responder}.
     * 
     * @param responseGeneratorProcessor
     *            processor that generates the response message for an exchange.
     */
    public Responder(Processor responseGeneratorProcessor) {
        this.responseGeneratorProcessor = responseGeneratorProcessor;
    }

    /**
     * Creates a new {@link Responder}.
     * 
     * @param responseGeneratorProducer
     *            producer that generates the response message for an exchange.
     */
    public Responder(Producer responseGeneratorProducer) {
        this.responseGeneratorProducer = responseGeneratorProducer;
    }

    /**
     * Sends the incoming {@link Exchange} to an
     * <code>responseGeneratorProcessor</code> (set at construction time) that
     * is generating a response message from the original {@link Exchange}. The
     * response is then communicated back to the initiator and the original
     * exchange is forwarded to the next processor as in-only {@link Exchange}.
     * 
     * @param exchange
     *            original exchange.
     */
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        Exchange delegateExchange = createDelegateExchange(exchange);
        Exchange serviceExchange = exchange.copy();
        
        getResponseGenerator().process(serviceExchange);
        // copy service exchange over to original exchange
        // (sends response back to exchange initiator)
        copyExchange(serviceExchange, exchange);
        
        if (process(exchange, serviceExchange)) {
            super.processNext(delegateExchange);
        }
        
    }

    /**
     * Processes the <code>original</code> exchange and the
     * <code>response</code> exchange (returned from a
     * <code>responseGeneratorProcessor</code>) and returns a decision whether
     * to continue processing. The default implementation always returns
     * <code>true</code>. This method is intended to be overwritten by
     * subclasses.
     * 
     * @param original
     *            original message exchange.
     * @param response
     *            response message exchange.
     * @return <code>true</code> if processing shall continue,
     *         <code>false</code> otherwise.
     */
    protected boolean process(Exchange original, Exchange response) {
        return true;
    }

    /**
     * Creates the exchange for the next processor returned by
     * {@link #getProcessor()} from a source exchange.
     * 
     * @param source
     *            a source exchange.
     * @return exchange for the next processor.
     */
    protected Exchange createDelegateExchange(Exchange source) {
        DefaultExchange result = new DefaultExchange(source.getContext());
        result.getIn().copyFrom(source.getIn());
        return result;
    }
    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
        if (responseGeneratorProducer != null) {
            responseGeneratorProducer.start();
        }
    }

    @Override
    protected void doStop() throws Exception {
        if (responseGeneratorProducer != null) {
            responseGeneratorProducer.stop();
        }
        super.doStop();
    }

    private Processor getResponseGenerator() {
        if (responseGeneratorProcessor != null) {
            return responseGeneratorProcessor;
        } else {
            return responseGeneratorProducer;
        }
    }
    
}
