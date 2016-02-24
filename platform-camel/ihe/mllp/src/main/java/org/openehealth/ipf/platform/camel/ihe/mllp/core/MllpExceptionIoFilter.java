/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;


import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.mina2.Mina2Consumer;
import org.apache.camel.component.mina2.Mina2Helper;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.camel.component.mina2.Mina2Constants.MINA_CLOSE_SESSION_WHEN_COMPLETE;
import static org.apache.camel.component.mina2.Mina2PayloadHelper.getIn;
import static org.apache.camel.component.mina2.Mina2PayloadHelper.getOut;
import static org.apache.camel.util.ExchangeHelper.isOutCapable;

/**
 * This {@link IoFilterAdapter} is used to catch all exceptions occurred inside the {@link MllpConsumer} filterChain,
 * before the message reaches the CamelRoute, and send appropriate response created by defined exceptionHandler
 * either over RouteBuilder exception handling using "consumer.bridgeErrorHandler=true" or directly by
 * "consumer.exceptionHandler=#myExceptionHandlerBean"
 *
 * @author Boris Stanojevic
 *
 */
public class MllpExceptionIoFilter extends IoFilterAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MllpExceptionIoFilter.class);

    private final Mina2Consumer mina2Consumer;

    public MllpExceptionIoFilter(Mina2Consumer mina2Consumer) {
        this.mina2Consumer = mina2Consumer;
    }

    @Override
    public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
        Exchange exchange = mina2Consumer.getEndpoint().createExchange();
        CamelException exception = new CamelException(cause.getMessage());
        exchange.setException(exception);
        mina2Consumer.getExceptionHandler().handleException("", exchange, exception);
        sendResponse(session, exchange);
    }

    private void sendResponse(IoSession session, Exchange exchange) throws Exception {
        boolean disconnect = mina2Consumer.getEndpoint().getConfiguration().isDisconnect();
        Object response = exchange.hasOut()? getOut(mina2Consumer.getEndpoint(), exchange):
                                             getIn(mina2Consumer.getEndpoint(), exchange);
        if (response != null) {
            LOG.debug("Writing body: {}", response);
            Mina2Helper.writeBody(session, response, exchange);
        } else {
            LOG.debug("Writing no response");
            disconnect = Boolean.TRUE;
        }

        // should session be closed after complete?
        Message message = isOutCapable(exchange)? exchange.getOut() : exchange.getIn();
        Boolean close = message.getHeader(MINA_CLOSE_SESSION_WHEN_COMPLETE, Boolean.class);

        // should we disconnect, the header can override the configuration
        if (close != null) {
            disconnect = close;
        }
        if (disconnect) {
            LOG.debug("Closing session when complete at address: {}", mina2Consumer.getAcceptor().getLocalAddress());
            session.close(true);
        }
    }
}
