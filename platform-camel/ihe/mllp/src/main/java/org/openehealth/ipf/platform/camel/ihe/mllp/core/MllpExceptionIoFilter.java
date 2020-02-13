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
import org.apache.camel.component.mina.MinaConsumer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;

import static java.util.concurrent.TimeUnit.*;
import static org.apache.camel.component.mina.MinaConstants.MINA_CLOSE_SESSION_WHEN_COMPLETE;
import static org.apache.camel.component.mina.MinaPayloadHelper.getIn;
import static org.apache.camel.component.mina.MinaPayloadHelper.getOut;

/**
 * This {@link IoFilterAdapter} is used to catch all exceptions occurred inside the {@link MllpConsumer} filterChain,
 * before the message reaches the CamelRoute, and send appropriate response created by defined exceptionHandler
 * either over RouteBuilder exception handling using "consumer.bridgeErrorHandler=true" or directly by
 * "exceptionHandler=#myExceptionHandlerBean"
 *
 * @author Boris Stanojevic
 *
 */
public class MllpExceptionIoFilter extends IoFilterAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MllpExceptionIoFilter.class);

    private final MinaConsumer minaConsumer;

    public MllpExceptionIoFilter(MinaConsumer minaConsumer) {
        this.minaConsumer = minaConsumer;
    }

    @Override
    public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
        if (!session.isClosing()) {
            if (sendResponse(cause)) {
                Exception exception = new CamelException(cause.getMessage());
                Exchange exchange = createExchange(exception);
                minaConsumer.getExceptionHandler().handleException("", exchange, exception);
                sendResponse(session, exchange);
            } else {
                session.closeNow();
                nextFilter.sessionClosed(session);
            }
        }
    }

    private void sendResponse(IoSession session, Exchange exchange) {
        boolean disconnect = minaConsumer.getEndpoint().getConfiguration().isDisconnect();
        Object response = exchange.hasOut()? getOut(minaConsumer.getEndpoint(), exchange):
                                             getIn(minaConsumer.getEndpoint(), exchange);
        if (response != null) {
            WriteFuture future = session.write(response);
            LOG.trace("Waiting for write to complete for body: {} using session: {}", response, session);
            if (!future.awaitUninterruptibly(10, SECONDS)) {
                LOG.warn("Cannot write body: " + response + " using session: " + session);
            }
        } else {
            LOG.debug("Writing no response");
            disconnect = Boolean.TRUE;
        }

        // should session be closed after complete?
        Boolean close = (Boolean)session.getAttribute(MINA_CLOSE_SESSION_WHEN_COMPLETE);
        if (close != null) {
            disconnect = close;
        }
        if (disconnect) {
            LOG.debug("Closing session when complete at address: {}", minaConsumer.getAcceptor().getLocalAddress());
            session.closeNow();
        }
    }

    private Exchange createExchange(Throwable cause){
        Exchange exchange = minaConsumer.getEndpoint().createExchange();
        exchange.setException(cause);
        return exchange;
    }

    private boolean sendResponse(Throwable cause){
        return !(cause instanceof SSLException);
    }
}
